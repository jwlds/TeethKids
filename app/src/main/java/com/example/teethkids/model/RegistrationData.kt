package com.example.teethkids.model

import android.graphics.Bitmap
import android.os.Parcelable
import com.example.teethkids.Helper.FirebaseHelper
import kotlinx.parcelize.Parcelize


// Classe representando os dados do processo de registro.
@Parcelize
data class RegistrationData(
    val userUid: String? = "",
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
    var rating: Double? = 0.0,
    var lat: Double? = null,
    var lng: Double? = null,

) : Parcelable {
    //Antes de inicializar a classe.
    init {
        // Atribui um ID único a addressId.
        this.addressId = FirebaseHelper.getDatabase().collection("addresses").document().id
    }
}
