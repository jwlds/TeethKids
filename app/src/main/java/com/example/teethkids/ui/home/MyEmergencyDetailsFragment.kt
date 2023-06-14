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

class MyEmergencyDetailsFragment : Fragment() {

    private var _binding: FragmentMyEmergencyDetailsBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123
    }

    private lateinit var viewPager: ViewPager2

    private lateinit var circleIndicator: CircleIndicator3

    private lateinit var adapter: PhotoAdapter

    private var lat1: Double? = null
    private var lon1: Double? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyEmergencyDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        circleIndicator = binding.indicator
        val emergencyId = arguments?.getString("emergencyId")
        val name = arguments?.getString("name")
        val phone = arguments?.getString("phone")
        val createdAt = arguments?.getString("createdAt")
        val locationArray = arguments?.getDoubleArray("location")
        val photos = arguments?.getStringArrayList("photos")

        binding.tvMyDetialsLabelName.text = name
        binding.tvMyDetialsLabelPhone.text = phone
        binding.tvMyDetialsDate.text = createdAt

        val adapter = photos?.let { PhotoAdapter(it) }
        binding.viewPager.adapter = adapter
        viewPager = binding.viewPager
        viewPager.adapter = adapter
        circleIndicator.setViewPager(viewPager)


        if(locationArray != null) {
            binding.tvLocation.text = Utils.calculateDistance(
                AddressPrimaryId.addressGeoPoint!!,
                GeoPoint(locationArray[0],locationArray[1])
            )
        }

        binding.tvMyDetialsLabelName.text = name
        binding.tvMyDetialsLabelName.text = name


        binding.toolbar.btnBack.setOnClickListener{
            findNavController().navigate(R.id.action_myEmergencyDetailsFragment_to_homeFragment)
        }
        binding.btnFinish.setOnClickListener{
            val dialog = ConfirmationFinalizeEmergency(requireContext(),emergencyId.toString())
            dialog.show()
        }

        binding.btnViewMap.setOnClickListener{
            val addressGeoPoint = AddressPrimaryId.addressGeoPoint


//            if(locationArray != null) {
//                 lat1 = locationArray[0]
//                  lon1 = locationArray[1]
//            }

             lat1 =  -22.834980
             lon1 = -47.053018
            val lat2 = addressGeoPoint!!.latitude
            val lon2 = addressGeoPoint.longitude



            val uri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=$lat1,$lon1&destination=$lat2,$lon2")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps") // abrir no aplicativo do Google Maps
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            } else {
                // O aplicativo do Google Maps não está instalado, abrir em um navegador
                intent.setPackage(null)
                startActivity(intent)
            }
        }

        binding.btnStartCall.setOnClickListener{

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}