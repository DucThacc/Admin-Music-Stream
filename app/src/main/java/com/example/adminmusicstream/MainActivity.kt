package com.example.adminmusicstream

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adminmusicstream.activity.AddSongActivity
import com.example.adminmusicstream.activity.SongListActivity
import com.example.adminmusicstream.activity.TopHitActivity
import com.example.adminmusicstream.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addImg.setOnClickListener {
            val intent = Intent(this, AddSongActivity::class.java)
            startActivity(intent)
        }

//        binding.userImg.setOnClickListener {
//            val intent = Intent(this, UsersActivity::class.java)
//            startActivity(intent)
//        }

        binding.songImg.setOnClickListener {
            val intent = Intent(this, SongListActivity::class.java)
            startActivity(intent)
        }

        binding.topHit.setOnClickListener {
            val intent = Intent(this, TopHitActivity::class.java)
            startActivity(intent)
        }
    }
}