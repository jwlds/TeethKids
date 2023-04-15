package com.example.teethkids.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.teethkids.R
import com.example.teethkids.database.FirebaseHelper.Companion.isAuth
import com.example.teethkids.databinding.FragmentSplashBinding
import com.example.teethkids.ui.home.MainActivity


class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed(this::checkAuth,3000)
    }

    private fun checkAuth(){
        if(isAuth()) {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_authenticate)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}