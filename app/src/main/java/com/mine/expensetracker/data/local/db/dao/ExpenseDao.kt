package com.mine.expensetracker.data.local.db.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mine.expensetracker.data.local.db.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expense_table")
    fun getAllExpense(): Flow<List<ExpenseEntity>>

    @Query("Delete from expense_table")
    suspend fun deleteAllExpense()

    @Insert()
    suspend fun addExpense(expense: ExpenseEntity)

    @Query("SELECT SUM(amount) FROM expense_table")
    suspend fun getTotalAmount(): Double?

    @Query("DELETE FROM expense_table WHERE id = :id")
    suspend fun deleteExpense(id: Int)

    @Query("select * from expense_table where isRecurring =:isRecurring")
    suspend fun getRecurringExpensesByFlag(isRecurring: Boolean): List<ExpenseEntity>

    @Query("select * from expense_table where paymentMode =:paymentMode")
    suspend fun getExpensesByPaymentMode(paymentMode: String): List<ExpenseEntity>

    @Query("select * from expense_table where expense_type =:expense_type")
    suspend fun getExpenseTypeWise(expense_type: String): List<ExpenseEntity>
}