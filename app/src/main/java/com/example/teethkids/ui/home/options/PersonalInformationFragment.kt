package com.example.teethkids.ui.home.options

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.teethkids.R
import com.example.teethkids.dao.AuthenticationDAO
import com.example.teethkids.dao.UserDao
import com.example.teethkids.database.FirebaseHelper
import com.example.teethkids.database.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.databinding.FragmentPersonalInformationBinding
import com.example.teethkids.databinding.FragmentProfileMainBinding
import com.example.teethkids.ui.home.MainActivity
import com.example.teethkids.utils.Utils
import com.google.firebase.firestore.auth.User
import java.util.*


class PersonalInformationFragment : Fragment(),View.OnClickListener, DatePickerDialog.OnDateSetListener{
    private var _binding: FragmentPersonalInformationBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalInformationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnUpdate.setOnClickListener(this)
        binding.edtDateBirth.setOnClickListener { showDatePicker() }
        binding.toolbar.screenName.text = "Meus dados"
        binding.toolbar.btnBack.setOnClickListener{
            findNavController().navigate(R.id.action_personalInformationFragment_to_profileMainFragment)
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.btnUpdate -> {
                val user = UserDao()
                user.updateUser(
                    authUid = getIdUser().toString(),
                    name = binding.edtName.text.toString().trim(),
                    dateOfBirth = binding.edtDateBirth.text.toString().trim(),
                    phone = binding.edtNumberPhone.text.toString().trim(),
                    cro = binding.edtCro.text.toString().trim(),
                    onSuccess = {
                        binding.loading.isVisible = false
                        Utils.showToast(requireContext(),"Update realizado com sucesso!")
                    }
                )
            }
        }
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}