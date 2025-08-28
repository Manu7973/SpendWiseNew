package com.mine.expensetracker.featureProfileSettings.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.ui.unit.Constraints
import androidx.lifecycle.AndroidViewModel

import com.google.android.gms.common.api.internal.zacp
import com.mine.expensetracker.data.prefrences.SharedPref
import com.mine.expensetracker.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    // StateFlow to expose current profile image URI
    private val _profileImageUri = MutableStateFlow<String?>(
        SharedPref.getString(
            application.applicationContext,
            Constants.PROFILE_URI
        )
    )
    val profileImageUri: StateFlow<String?> = _profileImageUri

    fun saveProfileImage(context: Context, uri: Uri) {
        try {
            // Persist read permission
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
            Log.d("ProfileViewModel", "exception: ${e.message}")
        }
        Log.d("ProfileViewModel", "saveProfileImage: $uri")
        SharedPref.setString(context, Constants.PROFILE_URI, uri.toString())
        _profileImageUri.value = uri.toString()
    }
}