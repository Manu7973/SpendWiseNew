package com.mine.expensetracker.featureWallet.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mine.expensetracker.R
import com.mine.expensetracker.ui.theme.Secondary
import com.mine.expensetracker.utils.AppUtils
import com.mine.expensetracker.viewmodel.ExpenseViewModel

//Wallet could be used to display users total budget, saving and expenses.
class ExpenseWallet {
    @Composable
    fun ShowWallet(
        activity: Activity,
        expenseViewModel: ExpenseViewModel
    ) {
        AppUtils.styleSystemBars(activity, backgroundColor = Secondary.toArgb(), true, false)
        Row(
            modifier = Modifier
                .fillMaxSize()                       // fill full width and height
                .background(Secondary),
            verticalAlignment = Alignment.CenterVertically,  // center vertically
            horizontalArrangement = Arrangement.Center       // center horizontally
        ) {
            val context = LocalContext.current
            Text(
                text = context.getString(R.string.featureUnavailable),
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Medium
            )
        }
    }
}