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
import com.example.teethkids.utils.AddressPrimaryId
import com.example.teethkids.utils.Utils
import com.google.firebase.firestore.GeoPoint
import me.relex.circleindicator.CircleIndicator3
import android.Manifest
import android.location.Location
import com.example.teethkids.service.MyLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar

class MyEmergencyDetailsFragment : Fragment() {

    private var _binding: FragmentMyEmergencyDetailsBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val REQUEST_CALL_PERMISSION = 456
    }

    private lateinit var viewPager: ViewPager2
    private lateinit var circleIndicator: CircleIndicator3

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

        val context = requireContext()

        circleIndicator = binding.indicator
        val emergencyId = arguments?.getString("emergencyId")
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

        if (locationArray != null) {
            binding.tvLocation.text = Utils.calculateDistance(
                AddressPrimaryId.addressGeoPoint!!,
                GeoPoint(locationArray[0], locationArray[1])
            )
        }

        binding.toolbar.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_myEmergencyDetailsFragment_to_homeFragment)
        }
        binding.btnFinish.setOnClickListener {
            val dialog = ConfirmationFinalizeEmergency(context, emergencyId.toString())
            dialog.show()
        }

        binding.btnViewMap.setOnClickListener {
            val addressGeoPoint = AddressPrimaryId.addressGeoPoint

            val myLocation = MyLocation(context)
            myLocation.getCurrentLocation { location: Location? ->
                if (location != null) {
                    val lat1 = location.latitude
                    val lon1 = location.longitude

                    val lat2 = addressGeoPoint!!.latitude
                    val lon2 = addressGeoPoint.longitude

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
                    Snackbar.make(
                        binding.root,
                        "Não foi possível obter a localização do dispositivo.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.btnStartCall.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    context,
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

        binding.btnSendLoc.setOnClickListener{
            Snackbar.make(
                binding.root,
                "Localização enviada ao socorrista.",
                Snackbar.LENGTH_SHORT
            ).show()
        }

//        binding.btnReciveLoc.setOnClickListener{
//            Snackbar.make(
//                binding.root,
//                "Localização recebida pelo socorrista.",
//                Snackbar.LENGTH_SHORT
//            ).show()
//        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone"))
                startActivity(intent)
            } else {
                // A permissão foi negada, tratar de acordo com o seu caso
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}