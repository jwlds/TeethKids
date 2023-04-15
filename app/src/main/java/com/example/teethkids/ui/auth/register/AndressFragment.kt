package com.example.teethkids.ui.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.teethkids.R
import com.example.teethkids.databinding.FragmentAndressBinding
import com.example.teethkids.utils.RegistrationDataHolder


class AndressFragment : Fragment(){
    private var _binding: FragmentAndressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAndressBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.btnBack.setOnClickListener(this)
//        binding.btnNext.setOnClickListener(this)
    }

    fun isValid(): Boolean {
        val edtAndress1 = binding.edtAndress1.text.toString().trim()
        val edtAndress2 = binding.edtAndress2.text.toString().trim()
        val edtAndress3 = binding.edtAndress3.text.toString().trim()

        if (edtAndress1.isNotEmpty() || edtAndress2.isNotEmpty() || edtAndress3.isNotEmpty()) {
            RegistrationDataHolder.registrationData.andress1 = binding.edtAndress1.text.toString().trim()
            RegistrationDataHolder.registrationData.andress2 = binding.edtAndress2.text.toString().trim()
            RegistrationDataHolder.registrationData.andress3 = binding.edtAndress3.text.toString().trim()
            return true
        }
        return false
    }

//    override fun onClick(v: View?) {
//        when(v!!.id) {
//            R.id.btnBack -> {
//                findNavController().navigate(R.id.action_andressFragment_to_profileFragment)
//            }
//            R.id.btnNext -> {
//                if(isValid()) {
//                    RegistrationDataHolder.registrationData.andress1 = binding.edtAndress1.text.toString().trim()
//                    RegistrationDataHolder.registrationData.andress2 = binding.edtAndress2.text.toString().trim()
//                    RegistrationDataHolder.registrationData.andress3 = binding.edtAndress3.text.toString().trim()
//                    findNavController().navigate(R.id.action_andressFragment_to_educationFragment)
//                }
//
//            }
//        }
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}