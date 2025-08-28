package com.mine.expensetracker.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mine.expensetracker.data.repositery.ExpenseRepositery
import com.mine.expensetracker.viewmodel.ExpenseViewModel
import kotlin.jvm.java

class ExpenseViewModelFactory(val repository: ExpenseRepositery) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}