package com.example.teethkids.ui.home

import EmergencyViewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.teethkids.Helper.FirebaseHelper
import com.example.teethkids.dao.UserDao
import com.example.teethkids.databinding.FragmentHomeBinding
import com.example.teethkids.utils.Utils
import com.example.teethkids.R
import com.example.teethkids.ui.adapter.recyclerviewadapter.ListMyEmergenciesAdapter
import com.example.teethkids.viewmodel.*

class HomeFragment : Fragment() {

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

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            Utils.loadImageFromUrl(user.urlImg, binding.toolbar.profileImage)
            binding.toolbar.userName.text = user.name
            binding.toolbar.ratingTextView.text = Utils.formatRating(user.rating)
        }

        initClicks()
        setupListAdapter()
        loadEmergencies()

    }


    private fun initClicks() {
        binding.btnReviewFake.setOnClickListener {
            val dao = UserDao(requireContext())
            dao.fakeReview(onSuccess = {}, onFailure = {})
        }

        binding.btnTest.setOnClickListener {
            val dao = UserDao(requireContext())
            dao.fakeCall(onSuccess = {
                Utils.showToast(requireContext(), "Chamado foi")
            },
                onFailure = {
                    Utils.showToast(requireContext(), "Chamado nao  foi")
                })
        }
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
            findNavController().navigate(
                R.id.action_homeFragment_to_myEmergencyDetailsFragment,
                data
            )
        }
        binding.listMyEmergency.adapter = listMyEmergenciesAdapter
    }


    private fun loadEmergencies() {
        val emergencyViewModel =
            ViewModelProvider(this).get(EmergencyViewModel::class.java)
        val myEmergencyViewModel =
            ViewModelProvider(this).get(MyEmergenciesViewModel::class.java)

        myEmergencyViewModel.myEmergenciesList.observe(viewLifecycleOwner) { responses ->
            val myEmergencies = responses
                .filter { it.status != "finished" }
                .filter { it.status != "expired" }
                .map { it.emergencyId.toString() }
            Log.d("tes11", myEmergencies.toString())

            emergencyViewModel.emergencyList.observe(viewLifecycleOwner) { emergencies ->
                val filteredEmergencies =
                    emergencies.filter { it.rescuerUid.toString() in myEmergencies }
                Log.d("tes33", filteredEmergencies.toString())
                listMyEmergenciesAdapter.submitList(filteredEmergencies)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}