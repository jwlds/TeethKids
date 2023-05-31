package com.example.teethkids.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teethkids.database.FirebaseHelper.Companion.getDatabase
import com.example.teethkids.model.Emergency
import com.google.firebase.firestore.Query

class EmergencyViewModel : ViewModel() {

    private val _emergencyList = MutableLiveData<List<Emergency>>()
    val emergencyList: LiveData<List<Emergency>> = _emergencyList

    init {
        val emergencyRef = getDatabase().collection("emergencies")
        emergencyRef.orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if (querySnapshot != null) {
                val emergencies = mutableListOf<Emergency>()
                for (document in querySnapshot.documents) {
                    val data = document.toObject(Emergency::class.java)
                    data?.let { emergencies.add(it) }
                }
                _emergencyList.value = emergencies
            }
        }
    }
}