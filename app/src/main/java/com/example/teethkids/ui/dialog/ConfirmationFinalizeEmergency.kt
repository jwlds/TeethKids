package com.example.teethkids.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.widget.CheckBox
import android.widget.LinearLayout
import com.example.teethkids.dao.EmergencyDao
import com.example.teethkids.dao.ReviewDao
import com.example.teethkids.model.Review
import com.example.teethkids.utils.Utils

class ConfirmationFinalizeEmergency(
    context: Context,
    emergencyId: String
) : AlertDialog(context) {

    init {
        setTitle("Confirmação")
        setMessage("Deseja Finalizar essa emergencia?")

        val checkbox = CheckBox(context)
        checkbox.text = "Solicitar avaliação do socorrista."
        checkbox.isChecked = false

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(checkbox)

        setView(layout)

        setButton(BUTTON_POSITIVE, "Confirmar") { _, _ ->
            val dao = EmergencyDao()
            dao.updateStatusMyEmergency(emergencyId, "finished", onSuccess = {}, onFailure = {})

            val receiveEvaluation = checkbox.isChecked
        }
        setButton(BUTTON_NEGATIVE, "Cancelar") { _, _ ->
            dismiss()
        }
    }

    override fun show() {
        super.show()
    }
}