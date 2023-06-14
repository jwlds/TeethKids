package com.example.teethkids.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class MyLocation(private val context: Context) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    fun getCurrentLocation(callback: (Location?) -> Unit) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            callback(null)
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                callback(location)
            }
            .addOnFailureListener { exception: Exception ->
                callback(null)
            }
    }
}