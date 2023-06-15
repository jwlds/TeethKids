package com.example.teethkids.model

import com.google.firebase.Timestamp

// Classe representando uma avaliação.
data class Review(
    val emergencyId: String? = null,
    val image: Int? = null,
    val name: String? = null,
    val rating: Float? = null,
    val createdAt: Timestamp?= null,
    val review: String?  = null ,
    val revision: Boolean? = false,
)