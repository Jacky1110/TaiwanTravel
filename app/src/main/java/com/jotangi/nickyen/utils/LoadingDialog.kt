package com.jotangi.nickyen.utils

import android.app.Dialog
import android.content.Context
import com.jotangi.nickyen.R

object LoadingDialog {
    private var dialog: Dialog? = null

    fun show(context: Context) {
        cancel()
        dialog = Dialog(context, R.style.loadingDialog)
        dialog?.setContentView(R.layout.dialog_loading)
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.show()
    }

    fun cancel() {
        if (dialog != null) {
            if (dialog?.isShowing!!)
                dialog?.dismiss()
        }
    }
}