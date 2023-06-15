package com.example.teethkids.ui.home.options

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.teethkids.R
import com.example.teethkids.dao.UserDao
import com.example.teethkids.databinding.FragmentMyAddressesBinding
import com.example.teethkids.databinding.FragmentMyDescriptionBinding
import com.example.teethkids.ui.adapter.recyclerviewadapter.ListHelpOptionAdapter
import com.example.teethkids.utils.Utils
import com.example.teethkids.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar

class MyDescriptionFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentMyDescriptionBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.edtDescription.setText(user.professionalDescription)
        }
        binding.btnUpdate.setOnClickListener(this)

        binding.toolbar.screenName.text = "Mini Currículo"
        binding.toolbar.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_myDescriptionFragment_to_profileMainFragment)
        }
    }

    override fun onClick(v: View?){
        when(v!!.id) {
            R.id.btnUpdate -> {
                val user = UserDao(requireContext())
                user.updateDescription(
                    professionalDescription = binding.edtDescription.text.toString().trim(),
                    onSuccess = {
                        binding.loading.isVisible = false
                        Utils.showToast(requireContext(), "Mini currículo atualizado.")
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}