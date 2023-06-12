package com.example.teethkids.model

import com.google.firebase.Timestamp

data class Review(
    val image: Int,
    val name: String,
    val rating: Double,
    val date: Timestamp,
    val review: String,
    val initials: String
)