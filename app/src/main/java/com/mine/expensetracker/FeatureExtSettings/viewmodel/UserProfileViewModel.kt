package com.mine.expensetracker.FeatureExtSettings.viewmodel

import android.app.Application
import android.content.Context
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.expense.expensetracker.utils.CustomToast
import com.mine.expensetracker.R
import com.mine.expensetracker.featureHome.repositery.UserRepository

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var gender by mutableStateOf("")

    // error messages
    var usernameError by mutableStateOf<String?>(null)
    var emailError by mutableStateOf<String?>(null)
    var genderError by mutableStateOf<String?>(null)

    init {
        // Load values from SharedPref when ViewModel is created
        username = repository.getUsername()
        email = repository.getEmail()
        gender = repository.getGender()
    }

    fun validate(): Boolean {
        var isValid = true

        if (username.isBlank()) {
            usernameError = "Username is required"
            isValid = false
        } else usernameError = null

        if (email.isBlank()) {
            emailError = "Email is required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email format"
            isValid = false
        } else emailError = null

        if (gender.isBlank()) {
            genderError = "Please select gender"
            isValid = false
        } else genderError = null

        return isValid
    }

    fun onSave(context: Context) {
        if (validate()) {
            CustomToast.Companion.showToast(context, context.getString(R.string.profileUpdate),false)
            UserRepository(context).saveUser(username, email, gender)
        }
    }
}