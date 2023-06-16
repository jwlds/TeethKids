package com.example.teethkids.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teethkids.database.FirebaseHelper
import com.example.teethkids.database.FirebaseHelper.Companion.getAuth
import com.example.teethkids.database.FirebaseHelper.Companion.getDatabase
import com.example.teethkids.database.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.datastore.UserPreferencesRepository
import com.example.teethkids.model.Address
import com.example.teethkids.utils.AddressPrimaryId


// Classe é responsável por gerencia os endereço do usuario atual e fornece um objeto
//LiveData de somente leitura chamado user para observar as alterações nos endereços do usuário
class AddressViewModel : ViewModel() {
    private val _addressList =
        MutableLiveData<List<Address>>() // Armazena os dados dos endereços como MutableLiveData.
    val addressList: LiveData<List<Address>> = _addressList  // LiveData para observação externa

    init {
        val authUid = getIdUser() // Obtém o ID do usuário autenticado
        if (authUid != null) {
            val profilesRef = getDatabase().collection("profiles")
                .document(authUid) // Obtém a referência da  coleção de responses.
            val addressRef =
                profilesRef.collection("addresses") // Obtém a referência da sub coleção de addresses  no documento do usuário
            addressRef.addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                val addressList = mutableListOf<Address>() // Lista temporária
                for (documentSnapshot in querySnapshot?.documents ?: emptyList()) {
                    val address =
                        documentSnapshot.toObject(Address::class.java) // Converte o documento em um objeto "Address".
                    address?.let {
                        addressList.add(it) // Adiciona o endereço à lista temporária ser for diferente de null.
                    }
                }
                _addressList.value =
                    addressList // Atualiza o valor do _addressList com a lista de endereços.
            }
        }
    }
}
