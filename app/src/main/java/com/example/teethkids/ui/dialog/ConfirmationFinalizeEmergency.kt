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



        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        setView(layout)

        setButton(BUTTON_POSITIVE, "Confirmar") { _, _ ->
            val dao = EmergencyDao()
            dao.updateStatusEmergency(emergencyId,"finished", onSuccess = {}, onFailure = {})
            dao.updateStatusMyEmergency(emergencyId, "finished", onSuccess = {}, onFailure = {})
        }
        setButton(BUTTON_NEGATIVE, "Cancelar") { _, _ ->
            dismiss()
        }
    }

    override fun show() {
        super.show()
    }
}