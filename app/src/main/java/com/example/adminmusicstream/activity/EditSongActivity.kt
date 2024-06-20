package com.example.adminmusicstream.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.adminmusicstream.R
import com.example.adminmusicstream.databinding.ActivityEditSongBinding
import com.google.firebase.firestore.FirebaseFirestore

class EditSongActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditSongBinding
    private var songId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val songTitle = intent.getStringExtra("SONG_TITLE")
        val songSubtitle = intent.getStringExtra("SONG_SUBTITLE")
        val songCoverUrl = intent.getStringExtra("SONG_COVER_URL")
        val songUrl = intent.getStringExtra("SONG_URL")
        songId = intent.getStringExtra("SONG_ID")

        binding.songTitleEditText.setText(songTitle)
        binding.songSubtitleEditText.setText(songSubtitle)
        Glide.with(this).load(songCoverUrl).into(binding.songCoverImageView)

        // Xử lý sự kiện click của nút cập nhật
        binding.updateSongButton.setOnClickListener {
            updateSongInFirestore()
        }
    }

    private fun updateSongInFirestore() {
        val updatedTitle = binding.songTitleEditText.text.toString()
        val updatedSubtitle = binding.songSubtitleEditText.text.toString()

        // Kiểm tra songId không null
        songId?.let { id ->
            val songRef = FirebaseFirestore.getInstance().collection("songs").document(id)
            songRef.update(
                mapOf(
                    "title" to updatedTitle,
                    "subtitle" to updatedSubtitle
                    // Thêm các trường khác nếu cần
                )
            ).addOnSuccessListener {
                Toast.makeText(this, "Song updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update song: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}