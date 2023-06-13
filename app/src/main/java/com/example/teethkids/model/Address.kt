package com.example.teethkids.model

import android.os.Parcelable
import com.example.teethkids.database.FirebaseHelper
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


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
  init {
    this.addressId = FirebaseHelper.getDatabase().collection("address").document().id
  }

}
