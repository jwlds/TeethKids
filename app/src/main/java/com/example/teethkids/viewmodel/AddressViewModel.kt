package com.example.teethkids.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teethkids.database.FirebaseHelper
import com.example.teethkids.database.FirebaseHelper.Companion.getAuth
import com.example.teethkids.database.FirebaseHelper.Companion.getDatabase
import com.example.teethkids.database.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.model.Address
import com.example.teethkids.utils.AddressPrimaryId


// Classe é responsável por gerencia os endereço do usuario atual e fornece um objeto
//LiveData de somente leitura chamado user para observar as alterações nos endereços do usuário
class AddressViewModel : ViewModel() {
    private val _addressList = MutableLiveData<List<Address>>()
    val addressList: LiveData<List<Address>> = _addressList

    init {
        val authUid = getIdUser()
        if (authUid != null) {
            val profilesRef = getDatabase().collection("profiles").document(authUid)
            val addressRef = profilesRef.collection("addresses")
            addressRef.addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                val addressList = mutableListOf<Address>()
                for (documentSnapshot in querySnapshot?.documents ?: emptyList()) {
                    Log.d("444",documentSnapshot.toString())
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
