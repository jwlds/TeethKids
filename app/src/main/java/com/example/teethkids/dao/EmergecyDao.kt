package com.example.teethkids.dao

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.teethkids.database.FirebaseHelper.Companion.getDatabase
import com.example.teethkids.database.FirebaseHelper.Companion.getFunctions
import com.example.teethkids.model.ResponseEmergency
import com.example.teethkids.ui.home.MainActivity

class EmergecyDao {

    fun createResponseEmergency(
        responseEmergency: ResponseEmergency,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val db = getDatabase()
        val emergencyData = hashMapOf(
            "professionalUid" to responseEmergency.professionalUid,
            "rescuerUid" to responseEmergency.rescuerUid,
            "status" to responseEmergency.status,
            "acceptedAt" to responseEmergency.acceptedAt
        )

        db.collection("responses")
            .add(emergencyData)
            .addOnSuccessListener { documentReference ->
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

}
