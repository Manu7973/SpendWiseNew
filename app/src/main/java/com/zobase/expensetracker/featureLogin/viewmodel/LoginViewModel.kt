package com.zobase.expensetracker.featureLogin.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.zobase.expensetracker.featureLogin.repositery.LoginRepository
import kotlin.text.isBlank

//Login view model
class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)
    var loginSuccess by mutableStateOf(false)

    fun onUsernameChange(newUsername: String) {
        username = newUsername
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun login() {
        // Validation
        if (username.isBlank() || password.isBlank()) {
            errorMessage = "Username and Password cannot be empty"
            return
        }
        if (password.length < 6) {
            errorMessage = "Password must be at least 6 characters"
            return
        }

        // Business Logic
        val isSuccess = repository.login(username, password)
        if (isSuccess) {
            repository.saveUserData(username)
            loginSuccess = true
            errorMessage = null
        } else {
            errorMessage = "Invalid username or password"
        }
    }
}

