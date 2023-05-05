package com.example.teethkids.ui.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.teethkids.R
import com.example.teethkids.databinding.FragmentEmailPasswordBinding
import com.example.teethkids.databinding.FragmentOTPVerificationBinding


class OTPVerificationFragment : Fragment() {

    private var _binding: FragmentOTPVerificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOTPVerificationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}