package com.example.gamelines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.gamelines.databinding.ActivityEndgameBinding

class EndgameActivity : AppCompatActivity() {
    lateinit var binding: ActivityEndgameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEndgameBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val current = intent.getStringExtra(resources.getString(R.string.current)) ?: 0
        binding.TotalScore.text = current.toString()
        val high = intent.getStringExtra(resources.getString(R.string.high)) ?: current
        binding.TotalBestScore.text = high.toString()
    }
}