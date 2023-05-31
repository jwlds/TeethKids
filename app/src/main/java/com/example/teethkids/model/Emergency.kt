package com.example.teethkids.model

data class Emergency
    (
    val emergencyId: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val status: String? = null,
    val dateTime: String? = null,
    val photos: List<String>? = null,
    val location: List<Double>? = null
    )