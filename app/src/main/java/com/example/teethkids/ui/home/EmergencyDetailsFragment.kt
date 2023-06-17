package com.example.teethkids.ui.home

import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.teethkids.R
import com.example.teethkids.dao.EmergencyDao
import com.example.teethkids.Helper.FirebaseHelper
import com.example.teethkids.databinding.FragmentEmergencyDetailsBinding
import com.example.teethkids.model.ResponseEmergency
import com.example.teethkids.service.MyLocation
import com.example.teethkids.ui.adapter.viewPagerAdapter.PhotoAdapter
import com.example.teethkids.utils.Utils
import com.example.teethkids.utils.Utils.formatDistance
import com.example.teethkids.viewmodel.EmergencyResponseViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import me.relex.circleindicator.CircleIndicator3

class EmergencyDetailsFragment : Fragment(), OnClickListener {

    private var _binding: FragmentEmergencyDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var circleIndicator: CircleIndicator3

    private lateinit var viewPager: ViewPager2

    private var response: ResponseEmergency? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmergencyDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.screenName.text = "Detalhes"
        setupActionBar()
        circleIndicator = binding.indicator
        binding.btnAccept.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
        loadData()
    }

    private fun loadData() {
        val emergencyId = arguments?.getString("emergencyId")
        val name = arguments?.getString("name")
        val status = arguments?.getString("status")
        val createdAt = arguments?.getString("createdAt")
        val locationArray = arguments?.getDoubleArray("location")
        val photos = arguments?.getStringArrayList("photos")

        val emergencyResponseViewModel = ViewModelProvider(this).get(EmergencyResponseViewModel::class.java)

        emergencyResponseViewModel.emergencyResponseList.observe(viewLifecycleOwner) { emergencies ->
            response = emergencies.find { it.rescuerUid == emergencyId }
            binding.btnAccept.isEnabled = response?.status != "waiting"
            binding.btnCancel.text = if (response?.status == "waiting") "Cancelar" else "Rejeitar"
        }

        Utils.setStatusIconColor(binding.tvStatus, status!!)
        binding.tvDate.text = createdAt
        binding.tvNome.text = name
        binding.tvStatusText.text = Utils.translateStatus(status)

        val adapter = photos?.let { PhotoAdapter(it) }
        binding.viewPager.adapter = adapter
        viewPager = binding.viewPager
        viewPager.adapter = adapter
        circleIndicator.setViewPager(viewPager)

        val myLocation = MyLocation(requireContext())
        myLocation.getCurrentLocation { location: Location? ->
            if (locationArray != null && location != null) {
                val distance = Utils.calculateDistance(
                    GeoPoint(locationArray[0], locationArray[1]),
                    GeoPoint(location.latitude, location.longitude)
                )
                binding.tvLocation.text = formatDistance(distance)
            }
        }
    }

    private fun setupActionBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }




    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnBack -> findNavController().navigate(R.id.action_emergencyDetailsFragment2_to_emergencyListFragment)
            R.id.btnAccept -> {
                val id = arguments?.getString("emergencyId")
                val currentTimestamp: Timestamp = Timestamp.now()
                val responseEmergency = ResponseEmergency(
                    professionalUid = FirebaseHelper.getIdUser().toString(),
                    rescuerUid = id.toString(),
                    status = "waiting",
                    acceptedAt = currentTimestamp ,
                    willProfessionalMove = -1
                )
                val dao = EmergencyDao()
                dao.createResponseEmergency(responseEmergency,
                    onSuccess = {
                        findNavController().navigate(R.id.action_emergencyDetailsFragment2_to_emergencyListFragment)
                    },
                    onFailure = { exception ->
                        findNavController().navigate(R.id.action_emergencyDetailsFragment2_to_emergencyListFragment)
                    }
                )
            }
            R.id.btnCancel -> {
                if (response?.status == "waiting") {
                    updateStatusEmergencyResponse("rejected", response?.rescuerUid, onSuccess = {
                        findNavController().navigate(R.id.action_emergencyDetailsFragment2_to_emergencyListFragment)
                    })
                } else {
                    val id = arguments?.getString("emergencyId")
                    val currentTimestamp: Timestamp = Timestamp.now()
                    val responseEmergency = ResponseEmergency(
                        professionalUid = FirebaseHelper.getIdUser().toString(),
                        rescuerUid = id.toString(),
                        status = "rejected",
                        acceptedAt = currentTimestamp,
                        willProfessionalMove = -1
                    )
                    val dao = EmergencyDao()
                    dao.createResponseEmergency(responseEmergency,
                        onSuccess = {
                            findNavController().navigate(R.id.action_emergencyDetailsFragment2_to_emergencyListFragment)
                        },
                        onFailure = { exception ->
                            findNavController().navigate(R.id.action_emergencyDetailsFragment2_to_emergencyListFragment)
                        }
                    )
                }
            }
        }
    }

    private fun updateStatusEmergencyResponse(status: String, id: String?, onSuccess: () -> Unit) {
        val collectionRef = FirebaseHelper.getDatabase().collection("responses")
        val query = collectionRef.whereEqualTo("rescuerUid", id)

        query.get().addOnSuccessListener { querySnapshot ->
            val batch = FirebaseHelper.getDatabase().batch()

            for (document in querySnapshot.documents) {
                val documentRef = collectionRef.document(document.id)
                batch.update(documentRef, "status", status)
            }

            batch.commit().addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener { exception ->

            }
        }.addOnFailureListener { exception ->

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

