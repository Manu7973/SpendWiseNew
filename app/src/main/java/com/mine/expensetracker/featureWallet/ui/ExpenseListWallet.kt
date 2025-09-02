package com.mine.expensetracker.featureWallet.ui

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expense.expensetracker.utils.CustomToast
import com.google.firebase.database.FirebaseDatabase
import com.mine.expensetracker.R
import com.mine.expensetracker.data.prefrences.SharedPref
import com.mine.expensetracker.featureWallet.data.ExpenseData
import com.mine.expensetracker.featureWallet.viewmodel.ExpenseViewModel
import com.mine.expensetracker.ui.theme.Green
import com.mine.expensetracker.ui.theme.Grey
import com.mine.expensetracker.ui.theme.Pink40
import com.mine.expensetracker.ui.theme.Secondary
import com.mine.expensetracker.ui.theme.TextBlack
import com.mine.expensetracker.ui.theme.ThemeBG
import com.mine.expensetracker.ui.theme.ThemePrimary
import com.mine.expensetracker.ui.theme.ThinBlack
import com.mine.expensetracker.ui.theme.ThinGrey
import com.mine.expensetracker.ui.theme.ThinThemePrimary
import com.mine.expensetracker.ui.theme.Warning
import com.mine.expensetracker.utils.AppUtils
import com.mine.expensetracker.utils.Constants
import java.util.Calendar
import java.util.UUID

class ExpenseListWallet {

    @Composable
    fun ExpenseListScreen(
        expenseAddedForName: String,
        selectedFriendId: String,
        viewModel: ExpenseViewModel = viewModel(),
        onExpenseClick: (ExpenseData) -> Unit,
        onBack: () -> Unit
    ) {
        val expenses by viewModel.expenses.collectAsState()
        val context = LocalContext.current
        var showAddExpenseScreen by remember { mutableStateOf(false) }
        val title = if (showAddExpenseScreen) "Add Expense" else "Expense"
        val myId = SharedPref.getString(context, Constants.UID)

        LaunchedEffect(selectedFriendId) {
            viewModel.startListening(myId, selectedFriendId)
        }

        val netAmount = remember(expenses, myId) {
            viewModel.calculateNetAmount(expenses, myId)
        }

        val (statusText, statusColor) = when {
            netAmount > 0 -> "You take ₹$netAmount" to Green
            netAmount < 0 -> "You give ₹${-netAmount}" to Warning
            else -> "You owe nothing" to TextBlack
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Secondary)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Secondary)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {

                    CustomTitleCommon(title = title, onBack = {
                        if (showAddExpenseScreen) {
                            showAddExpenseScreen = false
                        } else {
                            onBack()
                        }
                    })

                    if (showAddExpenseScreen) {
                        AddExpenseScreen(
                            expenseAddedForName = expenseAddedForName,
                            onSave = { expenseName, amount, note, date, type ->
                                showAddExpenseScreen = false
                                val exp = ExpenseData(
                                    myId = myId,
                                    friendID = selectedFriendId,
                                    name = expenseName,
                                    date = date,
                                    type = type,
                                    note = note,
                                    amount = amount.toDouble()
                                )
                                viewModel.addExpense(exp)
                            }
                        )
                    } else {
                        // Top Section
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, bottom = 12.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = expenseAddedForName,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                                color = Grey,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                modifier = Modifier.padding(top = 7.dp),
                                text = statusText,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp, fontStyle = FontStyle.Italic,
                                color = statusColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 18.dp),
                            horizontalArrangement = Arrangement.spacedBy(
                                16.dp,
                                Alignment.CenterHorizontally
                            )
                        ) {
                            Button(
                                onClick = { showAddExpenseScreen = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ThinThemePrimary,
                                    contentColor = TextBlack
                                ),
                                shape = RoundedCornerShape(25),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                elevation = ButtonDefaults.buttonElevation(0.dp)
                            ) {
                                Text(
                                    "Add Expense",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Button(
                                onClick = {
                                    if (expenses.isEmpty()) {
                                        CustomToast.showToast(
                                            context,
                                            context.getString(R.string.settleUpSplit),
                                            false
                                        )
                                    } else {
                                        // settle logic
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ThinThemePrimary,
                                    contentColor = TextBlack
                                ),
                                shape = RoundedCornerShape(25),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                elevation = ButtonDefaults.buttonElevation(0.dp)
                            ) {
                                Text(
                                    "Settle up!",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    if (expenses.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "You are all settled up",
                                fontSize = 16.sp,
                                fontStyle = FontStyle.Italic,
                                color = Color.Gray
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            items(expenses, key = { it.id }) { expense ->
                                ExpenseItem(selectedFriendId,
                                    viewModel,
                                    expense = expense,
                                    onClick = {

                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    object ExpenseType {
        const val PAID_BY_YOU = 1
        const val FRIEND_OWED_FULL = 2
        const val FRIEND_PAID_SPLIT = 3
        const val YOU_OWED_FULL_AMOUNT = 4

        fun mirror(type: Int): Int {
            return when (type) {
                PAID_BY_YOU -> FRIEND_PAID_SPLIT      // you paid → friend owes half
                FRIEND_PAID_SPLIT -> PAID_BY_YOU       // friend paid split → you owe half
                YOU_OWED_FULL_AMOUNT -> FRIEND_OWED_FULL // you are owed full → friend owes full
                FRIEND_OWED_FULL -> YOU_OWED_FULL_AMOUNT // friend is owed full → you owe full
                else -> type
            }
        }
    }

    @Composable
    fun AddExpenseScreen(
        expenseAddedForName: String,
        onSave: (String, String, String, String, Int) -> Unit
    ) {
        var expenseName by remember { mutableStateOf("") }
        var amount by remember { mutableStateOf("") }
        var note by remember { mutableStateOf("") }
        val today = remember { AppUtils().getTodayDate() }
        var datex by remember { mutableStateOf(today) }

        // Validation states
        var expenseNameError by remember { mutableStateOf(false) }
        var amountError by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Secondary)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Adding expense for : $expenseAddedForName",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextBlack
            )

            // Title
            OutlinedTextField(
                value = expenseName,
                onValueChange = {
                    expenseName = it
                    if (it.isNotBlank()) {
                        expenseNameError = false
                    }
                },
                label = { Text("Expense Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = textColors(),
                isError = expenseNameError
            )

            // Date picker
            DatePickerTextFieldRestriction(
                "Spend Date",
                date = datex,
                textColors(),
                onDateSelected = { date ->
                    datex = date
                })

            // Amount
            OutlinedTextField(
                value = amount,
                onValueChange = {
                    if (it.all { c -> c.isDigit() }) {
                        amount = it
                        if (it.isNotBlank()) {
                            amountError = false
                        }
                    }
                },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = textColors(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = amountError
            )

            // Split Options
            // Track selected ID instead of string
            var selectedOption by remember { mutableStateOf(ExpenseType.PAID_BY_YOU) }

            val options = listOf(
                ExpenseType.PAID_BY_YOU to "Paid by you, split equally.",
                ExpenseType.YOU_OWED_FULL_AMOUNT to "You are owed the full amount.",
                ExpenseType.FRIEND_PAID_SPLIT to "$expenseAddedForName paid, split equally.",
                ExpenseType.FRIEND_OWED_FULL to "$expenseAddedForName is owed the full amount."
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                options.forEach { (id, label) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedOption = id } // ✅ store ID
                    ) {
                        RadioButton(
                            selected = selectedOption == id,
                            colors = RadioButtonDefaults.colors(
                                selectedColor = ThemePrimary,
                                unselectedColor = Grey
                            ),
                            onClick = { selectedOption = id }
                        )
                        Text(label, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }


            Spacer(modifier = Modifier.weight(1f))

            // Save button
            Button(
                onClick = {
                    var isValid = true

                    if (expenseName.isBlank()) {
                        expenseNameError = true
                        isValid = false
                    }

                    if (amount.isBlank()) {
                        amountError = true
                        isValid = false
                    } else if (amount.toIntOrNull() == null || amount.toInt() <= 0) {
                        amountError = true
                        isValid = false
                    }

                    if (isValid) {
                        onSave(expenseName, amount, note, datex, selectedOption)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ThemePrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }

    @Composable
    fun ExpenseItem(
        selectedFriend: String,
        viewModel: ExpenseViewModel,
        expense: ExpenseData,
        onClick: () -> Unit
    ) {
        val context = LocalContext.current
        val myId = SharedPref.getString(context, Constants.UID)
        var showDeleteDialog by remember { mutableStateOf(false) }

        // Calculate owed/receivable amount
        val (amount, isPositive) = viewModel.calculateNetAmount(expense, myId)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDeleteDialog = true }
                .padding(vertical = 8.dp, horizontal = 12.dp)
                .border(
                    width = 1.dp,
                    color = ThinGrey,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFBBDEFB), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = expense.name.first().toString().uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Expense name + date
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = expense.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                if (expense.date.isNotEmpty()) {
                    Text(
                        text = expense.date,
                        fontSize = 14.sp,
                        color = Grey
                    )
                }
            }

            Text(
                text = if (isPositive) "+ ₹${amount.toInt()}" else "- ₹${amount.toInt()}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = if (isPositive) Green else Warning
            )
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Expense") },
                text = { Text("Are you sure you want to delete ${expense.name}?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteExpense(expense.id, myId, selectedFriend)
                        showDeleteDialog = false
                    }) {
                        Text("Delete", color = Warning)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel", color = TextBlack)
                    }
                },containerColor = ThinGrey
            )
        }
    }

    @Composable
    fun CustomTitleCommon(title: String, onBack: () -> Unit) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Secondary)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Back button aligned to start
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            // Title centered
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    fun DatePickerTextFieldRestriction(
        label: String,
        date: String,
        colors: TextFieldColors,
        onDateSelected: (String) -> Unit
    ) {
        val context = LocalContext.current
        val interactionSource = remember { MutableInteractionSource() }

        OutlinedTextField(
            value = date,
            onValueChange = {},
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .focusable(false)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    showDatePickerDialogRestriction(context, onDateSelected)
                },
            readOnly = true,
            enabled = false,
            colors = colors.copy(
                disabledTextColor = Grey,
                focusedTextColor = Grey,
                disabledContainerColor = Color.Transparent,
                disabledIndicatorColor = colors.unfocusedIndicatorColor,
                disabledLabelColor = colors.unfocusedLabelColor
            ),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.calendar_svg),
                    contentDescription = "Select Date",
                    tint = ThinGrey
                )
            }
        )
    }

    fun showDatePickerDialogRestriction(
        context: Context,
        onDateSelected: (String) -> Unit
    ) {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "%02d-%02d-%04d".format(
                    selectedDay, selectedMonth + 1, selectedYear
                )
                onDateSelected(formattedDate)
            },
            year, month, day
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    @Composable
    fun textColors(): TextFieldColors {
        return TextFieldDefaults.colors(
            focusedTextColor = Grey,
            unfocusedTextColor = Grey,
            disabledTextColor = ThinGrey,
            unfocusedLabelColor = Grey,
            focusedLabelColor = ThemePrimary,

            focusedContainerColor = Secondary,
            unfocusedContainerColor = Secondary,
            disabledContainerColor = Secondary,

            focusedIndicatorColor = ThemePrimary,
            unfocusedIndicatorColor = Grey,
            disabledIndicatorColor = ThinGrey,

            cursorColor = ThemePrimary
        )
    }

}