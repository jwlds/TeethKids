package com.example.teethkids.dao

import android.content.Context
import com.example.teethkids.database.FirebaseHelper
import com.example.teethkids.database.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.datastore.UserPreferencesRepository
import com.example.teethkids.model.ResponseEmergency
import com.example.teethkids.model.Review
import com.google.firebase.Timestamp

class ReviewDao(context: Context){

    private val userPreferencesRepository = UserPreferencesRepository.getInstance(context)
    fun reportReview(
        review: Review,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val id =  FirebaseHelper.getDatabase().collection("disputes").document().id
        val currentTimestamp: Timestamp = Timestamp.now()
        val db = FirebaseHelper.getDatabase()
        updateStatusReview(review.emergencyId!!,onSuccess =  {})
        val reportReviewData = hashMapOf(
            "reportId" to id,
            "professionalUid" to userPreferencesRepository.uid,
            "emergencyId" to review.emergencyId,
            "createdAt" to currentTimestamp,
            "commentReported" to hashMapOf("review" to review.review,"dataTime" to review.createdAt)
        )

        db.collection("disputes")
            .document(id)
            .set(reportReviewData)
            .addOnSuccessListener { documentReference ->
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun updateStatusReview(id: String, onSuccess: () -> Unit) {
        val userRef = FirebaseHelper.getDatabase().collection("profiles").document(userPreferencesRepository.uid)
        val reviewRef = userRef.collection("reviews").document(id)

        reviewRef.update("revision", true)
            .addOnSuccessListener {
                onSuccess()
            }
    }
}