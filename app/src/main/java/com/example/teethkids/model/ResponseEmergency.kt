package com.example.teethkids.model

import com.google.firebase.Timestamp
import retrofit2.Response


// Classe representando uma resposta da emergencia.
data class ResponseEmergency(
    val responseId: String? = null,
    var acceptedAt: Timestamp? = null,
    var rescuerUid: String? = null,
    var status: String? = null,
    var professionalUid: String? =  null,
    var willProfessionalMove: Int? = null
)