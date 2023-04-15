package com.example.teethkids.dao

import com.example.teethkids.database.FirebaseHelper.Companion.getDatabase
import com.example.teethkids.model.User
import com.example.teethkids.utils.Utils
import kotlinx.coroutines.tasks.await

class UserDao {

    private val collection = getDatabase().collection("profiles")

//    suspend fun getUserById(id: String): User? {
//        val userDoc = getDatabase().collection("users").document(id).get().await()
//        return if (userDoc.exists()) {
//            val userData = userDoc.toObject(User::class.java)
//            userData?.copy(authUid = userDoc.id)
//        } else {
//            null
//        }
//    }

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
}