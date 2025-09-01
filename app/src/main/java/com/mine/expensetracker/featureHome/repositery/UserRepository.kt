package com.mine.expensetracker.featureHome.repositery

import android.content.Context
import com.mine.expensetracker.data.prefrences.SharedPref
import com.mine.expensetracker.utils.Constants

class UserRepository(private val context: Context) {

    fun getUsername(): String =
        SharedPref.getString(context, Constants.USERNAME) ?: ""

    fun getEmail(): String =
        SharedPref.getString(context, Constants.EMAIL) ?: ""

    fun getGender(): String =
        SharedPref.getString(context, Constants.GENDER) ?: ""

    fun saveUser(username: String, email: String, gender: String) {
        SharedPref.setString(context, Constants.USERNAME, username)
        SharedPref.setString(context, Constants.EMAIL, email)
        SharedPref.setString(context, Constants.GENDER, gender)
    }
}
