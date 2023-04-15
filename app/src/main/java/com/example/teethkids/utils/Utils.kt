package com.example.teethkids.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

object Utils {

    private lateinit var storageReference: StorageReference

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    fun uploadProfileImage(img: Bitmap?, userId: String): Task<Uri> {
        val baos = ByteArrayOutputStream()
        img?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        storageReference = FirebaseStorage.getInstance().getReference("USERS/PHOTOS/$userId")
        val uploadTask = storageReference.putBytes(data)
        return uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
            }
            storageReference.downloadUrl
        }
    }


    fun getFirebaseErrorMessage(exception: Exception): String {
        return when (exception) {
            is FirebaseNetworkException -> "Falha na conexão com a internet"
            is FirebaseAuthWeakPasswordException -> "Senha invalida!"
            is FirebaseAuthInvalidCredentialsException -> "Login ou senha estão inválidos"
            is FirebaseAuthUserCollisionException -> "Já existe uma conta com este e-mail."
            is FirebaseAuthEmailException -> "Endereço de e-mail inválido."
            is FirebaseAuthActionCodeException -> "O código de verificação é inválido ou expirou."
            is FirebaseAuthInvalidUserException -> "Usuário inválido."
            is FirebaseAuthRecentLoginRequiredException -> "É necessário fazer login novamente."
            is FirebaseAuthWebException -> "Erro ao processar solicitação. Tente novamente mais tarde."
            is FirebaseTooManyRequestsException -> "Você realizou muitas solicitações. Tente novamente mais tarde."
            is FirebaseException -> "Erro ao processar solicitação. Tente novamente mais tarde."
            else -> "Ocorreu um erro desconhecido. Tente novamente mais tarde."
        }
    }

}
