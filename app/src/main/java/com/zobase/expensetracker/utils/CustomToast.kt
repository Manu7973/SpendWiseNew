package com.expense.expensetracker.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.zobase.expensetracker.R

class CustomToast {
    companion object {
        fun showToast(context: Context, showMessage: String, isLong: Boolean) {
            val layout = LayoutInflater.from(context).inflate(R.layout.custom_toast, null)
            val toastText = layout.findViewById<TextView>(R.id.toast_text)
            toastText.text = showMessage

            val toast = Toast(context)
            toast.view = layout
            if(isLong){
                toast.duration = Toast.LENGTH_LONG
            }else{
                toast.duration = Toast.LENGTH_SHORT
            }
            toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100)
            toast.show()
        }
    }
}