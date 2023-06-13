package com.example.teethkids.ui.home.options

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.teethkids.R
import com.example.teethkids.dao.AddressDao
import com.example.teethkids.database.FirebaseHelper
import com.example.teethkids.databinding.FragmentMyAddressesBinding
import com.example.teethkids.model.Address
import com.example.teethkids.ui.adapter.recyclerviewadapter.ListAddressesAdapter
import com.example.teethkids.ui.dialog.AddAddressDialog
import com.example.teethkids.ui.dialog.OptionAddressDialog
import com.example.teethkids.utils.AddressPrimaryId
import com.example.teethkids.utils.Utils
import com.example.teethkids.viewmodel.AddressViewModel
import com.google.firebase.firestore.GeoPoint


class MyAddressesFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentMyAddressesBinding? = null
    private val binding get() = _binding!!

    private lateinit var listAddressesAdapter: ListAddressesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyAddressesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textAddressNull.isVisible = false
        binding.textAddressNull.isVisible = false
        binding.fab.setOnClickListener(this)
        binding.toolbar.screenName.text = "Meus endereços"
        binding.toolbar.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_myAndressesFragment_to_profileMainFragment)
        }
        setupListAdapter()
        loadAddresses()
    }

    private fun setupListAdapter() {
        listAddressesAdapter = ListAddressesAdapter(requireContext())
        binding.listAddresses.adapter = listAddressesAdapter
    }



    private fun loadAddresses() {
        val addressViewModel = ViewModelProvider(this).get(AddressViewModel::class.java)
        addressViewModel.addressList.observe(viewLifecycleOwner) { addresses ->
            Log.d("test1",addresses.toString())
            listAddressesAdapter.submitList(addresses)
            showAddressState(addresses)
            binding.fab.isEnabled = addresses.size != 3
            val primaryAddress = addresses.find { it.primary }
            val primaryAddressId = primaryAddress?.addressId
            val lat = primaryAddress?.lat
            val lng= primaryAddress?.lng

            AddressPrimaryId.addressPrimaryId = primaryAddressId
          if(primaryAddress != null) {
               AddressPrimaryId.addressGeoPoint = GeoPoint(lat!!,lng!!)
           }



        }
    }

    private fun showAddressState(addressList: List<Address>) {
        if(addressList.isEmpty()) binding.textAddressNull.isVisible = true
        else {
            if (addressList.size > 2) {
                binding.textAddressFull.isVisible = true
            } else {
                binding.textAddressFull.isVisible = false
                binding.textAddressNull.isVisible = false
            }
        }
    }




    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab -> showDialogAdd()

        }
    }


    private fun showDialogAdd() {
        val dialog = AddAddressDialog()
        dialog.show(requireActivity().supportFragmentManager, "FullScreenDialog")
    }




    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}