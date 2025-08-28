package com.mine.expensetracker.featureLogin.viewmodel

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mine.expensetracker.featureLogin.repositery.LoginRepository
import kotlinx.coroutines.launch
import kotlin.text.isBlank

//Login view model
class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    var errorMessage by mutableStateOf<String?>(null)
    var loginSuccess by mutableStateOf(false)

    fun getGoogleSignInIntent(): Intent {
        return repository.getGoogleSignInIntent()
    }

    fun handleGoogleSignInResult(data: Intent?) {
        val result = repository.handleGoogleSignInResult(data)
        if (result.isSuccess) {
            val account = result.getOrNull()
            val username = account?.displayName ?: ""
            val email = account?.email ?: ""
            repository.saveUserData(username)
            repository.saveEmailData(email)
            loginSuccess = true
            errorMessage = null
        } else {
            errorMessage = result.exceptionOrNull()?.message ?: "Google Sign-In failed"
        }
    }

    // Email/Password Login
    fun loginWithEmail(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Email and Password cannot be empty"
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage = "Invalid email format"
            return
        }
        if (password.length < 6) {
            errorMessage = "Password must be at least 6 characters"
            return
        }

        viewModelScope.launch {
            val isSuccess = repository.loginWithEmail(email, password)
            if (isSuccess) {
                repository.saveEmailData(email)
                loginSuccess = true
                errorMessage = null
            } else {
                errorMessage = "Invalid email or password"
            }
        }
    }
}

