package com.example.teethkids.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.teethkids.R
import com.example.teethkids.dao.AuthenticationDAO
import com.example.teethkids.databinding.FragmentProfileMainBinding
import com.example.teethkids.ui.adapter.recyclerviewadapter.OptionsAdapter
import com.example.teethkids.ui.auth.AuthenticateActivity
import com.example.teethkids.utils.Utils.loadImageFromUrl
import com.example.teethkids.viewmodel.UserViewModel


class ProfileMainFragment : Fragment(),View.OnClickListener{

    private lateinit var userViewModel: UserViewModel
    private var _binding: FragmentProfileMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.toolbar.userName.text = user.name
            loadImageFromUrl(user.urlImg,binding.toolbar.profileImage)
        }

        binding.toolbar.btnLogout.setOnClickListener(this)
        binding.listOptions.setHasFixedSize(true)
        val adapter = OptionsAdapter(requireContext()) { option ->
            when (option.name) {
                "Endereços" -> findNavController().navigate(R.id.action_profileMainFragment_to_myAndressesFragment)
                "Meus dados" -> findNavController().navigate(R.id.action_profileMainFragment_to_personalInformationFragment)
            }
        }
        binding.listOptions.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.btnLogout -> {
                val auth = AuthenticationDAO()
                auth.logout()
                val intent = Intent(activity, AuthenticateActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
    }


}