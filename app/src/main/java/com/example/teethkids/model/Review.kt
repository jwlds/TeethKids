package com.example.teethkids.model

import com.google.firebase.Timestamp

data class Review(
    val EmergencyId: String? = null,
    val image: Int? = null,
    val name: String? = null,
    val rating: Float? = null,
    val createdAt: Timestamp?= null,
    val review: String?  = null ,
    val revision: Boolean? = false,
)