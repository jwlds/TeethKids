package com.example.teethkids.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.teethkids.model.Address
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    private lateinit var storageReference: StorageReference

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    fun closeKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity.currentFocus ?: View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getCurrentDateTime(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        return dateFormat.format(calendar.time)
    }


    fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    fun formatAddress(address: Address): String {
        return "${address.street}, ${address.number}, ${address.neighborhood}, ${address.zipeCode}, ${address.city}, ${address.state}"
    }

    fun loadImageFromUrl(url: String, view: CircleImageView) {
        Glide.with(view.context)
            .load(url)
            .into(view)
    }


    fun loadImageFromUrlIv(url: String, view: ImageView) {
        Glide.with(view.context)
            .load(url)
            .into(view)
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
