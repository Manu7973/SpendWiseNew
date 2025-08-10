package com.zobase.expensetracker.data.prefrences

import android.content.Context
import com.zobase.expensetracker.R

class SharedPref {
    companion object{
        fun getString(context: Context, key: String): String {
            val sPref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
            return sPref.getString(key, "").toString()
        }

        fun setString(context: Context, key: String, value: String){
            val sPref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
            val editor = sPref.edit().putString(key, value)
            editor.commit()
        }

        fun getInt(context: Context, key: String): Int {
            val sPref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
            return sPref.getInt(key, 0)
        }

        fun setInt(context: Context, key: String, value: Int){
            val sPref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
            val editor = sPref.edit().putInt(key, value)
            editor.commit()
        }

        fun getBoolean(context: Context, key: String): Boolean {
            val sPref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
            return sPref.getBoolean(key, false)
        }

        fun setBoolean(context: Context, key: String, value: Boolean){
            val sPref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
            val editor = sPref.edit().putBoolean(key, value)
            editor.commit()
        }

        fun clearSharedPref(context: Context){
            val sPref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
            val editor = sPref.edit().clear()
            editor.commit()
        }
    }
}