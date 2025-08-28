package com.mine.expensetracker.featureLogin.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mine.expensetracker.featureLogin.repositery.LoginRepository
import com.mine.expensetracker.featureLogin.viewmodel.LoginViewModel

//ViewModelFactory
class LoginViewModelFactory(private val repository: LoginRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        //Exception handling
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
