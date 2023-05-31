package com.example.teethkids.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.teethkids.R
import com.example.teethkids.dao.UserDao
import com.example.teethkids.database.FirebaseHelper
import com.example.teethkids.databinding.FragmentEmergencyBinding
import com.example.teethkids.databinding.FragmentEmergencyListBinding
import com.example.teethkids.databinding.FragmentMyAddressesBinding
import com.example.teethkids.databinding.FragmentPersonalInformationBinding
import com.example.teethkids.ui.adapter.recyclerviewadapter.ListAddressesAdapter
import com.example.teethkids.ui.adapter.recyclerviewadapter.ListEmergencyAdapter
import com.example.teethkids.utils.Utils
import com.example.teethkids.viewmodel.AddressViewModel
import com.example.teethkids.viewmodel.EmergencyViewModel
import com.example.teethkids.viewmodel.UserViewModel


class EmergencyListFragment : Fragment() {

    private var _binding: FragmentEmergencyListBinding? = null
    private val binding get() = _binding!!

    private lateinit var listEmergenciesAdapter: ListEmergencyAdapter

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmergencyListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        setupListAdapter()
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.statusBar.btnStatus.isChecked = user.status
            if (user.status) {
                binding.textViewStateList.isVisible = false
                loadEmergencies()
            } else {
                binding.textViewStateList.isVisible = true
                listEmergenciesAdapter.submitList(null)
            }

            val color = if (binding.statusBar.btnStatus.isChecked)
                ContextCompat.getColor(requireContext(), R.color.greenStatus)
            else
                ContextCompat.getColor(requireContext(), R.color.redStatus)
            binding.statusBar.toolbar.setBackgroundColor(color)
        }

        binding.statusBar.btnStatus.setOnCheckedChangeListener { _, isChecked ->
            val dao = UserDao()
            dao.updateStatus(
                FirebaseHelper.getAuth().uid!!, isChecked,
                onSuccess = {

                })
        }

    }

    private fun setupListAdapter() {
        listEmergenciesAdapter = ListEmergencyAdapter(requireContext()) { emergency ->
            val data = Bundle().apply {
                putString("emergencyId", emergency.emergencyId)
                putString("name", emergency.name)
                putString("phone", emergency.phone)
                putString("createdAt", Utils.formatTimestamp(emergency.createdAt!!))
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