package com.example.teethkids.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.teethkids.databinding.ActivityAuthenticateBinding


class AuthenticateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.findFragmentById(binding.contentAuthenticate.navHostFragment.id) as NavHostFragment
    }
}