package com.example.teethkids.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teethkids.Helper.FirebaseHelper
import com.example.teethkids.Helper.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.model.ResponseEmergency


// Classe é responsável por gerencia os dados das respostas das emergencias do usuario atual e fornece um objeto  liveData.
class EmergencyResponseViewModel : ViewModel()  {
    private val _emergencyResponseList = MutableLiveData<List<ResponseEmergency>>()// Armazena os dados das respostas das emergencias como MutableLiveData.
    val emergencyResponseList: LiveData<List<ResponseEmergency>> = _emergencyResponseList // LiveData para observação externa

    init {
        val emergencyResponseRef = FirebaseHelper.getDatabase().collection("responses") // Obtém a referência da  coleção de responses.
        emergencyResponseRef.whereEqualTo("professionalUid", getIdUser().toString()) // Obtém apenas que "professionalUid" seja igual o usuario atual.
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    val emergencyResponses = mutableListOf<ResponseEmergency>() // Lista temporária
                    for (document in querySnapshot.documents) {
                        val data = document.toObject(ResponseEmergency::class.java) // Converte o documento em um objeto "ResponseEmergency".
                        data?.let {
                            emergencyResponses.add(it) // Adiciona a resposta da emergencia à lista temporária ser for diferente de null.
                        }
                    }
                    _emergencyResponseList.value = emergencyResponses // Atualiza o valor do emergencyResponseList com a lista de respostas das emergencias.
                }
            }
    }
}