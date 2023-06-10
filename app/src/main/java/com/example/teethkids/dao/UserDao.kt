package com.example.teethkids.dao

import com.example.teethkids.database.FirebaseHelper
import com.example.teethkids.database.FirebaseHelper.Companion.getDatabase
import com.example.teethkids.model.Address
import com.example.teethkids.model.User
import com.example.teethkids.utils.Utils
import com.google.firebase.Timestamp
import kotlinx.coroutines.tasks.await

class UserDao {

    private val collection = getDatabase().collection("profiles")



    fun updateUser(
        authUid: String,
        name: String,
        dateOfBirth: String,
        phone: String,
        cro: String,
        onSuccess: () -> Unit,
//        onFailure: (exeception) -> Unit,
    ) {
        val userRef = collection.document(authUid)
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

    fun updateStatus(authUid: String, status: Boolean, onSuccess: () -> Unit) {
        val userRef = collection.document(authUid)
        userRef.update("status", status)
            .addOnSuccessListener {
                onSuccess()
            }
    }

    fun updateUrlProfileImage(authUid: String, url: String, onSuccess: () -> Unit) {
        val userRef = collection.document(authUid)
        userRef.update("urlImg", url)
            .addOnSuccessListener {
                onSuccess()
            }
    }


    fun fakeCall(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val id =  getDatabase().collection("emergencies").document().id
        val currentTimestamp: Timestamp = Timestamp.now()
        val data = hashMapOf(
            "rescuerUid" to id,
            "name" to "José da Silva",
            "phoneNumber" to "(19) 99999 - 9999",
            "status" to "Disponível",
            "createdAt" to currentTimestamp,
            "location" to listOf(-22.865334, -47.058264),
            "photos" to listOf("https://firebasestorage.googleapis.com/v0/b/teethkids-49f4b.appspot.com/o/EMERGENCIES%2FPHOTOS%2F1.jpg?alt=media&token=a15a465a-86d4-4e4d-942c-383e1dfb5edc",
                "https://firebasestorage.googleapis.com/v0/b/teethkids-49f4b.appspot.com/o/EMERGENCIES%2FPHOTOS%2F1.jpg?alt=media&token=a15a465a-86d4-4e4d-942c-383e1dfb5edc")
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

    fun updateStatusEmergency(id: String, onSuccess: () -> Unit) {
        val userRef = getDatabase().collection("emergencies").document(id)
        userRef.update("status", "ACEITADO")
            .addOnSuccessListener {
                onSuccess()
            }
    }

}