package com.example.teethkids.ui.home

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.teethkids.R
import com.example.teethkids.databinding.FragmentMyEmergencyDetailsBinding
import com.example.teethkids.ui.adapter.viewPagerAdapter.PhotoAdapter
import com.example.teethkids.ui.dialog.ConfirmationFinalizeEmergency
import com.example.teethkids.utils.Utils
import com.google.firebase.firestore.GeoPoint
import me.relex.circleindicator.CircleIndicator3
import android.Manifest
import androidx.lifecycle.ViewModelProvider
import com.example.teethkids.dao.EmergencyDao
import com.example.teethkids.service.MyLocation
import com.example.teethkids.ui.dialog.SendLocationEmergency
import com.example.teethkids.viewmodel.EmergencyResponseViewModel

class MyEmergencyDetailsFragment : Fragment() {

    private var _binding: FragmentMyEmergencyDetailsBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val REQUEST_CALL_PERMISSION = 456
    }

    private lateinit var viewPager: ViewPager2
    private lateinit var circleIndicator: CircleIndicator3

    private var lat2: String? = null
    private var lon2: String? = null
    private var phone: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyEmergencyDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        initClicks()
        updateEmergencyResponseStatus()
    }



    private fun loadData() {
        circleIndicator = binding.indicator


        val name = arguments?.getString("name")
        phone = arguments?.getString("phone")
        val createdAt = arguments?.getString("createdAt")
        val locationArray = arguments?.getDoubleArray("location")
        val photos = arguments?.getStringArrayList("photos")

        binding.tvMyDetialsName.text = name
        binding.tvMyDetialsPhone.text = phone
        binding.tvMyDetialsDate.text = createdAt

        val adapter = photos?.let { PhotoAdapter(it) }
        binding.viewPager.adapter = adapter
        viewPager = binding.viewPager
        viewPager.adapter = adapter
        circleIndicator.setViewPager(viewPager)

        val myLocation = MyLocation(requireContext())
        myLocation.getCurrentLocation { location ->
            if (locationArray != null && location != null) {
                binding.tvLocation.text = Utils.formatDistance(
                    Utils.calculateDistance(
                        GeoPoint(locationArray[0], locationArray[1]),
                        GeoPoint(location.latitude, location.longitude)
                    )
                )
            }
        }
    }

    private fun initClicks() {

        val emergencyId = arguments?.getString("emergencyId")
        val locationArray = arguments?.getDoubleArray("location")
        binding.toolbar.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_myEmergencyDetailsFragment_to_homeFragment)
        }

        binding.btnFinish.setOnClickListener {
            val dialog = ConfirmationFinalizeEmergency(requireContext(), emergencyId.toString())
            dialog.show()
        }

        binding.btnViewMap.setOnClickListener {
            val dao = EmergencyDao()
            dao.updateStatusMove(emergencyId.toString(), 1, onSuccess = {}, onFailure = {})

            val myLocation = MyLocation(requireContext())
            myLocation.getCurrentLocation { location ->
                if (location != null) {
                    val lat1 = location.latitude
                    val lon1 = location.longitude

                    if (locationArray != null) {
                        lat2 = locationArray[0].toString()
                        lon2 = locationArray[1].toString()
                    }

                    val uri =
                        Uri.parse("https://www.google.com/maps/dir/?api=1&origin=$lat1,$lon1&destination=$lat2,$lon2")
                    val intent = Intent(Intent.ACTION_VIEW, uri)

                    if (intent.resolveActivity(requireActivity().packageManager) != null) {
                        intent.setPackage("com.google.android.apps.maps")
                        startActivity(intent)
                    } else {
                        intent.setPackage(null)
                        startActivity(intent)
                    }
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_CALL_PERMISSION
                    )
                }
            }
        }

        binding.btnStartCall.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone"))
                startActivity(intent)
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CALL_PHONE),
                    REQUEST_CALL_PERMISSION
                )
            }
        }

        binding.btnSendLoc.setOnClickListener {
            val dialogLoc = SendLocationEmergency(requireContext(), emergencyId.toString())
            dialogLoc.show()
        }
    }

    private fun updateEmergencyResponseStatus() {
        val emergencyId = arguments?.getString("emergencyId")
        val emergencyResponseViewModel =
            ViewModelProvider(this).get(EmergencyResponseViewModel::class.java)

        emergencyResponseViewModel.emergencyResponseList.observe(viewLifecycleOwner) { emergencies ->
            val response = emergencies.find { it.rescuerUid == emergencyId }
            if (response?.willProfessionalMove == 0) {
                binding.btnViewMap.isEnabled = false
                binding.btnSendLoc.isEnabled = true
            }
            if (response?.willProfessionalMove == 1) {
                binding.btnSendLoc.isEnabled = false
                binding.btnViewMap.isEnabled = true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}