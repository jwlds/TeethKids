package com.example.teethkids.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teethkids.Helper.FirebaseHelper.Companion.getDatabase
import com.example.teethkids.model.Emergency
import com.google.firebase.firestore.Query


// Classe é responsável por gerencia os dados das emergencias e fornece um objeto  liveData.
class EmergencyViewModel() : ViewModel() {



    private val _emergencyList = MutableLiveData<List<Emergency>>()// Armazena os dados das  emergencias como MutableLiveData.
    val emergencyList: LiveData<List<Emergency>> = _emergencyList // LiveData para observação externa

    init {

        val emergencyRef = getDatabase().collection("emergencies") // Obtém a referência da  coleção de emergencias..
        emergencyRef.orderBy("createdAt", Query.Direction.DESCENDING) // Ordena as reviews por data de criação.
            .addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if (querySnapshot != null) {
                val emergencies = mutableListOf<Emergency>() // Lista temporária
                for (document in querySnapshot.documents) {
                    val data = document.toObject(Emergency::class.java) // Converte o documento em um objeto "Emergency".

                    data?.let {
                        emergencies.add(it) // Adiciona a emergencia à lista temporária ser for diferente de null.
                        }

                    }
                _emergencyList.value = emergencies // Atualiza o valor do _emergencyList com a lista de emergencias.
            }
        }
    }
}