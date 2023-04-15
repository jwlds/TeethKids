package com.example.teethkids.ui.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.teethkids.R
import com.example.teethkids.databinding.FragmentAndressBinding
import com.example.teethkids.utils.RegistrationDataHolder
import com.google.api.Context


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
        val zipe = binding.edtZipe.text.toString().trim()
        val street = binding.edtStreet.text.toString().trim()
        val number  = binding.edtNumber.text.toString().trim()
        val neighborhood = binding.edtNumber.text.toString().trim()
        val city = binding.edtCity.text.toString().trim()
        val state = binding.edtState.text.toString().trim()

        if (zipe.isEmpty()) {
            binding.edtZipe.error = "Cep não pode ser vazio"
           // binding.edtZipe.
            return false
        }

        if (street.isEmpty()) {
            binding.edtStreet.error = "Rua não pode ser vazio"
            return false
        }

        if (number.isEmpty()) {
            binding.edtNumber.error = "Numero não pode ser vazio"
            return false
        }
        if (neighborhood.isEmpty()) {
            binding.edtNeighbBorhood.error = "Bairro não pode ser vazio"
            return false
        }

        if (city.isEmpty()) {
            binding.edtCity.error = "Cidade não pode ser vazio"
            return false
        }
        if (state.isEmpty()) {
            binding.edtState.error = "Estado não pode ser vazio"
            return false
        }

        RegistrationDataHolder.registrationData.zipcode = zipe
        RegistrationDataHolder.registrationData.street  = street
        RegistrationDataHolder.registrationData.numberStreet = number
        RegistrationDataHolder.registrationData.neighborhood = neighborhood
        RegistrationDataHolder.registrationData.city = city
        RegistrationDataHolder.registrationData.state = state
        return true
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