package com.mine.expensetracker.featureWallet.ui

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expense.expensetracker.utils.CustomToast
import com.mine.expensetracker.R
import com.mine.expensetracker.data.prefrences.SharedPref
import com.mine.expensetracker.featureWallet.data.ExpenseData
import com.mine.expensetracker.featureWallet.data.FriendData
import com.mine.expensetracker.featureWallet.viewmodel.ExpenseViewModel
import com.mine.expensetracker.featureWallet.viewmodel.FriendsViewModel
import com.mine.expensetracker.ui.theme.Green
import com.mine.expensetracker.ui.theme.Grey
import com.mine.expensetracker.ui.theme.Secondary
import com.mine.expensetracker.ui.theme.TextBlack
import com.mine.expensetracker.ui.theme.ThemePrimary
import com.mine.expensetracker.ui.theme.ThinGrey
import com.mine.expensetracker.ui.theme.Warning
import com.mine.expensetracker.utils.AppUtils
import com.mine.expensetracker.utils.Constants
import kotlin.math.absoluteValue

//This is to manage the split money b/w friends.
class ExpenseWallet {

    @Composable
    fun ShowWallet(
        activity: Activity,
        friendsViewModel: FriendsViewModel
    ) {
        AppUtils.styleSystemBars(activity, backgroundColor = Secondary.toArgb(), true, false)

        var showExpenseScreen by remember { mutableStateOf(false) }
        var selectedFriend by remember { mutableStateOf<FriendData?>(null) }

        Box(modifier = Modifier.fillMaxSize()) {
            if (!showExpenseScreen) {
                Topbar(
                    context = LocalContext.current,
                    friendsViewModel = friendsViewModel,
                    onFriendClick = { friend ->
                        selectedFriend = friend
                        showExpenseScreen = true
                    }
                )
            } else {
                ExpenseListWallet().ExpenseListScreen(
                    selectedFriend?.name.toString(),
                    selectedFriend?.uid.toString(),
                    onExpenseClick = { expense ->
                        println("Clicked expense: ${expense.name}")
                    },
                    onBack = { showExpenseScreen = false }
                )
            }
        }
    }

    @Composable
    fun Topbar(
        context: Context,
        friendsViewModel: FriendsViewModel,
        onFriendClick: (FriendData) -> Unit
    ) {
        val userType = SharedPref.getInt(context, Constants.LOGIN_TYPE)
        var showDialog by remember { mutableStateOf(false) }
        var showDeleteFriendDialog by remember { mutableStateOf(false) }
        var friendCode by remember { mutableStateOf("") }
        var isError by remember { mutableStateOf(false) }

        val friendsList by friendsViewModel.friends.collectAsState(initial = emptyList())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Secondary)
                .padding(top = 20.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 10.dp, bottom = 15.dp),
                text = context.getString(R.string.featureWallet),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextBlack,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (userType == 3) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = context.getString(R.string.guestUserMessageLogin),
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {

                if (friendsList.isEmpty()) {
                    Text(
                        text = "No friend added",
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        color = TextBlack,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Friends:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Grey
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(friendsList) { friend ->
                            Log.d("balancecheck", "$friend.balance")
                            val (status, color) = when {
                                friend.balance > 0 -> "Gets ₹${friend.balance}" to Green
                                friend.balance < 0 -> "You owe ₹${-friend.balance}" to Warning
                                else -> "Settled up" to Gray
                            }

                            FriendItem(
                                friend = friend,
                                status = status,
                                statusColor = color,
                                onClick = { onFriendClick(friend) },
                                onLongClick = {
                                    friendCode = friend.uid
                                    showDeleteFriendDialog = true
                                }
                            )
                        }
                    }
                }

                if (showDeleteFriendDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteFriendDialog = false },
                        title = { Text("Remove Friend") },
                        text = { Text("Are you sure you want to remove friend from the list") },
                        confirmButton = {
                            TextButton(onClick = {
                                friendsViewModel.removeFriend(
                                    friendCode,
                                    onComplete = { success, message ->
                                        {
                                            if (success) {
                                                CustomToast.showToast(
                                                    context,
                                                    message,
                                                    false
                                                )
                                            } else {
                                                CustomToast.showToast(
                                                    context,
                                                    context.getString(R.string.unableToProcess),
                                                    false
                                                )
                                            }
                                        }
                                    })
                                showDeleteFriendDialog = false
                            }) {
                                Text("Remove", color = Warning)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDeleteFriendDialog = false }) {
                                Text("Cancel", color = TextBlack)
                            }
                        }, containerColor = ThinGrey
                    )
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    FloatingActionButton(
                        onClick = { showDialog = true },
                        containerColor = ThemePrimary,
                        contentColor = Secondary,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            Icons.Default.AddCircle,
                            contentDescription = "Add Friend",
                            tint = Secondary
                        )
                    }
                }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        shape = RoundedCornerShape(16.dp),
                        title = {
                            Text(
                                "Add Friend",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        },
                        text = {
                            OutlinedTextField(
                                value = friendCode,
                                onValueChange = {
                                    friendCode = it
                                    isError = false
                                },
                                label = { Text("Enter Friend's Code") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                isError = isError,
                                colors = ExpenseListWallet().textColors()
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    if (friendCode.isBlank()) {
                                        isError = true
                                    } else {
                                        friendsViewModel.addFriend(
                                            context,
                                            friendCode,
                                            onComplete = { success, message ->
                                                if (!success) {
                                                    CustomToast.showToast(
                                                        context,
                                                        message
                                                            ?: context.getString(R.string.somethingWentWrong),
                                                        false
                                                    )
                                                } else {
                                                    friendsViewModel.fetchFriends()
                                                }
                                            })
                                        showDialog = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ThemePrimary,
                                    contentColor = Secondary
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Add")
                            }
                        },
                        dismissButton = {
                            OutlinedButton(
                                onClick = {
                                    showDialog = false
                                    isError = false
                                },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Cancel", color = TextBlack)
                            }
                        }, containerColor = ThinGrey
                    )
                }
            }
        }
    }

    @Composable
    fun FriendItem(
        friend: FriendData,
        status: String,
        statusColor: Color,
        modifier: Modifier = Modifier,
        onClick: (FriendData) -> Unit,
        onLongClick: (FriendData) -> Unit
    ) {
        val avatarColor = remember(friend.name) {
            avatarColors[friend.name.hashCode().absoluteValue % avatarColors.size]
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onClick(friend) },
                        onLongPress = { onLongClick(friend) }
                    )
                }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(avatarColor, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = friend.name.first().uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = friend.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = status,
                    fontSize = 14.sp,
                    color = statusColor
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Go to details",
                tint = Color.Black
            )
        }
        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
    }

    val avatarColors = listOf(
        Color(0xFFF5E0C3), // beige
        Color(0xFFBBDEFB), // light blue
        Color(0xFFC8E6C9), // light green
        Color(0xFFFFCDD2), // light red
        Color(0xFFD1C4E9), // lavender
        Color(0xFFFFF9C4)  // light yellow
    )

}