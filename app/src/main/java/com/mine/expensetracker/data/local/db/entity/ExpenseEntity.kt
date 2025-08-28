package com.mine.expensetracker.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_table")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo val userName: String,
    @ColumnInfo val expense_type: Int, //1 - send, 2- Received
    @ColumnInfo val amount: Int,
    @ColumnInfo val expenseName: String,
    @ColumnInfo val spendDate: String,
    @ColumnInfo val paymentMode: String,
    @ColumnInfo val isRecurring: Boolean,
    @ColumnInfo val note: String
)