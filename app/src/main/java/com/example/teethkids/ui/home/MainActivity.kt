package com.example.teethkids.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.teethkids.R
import com.example.teethkids.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this);

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportFragmentManager.findFragmentById(binding.contentMain.navHostFragment.id) as NavHostFragment

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.contentMain.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment,
                R.id.profileMainFragment,
                R.id.settingsFragment,
                R.id.notificationFragment,
                R.id.emergencyListFragment -> {
                    binding.bottomNavigationView.isVisible = true
                }
                else -> {
                    binding.bottomNavigationView.isVisible = false
                }
            }
        }

    }
}