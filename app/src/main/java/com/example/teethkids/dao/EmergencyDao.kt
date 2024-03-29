package com.example.teethkids.dao

import com.example.teethkids.Helper.FirebaseHelper.Companion.getDatabase
import com.example.teethkids.Helper.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.model.ResponseEmergency

class EmergencyDao(){


    // Função para criar uma resposta de emergência
    fun createResponseEmergency(
        responseEmergency: ResponseEmergency,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val responseId = getDatabase().collection("responses").document().id
        val db = getDatabase()

        val emergencyData = hashMapOf(
            "responseId" to responseId,
            "professionalUid" to responseEmergency.professionalUid,
            "rescuerUid" to responseEmergency.rescuerUid,
            "status" to responseEmergency.status,
            "acceptedAt" to responseEmergency.acceptedAt,
            "willProfessionalMove" to responseEmergency.willProfessionalMove
        )

        db.collection("responses")
            .document(responseId)
            .set(emergencyData)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


    // Função para atualizar o status da resposta de emergência
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

    // Função para atualizar o status da  emergência atual.
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

    // Função para atualizar o willProfessionalMove da resposta de emergência.
   // willProfessionalMove:
   // -1 : não está definido ainda - "aguardando dentista"/não definido mesmo
   // 0 : venha até o consultorio - "enviar localização"
   // 1 : dentista vai até o socorrista - "solicitar localização"

    fun updateStatusMove(
        emergencyId: String,
        status: Int,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val responseRef = getDatabase().collection("responses")
        responseRef
            .whereEqualTo("rescuerUid", emergencyId)
            .whereEqualTo("professionalUid", getIdUser().toString())
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val responseDoc = querySnapshot.documents[0]
                    responseRef.document(responseDoc.id)
                        .update("willProfessionalMove", status)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { exception ->
                            onFailure(exception)
                        }
                } else {
                    onFailure(Exception("Response document not found"))
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


    // Função para atualizar o status de uma resposta de emergência
    fun updateStatusResponse(
        emergencyId: String,
        status: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val responseRef = getDatabase().collection("responses")
        responseRef
            .whereEqualTo("rescuerUid", emergencyId)
            .whereEqualTo("professionalUid", getIdUser().toString())
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val responseDoc = querySnapshot.documents[0]
                    responseRef.document(responseDoc.id)
                        .update("status", status)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { exception ->
                            onFailure(exception)
                        }
                } else {
                    onFailure(Exception("Response document not found"))
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
    
}
