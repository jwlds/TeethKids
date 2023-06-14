package com.example.teethkids.ui.auth

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.teethkids.R
import com.example.teethkids.dao.AuthenticationDAO
import com.example.teethkids.database.FirebaseHelper
import com.example.teethkids.databinding.FragmentLoginBinding
import com.example.teethkids.datastore.UserPreferencesRepository
import com.example.teethkids.service.ConnectivityManager
import com.example.teethkids.service.FirebaseMessagingService
import com.example.teethkids.ui.home.MainActivity
import com.example.teethkids.utils.Utils
import com.example.teethkids.utils.Utils.hideKeyboard
import com.example.teethkids.utils.Utils.setErrorState
import com.example.teethkids.utils.Utils.setUnderlinedText
import com.example.teethkids.utils.Utils.showSnackBarError
import com.example.teethkids.utils.Utils.showSnackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.messaging.FirebaseMessaging

class LoginFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var connectivityManager: ConnectivityManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        connectivityManager = ConnectivityManager(requireContext())
        binding.btnRecover.setUnderlinedText(binding.btnRecover.text.toString())
        binding.btnRegister.setUnderlinedText(binding.btnRegister.text.toString())
        binding.tvErrorMessage.setUnderlinedText(binding.tvErrorMessage.text.toString())

        binding.btnLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        binding.btnRecover.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnLogin -> {
                hideKeyboard()
                if (isValid()) {
                    binding.loading.isVisible = true
                    val auth = AuthenticationDAO()
                    auth.login(
                        binding.edtEmail.text.toString().trim(),
                        binding.edtPassword.text.toString().trim(),

                        onSuccess = {
                            val userPreferencesRepository = UserPreferencesRepository.getInstance(requireContext())
                            userPreferencesRepository.updateUid(FirebaseHelper.getIdUser().toString())
                            binding.loading.isVisible = false
                            Utils.showToast(requireContext(), "Login realizado com sucesso!")
                            val intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        },
                        onFailure = { exception ->
                            binding.loading.isVisible = false
                            Utils.showSnackbar(requireView(), exception)
                        }
                    )
                } else {
                    binding.tvErrorMessage.isVisible = true
                }
            }
            R.id.btnRegister -> {
                if(connectivityManager.checkInternet())    findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
                else showSnackBarError(requireView(),"Sem conexão de internet")

            }
            R.id.btnRecover -> {
                if(connectivityManager.checkInternet())  findNavController().navigate(R.id.action_loginFragment_to_recoverAccountFragment)
                else showSnackBarError(requireView(),"Sem conexão de internet")
            }
        }
    }

    private fun isValid(): Boolean {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.requestFocus()
            binding.inputEmail.setErrorState()
            return false
        }
        if (password.isEmpty() || password.length < 6) {
            binding.edtPassword.requestFocus()
            binding.inputPassword.setErrorState()
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
