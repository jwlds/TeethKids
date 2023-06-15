package com.example.teethkids.model

import com.google.firebase.Timestamp


// Classe representando uma resposta da emergencia.
data class ResponseEmergency(
    var acceptedAt: Timestamp? = null,
    var rescuerUid: String? = null,
    var status: String? = null,
    var professionalUid: String? =  null,
    var willProfessionalMove: Int? = null
)