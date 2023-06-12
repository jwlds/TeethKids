package com.example.teethkids.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import com.example.teethkids.dao.AddressDao
import com.example.teethkids.dao.ReviewDao
import com.example.teethkids.model.Review
import com.example.teethkids.utils.Utils

class ConfirmationReportReview(
    context: Context,
review: Review
)
    : AlertDialog(context) {

    init {
        setTitle("Confirmação")
        setMessage("Deseja reportar o comentário?")
        setButton(BUTTON_POSITIVE, "Confirmar") { _, _ ->
            val dao = ReviewDao()
            dao.reportReview(review,
                onSuccess = {
                Utils.showToast(context,"Revisão enviada com sucesso")
            },
                onFailure = {
                    Utils.showToast(context,"Revisão Falhou")
                })
        }
        setButton(BUTTON_NEGATIVE, "Cancelar") { _, _ ->
            dismiss()
        }
    }

    override fun show() {
        super.show()
    }
}