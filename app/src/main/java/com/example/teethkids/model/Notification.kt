package com.example.teethkids.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize


// Classe representando uma notificação.
@Parcelize
data class Notification(
    val title: String? = null,
    val body: String? = null,
    var timeStamp: Timestamp? = null,
) : Parcelable {
    //Antes de inicializar a classe.
    init {
        // Atribui da data e hora atual a timestamp.
        this.timeStamp = Timestamp.now()
    }
}