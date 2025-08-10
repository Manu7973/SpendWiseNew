package com.zobase.expensetracker.featureExpenseReport.ui

import android.app.Activity
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.zobase.expensetracker.R
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewModelScope
import com.zobase.expensetracker.data.local.db.entity.ExpenseEntity
import com.zobase.expensetracker.ui.theme.Alert
import com.zobase.expensetracker.ui.theme.Black
import com.zobase.expensetracker.ui.theme.Blue
import com.zobase.expensetracker.ui.theme.Green
import com.zobase.expensetracker.ui.theme.Pink40
import com.zobase.expensetracker.ui.theme.Purple40
import com.zobase.expensetracker.ui.theme.Secondary
import com.zobase.expensetracker.ui.theme.TextBlack
import com.zobase.expensetracker.ui.theme.ThemeBG
import com.zobase.expensetracker.ui.theme.ThinBlack
import com.zobase.expensetracker.ui.theme.ThinGrey
import com.zobase.expensetracker.ui.theme.Warning
import com.zobase.expensetracker.utils.AppUtils
import com.zobase.expensetracker.viewmodel.ExpenseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ExpenseReportView {

    @Composable
    fun ShowReportScreen(
        activity: Activity,
        expenseViewModel: ExpenseViewModel
    ) {
        AppUtils.styleSystemBars(activity, backgroundColor = Secondary.toArgb(), true, false)
        Row(modifier = Modifier.fillMaxWidth()) {
            Topbar(expenseViewModel)
        }
    }

    @Composable
    fun Topbar(expenseViewModel: ExpenseViewModel) {
        val context = LocalContext.current
        val categoryTotalsState = remember { mutableStateOf<Map<String, Int>>(emptyMap()) }
        val totalAmountState = remember { mutableStateOf(0) }
        val isLoading = remember { mutableStateOf(false) }

        // Trigger initial load with "All" filter
        LaunchedEffect(Unit) {
            isLoading.value = true
            calculateTotalExpense(
                context,
                expenseViewModel = expenseViewModel,
                filterType = "All"
            ) { categoryTotals, totalAmount ->
                categoryTotalsState.value = categoryTotals
                totalAmountState.value = totalAmount
                isLoading.value = false
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Secondary)
                .padding(top = 20.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 10.dp, bottom = 15.dp),
                text = "Insight",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextBlack,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    "Filter: ",
                    color = Black,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 10.dp),
                    fontWeight = FontWeight.Bold
                )

                DropdownFilter(onFilterSelected = { filterType ->
                    isLoading.value = true
                    categoryTotalsState.value = emptyMap()
                    totalAmountState.value = 0

                    calculateTotalExpense(context, expenseViewModel, filterType) { categoryTotals, totalAmount ->
                        categoryTotalsState.value = categoryTotals
                        totalAmountState.value = totalAmount
                        isLoading.value = false
                    }
                })
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Secondary, shape = RoundedCornerShape(12.dp)),
            ) {
                if (categoryTotalsState.value.isNotEmpty() && totalAmountState.value > 0) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        StylishDonutChart(
                            categoryTotals = categoryTotalsState.value,
                            totalAmountState.value
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Top Categories",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 15.dp),
                            color = TextBlack,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            items(categoryTotalsState.value.entries.toList()) { expense ->
                                Transactions(expense, totalAmountState.value)
                            }
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.no_transation),
                            contentDescription = "No transactions",
                            modifier = Modifier.size(200.dp)
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun Transactions(expense: Map.Entry<String, Int>, total: Int) {
        val category = expense.key
        val amount = expense.value

        @DrawableRes
        val iconId: Int = when (category) {
            "Grocery" -> R.drawable.grocery
            "Travel" -> R.drawable.travel
            "Food" -> R.drawable.food
            "Medical" -> R.drawable.medical
            "Shopping" -> R.drawable.shopping
            "Recharge & Bill" -> R.drawable.bill
            "Other" -> R.drawable.application
            else -> R.drawable.application
        }

        val categoryColorMap = mapOf(
            "Grocery" to Green,
            "Travel" to Blue,
            "Food" to Purple40,
            "Medical" to Alert,
            "Shopping" to Warning,
            "Recharge & Bill" to ThinBlack,
            "Other" to Pink40,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val color = categoryColorMap[category] ?: Color.Gray

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = color,
                    tonalElevation = 3.dp,
                    shadowElevation = 3.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = iconId),
                            contentDescription = category,
                            modifier = Modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(Secondary)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier
                            .padding(bottom = 3.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = category,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )

                        Text(
                            text = "₹ ${amount}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = color
                        )
                    }

                    LinearProgressIndicator(
                        progress = amount.toFloat() / total.toFloat(),
                        modifier = Modifier.fillMaxWidth(),
                        color = color,
                        trackColor = ThinGrey
                    )
                }
            }
        }
    }

    @Composable
    fun StylishDonutChart(
        categoryTotals: Map<String, Int>, total: Int
    ) {
        val totalAmount = categoryTotals.values.sum()

        val categoryColorMap = mapOf(
            "Grocery" to Green,
            "Travel" to Blue,
            "Food" to Purple40,
            "Medical" to Alert,
            "Shopping" to Warning,
            "Recharge & Bill" to ThinBlack,
            "Other" to Pink40,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(180.dp)) {
                var startAngle = -90f

                categoryTotals.entries.forEach { (category, value) ->
                    val angle = 360f * value / totalAmount.toFloat()
                    val color = categoryColorMap[category] ?: Color.Gray

                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = angle,
                        useCenter = false,
                        style = Stroke(width = 40.dp.toPx(), cap = StrokeCap.Round)
                    )
                    startAngle += angle
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "₹${total}",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "your limit",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                )
            }
        }
    }

    fun calculateTotalExpense(
        context: Context,
        expenseViewModel: ExpenseViewModel,
        filterType: String,
        onResult: (Map<String, Int>, Int) -> Unit
    ) {
        expenseViewModel.viewModelScope.launch {
            val expenses = expenseViewModel.getExpensesSync() // Fetch all expenses

            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

            val filteredExpenses = when (filterType) {
                "Today" -> {
                    val today = LocalDate.now()
                    expenses.filter { LocalDate.parse(it.spendDate, formatter) == today }
                }
                "Past Week" -> {
                    val weekAgo = LocalDate.now().minusWeeks(1)
                    expenses.filter { LocalDate.parse(it.spendDate, formatter).isAfter(weekAgo) }
                }
                "Past Month" -> {
                    val monthAgo = LocalDate.now().minusMonths(1)
                    expenses.filter { LocalDate.parse(it.spendDate, formatter).isAfter(monthAgo) }
                }
                else -> expenses
            }

            val categoryTotals = filteredExpenses.groupBy { it.expenseName ?: "Other" }
                .mapValues { entry -> entry.value.sumOf { it.amount.toInt() } }

            val totalAmount = filteredExpenses.sumOf { it.amount.toInt() }

            withContext(Dispatchers.Main) {
                onResult(categoryTotals, totalAmount)
            }
        }
    }

    @Composable
    fun DropdownFilter(
        onFilterSelected: (String) -> Unit
    ) {
        var mExpanded by remember { mutableStateOf(false) }
        var mSelectedText by remember { mutableStateOf("All") }
        var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

        val icon = if (mExpanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown

        val filterOptions = listOf("All", "Past Week", "Past Month", "Today")

        Column(modifier = Modifier.padding(2.dp)) {
            Box(
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        mTextFieldSize = coordinates.size.toSize()
                    }
            ) {
                OutlinedTextField(
                    value = mSelectedText,
                    onValueChange = {},
                    label = { Text("Select Filter") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            icon,
                            contentDescription = "Dropdown Icon"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { mExpanded = true }
                )
            }

            DropdownMenu(
                expanded = mExpanded,
                onDismissRequest = { mExpanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
            ) {
                filterOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            mSelectedText = option
                            mExpanded = false
                            onFilterSelected(option)
                        }
                    )
                }
            }
        }
    }
}
