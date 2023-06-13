package com.example.teethkids.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class Emergency
    (
    val name: String? = null,
    val phoneNumber: String? = null,
    val status: String? = null,
    val createdAt: Timestamp? = null,
    val photos: List<String>? = null,
    val location: List<Double>? = null,
    val rescuerUid: String? = null,
    var timer: Int = 60,
    )

// val location: List<Double>? = null,