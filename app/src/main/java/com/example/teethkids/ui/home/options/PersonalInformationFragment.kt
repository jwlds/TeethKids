package com.example.teethkids.ui.home.options

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.teethkids.R
import com.example.teethkids.dao.UserDao
import com.example.teethkids.databinding.FragmentPersonalInformationBinding
import com.example.teethkids.ui.dialog.UpdateImgProfileDialog
import com.example.teethkids.utils.Utils
import com.example.teethkids.viewmodel.UserViewModel
import java.util.*


class PersonalInformationFragment : Fragment(),View.OnClickListener, DatePickerDialog.OnDateSetListener{
    private var _binding: FragmentPersonalInformationBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private var name: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalInformationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupActionBar()
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            Utils.loadImageFromUrl(user.urlImg, binding.imgProfile)
            binding.edtName.setText(user.name)
            name = user.name

            binding.edtDateBirth.setText(user.dateBrith)
            binding.edtNumberPhone.setText(user.numberPhone)
            binding.edtCro.setText(user.cro)
            binding.edtEmail.setText(user.email)
        }
        binding.btnUpdate.setOnClickListener(this)
        binding.btnUpdateImgProfile.setOnClickListener(this)
        binding.edtDateBirth.setOnClickListener { showDatePicker() }
        binding.toolbar.screenName.text = "Meus dados"


    }

    private fun setupActionBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.btnUpdate -> {
                val user = UserDao(requireContext())
                user.updateUser(
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
            R.id.btnUpdateImgProfile -> {showDialogUpdateImgProfile()}

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

    private fun showDialogUpdateImgProfile() {
        val dialog = UpdateImgProfileDialog(name)
        dialog.show(requireActivity().supportFragmentManager, "dialogUpdateImgProfile")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}