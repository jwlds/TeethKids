package com.example.teethkids.ui.home

import android.Manifest
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
import com.example.teethkids.databinding.FragmentEmergencyListBinding
import com.example.teethkids.ui.adapter.recyclerviewadapter.ListEmergencyAdapter
import com.example.teethkids.utils.Utils
import com.example.teethkids.viewmodel.EmergencyResponseViewModel
import com.example.teethkids.viewmodel.EmergencyViewModel
import com.example.teethkids.viewmodel.UserViewModel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.example.teethkids.service.MyLocation
import com.google.firebase.firestore.GeoPoint


class EmergencyListFragment : Fragment() {

    private var _binding: FragmentEmergencyListBinding? = null
    private val binding get() = _binding!!

    private lateinit var listEmergenciesAdapter: ListEmergencyAdapter

    private lateinit var userViewModel: UserViewModel

    private val LOCATION_PERMISSION_REQUEST_CODE = 111

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
                checkLocationPermission()
                setupToggleListener()
                binding.textViewStateList.isVisible = false
                loadEmergencies()
            } else {
                binding.textViewStateList.isVisible = true
                listEmergenciesAdapter.submitList(null)
                binding.toggleButton.isEnabled = false
            }
            updateStatusBarColor()
        }
        initClicks()
    }



    private fun initClicks() {
        binding.statusBar.btnStatus.setOnCheckedChangeListener { _, isChecked ->
            val dao = UserDao(requireContext())
            dao.updateStatus(isChecked) {
                if (isChecked && !isNotificationPermissionGranted()) {
                    requestNotificationPermission()
                }
            }
        }
    }

    private fun isNotificationPermissionGranted(): Boolean {
        val notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager.areNotificationsEnabled()
    }

    private fun setupToggleListener() {
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
    }

    private fun updateStatusBarColor() {
        val color = if (binding.statusBar.btnStatus.isChecked) {
            ContextCompat.getColor(requireContext(), R.color.greenStatus)
        } else {
            ContextCompat.getColor(requireContext(), R.color.redStatus)
        }
        binding.statusBar.toolbar.setBackgroundColor(color)
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

    private fun loadEmergenciesByAccepted() {
        val emergencyViewModel = ViewModelProvider(this).get(EmergencyViewModel::class.java)
        val emergencyResponseViewModel = ViewModelProvider(this).get(EmergencyResponseViewModel::class.java)

        emergencyResponseViewModel.emergencyResponseList.observe(viewLifecycleOwner) { responses ->
            val acceptedResourceIds = responses
                .filter { it.status == "waiting" }
                .map { it.rescuerUid }

            emergencyViewModel.emergencyList.observe(viewLifecycleOwner) { emergencies ->
                val filteredEmergencies = emergencies.filter { it.rescuerUid in acceptedResourceIds }
                listEmergenciesAdapter.submitList(filteredEmergencies)
            }
        }
    }

    private fun loadEmergencies() {
        val emergencyViewModel = ViewModelProvider(this).get(EmergencyViewModel::class.java)
        val emergencyResponseViewModel = ViewModelProvider(this).get(EmergencyResponseViewModel::class.java)
        val myLocation = MyLocation(requireContext())

        emergencyResponseViewModel.emergencyResponseList.observe(viewLifecycleOwner) { responses ->
            val acceptedStatusList = listOf("rejected", "finished", "expired", "waiting", "onGoing")
            val acceptedResourceIds = responses.filter { it.status in acceptedStatusList }.map { it.rescuerUid }

            myLocation.getCurrentLocation { location: Location? ->
                location?.let {
                    emergencyViewModel.emergencyList.observe(viewLifecycleOwner) { emergencies ->
                        val filteredEmergencies = emergencies.filter { emergency ->
                            emergency.rescuerUid !in acceptedResourceIds &&
                                    Utils.calculateDistance(
                                        GeoPoint(it.latitude, it.longitude),
                                        GeoPoint(emergency.location!![0], emergency.location[1])
                                    ) <= 20.00
                        }
                        listEmergenciesAdapter.submitList(filteredEmergencies)
                    }
                }
            }
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Solicita permissão de GPS
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

