package com.example.teethkids.model

import com.google.firebase.Timestamp

data class Emergency
    (
    val name: String? = null,
    val phoneNumber: String? = null,
    val status: String? = null,
    val createdAt: Timestamp? = null,
    val photos: List<String>? = null,
    val location: List<Double>? = null,
    val rescuerUid: String? = null
    )