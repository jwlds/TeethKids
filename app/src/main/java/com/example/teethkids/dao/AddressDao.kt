package com.example.teethkids.dao
import android.content.Context
import com.example.teethkids.Helper.FirebaseHelper
import com.example.teethkids.Helper.FirebaseHelper.Companion.getDatabase
import com.example.teethkids.datastore.UserPreferencesRepository
import com.example.teethkids.model.Address

//Classe responsável por acessar e manipular dados de endereços no Firebase
class AddressDao(context: Context) {


    private val userPreferencesRepository = UserPreferencesRepository.getInstance(context)

    // Função para adicionar um endereço

    fun addAddress(address: Address, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {

        val addressData = hashMapOf(
            "addressId" to address.addressId,
            "userId" to address.userId,
            "street" to address.street,
            "neighborhood" to address.neighborhood,
            "zipeCode" to address.zipeCode,
            "state" to address.state,
            "city" to address.city,
            "number" to address.number,
            "primary" to address.primary,
            "lat" to address.lat,
            "lng" to address.lng
        )
        val data = hashMapOf(
            "authUid" to userPreferencesRepository.uid,
            "addressData" to addressData
        )
        val addAddress = FirebaseHelper.getFunctions().getHttpsCallable("addAddress")
        // Chama a função addAddress da functions
        addAddress.call(data)
            .addOnSuccessListener {
                // Caso a chamada seja bem-sucedida, chama o callback onSuccess
                onSuccess()
            }
            .addOnFailureListener { exception ->
                // Caso ocorra uma falha na chamada, chama o callback onFailure com a exceção
                onFailure(exception)
            }
    }
    // Função para atualizar um endereço
    fun updateAddress(address: Address,addressId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val addressData = hashMapOf(
            "addressId" to addressId,
            "userId" to address.userId,
            "street" to address.street,
            "neighborhood" to address.neighborhood,
            "zipeCode" to address.zipeCode,
            "state" to address.state,
            "city" to address.city,
            "number" to address.number,
            "primary" to address.primary
        )
        val data = hashMapOf(
            "authUid" to userPreferencesRepository.uid,
            "addressData" to addressData
        )
        val update = FirebaseHelper.getFunctions().getHttpsCallable("updateAddress")
        // Chama a função updateAddress da functions
        update.call(data)
            .addOnSuccessListener { result ->
                // Caso a chamada seja bem-sucedida, chama o callback onSuccess
                onSuccess()
            }
            .addOnFailureListener { exception ->
                // Caso ocorra uma falha na chamada, chama o callback onFailure com a exceção
                onFailure(exception)
            }
    }

    // Função para excluir um endereço
    fun deleteAddress(addressId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val data = hashMapOf(
            "authUid" to userPreferencesRepository.uid,
            "addressId" to addressId
        )
        val remove = FirebaseHelper.getFunctions().getHttpsCallable("deleteAddress")
        // Chama a função deleteAddress da functions
        remove.call(data)
            .addOnSuccessListener { result ->
                // Caso a chamada seja bem-sucedida, chama o callback onSuccess
                onSuccess()
            }
            .addOnFailureListener { exception ->
                // Caso ocorra uma falha na chamada, chama o callback onFailure com a exceção
                onFailure(exception)
            }
    }

    fun updatePrimaryAddress(isPrimary: Boolean,addressId: String,onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
         val ref = getDatabase().collection("profiles").document(userPreferencesRepository.uid)
             .collection("addresses").document(addressId)
         ref.update("primary", isPrimary)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

}