
package com.mine.expensetracker.featureWallet.data

data class ExpenseData(
    var id : String ="",
    var myId: String = "",
    var friendID: String = "",
    var name: String = "",
    var date: String = "",
    var type: Int = 1,
    var note: String = "",
    var amount: Double = 0.0
)

