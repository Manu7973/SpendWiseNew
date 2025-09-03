package com.mine.expensetracker.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.remember
import androidx.core.content.FileProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.mine.expensetracker.R
import com.mine.expensetracker.data.prefrences.SharedPref
import com.mine.expensetracker.featureSplash.ui.SplashScreen
import java.io.File
import java.io.FileOutputStream
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

    fun shareCode(context: Context) {
        val uniqueCode = SharedPref.getString(context, Constants.UID)
        // Text to share
        val shareText = """
        Hey! 👋  
        Download this awesome app and add me as a friend! 🎉
        
        👉 My Code: $uniqueCode
        👉 Download here: https://play.google.com/store/apps/details?id=${context.packageName}
    """.trimIndent()

        // Pick an image (app logo from drawable)
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.app_icon) // replace with your image
        val file = File(context.cacheDir, "share_image.png")
        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        val imageUri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider", // defined in manifest
            file
        )

        // Create share intent
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_STREAM, imageUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        // Launch chooser
        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
}