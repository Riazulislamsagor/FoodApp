package com.example.adminwaveoffood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adminwaveoffood.databinding.ActivityCreateUserBinding
import com.example.adminwaveoffood.databinding.ActivityMainBinding

class CreateUserActivity : AppCompatActivity() {
    private val binding: ActivityCreateUserBinding by lazy {
        ActivityCreateUserBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.backButtonId.setOnClickListener{
            finish()
        }

    }
}