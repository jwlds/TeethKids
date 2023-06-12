package com.example.teethkids.model

import com.google.firebase.Timestamp

data class MyEmergency(
    val createdAt: Timestamp? = null,
    val emergencyId: String? = null,
    val status: String? = null
)