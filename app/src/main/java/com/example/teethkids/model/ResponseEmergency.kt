package com.example.teethkids.model

import com.google.firebase.Timestamp

data class ResponseEmergency(
    var acceptedAt: Timestamp? = null,
    var rescuerUid: String? = null,
    var status: String? = null,
    var professionalUid: String? =  null
)