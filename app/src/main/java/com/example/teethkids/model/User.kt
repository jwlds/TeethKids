package com.example.teethkids.model




// Classe representando um "User".
data class User(
    var status: Boolean = false,
    var authUid: String = "",
    var cro: String = "",
    var dateBrith: String = "",
    var email: String = "",
    var graduationDate: String = "",
    var professionalDescription: String = "",
    val name:String = "",
    var numberPhone: String = "",
    var university: String = "",
    var urlImg: String = "",
    var fcmToken: String = "",
    var rating: Double? = null,
)



