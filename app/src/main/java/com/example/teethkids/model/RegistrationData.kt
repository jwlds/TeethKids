package com.example.teethkids.model

import android.graphics.Bitmap

data class RegistrationData(
    var email: String? = "",
    var password: String? = "",
    var name: String? = null,
    var dateBrith: String? = null,
    var numberPhone: String? = null,
    var cro: String? = null,
    var andress1: String? = null,
    var andress2: String? = null,
    var andress3: String? = null,
    var university: String? = null,
    var graduationDate: String? = null,
    var photo: Bitmap? = null
)