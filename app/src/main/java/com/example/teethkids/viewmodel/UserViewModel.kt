package com.example.teethkids.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teethkids.Helper.FirebaseHelper.Companion.getDatabase
import com.example.teethkids.Helper.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.model.User


// Classe é responsável por gerencia os dados do usuário atual e fornece um objeto
//LiveData de somente leitura chamado user para observar as alterações nos dados do usuário
class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<User>() // Armazena os dados do usuário como MutableLiveData
    val user: LiveData<User> = _user // LiveData para observação externa

    init {
        val authUid = getIdUser() // Obtém o ID do usuário autenticado
        if (authUid != null) {
            val userRef = getDatabase().collection("profiles").document(authUid) // Obtém a referência do documento do usuário a partir do "authUid"
            userRef.addSnapshotListener { documentSnapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    val data = documentSnapshot.toObject(User::class.java) // Converte o documento do usuário para um objeto da classe "User".
                    _user.value = data ?: User() // Atualiza o valor do "_user" com os novos dados do usuário ou um objeto User vazio.
                }
            }
        }
    }

}


