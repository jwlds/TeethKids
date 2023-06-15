package com.example.teethkids.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.teethkids.R
import com.example.teethkids.databinding.ActivityMainBinding
import com.example.teethkids.service.ConnectivityManager
import com.example.teethkids.utils.AddressPrimaryId
import com.example.teethkids.viewmodel.AddressViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.GeoPoint

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    // Gerenciador de conectividade para verificar o estado da conexão de rede
    private lateinit var connectivityManager: ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Inicialização do ConnectivityManager
        connectivityManager = ConnectivityManager(this)

        // Inicialização do FirebaseApp
        FirebaseApp.initializeApp(this);

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Observa as alterações na conectividade de rede usando o ConnectivityManager
        connectivityManager.observe(this) { isConnected ->
            if (isConnected) {
                // Se há conexão de rede, o layout de erro não é mostrado
                binding.contentMain.errorLayout.errorLayout.isVisible = false
                binding.contentMain.navHostFragment.isVisible = true
            } else {
                // Se não há conexão de rede, o layout de erro é mostrado
                binding.contentMain.errorLayout.errorLayout.isVisible = true
                binding.contentMain.navHostFragment.isVisible = false
            }
        }

        // Encontra o fragmento de navegação pelo ID e obtém o NavController associado a ele
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.contentMain.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController

        // Configura a navegação do BottomNavigationView com o NavController
        binding.bottomNavigationView.setupWithNavController(navController)

        // Adiciona um listener para detectar mudanças de destino na navegação
        navController.addOnDestinationChangedListener { _, destination, _ ->

            // Se o destino for um dos seguintes fragmentos, torna o BottomNavigationView visível
            when (destination.id) {
                R.id.homeFragment,
                R.id.profileMainFragment,
                R.id.settingsFragment,
                R.id.notificationFragment,
                R.id.emergencyListFragment -> {
                    binding.bottomNavigationView.isVisible = true
                }
                // Caso contrário, torna o BottomNavigationView invisível (Para não a bottomBar nas sub_telas)
                else -> {
                    binding.bottomNavigationView.isVisible = false
                }
            }
        }


    }





}




