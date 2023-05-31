package com.example.teethkids.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import com.example.teethkids.dao.AddressDao

//test de dialog


class ConfirmationMainAddressDialog(
    context: Context,
    newAddressPrimary: String,
    currentAddressPrimary:String? = null
)
    : AlertDialog(context) {

    init {
        Log.d("111",newAddressPrimary)
        setTitle("Confirmação")
        setMessage("Deseja confirmar o endereço como principal?")
        setButton(BUTTON_POSITIVE, "Confirmar") { _, _ ->
            val dao = AddressDao()
            if(currentAddressPrimary == null) {
                dao.updatePrimaryAddress(true,newAddressPrimary,
                    onSuccess = {
                        Log.d("address", "Successfully updated")
                    }, onFailure = { it
                        Log.e("address", "Error updating")
                    }
                )
            }
            else {
                dao.updatePrimaryAddress(false,currentAddressPrimary,
                    onSuccess = {
                        Log.d("address", "Successfully updated primary old address to false")
                        dao.updatePrimaryAddress(true, newAddressPrimary,
                            onSuccess = {
                                Log.d("address", "Successfully updated primary new address to true")
                            },
                            onFailure = { error ->
                                Log.e("address", "Error updating primary new address to true", error)
                            }
                        )
                    },
                    onFailure = {it
                        Log.e("address", "Error updating primary old address to true", it)
                    })
            }
        }
        setButton(BUTTON_NEGATIVE, "Cancelar") { _, _ ->
            dismiss()
        }
    }

    override fun show() {
        super.show()
    }
}