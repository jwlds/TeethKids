package com.example.teethkids.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teethkids.database.FirebaseHelper
import com.example.teethkids.model.MyEmergency
import com.google.firebase.firestore.Query

class MyEmergenciesViewModel : ViewModel(){

    private val _myEmergenciesList = MutableLiveData<List<MyEmergency>>()
    val myEmergenciesList: LiveData<List<MyEmergency>> = _myEmergenciesList

    init {
        val authUid = FirebaseHelper.getIdUser()
        if (authUid != null) {
            val profilesRef = FirebaseHelper.getDatabase().collection("profiles").document(authUid)
            val emergencyRef = profilesRef.collection("myEmergencies")
            emergencyRef.orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        return@addSnapshotListener
                    }
                    if (querySnapshot != null) {
                        val myEmergenciesList = mutableListOf<MyEmergency>()
                        for (document in querySnapshot.documents) {
                            val data = document.toObject(MyEmergency::class.java)
                                data?.let { myEmergenciesList.add(it) }

                        }
                        _myEmergenciesList.value = myEmergenciesList
                    }
                }
        }
    }
}