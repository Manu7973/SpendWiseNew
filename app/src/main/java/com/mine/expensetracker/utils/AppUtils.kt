package com.mine.expensetracker.utils

import android.app.Activity
import android.content.Context
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.mine.expensetracker.data.prefrences.SharedPref


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

}