package com.example.teethkids.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.teethkids.R
import com.example.teethkids.database.FirebaseRoom.Companion.getAuth
import com.example.teethkids.databinding.FragmentRecoverAccountBinding
import com.example.teethkids.utils.Utils


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

        binding.btnRecover.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.btnRecover -> {
                try {

                    binding.loading.isVisible = true
                    recoverAccount(binding.edtEmail.text.toString().trim())
                } catch (arg: IllegalArgumentException) {
                    Utils.showToast(requireContext(),"Digite o email !")
                }
            }
            R.id.btnBack -> {
                findNavController().navigate(R.id.action_recoverAccountFragment_to_loginFragment)
            }
        }
    }

    private fun recoverAccount(email: String) {
        getAuth().sendPasswordResetEmail(email)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    binding.loading.isVisible = false
                    Utils.showToast(requireContext(),"A senha foi enviada com sucesso no seu email!")
                }
                else {
                    binding.loading.isVisible = false
                    Utils.showToast(requireContext(),"ERRO")
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}