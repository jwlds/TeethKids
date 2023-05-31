package com.example.teethkids.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.teethkids.R
import com.example.teethkids.dao.EmergecyDao
import com.example.teethkids.dao.UserDao
import com.example.teethkids.database.FirebaseHelper
import com.example.teethkids.databinding.FragmentEmergencyDetailsBinding
import com.example.teethkids.databinding.FragmentEmergencyListBinding
import com.example.teethkids.model.ResponseEmergency

import com.example.teethkids.ui.adapter.viewPagerAdapter.PhotoAdapter
import com.example.teethkids.ui.adapter.viewPagerAdapter.StageRegisterAdapter
import com.example.teethkids.utils.Utils
import me.relex.circleindicator.CircleIndicator
import me.relex.circleindicator.CircleIndicator3


class EmergencyDetailsFragment : Fragment(),OnClickListener{


    private var _binding: FragmentEmergencyDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var circleIndicator: CircleIndicator3

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: PhotoAdapter

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
        binding.toolbar.btnBack.setOnClickListener(this)

        circleIndicator = binding.indicator




        binding.btnAccept.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)



        val name = arguments?.getString("name")
        val phone = arguments?.getString("phone")
        val createdAt = arguments?.getString("createdAt")
        val locationArray = arguments?.getDoubleArray("location")
        val photos = arguments?.getStringArrayList("photos")

        if(locationArray != null ) {
            binding.tvLocation.text = Utils.calculateDistance(
                -22.903449,
                -47.063588,
                locationArray[0],
                locationArray[1],

                ).toString()
        }
        binding.tvNome.text = name
        binding.tvPhone.text =  phone
        binding.tvDate.text = createdAt

        val adapter = photos?.let { PhotoAdapter(it) }
        binding.viewPager.adapter = adapter
        viewPager = binding.viewPager
        viewPager.adapter = adapter
        circleIndicator.setViewPager(viewPager)



    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.btnBack -> findNavController().navigate(R.id.action_emergencyDetailsFragment2_to_emergencyListFragment)
            R.id.btnAccept -> {
                val id = arguments?.getString("emergencyId")
                val responseEmergency = ResponseEmergency(
                    professional =  FirebaseHelper.getIdUser().toString(),
                    emergencyId= id.toString(),
                    status = "ACEITADO",
                    dateTime = Utils.getCurrentDateTime()
                )
                val dao = EmergecyDao()
                val daoUser = UserDao()
                daoUser.updateStatusEmergency(id.toString(), onSuccess = {

                })
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
               // binding.loading.isVisible = true
                val id = arguments?.getString("emergency_id")
                val responseEmergency = ResponseEmergency(
                    professional =  FirebaseHelper.getIdUser().toString(),
                    emergencyId= id.toString(),
                    status = "REJEITADA",
                    dateTime = Utils.getCurrentDateTime()
                )
                val dao = EmergecyDao()
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

