package com.example.teethkids.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teethkids.database.FirebaseHelper
import com.example.teethkids.model.MyEmergency
import com.google.firebase.firestore.Query


// Classe é responsável por gerencia os dados das emergencias do usuario atual e fornece um objeto  liveData.
class MyEmergenciesViewModel : ViewModel(){

    private val _myEmergenciesList = MutableLiveData<List<MyEmergency>>() // Armazena os dados das minhas emergencias como MutableLiveData.
    val myEmergenciesList: LiveData<List<MyEmergency>> = _myEmergenciesList // LiveData para observação externa

    init {
        val authUid = FirebaseHelper.getIdUser()
        if (authUid != null) {
            val profilesRef = FirebaseHelper.getDatabase().collection("profiles").document(authUid) // Obtém a referência do documento do usuário a partir do "authUid".
            val emergencyRef = profilesRef.collection("myEmergencies") // Obtém a referência da sub coleção de emergencias  no documento do usuário.
            emergencyRef.orderBy("createdAt", Query.Direction.DESCENDING)// Ordena as reviews por data de criação.
                .addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        return@addSnapshotListener
                    }
                    if (querySnapshot != null) {
                        val myEmergenciesList = mutableListOf<MyEmergency>() // Lista temporária
                        for (document in querySnapshot.documents) {
                            val data = document.toObject(MyEmergency::class.java)// Converte o documento em um objeto "MyEmergency".
                                data?.let {
                                    myEmergenciesList.add(it)  // Adiciona a review à lista temporária ser for diferente de null.
                                }

                        }
                        _myEmergenciesList.value = myEmergenciesList // Atualiza o valor do _myEmergenciesList com a lista de emergencias.
                    }
                }
        }
    }
}