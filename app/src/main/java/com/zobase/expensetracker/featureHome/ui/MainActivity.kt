package com.zobase.expensetracker.featureHome.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.zobase.expensetracker.R
import com.zobase.expensetracker.data.local.db.ExpenseInstance
import com.zobase.expensetracker.data.repositery.ExpenseRepositery
import com.zobase.expensetracker.featureExpense.ui.ExpenseView
import com.zobase.expensetracker.featureExpenseReport.ui.ExpenseReportView
import com.zobase.expensetracker.featureProfileSettings.ui.ProfileSettings
import com.zobase.expensetracker.featureWallet.ui.ExpenseWallet
import com.zobase.expensetracker.ui.theme.Black
import com.zobase.expensetracker.ui.theme.Secondary
import com.zobase.expensetracker.ui.theme.ThemePrimary
import com.zobase.expensetracker.viewmodel.ExpenseViewModel
import com.zobase.expensetracker.viewmodel.viewmodelfactory.ExpenseViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var expenseViewModel: ExpenseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ViewModel setup
        val expenseDao = ExpenseInstance.getInstance(this).expenseDao()
        val expenseRepository = ExpenseRepositery(expenseDao)
        val expenseFactory = ExpenseViewModelFactory(expenseRepository)
        expenseViewModel = ViewModelProvider(this, expenseFactory)[ExpenseViewModel::class.java]

        setContent {
            MainScreen(expenseViewModel)
        }
    }

    @Composable
    fun MainScreen(expenseViewModel: ExpenseViewModel) {
        var currentTab by remember { mutableIntStateOf(0) }

        val tabs = listOf(
            TabItem("Home", R.drawable.home_svg),
            TabItem("Graph", R.drawable.expense_svg),
            TabItem("Wallet", R.drawable.wallet_svg),
            TabItem("Profile", R.drawable.person_svg)
        )

        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = Secondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    tabs.forEachIndexed { index, tab ->
                        NavigationBarItem(
                            icon = {
                                if (currentTab == index) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(ThemePrimary, shape = CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(id = tab.icon),
                                            contentDescription = tab.label,
                                            tint = Secondary
                                        )
                                    }
                                } else {
                                    Icon(
                                        painter = painterResource(id = tab.icon),
                                        contentDescription = tab.label,
                                        tint = Black
                                    )
                                }
                            },
                            selected = currentTab == index,
                            onClick = { currentTab = index },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent
                            ),
                            label = null
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                //Navigate b/w features.
                when (currentTab) {
                    0 -> ExpenseView().ShowExpenseView(this@MainActivity, expenseViewModel)
                    1 -> ExpenseReportView().ShowReportScreen(this@MainActivity, expenseViewModel)
                    2 -> ExpenseWallet().ShowWallet(this@MainActivity,expenseViewModel)
                    3 -> ProfileSettings().ShowProfile(this@MainActivity)
                }
            }
        }
    }

    data class TabItem(val label: String, val icon: Int)
}



