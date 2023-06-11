package com.example.teethkids.ui.auth.register

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.teethkids.R
import com.example.teethkids.databinding.FragmentResumeBinding
import com.example.teethkids.utils.RegistrationDataHolder

class ResumeFragment : Fragment() {
    private var _binding: FragmentResumeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResumeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    fun isValid(): Boolean {

        val description = binding.edtDescription.text.toString().trim()

        if (description.isEmpty()) {
            binding.edtDescription.error = "Vazio"
            binding.edtDescription.requestFocus()
            return false
        }
        RegistrationDataHolder.registrationData.professionalDescription = binding.edtDescription.text.toString().trim()
        return true
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}