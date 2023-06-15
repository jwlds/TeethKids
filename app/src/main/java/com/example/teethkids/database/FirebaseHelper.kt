package com.example.teethkids.database

import com.example.teethkids.datastore.UserPreferencesRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

// Classe responsável por fornece métodos auxiliares para acessar recursos do Firebase
class FirebaseHelper {
    companion object {
        // Obtém a instância do banco de dados do Firestore
        fun getDatabase() =  FirebaseFirestore.getInstance()

        // Obtém a instância das Cloud Functions do Firebase - 'COM A REGIAO ADEQUADA'
        fun getFunctions() = FirebaseFunctions.getInstance("southamerica-east1")

        // Obtém a instância do serviço de autenticação do Firebase
        fun getAuth() = Firebase.auth

        // Obtém o Uid do usuário autenticado
        fun getIdUser() = getAuth().uid
        // Verifica se há um usuário autenticado
        fun isAuth() = getAuth().currentUser != null

         fun getFcmToken(onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
            FirebaseMessaging.getInstance().token
                .addOnSuccessListener { token ->
                    onSuccess(token)
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        }

    }
}