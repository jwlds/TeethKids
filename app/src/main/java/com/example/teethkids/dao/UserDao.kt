package com.example.teethkids.dao

import android.content.Context
import android.util.Log
import com.example.teethkids.database.FirebaseHelper
import com.example.teethkids.database.FirebaseHelper.Companion.getDatabase
import com.example.teethkids.database.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.datastore.UserPreferencesRepository
import com.example.teethkids.model.Address
import com.example.teethkids.model.User
import com.example.teethkids.utils.Utils
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.tasks.await

class UserDao(context: Context) {

    private val collection = getDatabase().collection("profiles")

    private val userPreferencesRepository = UserPreferencesRepository.getInstance(context)


    // Função para atualizar os dados do usuário.
    fun updateUser(
        name: String,
        dateOfBirth: String,
        phone: String,
        cro: String,
        onSuccess: () -> Unit,
//        onFailure: (exeception) -> Unit,
    ) {
        val userRef = collection.document(userPreferencesRepository.uid)
        userRef.update(
            "name", name,
            "dateBirth", dateOfBirth,
            "numberPhone", phone,
            "cro", cro
        ).addOnSuccessListener {
            onSuccess()
//        }.addOnFailureListener { exception ->
//            onFailure(exception)
//        }
        }

    }

    // Função para atualizar a descrição profissional do usuário.
    fun updateDescription(professionalDescription: String, onSuccess: () -> Unit) {
        val userRef = collection.document(userPreferencesRepository.uid)
        userRef.update("professionalDescription", professionalDescription)
            .addOnSuccessListener { onSuccess() }
    }


    // Função para atualizar o status de emergencias do usuário.
    fun updateStatus(status: Boolean, onSuccess: () -> Unit) {
        val userRef = collection.document(userPreferencesRepository.uid)
        userRef.update("status", status)
            .addOnSuccessListener {
                onSuccess()
            }
    }

    // Função para atualizar a URL da imagem de perfil do usuário.
    fun updateUrlProfileImage(url: String, onSuccess: () -> Unit) {
        val userRef = collection.document(userPreferencesRepository.uid)
        userRef.update("urlImg", url)
            .addOnSuccessListener {
                onSuccess()
            }
    }


    // Função para simular uma chamada falsa de emergência (Apenas para teste)
    fun fakeCall(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val id = getDatabase().collection("emergencies").document().id
        val currentTimestamp: Timestamp = Timestamp.now()
        val data = hashMapOf(
            "rescuerUid" to id,
            "name" to "José da Silva",
            "phoneNumber" to "(19) 99999 - 9999",
            "status" to "waiting",
            "createdAt" to currentTimestamp,
            "location" to listOf(-22.835326, -47.052990),
            "photos" to listOf(
                "https://firebasestorage.googleapis.com/v0/b/teethkids-49f4b.appspot.com/o/EMERGENCIES%2FPHOTOS%2F1.jpg?alt=media&token=a15a465a-86d4-4e4d-942c-383e1dfb5edc",
                "https://firebasestorage.googleapis.com/v0/b/teethkids-49f4b.appspot.com/o/EMERGENCIES%2FPHOTOS%2F1.jpg?alt=media&token=a15a465a-86d4-4e4d-942c-383e1dfb5edc"
            )
        )
        getDatabase()
            .collection("emergencies")
            .document(id)
            .set(data)
            .addOnSuccessListener { documentReference ->
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }


    // Função para simular uma avaliação falsa (Apenas para teste)
    fun fakeReview(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val id = getDatabase().collection("emergencies").document().id
        val currentTimestamp: Timestamp = Timestamp.now()
        val data = hashMapOf(
            "emergencyId" to id,
            "createdAt" to currentTimestamp,
            "name" to "Gustavo",
            "rating" to 3.0,
            "review" to "sdjsdhlshsada",
            "revision" to false,
        )
        val userRef = getDatabase().collection("profiles").document(userPreferencesRepository.uid)
        userRef.collection("reviews")
            .document(id)
            .set(data)
            .addOnSuccessListener { documentReference ->
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }


    // Função para atualizar a  média da nota do usuário.
    fun updateRating(average: Double, onSuccess: () -> Unit) {
        val userRef = collection.document(userPreferencesRepository.uid)
        userRef.update("rating", average)
            .addOnSuccessListener {
                onSuccess()
            }
    }

    // Função para atualizar o fcmToken de registro do usuário.
    fun sendRegistrationToServer(token: String) {
        val authUid = userPreferencesRepository.uid
        val userRef =collection.document(authUid)
        userRef.update("fcmToken", token)
            .addOnSuccessListener {
                Log.d("sendRegistrationToServer", "Refreshed token: $token")
            }
            .addOnFailureListener { exception ->
                Log.d("sendRegistrationToServer", "Erro Refreshed token: $exception")
            }
    }

}