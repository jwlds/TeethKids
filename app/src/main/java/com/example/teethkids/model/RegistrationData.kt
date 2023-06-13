package com.example.teethkids.model

import android.graphics.Bitmap
import android.os.Parcelable
import com.example.teethkids.database.FirebaseHelper
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegistrationData(
    var email: String? = "",
    var password: String? = "",
    var name: String? = null,
    var dateBrith: String? = null,
    var numberPhone: String? = null,
    var cro: String? = null,
    var university: String? = null,
    var graduationDate: String? = null,
    var photo: Bitmap? = null,
    var street: String? = null,
    var neighborhood: String? = null,
    var zipcode: String? = null,
    var state: String? = null,
    var city: String? = null,
    var numberStreet: String? = null,
    var fcmToken: String? = null,
    var addressId: String = "",
    var primary: Boolean? = false,
    var professionalDescription: String? = null,
    var rating: Double? = null,
    var lat: Double? = null,
    var lng: Double? = null,

) : Parcelable {
    init {
        this.addressId = FirebaseHelper.getDatabase().collection("address").document().id
    }
}
