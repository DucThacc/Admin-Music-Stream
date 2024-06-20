package com.example.adminmusicstream.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adminmusicstream.MainActivity
import com.example.adminmusicstream.R
import com.example.adminmusicstream.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {

    lateinit var binding:ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this@IntroActivity,MainActivity::class.java))
        }

    }
}