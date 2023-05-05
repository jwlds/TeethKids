package com.example.teethkids.dao



import com.example.teethkids.database.FirebaseHelper
import com.example.teethkids.database.FirebaseHelper.Companion.getDatabase
import com.example.teethkids.database.FirebaseHelper.Companion.getFunctions
import com.example.teethkids.database.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.model.Address


class AddressDao {
    private val db = getDatabase()

    fun getAll(callback: (List<Address>) -> Unit) {
        getDatabase()
            .collection("profiles")
            .document(getIdUser().toString())
            .collection("address")
            .get()
            .addOnSuccessListener { result ->
                val addresses = mutableListOf<Address>()
                for (document in result) {
                    val address = document.toObject(Address::class.java)
                    addresses.add(address)
                }
                callback(addresses)
            }
            .addOnFailureListener { exception ->
                callback(emptyList())
            }
    }


    fun addAddress(address: Address, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        getDatabase()
            .collection("profiles")
            .document(getIdUser().toString())
            .collection("address")
            .document(address.addressId)
            .set(address)
            .addOnSuccessListener { documentReference ->
                    onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun deleteAddress(authUid: String, addressId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val data = hashMapOf(
            "authUid" to authUid,
            "addressId" to addressId
        )
        val remove = FirebaseHelper.getFunctions().getHttpsCallable("deleteAddress")
        remove.call(data)
            .addOnSuccessListener { result ->
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }



    fun updateAddress(userId: String, addressId: String, address: Address, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("profiles").document(userId).collection("addresses").document(addressId)
            .set(address)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }


}