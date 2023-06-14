package com.example.teethkids.ui.auth

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.teethkids.R
import com.example.teethkids.dao.AuthenticationDAO
import com.example.teethkids.databinding.FragmentRecoverAccountBinding
import com.example.teethkids.utils.Utils
import com.example.teethkids.utils.Utils.hideKeyboard
import com.example.teethkids.utils.Utils.setErrorState
import com.example.teethkids.utils.Utils.setUnderlinedText


class RecoverAccountFragment : Fragment(), View.OnClickListener{

    private var _binding: FragmentRecoverAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecoverAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvRecoverErrorMessage.isVisible = false
        binding.tvRecoverErrorMessage.setUnderlinedText(binding.tvRecoverErrorMessage.text.toString())

        binding.btnRecover.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.btnRecover -> {
                hideKeyboard()
                if (isValid()){
                    binding.loading.isVisible = true
                    val auth = AuthenticationDAO(requireContext())
                    auth.recoverAccount(binding.edtEmail.text.toString().trim(),
                        onSuccess = {
                            binding.loading.isVisible = false
                        },
                        onFailure = { exception ->
                            binding.loading.isVisible = false
                            Utils.showSnackbar(requireView(),exception)
                        }
                    )
                } else {
                    binding.tvRecoverErrorMessage.isVisible = true
                }
            }
            R.id.btnBack -> {
                findNavController().navigate(R.id.action_recoverAccountFragment_to_loginFragment)
            }
        }
    }

    private fun isValid(): Boolean {
        val email = binding.edtEmail.text.toString().trim()

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.requestFocus()
            binding.inputEmail.setErrorState()
            return false
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}