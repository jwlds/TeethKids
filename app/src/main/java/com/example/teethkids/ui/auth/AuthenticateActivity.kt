package com.example.teethkids.ui.auth

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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