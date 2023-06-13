package com.example.teethkids.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.teethkids.R
import com.example.teethkids.databinding.ActivityMainBinding
import com.example.teethkids.service.ConnectivityManager
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private lateinit var connectivityManager: ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        connectivityManager = ConnectivityManager(this)

        FirebaseApp.initializeApp(this);

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //val errorLayout = binding.errorLayout


        connectivityManager.observe(this) { isConnected ->
            if (isConnected) {
                binding.contentMain.errorLayout.errorLayout.isVisible = false
                binding.contentMain.navHostFragment.isVisible = true
            } else {
                binding.contentMain.errorLayout.errorLayout.isVisible = true
                binding.contentMain.navHostFragment.isVisible = false
            }
        }
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