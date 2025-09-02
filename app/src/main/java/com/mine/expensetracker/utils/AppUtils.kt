package com.mine.expensetracker.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.mine.expensetracker.data.prefrences.SharedPref
import com.mine.expensetracker.featureSplash.ui.SplashScreen
import java.util.Calendar


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
            return name
        }
    }

    fun logout(context: Context) {
        SharedPref.clearSharedPref(context)
        val intent = Intent(context, SplashScreen::class.java)
        context.startActivity(intent)
    }

    fun getTodayDate(): String {
        val calendar = Calendar.getInstance()
        return "%02d-%02d-%04d".format(
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.YEAR)
        )
    }

}