package com.example.teethkids.model

import android.os.Parcelable
import com.example.teethkids.Helper.FirebaseHelper
import kotlinx.parcelize.Parcelize


@Parcelize
data class Address(
  var addressId: String = "",
  val userId: String? = null,
  var street: String? = null,
  var neighborhood: String? = null,
  var zipeCode: String = "",
  var state: String? = null,
  var city: String? = null,
  var number: String? = null,
  var primary: Boolean =  false,
  var lat: Double? = null,
  var lng: Double? = null,
) : Parcelable {

  //Antes de inicializar a classe.
  init {
    // Atribui um ID único a addressId.
    this.addressId = FirebaseHelper.getDatabase().collection("addresses").document().id
  }
  override fun toString(): String {
    return "$street, $number, $neighborhood, $city, $state, $zipeCode"
  }
}
