package com.example.quizfirebase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quizfirebase.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityResultBinding
    var correctCount = 0
    var wrongCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        correctCount = intent.getIntExtra("correctCount", 0)
        wrongCount = intent.getIntExtra("wrongCount", 0)
        binding.txtResult.text = "$correctCount / 4"
        binding.circularProgressBar.progress = correctCount.toFloat()
        binding.konfettiView.start(Presets.parade())
    }
}