package com.example.teethkids.ui.home

import android.os.Bundle
import android.util.Log
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
import com.example.teethkids.databinding.FragmentEmergencyListBinding
import com.example.teethkids.ui.adapter.recyclerviewadapter.ListEmergencyAdapter
import com.example.teethkids.utils.AddressPrimaryId
import com.example.teethkids.utils.Utils
import com.example.teethkids.viewmodel.AddressViewModel
import com.example.teethkids.viewmodel.EmergencyResponseViewModel
import com.example.teethkids.viewmodel.EmergencyViewModel
import com.example.teethkids.viewmodel.UserViewModel
import com.google.firebase.firestore.GeoPoint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.provider.Settings


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
        setAddressPrimary()
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        setupListAdapter()
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.statusBar.btnStatus.isChecked = user.status
            if (user.status) {
                binding.toggleButton.addOnButtonCheckedListener { group, checkedId, isChecked ->
                    if (isChecked) {
                        when (checkedId) {
                            R.id.filterAvailable -> {
                                loadEmergencies()
                            }

                            R.id.filterWaiting -> {
                                loadEmergenciesByAccepted()
                            }
                        }
                    }
                }
                binding.toggleButton.isEnabled = true
                binding.toggleButton.check(R.id.filterAvailable)
                binding.textViewStateList.isVisible = false
                loadEmergencies()
            } else {
                binding.textViewStateList.isVisible = true
                listEmergenciesAdapter.submitList(null)
                binding.toggleButton.isEnabled = false
            }

            val color = if (binding.statusBar.btnStatus.isChecked)
                ContextCompat.getColor(requireContext(), R.color.greenStatus)
            else
                ContextCompat.getColor(requireContext(), R.color.redStatus)
            binding.statusBar.toolbar.setBackgroundColor(color)
        }

        binding.statusBar.btnStatus.setOnCheckedChangeListener { _, isChecked ->
            val dao = UserDao(requireContext())
            dao.updateStatus(isChecked,
                onSuccess = {
                    if (isChecked && !isNotificationPermissionGranted()) {
                        requestNotificationPermission()
                    }
                })
        }

    }

    private fun isNotificationPermissionGranted(): Boolean {
        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager.areNotificationsEnabled()
    }

    private fun loadEmergenciesByAccepted() {
        val emergencyViewModel = ViewModelProvider(this).get(EmergencyViewModel::class.java)
        val emergencyResponseViewModel =
            ViewModelProvider(this).get(EmergencyResponseViewModel::class.java)

        emergencyResponseViewModel.emergencyResponseList.observe(viewLifecycleOwner) { responses ->
            val acceptedResourceIds = responses
                .filter { it.status == "waiting" }
                .map { it.rescuerUid }

            emergencyViewModel.emergencyList.observe(viewLifecycleOwner) { emergencies ->
                val filteredEmergencies =
                    emergencies.filter { it.rescuerUid in acceptedResourceIds }
                listEmergenciesAdapter.submitList(filteredEmergencies)
            }
        }
    }

    private fun setupListAdapter() {
        listEmergenciesAdapter = ListEmergencyAdapter(requireContext()) { emergency ->
            val data = Bundle().apply {
                putString("emergencyId", emergency.rescuerUid)
                putString("name", emergency.name)
                putString("status", emergency.status)
                putString("phone", emergency.phoneNumber)
                putString("createdAt", Utils.formatTimestamp(emergency.createdAt!!))
                putStringArrayList("photos", ArrayList(emergency.photos))
                val locationArray = emergency.location?.toDoubleArray()
                putDoubleArray("location", locationArray)
            }
            findNavController().navigate(
                R.id.action_emergencyListFragment_to_emergencyDetailsFragment2,
                data
            )
        }
        binding.listEmergency.adapter = listEmergenciesAdapter
    }

    private fun requestNotificationPermission() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
        startActivity(intent)
    }


    private fun loadEmergencies() {
        val emergencyViewModel = ViewModelProvider(this).get(EmergencyViewModel::class.java)
        val emergencyResponseViewModel =
            ViewModelProvider(this).get(EmergencyResponseViewModel::class.java)

        emergencyResponseViewModel.emergencyResponseList.observe(viewLifecycleOwner) { responses ->
            val acceptedResourceIds = responses
                .filter { it.status in listOf("rejected", "finished", "expired","waiting","onGoing") }
                .map { it.rescuerUid }

            emergencyViewModel.emergencyList.observe(viewLifecycleOwner) { emergencies ->
                val filteredEmergencies =
                    emergencies.filter { it.rescuerUid !in acceptedResourceIds }
                listEmergenciesAdapter.submitList(filteredEmergencies)
            }
        }
    }

    private fun setAddressPrimary() {
        val addressViewModel = ViewModelProvider(this).get(AddressViewModel::class.java)
        addressViewModel.addressList.observe(viewLifecycleOwner) { addresses ->
            Log.d("test1", addresses.toString())
            val primaryAddress = addresses.find { it.primary }
            val primaryAddressId = primaryAddress?.addressId
            if (primaryAddress != null) {
                val lat = primaryAddress.lat
                val lng = primaryAddress.lng
                val geoPoint = GeoPoint(lat!!, lng!!)
                Log.d("444", geoPoint.toString())
                AddressPrimaryId.addressPrimaryId = primaryAddressId
                AddressPrimaryId.addressGeoPoint = geoPoint
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding
    }


}

