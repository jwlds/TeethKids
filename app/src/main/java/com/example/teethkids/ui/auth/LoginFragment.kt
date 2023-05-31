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
import com.example.teethkids.databinding.FragmentLoginBinding
import com.example.teethkids.service.FirebaseMessagingService
import com.example.teethkids.ui.home.MainActivity
import com.example.teethkids.utils.Utils
import com.example.teethkids.utils.Utils.hideKeyboard
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.messaging.FirebaseMessaging


class LoginFragment : Fragment(), View.OnClickListener {
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

        val mUnderlineSpan = UnderlineSpan()

        val recoverText = binding.btnRecover.text.toString()
        val mBSpannableStringRecover = SpannableString(recoverText)
        mBSpannableStringRecover.setSpan(mUnderlineSpan, 0, recoverText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.btnRecover.text = mBSpannableStringRecover

        val registerText = binding.btnRegister.text.toString()
        val mBSpannableStringRegister = SpannableString(registerText)
        mBSpannableStringRegister.setSpan(mUnderlineSpan, 0, registerText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.btnRegister.text = mBSpannableStringRegister

        val errorMessage = binding.tvErrorMessage.text.toString()
        val mBSpannableStringErrorMessage = SpannableString(errorMessage)
        mBSpannableStringErrorMessage.setSpan(mUnderlineSpan, 0, errorMessage.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvErrorMessage.text = mBSpannableStringErrorMessage

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
                            binding.loading.isVisible = false
                            Utils.showToast(requireContext(), "Login realizado com sucesso!")
                            val intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        },
                        onFailure = { exception ->
                            binding.loading.isVisible = false
                            binding.tvErrorMessage.visibility = View.VISIBLE
                            setFieldErrorState(binding.inputEmail)
                            setFieldErrorState(binding.inputPassword)
                            Utils.showSnackbar(requireView(), exception)
                        }
                    )
                } else {
                    Utils.showToast(requireContext(), "Preencha todos os dados!")
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
            binding.edtEmail.requestFocus()
            setFieldErrorState(binding.inputEmail)
            return false
        }
        if (password.isEmpty() || password.length < 6) {
            binding.edtPassword.requestFocus()
            setFieldErrorState(binding.inputPassword)
            return false
        }
        return true
    }

    private fun setFieldErrorState(field: TextInputLayout) {
        val errorColor = Color.parseColor("#FF5252")
        val errorColorStateList = ColorStateList.valueOf(errorColor)
        val errorColorFilter = PorterDuffColorFilter(errorColor, PorterDuff.Mode.SRC_IN)

        field.defaultHintTextColor = errorColorStateList
        field.boxStrokeColor = errorColor
        field.startIconDrawable?.colorFilter = errorColorFilter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}