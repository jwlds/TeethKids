package com.example.teethkids.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teethkids.database.FirebaseHelper
import com.example.teethkids.model.Review

class ReviewsViewModel : ViewModel() {
    private val _reviewsList = MutableLiveData<List<Review>>()
    val reviewsList: LiveData<List<Review>> = _reviewsList

    init {
        val authUid = FirebaseHelper.getIdUser()
        if (authUid != null) {
            val profilesRef = FirebaseHelper.getDatabase().collection("profiles").document(authUid)
            val reviewsRef = profilesRef.collection("reviews")
            reviewsRef.addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                val reviewsList = mutableListOf<Review>()
                for (documentSnapshot in querySnapshot?.documents ?: emptyList()) {
                    Log.d("444",documentSnapshot.toString())
                    val address = documentSnapshot.toObject(Review::class.java)
                    address?.let {
                        reviewsList.add(it)
                    }
                }
                _reviewsList.value = reviewsList
            }
        }
    }

}