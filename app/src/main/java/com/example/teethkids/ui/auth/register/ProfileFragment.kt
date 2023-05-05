package com.example.teethkids.ui.auth.register

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.navigation.fragment.findNavController
import com.example.teethkids.R
import com.example.teethkids.databinding.FragmentProfileBinding
import com.example.teethkids.utils.RegistrationDataHolder
import java.util.*


class ProfileFragment : Fragment(),DatePickerDialog.OnDateSetListener{
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.edtDateBirth.setOnClickListener { showDatePicker() }
//        binding.btnBack.setOnClickListener(this)
//        binding.btnNext.setOnClickListener(this)

    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val formattedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
        binding.edtDateBirth.setText(formattedDate)
    }

    fun isValid(): Boolean {
        val name = binding.edtName.text.toString().trim()
        val dateBirth = binding.edtDateBirth.text.toString().trim()
        val phoneNumber = binding.edtNumberPhone.unMasked
        val cro = binding.edtCro.unMasked

        if (name.isEmpty()) {
            binding.edtName.error = "Nome não pode ser vazio"
            return false
        }

        if (dateBirth.isEmpty()) {
            binding.edtDateBirth.error = "Data de nascimento não pode ser vazio"
            return false
        }

        if (phoneNumber.isEmpty()) {
            binding.edtNumberPhone.error = "Telefone não pode ser vazio"
            return false
        }

        if (cro.isEmpty()) {
            binding.edtCro.error = "CRO não pode ser vazio"
            return false
        }
        RegistrationDataHolder.registrationData.name = name
        RegistrationDataHolder.registrationData.dateBrith = dateBirth
        RegistrationDataHolder.registrationData.numberPhone = phoneNumber
        RegistrationDataHolder.registrationData.cro = cro
        return true
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}