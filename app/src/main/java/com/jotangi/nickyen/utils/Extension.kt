package com.jotangi.nickyen.utils

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.jotangi.nickyen.R
import java.time.DayOfWeek
import java.time.temporal.WeekFields
import java.util.*
import androidx.core.content.ContextCompat.getSystemService
import android.view.WindowManager
import android.widget.ImageView
import java.text.SimpleDateFormat


/**
 *Created by Luke Liu on 2021/9/11.
 */

fun setupToolbarBtn(iv: ImageView?, onClick: () -> Unit) {
    iv?.apply {
        setOnClickListener {
            onClick.invoke()
        }
    }
}
fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInVisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

internal fun TextView.setTextColorRes(@ColorRes color: Int) = setTextColor(context.getColorCompat(color))

fun dialogQuestionnaireOther(context: Context, content: String? = null, onEdit: (content: String) -> Unit){
    val alert = AlertDialog.Builder(context, R.style.AlertDialogCustom)
    val edittext = EditText(context)
    edittext.setText(content)
    edittext.setTextColorRes(R.color.black)
    edittext.setBackgroundResource(R.drawable.underline_gray)
    alert.setView(edittext)
    alert.setPositiveButton("確定") { dialog, which ->
        onEdit.invoke(edittext.text.toString())
    }
    alert.setNegativeButton("取消") { dialog, which ->
        dialog.dismiss()
    }
    alert.show()
}

fun getCurrentTime(): String {
    val timeFormat = SimpleDateFormat("HH:mm")
    timeFormat.timeZone = TimeZone.getTimeZone("GMT+8")
    return timeFormat.format(Date())
}