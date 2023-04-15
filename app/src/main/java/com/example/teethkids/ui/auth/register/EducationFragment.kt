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
import com.example.teethkids.databinding.FragmentEducationBinding
import com.example.teethkids.utils.RegistrationDataHolder
import java.util.*


class EducationFragment : Fragment(),DatePickerDialog.OnDateSetListener{
    private var _binding: FragmentEducationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEducationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.edtGraduationDate.setOnClickListener { showDatePicker() }
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
        binding.edtGraduationDate.setText(formattedDate)
    }

    fun isValid(): Boolean {
        val edtInstitution = binding.edtInstitution.text.toString().trim()
        val edtGraduationDate = binding.edtGraduationDate.text.toString().trim()

        if (edtInstitution.isEmpty()) {
            return false
        }

        if (edtGraduationDate.isEmpty()) {
            binding.edtGraduationDate.error = "Data de nascimento não pode ser vazio"
            return false
        }
        RegistrationDataHolder.registrationData.university = binding.edtInstitution.text.toString().trim()
        RegistrationDataHolder.registrationData.graduationDate = binding.edtGraduationDate.text.toString().trim()
        return true
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}