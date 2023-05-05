package com.example.teethkids.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teethkids.database.FirebaseHelper.Companion.getAuth
import com.example.teethkids.database.FirebaseHelper.Companion.getDatabase
import com.example.teethkids.model.Address

class AddressViewModel : ViewModel() {
    private val _addressList = MutableLiveData<List<Address>>()
    val addressList: LiveData<List<Address>> = _addressList

    init {
        val authUid = getAuth().currentUser?.uid
        if (authUid != null) {
            val profilesRef = getDatabase().collection("profiles").document(authUid)
            val addressRef = profilesRef.collection("address")
            addressRef.addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                val addressList = mutableListOf<Address>()
                for (documentSnapshot in querySnapshot?.documents ?: emptyList()) {
                    val address = documentSnapshot.toObject(Address::class.java)
                    address?.let {
                        addressList.add(it)
                    }
                }
                _addressList.value = addressList
            }
        }
    }

    fun updateAddressList(addressList: List<Address>) {
        _addressList.value = addressList
    }
}
