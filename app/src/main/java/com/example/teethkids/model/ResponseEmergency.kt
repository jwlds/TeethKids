package com.example.teethkids.model

data class ResponseEmergency(
    val professional: String,
    val emergencyId: String,
    val status: String,
    val dateTime: String
)