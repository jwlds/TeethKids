package com.example.teethkids.model

import android.os.Parcelable
import com.example.teethkids.database.FirebaseHelper
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Notification(
    val title: String? = null,
    val body: String? = null,
    var timestamp: Timestamp? = null,
) : Parcelable {
    init {
        this.timestamp = Timestamp.now()
    }
}