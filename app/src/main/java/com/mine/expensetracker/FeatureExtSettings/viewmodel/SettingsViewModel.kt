package com.mine.expensetracker.FeatureExtSettings.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.mine.expensetracker.data.prefrences.SharedPref
import com.mine.expensetracker.utils.Constants

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext

    var notificationsEnabled by mutableStateOf(false)

    var guestUserLogin by mutableStateOf(0)

    var storagePreference by mutableStateOf(false)
        internal set

    var remindersEnabled by mutableStateOf(false)
        internal set

    var darkModeEnabled by mutableStateOf(false)
        private set

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        notificationsEnabled = SharedPref.getBoolean(context, Constants.NOTIFICATION)
        remindersEnabled = SharedPref.getBoolean(context, Constants.REMINDERS)
        darkModeEnabled = SharedPref.getBoolean(context, Constants.DARK_MODE)
        guestUserLogin = SharedPref.getInt(context, Constants.LOGIN_TYPE)
        storagePreference = SharedPref.getBoolean(context, Constants.STORAGE_PREF)
    }

    fun setNotifications(enabled: Boolean) {
        notificationsEnabled = enabled
        SharedPref.setBoolean(context, Constants.NOTIFICATION, enabled)
    }

    fun setReminders(enabled: Boolean) {
        remindersEnabled = enabled
        SharedPref.setBoolean(context, Constants.REMINDERS, enabled)
    }

    fun setDarkMode(enabled: Boolean) {
        darkModeEnabled = enabled
        SharedPref.setBoolean(context, Constants.DARK_MODE, enabled)
    }

    fun setStoragePreference(enabled: Boolean) {
        storagePreference = enabled
        SharedPref.setBoolean(context, Constants.STORAGE_PREF, enabled)
    }
}
