package com.zobase.expensetracker.featureLogin.repositery

import android.content.Context
import com.zobase.expensetracker.data.prefrences.SharedPref
import com.zobase.expensetracker.utils.Constants

//Repository for login
class LoginRepository(private val context: Context) {

    fun login(userName: String, password: String): Boolean {
        return userName.isNotEmpty() && password.isNotEmpty()
    }

    fun saveUserData(userName: String) {
        SharedPref.setString(context, Constants.USERNAME, userName)
        SharedPref.setInt(context, Constants.LOGIN_TYPE, 1)
    }
}