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

        val checkbox1 = CheckBox(context)
        checkbox1.text = "Enviar localização atual."
        checkbox1.isChecked = false

        val checkbox2 = CheckBox(context)
        checkbox2.text = "Enviar localização do endereço padrão."
        checkbox2.isChecked = false

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(checkbox1)
        layout.addView(checkbox2)

        setView(layout)

        checkboxActivateListener(checkbox1, checkbox2)

        setButton(BUTTON_POSITIVE, "Confirmar") { _, _ ->
            val sendCurrentLocation = checkbox1.isChecked
            val sendDefaultAddressLocation = checkbox2.isChecked

            handleLocationSending(sendCurrentLocation, sendDefaultAddressLocation)
        }
        setButton(BUTTON_NEGATIVE, "Cancelar") { _, _ ->
            dismiss()
        }
    }

    private fun checkboxActivateListener(checkbox1: CheckBox, checkbox2: CheckBox) {
        checkbox1.setOnCheckedChangeListener { _, isCheked ->
            if (isCheked) {
                checkbox2.isChecked = false
                checkbox2.isEnabled = false
            } else {
                checkbox2.isEnabled = true
            }
        }

        checkbox2.setOnCheckedChangeListener { _, isCheked ->
            if (isCheked) {
                checkbox1.isChecked = false
                checkbox1.isEnabled = false
            } else {
                checkbox1.isEnabled = true
            }
        }
    }

    private fun handleLocationSending(
        sendCurrentLocation: Boolean,
        sendDefaultAddressLocation: Boolean
    ) {

        if (sendCurrentLocation) {
            val myLocation = MyLocation(context)
            myLocation.getCurrentLocation { location: Location? ->
                if (location != null) {
                    val latCurrent = location.latitude
                    val lonCurrent = location.longitude

                    Log.d(
                        "SendTest",
                        "Enviando localização atual: lat - $latCurrent | lon - $lonCurrent"
                    )
                } else {
                    // Solicitar permissão

                    Log.d("SendTest", "Não foi possivel enviar localização atual")
                }
            }
        } else if (sendDefaultAddressLocation) {
            val adressGeoPoint = AddressPrimaryId.addressGeoPoint

            val latAddress = adressGeoPoint!!.latitude
            val lonAddress = adressGeoPoint.longitude

            Log.d(
                "SendTest",
                "Enviando localização do endereço padrão: lat - $latAddress | lon - $lonAddress"
            )
        }
    }

    override fun show() {
        super.show()
    }
}