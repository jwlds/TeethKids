package com.example.teethkids.dao

import com.example.teethkids.Helper.FirebaseHelper
import com.example.teethkids.Helper.FirebaseHelper.Companion.getFcmToken
import com.example.teethkids.Helper.FirebaseHelper.Companion.getFunctions
import com.example.teethkids.Helper.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.model.RegistrationData
import com.example.teethkids.utils.Utils
import com.example.teethkids.utils.Utils.getFirebaseErrorMessage
import com.google.firebase.auth.FirebaseUser

class AuthenticationDAO(){

    //login
     fun login(
        email: String,
        password: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (String) -> Unit) {
        FirebaseHelper.getAuth().signInWithEmailAndPassword(email, password)
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
        FirebaseHelper.getAuth().createUserWithEmailAndPassword(data.email!!, data.password!!)
            .addOnSuccessListener {
                getFcmToken(onSuccess = { token ->
                    data.fcmToken = token
                },
                onFailure = {

                })
                val currentUser = getIdUser().toString()
               Utils.uploadProfileImage(data.photo,currentUser)
                   .addOnSuccessListener { url ->
                       val userData = hashMapOf(
                           "authUid" to currentUser,
                           "email" to data.email,
                           "name" to data.name,
                           "dateBrith" to data.dateBrith,
                           "numberPhone" to data.numberPhone,
                           "cro" to data.cro,
                           "university" to data.university,
                           "graduationDate" to data.graduationDate,
                           "urlImg" to url.toString(),
                           "status" to false,
                           "fcmToken" to data.fcmToken,
                           "professionalDescription" to data.professionalDescription,
                           "rating" to data.rating,
                       )
                       val addressData = hashMapOf(
                           "addressId" to data.addressId,
                           "userId" to currentUser,
                           "zipeCode" to data.zipcode,
                           "street" to data.street,
                           "number" to data.numberStreet,
                           "neighborhood" to data.neighborhood,
                           "city" to data.city,
                           "state" to data.state,
                           "primary" to data.primary,
                           "lat" to data.lat,
                           "lng" to data.lng
                       )
                       val createUser = getFunctions().getHttpsCallable("createUserDoc")
                       val data = hashMapOf(
                           "userId" to currentUser,
                           "userData" to userData,
                           "addressData" to addressData
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
        FirebaseHelper.getAuth().sendPasswordResetEmail(email)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(getFirebaseErrorMessage(exception))
            }
    }





    //logout
    fun logout() =  FirebaseHelper.getAuth().signOut()

}