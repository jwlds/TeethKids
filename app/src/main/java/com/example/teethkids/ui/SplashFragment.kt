package com.example.teethkids.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.teethkids.R
import com.example.teethkids.database.FirebaseHelper.Companion.isAuth
import com.example.teethkids.databinding.FragmentSplashBinding
import com.example.teethkids.datastore.UserPreferencesRepository
import com.example.teethkids.service.ConnectivityManager
import com.example.teethkids.ui.home.MainActivity
import com.example.teethkids.utils.Utils


// Tela de splash do app.
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private lateinit var connectivityManager: ConnectivityManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        connectivityManager = ConnectivityManager(requireContext())

        // Aguarda 3 segundos e chama o método checkAuth()
        Handler(Looper.getMainLooper()).postDelayed(this::checkAuth,3000)
    }


    // Verifica se o usuário está autenticado
    private fun checkAuth() {
        val connectivityManager = ConnectivityManager(requireContext())
        val userPreferencesRepository = UserPreferencesRepository.getInstance(requireContext())
        if (connectivityManager.checkInternet()) {
            if (userPreferencesRepository.uid != "") {

                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_authenticate)
            }
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_authenticate)
           Utils.showSnackBarError(requireView(), "Sem conexão de internet")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}