package com.mine.expensetracker.featureExpense.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import com.mine.expensetracker.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.expense.expensetracker.utils.CustomToast
import com.mine.expensetracker.FeatureExtSettings.ui.CustomExtSettings
import com.mine.expensetracker.data.local.db.entity.ExpenseEntity
import com.mine.expensetracker.featureProfileSettings.viewmodel.ProfileViewModel
import com.mine.expensetracker.ui.theme.ExtraThinThemePrimary
import com.mine.expensetracker.ui.theme.Grey
import com.mine.expensetracker.ui.theme.Secondary
import com.mine.expensetracker.ui.theme.TextBlack
import com.mine.expensetracker.ui.theme.ThemeBG
import com.mine.expensetracker.ui.theme.ThemePrimary
import com.mine.expensetracker.ui.theme.ThinGrey
import com.mine.expensetracker.utils.AppUtils
import com.mine.expensetracker.utils.AppUtils.Companion.getUserName
import com.mine.expensetracker.viewmodel.ExpenseViewModel
import java.util.Calendar
import kotlin.math.abs

class ExpenseView {
    @Composable
    fun ShowExpenseView(
        activity: Activity, expenseViewModel: ExpenseViewModel, profileViewModel: ProfileViewModel
    ) {
        AppUtils.styleSystemBars(
            activity,
            backgroundColor = ThemeBG.toArgb(),
            false, false
        )
        Row {
            MainBackGround(expenseViewModel, profileViewModel)
        }
    }

    @Composable
    fun MainBackGround(expenseViewModel: ExpenseViewModel, profileViewModel: ProfileViewModel) {
        val context = LocalContext.current
        val expenses by expenseViewModel.expenses.collectAsState()
        val insets = WindowInsets.statusBars.asPaddingValues()
        val topPadding = min(insets.calculateTopPadding(), 20.dp)

        var showNotifications by remember { mutableStateOf(false) }
        var showProfileView by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxSize()) {
            if (showNotifications) {
                CustomExtSettings().Notifications(
                    context,
                    onBack = { showNotifications = false }
                )
            } else if (showProfileView) {
                CustomExtSettings().EditProfile(context, onBack = { showNotifications = false })
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(ThemeBG)
                            .padding(top = topPadding)
                    ) {
                        // Top Section
                        Column(
                            modifier = Modifier
                                .weight(0.3f)
                                .fillMaxWidth()
                                .background(ThemeBG)
                        ) {
                            Header(context, profileViewModel, onProfileClick = {

                            }, onNotificationClick = {
                                showNotifications = true
                            })
                        }

                        // Bottom Rounded White Section
                        Column(
                            modifier = Modifier
                                .weight(0.7f)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(topStart = 34.dp, topEnd = 34.dp))
                                .background(Secondary)
                                .padding(top = topPadding + 60.dp)
                        ) {
                            ActionButtonsRow(context, expenseViewModel)

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Transactions",
                                fontSize = 20.sp,
                                color = TextBlack,
                                modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                                fontWeight = FontWeight.SemiBold
                            )

                            if (expenses.isEmpty()) {
                                Text(
                                    text = "No Transactions Available!",
                                    fontSize = 18.sp,
                                    color = Grey,
                                    modifier = Modifier.padding(top = 40.dp, start = 20.dp),
                                    fontWeight = FontWeight.Medium
                                )
                            } else {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp, bottom = 5.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(20.dp)
                                ) {
                                    items(expenses) { expense ->
                                        Transactions(expense) //Composable that renders a single expense
                                    }
                                }
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = topPadding + 60.dp)
                    ) {
                        BudgetCard(expenseViewModel)
                    }
                }
            }
        }
    }

    @Composable
    fun Header(
        context: Context, profileViewModel: ProfileViewModel,
        onNotificationClick: () -> Unit,
        onProfileClick: () -> Unit
    ) {
        val profileImageUri by profileViewModel.profileImageUri.collectAsState()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile animation
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .border(1.dp, ThinGrey, CircleShape)
                    .clickable { onProfileClick() }
                    .background(Secondary),
                contentAlignment = Alignment.Center
            ) {
                if (profileImageUri != null && !profileImageUri.toString().isEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(profileImageUri),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape)
                        ,contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.dummy_display_pic),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape)
                        ,contentScale = ContentScale.Crop
                    )
                }
            }

            // User name
            Text(
                text = "Hi, \n ${getUserName(context)}",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp),
                color = ThinGrey,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )

            // Notification icon
            Box(
                modifier = Modifier
                    .clickable { onNotificationClick() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.notification),
                    contentDescription = "Notification",
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }
    }

    @Composable
    fun Transactions(expense: ExpenseEntity) {

        val expenseName = expense.expenseName
        var showDetails by remember { mutableStateOf(false) }

        @DrawableRes
        val iconId: Int = when (expenseName) {
            "Grocery" -> R.drawable.grocery
            "Travel" -> R.drawable.travel
            "Food" -> R.drawable.food
            "Medical" -> R.drawable.medical
            "Shopping" -> R.drawable.shopping
            "Recharge & Bill" -> R.drawable.bill
            "Other" -> R.drawable.application
            else -> R.drawable.application
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDetails = true }
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = ExtraThinThemePrimary,
                    tonalElevation = 3.dp,
                    shadowElevation = 3.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = iconId),
                            contentDescription = expense.expenseName,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = expense.expenseName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Text(
                        text = expense.spendDate,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }

                val isPositive = expense.expense_type == 1
                Text(
                    text = (if (isPositive) "- ₹" else "+ ₹") + abs(expense.amount).toString(),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isPositive) Color(0xFFF44336) else Color(0xFF4CAF50)
                )
            }
        }

        // Show popup on click
        if (showDetails) {
            AlertDialog(
                onDismissRequest = { showDetails = false },
                confirmButton = {
                    TextButton(onClick = { showDetails = false }) {
                        Text("Close")
                    }
                },
                title = { Text("Transaction Info") },
                text = {
                    Column {
                        Text("Payment Mode: ${expense.paymentMode}")
                        if (expense.isRecurring == true) {
                            Text("This is a recurring transaction.")
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (!expense.note.isEmpty()) {
                            Text("Note: ${expense.note}")
                        }
                    }
                }


            )
        }
    }

    @Composable
    fun ActionButtonsRow(
        context: Context,
        expenseViewModel: ExpenseViewModel
    ) {
        var sendDialog by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ActionButton("Spent", R.drawable.spend) {
                sendDialog = true
            }
            ActionButton("Received", R.drawable.receive) {
                CustomToast.showToast(
                    context,
                    context.getString(R.string.featureUnavailable),
                    false
                )
            }
        }

        if (sendDialog) {
            AddExpense(
                onDismiss = { sendDialog = false },
                onSave = { expenseType, amount, spendDate, note, isRecurring, paymentMode ->
                    sendDialog = false
                    val expense = ExpenseEntity(
                        0,
                        userName = getUserName(context),
                        expense_type = 1,
                        amount = amount.toInt(),
                        expenseName = expenseType,
                        spendDate = spendDate,
                        isRecurring = isRecurring,
                        paymentMode = paymentMode,
                        note = note
                    )
                    CustomToast.showToast(context, "Expense added!", false)
                    expenseViewModel.addExpense(expense)
                },
            )
        }
    }

    @Composable
    fun ActionButton(label: String, @DrawableRes iconRes: Int, onClick: () -> Unit) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                    .shadow(6.dp, RoundedCornerShape(12.dp), clip = false)
                    .background(Secondary),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onClick, modifier = Modifier.size(40.dp)) {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = label,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(label, fontSize = 12.sp, color = TextBlack, fontWeight = FontWeight.SemiBold)
        }
    }

    @Composable
    fun AddExpense(
        onDismiss: () -> Unit,
        onSave: (String, String, String, String, Boolean, String) -> Unit
    ) {
        var expense by remember { mutableStateOf("") }
        var amount by remember { mutableStateOf("") }
        var spendDate by remember { mutableStateOf("") }
        var paymentMode by remember { mutableStateOf("") }
        var isRecurring by remember { mutableStateOf(false) }
        var note by remember { mutableStateOf("") }
        val context = LocalContext.current

        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = ThemeBG,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Column(modifier = Modifier.padding(20.dp)) {

                    // Header Row with title and close icon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Add Expense",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary
                        )
                        IconButton(onClick = onDismiss) {
                            Icon(
                                painter = painterResource(id = R.drawable.close),
                                contentDescription = "Close",
                                tint = Secondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    var expenseType: List<String> = listOf(
                        "Grocery",
                        "Travel",
                        "Food",
                        "Medical",
                        "Shopping",
                        "Recharge & Bill",
                        "Other"
                    )

                    var transactionMode: List<String> =
                        listOf("Cash", "UPI", "Credit-Card", "Debit-Card")

                    ExpenseTypeDropdown(
                        "Expense Type",
                        selectedType = expense,
                        onTypeSelected = { expense = it },
                        expenseType
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Amount", color = ThinGrey) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = textColors()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    DatePickerTextFieldRestriction(
                        "Spend Date",
                        spendDate,
                        textColors(),
                        onDateSelected = { date ->
                            spendDate = date
                        })

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp)
                    ) {
                        Checkbox(
                            checked = isRecurring,
                            onCheckedChange = { isRecurring = it },
                            modifier = Modifier.padding(start = 0.dp),
                            colors = CheckboxDefaults.colors(
                                checkedColor = ThemePrimary,
                                uncheckedColor = Grey,
                                checkmarkColor = Secondary
                            )
                        )

                        Text(
                            text = "is Recurring?",
                            modifier = Modifier.padding(start = 8.dp),
                            color = ThinGrey
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    ExpenseTypeDropdown(
                        "Payment Mode",
                        selectedType = paymentMode,
                        onTypeSelected = { paymentMode = it },
                        transactionMode
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("Note", color = ThinGrey) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textColors()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel", color = Secondary)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (isAddExpenseValid(
                                        context,
                                        expense,
                                        amount,
                                        spendDate,
                                        note,
                                        paymentMode
                                    )
                                ) {
                                    onSave(
                                        expense,
                                        amount,
                                        spendDate,
                                        note,
                                        isRecurring,
                                        paymentMode
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ThemePrimary)
                        ) {
                            Text("Save", color = Secondary)
                        }
                    }
                }
            }
        }
    }

    fun isAddExpenseValid(
        context: Context,
        expenseType: String,
        amount: String,
        spendDate: String,
        note: String,
        paymentMode: String
    ): Boolean {
        if (expenseType.isBlank()) {
            CustomToast.showToast(context, "Please select expense type", false)
            return false
        }

        if (amount.isEmpty() || amount.isBlank() && amount.toInt() <= 0) {
            CustomToast.showToast(context, "Please enter a valid Amount", false)
            return false
        }

        if (spendDate.isBlank()) {
            CustomToast.showToast(context, "Please enter a spend date", false)
            return false
        }

        if (paymentMode.isBlank()) {
            CustomToast.showToast(context, "Please select payment mode", false)
            return false
        }
        return true
    }

    @Composable
    fun ExpenseTypeDropdown(
        text: String,
        selectedType: String,
        onTypeSelected: (String) -> Unit,
        expenseTypes: List<String>
    ) {
        var expanded by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }) {
                OutlinedTextField(
                    value = selectedType,
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text(text, color = Secondary) },
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                tint = Secondary,
                                painter = painterResource(R.drawable.arrow_drop_down),
                                contentDescription = "Dropdown Icon"
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = !expanded },
                    colors = textColors()
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    expenseTypes.forEach { type ->
                        DropdownMenuItem(
                            onClick = {
                                onTypeSelected(type)
                                expanded = false
                            },
                            text = { Text(type) }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun BudgetCard(expenseViewModel: ExpenseViewModel) {
        // Collect the expenses list as State in Compose
        val expenses by expenseViewModel.expenses.collectAsState()

        val totalAmount = remember(expenses) {
            // If amount is Int type, sum as Int and convert to Double
            expenses.sumOf {
                when (it.amount) {
                    is Int -> (it.amount as Int).toDouble()
                    is String -> (it.amount as String).toDoubleOrNull() ?: 0.0
                    is Double -> it.amount as Double
                    else -> 0.0
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.90f)  // 90% width
                    .height(180.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(
                    pressedElevation = 4.dp,
                    focusedElevation = 4.dp,
                    defaultElevation = 6.dp
                )
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.credit_card_bg_2),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = "Calendar",
                        tint = Secondary,
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.TopEnd)
                            .padding(top = 10.dp, end = 10.dp)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Total Spent",
                            style = MaterialTheme.typography.titleMedium.copy(color = Secondary)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "₹ ${"%.2f".format(totalAmount)}",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = Secondary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }


    @Composable
    fun textColors(): TextFieldColors {
        return TextFieldDefaults.colors(
            focusedTextColor = Secondary,
            unfocusedTextColor = Secondary,
            disabledTextColor = ThinGrey,
            focusedContainerColor = ThemeBG,
            unfocusedContainerColor = ThemeBG,
            disabledContainerColor = ThemeBG,
            focusedIndicatorColor = ThemePrimary,
            unfocusedIndicatorColor = Grey,
            disabledIndicatorColor = Grey,
            cursorColor = Secondary
        )
    }

    @Composable
    fun DatePickerTextFieldRestriction(
        label: String,
        date: String,
        colors: TextFieldColors,
        onDateSelected: (String) -> Unit
    ) {
        val context = LocalContext.current

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showDatePickerDialogRestriction(context, onDateSelected)
                }
        ) {
            OutlinedTextField(
                value = date,
                onValueChange = {},
                label = { Text(label, color = ThinGrey) },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false,
                colors = colors,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar_svg),
                        contentDescription = "Select Date",
                        tint = ThinGrey
                    )
                }
            )
        }
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

        // ✅ Prevent selecting future dates
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        datePickerDialog.show()
    }
}