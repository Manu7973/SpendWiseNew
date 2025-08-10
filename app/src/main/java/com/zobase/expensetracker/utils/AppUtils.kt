package com.zobase.expensetracker.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.zobase.expensetracker.data.prefrences.SharedPref
import com.zobase.expensetracker.featureSplash.ui.SplashScreen
import kotlin.jvm.java


class AppUtils {
    companion object {
        fun styleSystemBars(
            activity: Activity,
            backgroundColor: Int,
            lightStatusBarText: Boolean, bottomBarColorSet: Boolean
        ) {
            val window = activity.window
            WindowCompat.setDecorFitsSystemWindows(window, false)

            window.statusBarColor = backgroundColor
            if (bottomBarColorSet) {
                window.navigationBarColor = backgroundColor
            }

            val controller = WindowInsetsControllerCompat(window, window.decorView)
            controller.isAppearanceLightStatusBars = lightStatusBarText
            controller.isAppearanceLightNavigationBars = lightStatusBarText
        }

        fun getUserName(context: Context): String {
            val name = SharedPref.getString(context, Constants.USERNAME)
            return if(name.isEmpty()){
                "Guest"
            }else{
                name
            }
        }
    }

}