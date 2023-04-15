package com.example.teethkids.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teethkids.database.FirebaseHelper.Companion.getAuth
import com.example.teethkids.database.FirebaseHelper.Companion.getDatabase
import com.example.teethkids.model.User

class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    init {
        val authUid = getAuth().currentUser?.uid
        if (authUid != null) {
            val userRef = getDatabase().collection("profiles").document(authUid)
            userRef.addSnapshotListener { documentSnapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    val data = documentSnapshot.toObject(User::class.java)
                    _user.value = data ?: User()
                }
            }
        }
    }

    fun updateUser(user: User) {
        _user.value = user
    }
}


