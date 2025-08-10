package com.zobase.expensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zobase.expensetracker.data.local.db.entity.ExpenseEntity
import com.zobase.expensetracker.data.repositery.ExpenseRepositery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepositery
) : ViewModel() {

    private val _expenses = MutableStateFlow<List<ExpenseEntity>>(emptyList())
    val expenses: StateFlow<List<ExpenseEntity>> = _expenses.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllExpenses().collect {
                _expenses.value = it
            }
        }
    }

    suspend fun getExpensesSync(): List<ExpenseEntity> {
        return repository.getAllExpenses().first()
    }

    suspend fun getTotalExpenseAmount(): Double {
        return repository.getTotalExpenseAmount()
    }

    fun addExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            repository.addExpense(expense)
        }
    }

    fun deleteAllExpenses() {
        viewModelScope.launch {
            repository.deleteAllExpenses()
        }
    }
}
