package com.mine.expensetracker.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mine.expensetracker.data.local.db.dao.ExpenseDao
import com.mine.expensetracker.data.local.db.entity.ExpenseEntity
import kotlin.jvm.java

@Database(entities = [ExpenseEntity::class], version = 1)
abstract class ExpenseInstance : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: ExpenseInstance? = null

        fun getInstance(context: Context): ExpenseInstance {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseInstance::class.java,
                    "expense_table"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}