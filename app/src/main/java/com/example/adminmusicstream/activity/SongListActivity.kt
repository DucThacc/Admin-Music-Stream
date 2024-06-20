package com.example.adminmusicstream.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adminmusicstream.MainActivity
import com.example.adminmusicstream.R
import com.example.adminmusicstream.adapter.SongAdapter
import com.example.adminmusicstream.databinding.ActivityMainBinding
import com.example.adminmusicstream.model.SongModel
import com.google.firebase.firestore.FirebaseFirestore

class SongListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var songAdapter: SongAdapter
    private val songList = mutableListOf<SongModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_list)


        recyclerView = findViewById(R.id.songs_list_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        songAdapter = SongAdapter(this, songList)
        recyclerView.adapter = songAdapter

        fetchSongs()
    }

    private fun fetchSongs() {
        val db = FirebaseFirestore.getInstance()
        db.collection("songs").get().addOnSuccessListener { result ->
            songList.clear()
            for (document in result) {
                val song = document.toObject(SongModel::class.java)
                songList.add(song)
            }
            songAdapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            // Handle any errors
        }
    }


}