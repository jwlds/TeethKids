package com.example.teethkids.ui.auth.register

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.teethkids.R
import com.example.teethkids.databinding.FragmentEmailPasswordBinding


class EmailPasswordFragment : Fragment() {
    private var _binding: FragmentEmailPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmailPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun isValid(): Boolean {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val confirmPassword = binding.edtPassword2.text.toString().trim()

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

        if (confirmPassword.isEmpty() || confirmPassword != password) {
            binding.edtPassword2.error = "As senhas não coincidem"
            binding.edtPassword2.requestFocus()
            return false
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}