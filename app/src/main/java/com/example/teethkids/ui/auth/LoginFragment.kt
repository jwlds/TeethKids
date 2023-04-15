package com.example.teethkids.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.teethkids.R
import com.example.teethkids.dao.AuthenticationDAO
import com.example.teethkids.databinding.FragmentLoginBinding
import com.example.teethkids.utils.Utils


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
                try {
                    binding.loading.isVisible = true
                    val auth = AuthenticationDAO()
                    auth.login(
                        binding.edtEmail.text.toString().trim(),
                        binding.edtPassword.text.toString().trim(),
                        onSuccess = {
                            binding.loading.isVisible = false
                            Utils.showToast(requireContext(),"Login realizado com sucesso!")
                            findNavController().navigate(R.id.action_global_homeFragment2)
                        },
                        onFailure = { exception ->
                            binding.loading.isVisible = false
                            Utils.showSnackbar(requireView(),exception)
                        }
                    )
                } catch (arg: IllegalArgumentException)
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



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}