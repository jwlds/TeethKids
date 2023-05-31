package com.example.teethkids.model

import com.google.firebase.Timestamp

data class Emergency
    (
    val emergencyId: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val status: String? = null,
    val createdAt: Timestamp? = null,
    val photos: List<String>? = null,
    val location: List<Double>? = null
    )