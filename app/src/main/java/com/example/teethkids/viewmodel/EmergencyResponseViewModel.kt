package com.example.teethkids.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teethkids.database.FirebaseHelper
import com.example.teethkids.database.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.model.ResponseEmergency

class EmergencyResponseViewModel : ViewModel()  {
    private val _emergencyResponseList = MutableLiveData<List<ResponseEmergency>>()
    val emergencyResponseList: LiveData<List<ResponseEmergency>> = _emergencyResponseList

    init {
        val emergencyResponseRef = FirebaseHelper.getDatabase().collection("responses")
        emergencyResponseRef.whereEqualTo("professionalUid", getIdUser().toString())
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    val emergencyResponses = mutableListOf<ResponseEmergency>()
                    for (document in querySnapshot.documents) {
                        val data = document.toObject(ResponseEmergency::class.java)
                        data?.let { emergencyResponses.add(it) }
                    }
                    _emergencyResponseList.value = emergencyResponses
                }
            }
    }
}