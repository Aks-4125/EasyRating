package com.review.easyrating

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.review.easyrating.MainActivity.Companion.mAdCounter
import com.review.easyrating.databinding.ActivityImageBinding

class ImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonSecond.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
        binding.textView.text = "Great! You have completed $mAdCounter/5 tasks."

        Glide.with(this)
            .load(R.drawable.ic_launcher_foreground) // any placeholder to load at start
            .override(200, 200) // resizing
            .centerCrop()
            .into(binding.imageView)
    }
}