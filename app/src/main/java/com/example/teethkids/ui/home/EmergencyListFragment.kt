package com.example.teethkids.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.teethkids.R
import com.example.teethkids.databinding.FragmentEmergencyBinding
import com.example.teethkids.databinding.FragmentEmergencyListBinding
import com.example.teethkids.databinding.FragmentMyAddressesBinding
import com.example.teethkids.databinding.FragmentPersonalInformationBinding
import com.example.teethkids.ui.adapter.recyclerviewadapter.ListAddressesAdapter
import com.example.teethkids.ui.adapter.recyclerviewadapter.ListEmergencyAdapter
import com.example.teethkids.viewmodel.AddressViewModel
import com.example.teethkids.viewmodel.EmergencyViewModel


class EmergencyListFragment : Fragment() {

    private var _binding: FragmentEmergencyListBinding? = null
    private val binding get() = _binding!!

    private lateinit var listEmergenciesAdapter: ListEmergencyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmergencyListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
        loadEmergencies()
    }

    private fun setupListAdapter() {
        listEmergenciesAdapter = ListEmergencyAdapter(requireContext()) { emergency ->
            val data = Bundle().apply {
                putString("emergencyId", emergency.emergencyId)
                putString("name", emergency.name)
                putString("phone", emergency.phone)
                putString("dateTime", emergency.dateTime)
                putStringArrayList("photos", ArrayList(emergency.photos))
                val locationArray = emergency.location?.toDoubleArray()
                putDoubleArray("location", locationArray)
            }
            findNavController().navigate(R.id.action_emergencyListFragment_to_emergencyDetailsFragment2,data)
    }
        binding.listEmergency.adapter = listEmergenciesAdapter
    }



    private fun loadEmergencies() {
        val emergencyViewModel = ViewModelProvider(this).get(EmergencyViewModel::class.java)
        emergencyViewModel.emergencyList.observe(viewLifecycleOwner) { emergencies ->
            listEmergenciesAdapter.submitList(emergencies)
        }
    }


}