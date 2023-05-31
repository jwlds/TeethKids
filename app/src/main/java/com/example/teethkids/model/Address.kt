package com.example.teethkids.model

import android.os.Parcelable
import com.example.teethkids.database.FirebaseHelper
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
  var primary: Boolean =  false
) : Parcelable {
  init {
    this.addressId = FirebaseHelper.getDatabase().collection("address").document().id
  }
}
