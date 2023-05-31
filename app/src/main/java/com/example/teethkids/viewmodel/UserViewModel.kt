package com.example.teethkids.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teethkids.database.FirebaseHelper
import com.example.teethkids.database.FirebaseHelper.Companion.getAuth
import com.example.teethkids.database.FirebaseHelper.Companion.getDatabase
import com.example.teethkids.database.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.model.User



// Classe é responsável por gerencia os dados do usuário atual e fornece um objeto
//LiveData de somente leitura chamado user para observar as alterações nos dados do usuário
class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    init {
        val authUid = getIdUser()
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


