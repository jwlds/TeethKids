package com.example.teethkids.dao

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.teethkids.R
import com.example.teethkids.database.FirebaseRoom
import com.example.teethkids.database.FirebaseRoom.Companion.getDatabase
import com.example.teethkids.database.FirebaseRoom.Companion.getFunctions
import com.example.teethkids.database.FirebaseRoom.Companion.getIdUser
import com.example.teethkids.model.RegistrationData
import com.example.teethkids.utils.RegistrationDataHolder
import com.example.teethkids.utils.Utils
import com.example.teethkids.utils.Utils.getFirebaseErrorMessage
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser

class AuthenticationDAO {


    //login
     fun login(
        email: String,
        password: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (String) -> Unit) {
        FirebaseRoom.getAuth().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                onSuccess(result.user!!)
            }
            .addOnFailureListener { exception ->
                onFailure(getFirebaseErrorMessage(exception))
            }
    }

    //register
    fun registerAccount(
        data: RegistrationData,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        FirebaseRoom.getAuth().createUserWithEmailAndPassword(data.email!!, data.password!!)
            .addOnSuccessListener {
                val currentUser = getIdUser().toString()
               Utils.uploadProfileImage(data.photo!!,currentUser)
                   .addOnSuccessListener { url ->
                       val userData = hashMapOf(
                           "id" to currentUser,
                           "email" to data.email,
                           "name" to data.name,
                           "dateBrith" to data.dateBrith,
                           "numberPhone" to data.numberPhone,
                           "cro" to data.cro,
                           "andress1" to data.andress1,
                           "andress2" to data.andress2,
                           "andress3" to data.andress3,
                           "university" to data.university,
                           "graduationDate" to data.graduationDate,
                       )
                       val createUser = getFunctions().getHttpsCallable("createUser")
                       val data = hashMapOf(
                           "userId" to currentUser,
                           "userData" to userData
                       )
                       createUser.call(data)
                           .addOnSuccessListener {
                               onSuccess()
                           }
                           .addOnFailureListener { exception ->
                               onFailure(getFirebaseErrorMessage(exception))
                           }
                   }
                   }
            .addOnFailureListener { exception ->
                onFailure(getFirebaseErrorMessage(exception))
            }
    }


    //recover
    fun recoverAccount(
        email: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit)
    {
        FirebaseRoom.getAuth().sendPasswordResetEmail(email)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(getFirebaseErrorMessage(exception))
            }
    }

}