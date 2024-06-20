package com.example.adminmusicstream.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.adminmusicstream.databinding.ActivityAddSongBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddSongActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSongBinding
    private lateinit var selectImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var selectAudioLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null
    private var audioUri: Uri? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null && result.data!!.data != null) {
                imageUri = result.data!!.data
                binding.statusImageTextView.text = "Image Selected: ${imageUri?.path}"
                Glide.with(this).load(imageUri).into(binding.selectedImageView)
                binding.selectedImageView.visibility = View.VISIBLE
            }
        }

        selectAudioLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null && result.data!!.data != null) {
                audioUri = result.data!!.data
                binding.statusAudioTextView.text = "Audio Selected: ${audioUri?.path}"
            }
        }

        binding.buttonSelectImage.setOnClickListener {
            selectImage()
        }

        binding.buttonSelectAudio.setOnClickListener {
            selectAudio()
        }

        binding.buttonUploadSong.setOnClickListener {
            val songTitle = binding.editTextSongTitle.text.toString().trim()
            val songSubtitle = binding.editTextSongSubtitle.text.toString().trim()

            if (songTitle.isEmpty()) {
                Toast.makeText(this, "Please enter a song title", Toast.LENGTH_SHORT).show()
            } else if (songSubtitle.isEmpty()) {
                Toast.makeText(this, "Please enter the author's name", Toast.LENGTH_SHORT).show()
            } else {
                if (imageUri == null || audioUri == null) {
                    Toast.makeText(this, "Please select both image and audio files", Toast.LENGTH_SHORT).show()
                } else {
                    uploadFilesToFirebaseStorage(songTitle, songSubtitle, imageUri!!, audioUri!!)
                }
            }
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        selectImageLauncher.launch(Intent.createChooser(intent, "Select Image"))
    }

    private fun selectAudio() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "audio/*"
        selectAudioLauncher.launch(Intent.createChooser(intent, "Select Audio"))
    }

    @SuppressLint("SetTextI18n")
    private fun uploadFilesToFirebaseStorage(title: String, subtitle: String, imageUri: Uri, audioUri: Uri) {
        val storageReference = FirebaseStorage.getInstance().reference
        val firestore = FirebaseFirestore.getInstance()

        val imageReference: StorageReference = storageReference.child("images/$title.jpg")
        imageReference.putFile(imageUri)
            .addOnSuccessListener {
                imageReference.downloadUrl.addOnSuccessListener { imageUrl ->
                    val audioReference: StorageReference = storageReference.child("songs/$title.mp3")
                    audioReference.putFile(audioUri)
                        .addOnSuccessListener {
                            audioReference.downloadUrl.addOnSuccessListener { audioUrl ->
                                val song = hashMapOf(
                                    "id" to title,
                                    "title" to title,
                                    "subtitle" to subtitle,
                                    "coverUrl" to imageUrl.toString(),
                                    "url" to audioUrl.toString()
                                )
                                firestore.collection("songs").document(title).set(song)
                                    .addOnSuccessListener {
                                        binding.statusTextView.text = "Song uploaded successfully"
                                        Toast.makeText(this, "Song uploaded successfully", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { e ->
                                        binding.statusTextView.text = "Failed to upload song: ${e.message}"
                                        Toast.makeText(this, "Failed to upload song: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.d("AddSongActivity", "Audio upload failed: $e")
                            Toast.makeText(this, "Audio upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                        .addOnProgressListener { taskSnapshot ->
                            val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                            binding.statusTextView.text = "Upload is $progress% done"
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.d("AddSongActivity", "Image upload failed: $e")
                Toast.makeText(this, "Image upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}