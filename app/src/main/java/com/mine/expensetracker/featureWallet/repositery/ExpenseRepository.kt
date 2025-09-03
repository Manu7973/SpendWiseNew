package com.mine.expensetracker.featureWallet.repositery

import android.util.Log
import com.expense.expensetracker.utils.CustomToast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mine.expensetracker.featureWallet.data.ExpenseData
import com.mine.expensetracker.featureWallet.ui.ExpenseListWallet.ExpenseType
import java.util.UUID

class ExpenseRepository {

    private val dbRef = FirebaseDatabase.getInstance().getReference("users")

    fun addExpense(expense: ExpenseData) {
        val expenseId = dbRef.push().key ?: UUID.randomUUID().toString()
        expense.id = expenseId // <-- assign ID here

        val splitWith = mutableMapOf<String, Double>()
        val status = mutableMapOf<String, String>()

        when (expense.type) {
            1 -> {
                val splitAmount = expense.amount / 2
                splitWith[expense.friendID] = splitAmount
                status[expense.myId] = "take"
                status[expense.friendID] = "give"
            }

            2 -> {
                splitWith[expense.friendID] = expense.amount
                status[expense.myId] = "take"
                status[expense.friendID] = "give"
            }

            3 -> {
                val splitAmount = expense.amount / 2
                splitWith[expense.myId] = splitAmount
                status[expense.friendID] = "take"
                status[expense.myId] = "give"
            }

            4 -> {
                splitWith[expense.myId] = expense.amount
                status[expense.friendID] = "take"
                status[expense.myId] = "give"
            }
        }

        val myExpenseMap = mapOf(
            "id" to expense.id,
            "name" to expense.name,
            "date" to expense.date,
            "note" to expense.note,
            "amount" to expense.amount,
            "paidBy" to expense.myId,
            "status" to status[expense.myId],
            "splitWith" to splitWith,
            "type" to expense.type
        )

        val friendExpenseMap = mapOf(
            "id" to expense.id,
            "name" to expense.name,
            "date" to expense.date,
            "note" to expense.note,
            "amount" to expense.amount,
            "paidBy" to expense.myId,
            "status" to status[expense.friendID],
            "splitWith" to splitWith,
            "type" to ExpenseType.mirror(expense.type)
        )

        val updates = hashMapOf<String, Any>(
            "${expense.myId}/friends/${expense.friendID}/expenses/${expense.id}" to myExpenseMap,
            "${expense.friendID}/friends/${expense.myId}/expenses/${expense.id}" to friendExpenseMap
        )

        dbRef.updateChildren(updates)
    }

    fun listenToExpenses(
        myId: String,
        friendId: String,
        onDataChanged: (List<ExpenseData>) -> Unit
    ) {
        val dbRef = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(myId)
            .child("friends")
            .child(friendId)
            .child("expenses")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val expenses = mutableListOf<ExpenseData>()
                for (child in snapshot.children) {
                    val expense = child.getValue(ExpenseData::class.java)
                    if (expense != null) {
                        expenses.add(expense)
                    }
                }
                onDataChanged(expenses)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ExpenseRepository", "Failed to fetch: ${error.message}")
            }
        })
    }

    fun deleteExpense(
        expenseId: String,
        myId: String,
        friendId: String,
        onResult: (Boolean) -> Unit
    ) {
        val updates = hashMapOf<String, Any?>(
            "$myId/friends/$friendId/expenses/$expenseId" to null,
            "$friendId/friends/$myId/expenses/$expenseId" to null
        )

        dbRef.updateChildren(updates)
            .addOnSuccessListener {
                Log.d("DeleteExpense", "Deleted successfully: $expenseId")
                onResult(true)
            }
            .addOnFailureListener { e ->
                Log.e("DeleteExpense", "Failed to delete", e)
                onResult(false)
            }
    }

    fun settleUpTransactions(
        expenseId: String,
        myId: String,
        friendId: String,
        onResult: (Boolean) -> Unit
    ) {
        val updates = hashMapOf<String, Any?>(
            "$myId/friends/$friendId/expenses" to mapOf<String, Any>(),
            "$friendId/friends/$myId/expenses" to mapOf<String, Any>(),
            "$myId/friends/$friendId/isFriend" to true,
            "$friendId/friends/$myId/isFriend" to true
        )

        dbRef.updateChildren(updates)
            .addOnSuccessListener {
                Log.d("SettleUp", "All expenses cleared but friendship kept")
                onResult(true)
            }
            .addOnFailureListener { e ->
                Log.e("SettleUp", "Failed to settle up", e)
                onResult(false)
            }
    }

}
