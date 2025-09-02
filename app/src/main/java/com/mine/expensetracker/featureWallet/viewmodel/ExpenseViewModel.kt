package com.mine.expensetracker.featureWallet.viewmodel

import androidx.lifecycle.ViewModel
import com.mine.expensetracker.featureWallet.data.ExpenseData
import com.mine.expensetracker.featureWallet.repositery.ExpenseRepository
import com.mine.expensetracker.featureWallet.ui.ExpenseListWallet.ExpenseType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ExpenseViewModel(
    private val repository: ExpenseRepository = ExpenseRepository()
) : ViewModel() {

    private val _expenses = MutableStateFlow<List<ExpenseData>>(emptyList())
    val expenses: StateFlow<List<ExpenseData>> = _expenses

    fun startListening(myId: String, friendId: String) {
        repository.listenToExpenses(myId, friendId) { list ->
            _expenses.value = list
        }
    }

    fun addExpense(expense: ExpenseData) {
        repository.addExpense(expense)
    }

    fun calculateNetAmount(expense: ExpenseData, myId: String): Pair<Double, Boolean> {
        return when (expense.type) {
            ExpenseType.PAID_BY_YOU -> Pair(expense.amount / 2, true)      // ✅ you get money
            ExpenseType.FRIEND_OWED_FULL -> Pair(expense.amount, false)    // ❌ you owe
            ExpenseType.FRIEND_PAID_SPLIT -> Pair(expense.amount / 2, false) // ❌ you owe half
            ExpenseType.YOU_OWED_FULL_AMOUNT -> Pair(expense.amount, true) // ✅ you get full
            else -> Pair(0.0, true)
        }
    }

    fun calculateNetAmount(expenses: List<ExpenseData>, myId: String): Double {
        var netAmount = 0.0
        for (expense in expenses) {
            when (expense.type) {
                ExpenseType.PAID_BY_YOU -> netAmount += expense.amount
                ExpenseType.FRIEND_OWED_FULL -> netAmount += expense.amount
                ExpenseType.FRIEND_PAID_SPLIT -> netAmount -= expense.amount / 2
                ExpenseType.YOU_OWED_FULL_AMOUNT -> netAmount -= expense.amount
            }
        }
        return netAmount
    }

    fun deleteExpense(expenseId: String, myId: String, friendId: String) {
        repository.deleteExpense(expenseId, myId, friendId)

    }


}
