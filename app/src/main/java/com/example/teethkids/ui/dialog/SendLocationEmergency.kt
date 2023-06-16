package com.example.teethkids.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.widget.CheckBox
import android.widget.LinearLayout
import com.example.teethkids.dao.EmergencyDao

class SendLocationEmergency(
    context: Context,
    emergencyId: String
) : AlertDialog(context) {

    init {
        setTitle("Envio de Localização")
        setMessage("Selecione o tipo de localização que será encaminhada.")

        val checkbox2 = CheckBox(context)
        checkbox2.text = "Enviar localização do endereço principal."
        checkbox2.isChecked = true

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