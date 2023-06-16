package com.example.teethkids.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teethkids.database.FirebaseHelper
import com.example.teethkids.database.FirebaseHelper.Companion.getDatabase
import com.example.teethkids.database.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.model.Review
import com.google.firebase.firestore.Query


// Classe é responsável por gerencia os dados das reviews do usuario atual e fornece um objeto  liveData.
class ReviewsViewModel : ViewModel() {
    private val _reviewsList = MutableLiveData<List<Review>>() // Armazena os dados das reviews como MutableLiveData.
    val reviewsList: LiveData<List<Review>> = _reviewsList // LiveData para observação externa.

    init {
        val authUid = getIdUser() // Obtém o ID do usuário autenticado
        if (authUid != null) {
            val profilesRef = getDatabase().collection("profiles").document(authUid)// Obtém a referência do documento do usuário a partir do "authUid".
            val reviewsRef = profilesRef.collection("reviews") // Obtém a referência da sub coleção de reviews no documento do usuário.
            reviewsRef.orderBy("createdAt", Query.Direction.DESCENDING)  // Ordena as reviews por data de criação.
                .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                val reviewsList = mutableListOf<Review>() // Lista temporária
                for (documentSnapshot in querySnapshot?.documents ?: emptyList()) {
                    val address = documentSnapshot.toObject(Review::class.java) // Converte o documento em um objeto "Review".
                    address?.let {
                        reviewsList.add(it) // Adiciona a review à lista temporária ser for diferente de null..
                    }
                }
                _reviewsList.value = reviewsList // Atualiza o valor do _reviewsList com a lista de reviews.
            }
        }
    }

}