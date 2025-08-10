package com.zobase.expensetracker.data.repositery

import com.zobase.expensetracker.data.local.db.dao.ExpenseDao
import com.zobase.expensetracker.data.local.db.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

class ExpenseRepositery(val expenseDao: ExpenseDao) {

    suspend fun getAllExpenses(): Flow<List<ExpenseEntity>> {
        return expenseDao.getAllExpense()
    }

    suspend fun addExpense(expense: ExpenseEntity) {
        expenseDao.addExpense(expense)
    }

    suspend fun deleteAllExpenses() {
        expenseDao.deleteAllExpense()
    }

    suspend fun deleteExpenseById(id: Int) {
        expenseDao.deleteExpense(id)
    }

    suspend fun getTotalExpenseAmount(): Double {
        return expenseDao.getTotalAmount() ?: 0.0
    }
}