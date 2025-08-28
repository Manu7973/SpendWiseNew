package com.mine.expensetracker.featureSplash.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.mine.expensetracker.data.prefrences.SharedPref
import com.mine.expensetracker.featureHome.ui.MainActivity
import com.mine.expensetracker.featureLogin.ui.LoginActivity
import com.mine.expensetracker.utils.Constants

//Splash screen viewmodel
class SplashViewModel: ViewModel() {

    //Get next screen based on login status
    fun getNextScreen(context: Context): Class<*>{
        val loggedIn = SharedPref.getInt(context, Constants.LOGIN_TYPE)
        val userName = SharedPref.getString(context, Constants.USERNAME)

        return if (loggedIn != 0 && !userName.isEmpty()) {
            MainActivity::class.java
        } else {
            LoginActivity::class.java
        }
    }

}