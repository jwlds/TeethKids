package com.example.teethkids.ui.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.teethkids.dao.UserDao
import com.example.teethkids.databinding.FragmentHomeBinding
import com.example.teethkids.utils.Utils
import com.example.teethkids.R
import com.example.teethkids.datastore.UserPreferencesRepository
import com.example.teethkids.ui.adapter.recyclerviewadapter.ListMyEmergenciesAdapter
import com.example.teethkids.utils.AddressPrimaryId
import com.example.teethkids.viewmodel.*
import com.google.firebase.firestore.GeoPoint

class HomeFragment : Fragment(){

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var listMyEmergenciesAdapter: ListMyEmergenciesAdapter

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        val userPreferencesRepository = UserPreferencesRepository.getInstance(requireContext())


        Log.d("88",userPreferencesRepository.uid)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            Utils.loadImageFromUrl(user.urlImg, binding.toolbar.profileImage)
            binding.toolbar.userName.text = user.name
            binding.toolbar.ratingTextView.text = Utils.formatRating(user.rating)
        }

        val addressViewModel = ViewModelProvider(this).get(AddressViewModel::class.java)
        addressViewModel.addressList.observe(viewLifecycleOwner) { addresses ->
            val primaryAddress = addresses.find { it.primary }
            val primaryAddressId = primaryAddress?.addressId
            if(primaryAddress != null) {
                val lat = primaryAddress.lat
                val lng= primaryAddress.lng
                val geoPoint = GeoPoint(lat!!,lng!!)
                Log.d("444",geoPoint.toString())
                AddressPrimaryId.addressPrimaryId = primaryAddressId
                AddressPrimaryId.addressGeoPoint = geoPoint
            }

        }

        binding.btnReviewFake.setOnClickListener{
            val dao = UserDao(requireContext())
            dao.fakeReview(onSuccess = {}, onFailure = {})
        }

        binding.btnTest.setOnClickListener{
            val dao = UserDao(requireContext())
            dao.fakeCall(onSuccess = {
                Utils.showToast(requireContext(),"Chamado foi")
            },
            onFailure = {
                Utils.showToast(requireContext(),"Chamado nao  foi")
            })
        }
        setupListAdapter()
        loadEmergencies()

    }

    private fun setupListAdapter() {
        listMyEmergenciesAdapter = ListMyEmergenciesAdapter(requireContext()) { emergency ->
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
            findNavController().navigate(R.id.action_homeFragment_to_myEmergencyDetailsFragment,data)
        }
        binding.listMyEmergency.adapter = listMyEmergenciesAdapter
    }




    private fun loadEmergencies() {
        val emergencyViewModel = ViewModelProvider(this).get(EmergencyViewModel::class.java)
        val myEmergencyViewModel =
            ViewModelProvider(this).get(MyEmergenciesViewModel::class.java)

        myEmergencyViewModel.myEmergenciesList.observe(viewLifecycleOwner) { responses ->
            val myEmergencies = responses
                .filter { it.status != "finished"}
                .filter { it.status != "expired"}
                .map { it.emergencyId.toString() }
            Log.d("tes11", myEmergencies.toString())

            emergencyViewModel.emergencyList.observe(viewLifecycleOwner) { emergencies ->
                val filteredEmergencies =
                    emergencies.filter { it.rescuerUid.toString() in myEmergencies }
                Log.d("tes33",filteredEmergencies.toString())
                listMyEmergenciesAdapter.submitList(filteredEmergencies)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

}