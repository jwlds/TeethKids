package com.example.teethkids.ui.home.options

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.teethkids.R
import com.example.teethkids.databinding.FragmentMyAndressesBinding
import com.example.teethkids.databinding.FragmentPersonalInformationBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MyAndressesFragment : Fragment(),View.OnClickListener{

    private var _binding: FragmentMyAndressesBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyAndressesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.screenName.text = "Meus endereços"
        binding.toolbar.btnBack.setOnClickListener{
            findNavController().navigate(R.id.action_myAndressesFragment_to_profileMainFragment)
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id)  {

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}