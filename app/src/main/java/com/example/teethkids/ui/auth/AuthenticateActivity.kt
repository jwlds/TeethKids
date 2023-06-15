package com.example.teethkids.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.teethkids.databinding.ActivityAuthenticateBinding


class AuthenticateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // binding
        binding = ActivityAuthenticateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Encontra o fragmento de navegação pelo ID e obtém o NavController associado a ele
        supportFragmentManager.findFragmentById(binding.contentAuthenticate.navHostFragment.id) as NavHostFragment
    }
}