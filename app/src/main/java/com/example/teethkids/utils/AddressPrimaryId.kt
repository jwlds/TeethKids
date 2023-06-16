package com.example.teethkids.utils

import com.google.firebase.firestore.GeoPoint


// usada para armazenar o id do endereço principal e o ponto geográfico (Para calcular a distância dele para emergência).
object AddressPrimaryId {
    var addressPrimaryId: String? = null
    var addressGeoPoint: GeoPoint? = null
}