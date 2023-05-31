package com.example.teethkids.dao
import android.util.Log
import com.example.teethkids.database.FirebaseHelper
import com.example.teethkids.database.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.model.Address

//Classe responsável por acessar e manipular dados de endereços no Firebase
class AddressDao {

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
            "isPrimary" to address.isPrimary
        )
        val data = hashMapOf(
            "authUid" to getIdUser().toString(),
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
            "isPrimary" to address.isPrimary
        )
        val data = hashMapOf(
            "authUid" to getIdUser().toString(),
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
    fun deleteAddress(authUid: String, addressId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val data = hashMapOf(
            "authUid" to authUid,
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
}