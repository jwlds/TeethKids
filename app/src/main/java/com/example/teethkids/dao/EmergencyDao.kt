package com.example.teethkids.dao

import com.example.teethkids.database.FirebaseHelper
import com.example.teethkids.database.FirebaseHelper.Companion.getDatabase
import com.example.teethkids.database.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.model.ResponseEmergency

class EmergencyDao {

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

    fun updateStatusEmergency(
        emergencyId: String,
        status: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val db = getDatabase()
        val emergencyRef = db.collection("emergencies").document(emergencyId)
        emergencyRef.update("status", status)
            .addOnSuccessListener { documentReference ->
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun updateStatusMyEmergency(
        emergencyId: String,
        status: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userRef = getDatabase().collection("profiles").document(getIdUser().toString())
        val emergencyRef = userRef.collection("myEmergencies").document(emergencyId)
        emergencyRef.update("status", status)
            .addOnSuccessListener { documentReference ->
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
    
}
