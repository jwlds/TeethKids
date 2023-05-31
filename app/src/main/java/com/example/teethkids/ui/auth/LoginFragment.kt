package com.example.teethkids.ui.auth

import android.content.Intent
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
import com.example.teethkids.databinding.FragmentLoginBinding
import com.example.teethkids.service.FirebaseMessagingService
import com.example.teethkids.ui.home.MainActivity
import com.example.teethkids.utils.Utils
import com.example.teethkids.utils.Utils.hideKeyboard
import com.google.firebase.messaging.FirebaseMessaging


class LoginFragment : Fragment(),View.OnClickListener{
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        binding.btnRecover.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.btnLogin -> {
                hideKeyboard()
                if(isValid()) {
                    binding.loading.isVisible = true
                    val auth = AuthenticationDAO()
                    auth.login(
                        binding.edtEmail.text.toString().trim(),
                        binding.edtPassword.text.toString().trim(),
                        onSuccess = {
                            binding.loading.isVisible = false
                            Utils.showToast(requireContext(),"Login realizado com sucesso!")
                            val intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        },
                        onFailure = { exception ->
                            binding.loading.isVisible = false
                            Utils.showSnackbar(requireView(),exception)
                        }
                    )
                } else
                {
                    Utils.showToast(requireContext(),"Preecha todos os dados!")
                }
            }
            R.id.btnRegister -> {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            R.id.btnRecover -> {
                findNavController().navigate(R.id.action_loginFragment_to_recoverAccountFragment)
            }

        }


    }

    private fun isValid(): Boolean {

        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.error = "Digite um e-mail válido"
            binding.edtEmail.requestFocus()
            return false
        }
        if (password.isEmpty() || password.length < 6) {
            binding.edtPassword.error = "A senha deve ter pelo menos 6 caracteres"
            binding.edtPassword.requestFocus()
            return false
        }
        return true
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}