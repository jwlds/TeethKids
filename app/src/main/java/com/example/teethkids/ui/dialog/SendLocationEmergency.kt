package com.example.teethkids.ui.dialog

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.location.Location
import android.util.Log
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import com.example.teethkids.dao.EmergencyDao
import com.example.teethkids.service.MyLocation
import com.example.teethkids.ui.home.MyEmergencyDetailsFragment
import com.example.teethkids.utils.AddressPrimaryId

class SendLocationEmergency(
    context: Context,
    emergencyId: String
) : AlertDialog(context) {

    companion object {
        private const val REQUEST_CALL_PERMISSION = 456
    }

    init {
        setTitle("Envio de Localização")
        setMessage("Selecione o tipo de localização que será encaminhada.")

        val checkbox2 = CheckBox(context)
        checkbox2.text = "Enviar localização do endereço principal."
        checkbox2.isChecked = false

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(checkbox2)

        setView(layout)

        setButton(BUTTON_POSITIVE, "Confirmar") { _, _ ->
            val dao = EmergencyDao()
            dao.updateStatusMove(emergencyId, 0, onSuccess = {}, onFailure = {})
        }
        setButton(BUTTON_NEGATIVE, "Cancelar") { _, _ ->
            dismiss()
        }
    }


    override fun show() {
        super.show()
    }
}