package com.mine.expensetracker.FeatureExtSettings.ui

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.BuildConfig
import com.mine.expensetracker.FeatureExtSettings.viewmodel.SettingsViewModel
import com.mine.expensetracker.R
import com.mine.expensetracker.data.prefrences.SharedPref
import com.mine.expensetracker.featureExpense.ui.ExpenseView
import com.mine.expensetracker.FeatureExtSettings.viewmodel.UserProfileViewModel
import com.mine.expensetracker.ui.theme.Grey
import com.mine.expensetracker.ui.theme.Secondary
import com.mine.expensetracker.ui.theme.TextBlack
import com.mine.expensetracker.ui.theme.ThemeBG
import com.mine.expensetracker.ui.theme.ThemePrimary
import com.mine.expensetracker.ui.theme.ThinGrey
import com.mine.expensetracker.ui.theme.Warning
import com.mine.expensetracker.utils.AppUtils
import com.mine.expensetracker.utils.Constants

class CustomExtSettings() {

    //EditProfile
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EditProfile(
        context: Context,
        viewModel: UserProfileViewModel = viewModel(),
        onBack: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            CustomProfileTitleCommon("Edit Profile", onBack = {
                onBack()
            })

            val genderOptions = listOf("Male", "Female", "Other")
            var expanded by remember { mutableStateOf(false) }
            var type by remember { mutableStateOf("Guest") }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.Start
            ) {

                if (SharedPref.getInt(context, Constants.LOGIN_TYPE) == 3) {
                    Text(
                        "UserType : Guest",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Warning
                    )
                }

                // Username
                Text("Username", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = viewModel.username,
                    onValueChange = { viewModel.username = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp), supportingText = {
                        viewModel.usernameError?.let {
                            Text(
                                it,
                                color = Color.Red,
                                fontSize = 12.sp
                            )
                        }
                    },
                    singleLine = true, colors = textColors()
                )

                // Date of Birth
                Text("Email", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp), supportingText = {
                        viewModel.emailError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }
                    },
                    singleLine = true, colors = textColors()
                )

                // Gender
                Text("Gender", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = viewModel.gender,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }, colors = textColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        genderOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    viewModel.gender = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                viewModel.genderError?.let {
                    Text(
                        it,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                // Save Button
                Button(
                    onClick = { viewModel.onSave(context) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ThemePrimary)
                ) {
                    Text("Save", fontSize = 16.sp, color = Secondary)
                }
            }
        }
    }

    //About
    @Composable
    fun About(context: Context, onBack: () -> Unit) {
        val versionName = BuildConfig.VERSION_NAME
        val appName = stringResource(id = R.string.app_name)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            CustomProfileTitleCommon("About", onBack = {
                onBack()
            })

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // App Logo
                Image(
                    painter = painterResource(id = R.drawable.app_icon), // replace with your logo
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(100.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // App Name
                Text(
                    text = "App Name: $appName",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // App Version
                Text(
                    text = "App Version: $versionName",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Copyrights
                Text(
                    text = "Copyrights @2025",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }

    //Settings
    @Composable
    fun SettingsScreen(
        context: Context,
        onBack: () -> Unit,
        viewModel: SettingsViewModel = viewModel()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            CustomProfileTitleCommon("About", onBack = {
                onBack()
            })

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp)
            ) {
                // Preferences Section
                item {
                    Text(
                        text = "Preferences",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold, fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(
                            containerColor = Secondary
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            SettingToggleRow(
                                title = "Enable Notifications",
                                checked = viewModel.notificationsEnabled,
                                onCheckedChange = { viewModel.setNotifications(it) }
                            )

                            Divider()

                            SettingToggleRow(
                                title = "Enable Reminders",
                                checked = viewModel.remindersEnabled,
                                onCheckedChange = { viewModel.setReminders(it) },
                            )

                            Divider()

                            SettingToggleRow(
                                title = "Dark Mode",
                                checked = viewModel.darkModeEnabled,
                                onCheckedChange = { viewModel.setDarkMode(it) }
                            )
                        }
                    }
                }

                // Account Section
                item {
                    Text(
                        text = "Data & Preference",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(
                            containerColor = Secondary
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            SettingToggleRow(
                                title = "Offline Storage Only",
                                checked = viewModel.storagePreference,
                                onCheckedChange = { viewModel.setStoragePreference(it) }
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SettingToggleRow(
        title: String,
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Switch(
                checked = checked, onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Secondary,
                    uncheckedThumbColor = ThinGrey,
                    checkedTrackColor = ThemePrimary,
                    uncheckedTrackColor = Grey
                )
            )
        }
    }

    //Notification
    @Composable
    fun Notifications(context: Context, onBack: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight().background(ThinGrey)
        ) {
            CustomProfileTitleCommon("Notifications", onBack = {
                onBack()
            })

        }
    }

    @Composable
    fun CustomProfileTitleCommon(title: String, onBack: () -> Unit) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ThinGrey)
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
    fun textColors(): TextFieldColors {
        return TextFieldDefaults.colors(
            focusedTextColor = TextBlack,
            unfocusedTextColor = TextBlack,
            disabledTextColor = ThinGrey,
            focusedContainerColor = Secondary,
            unfocusedContainerColor = Secondary,
            disabledContainerColor = Secondary,
            focusedIndicatorColor = ThemePrimary,
            unfocusedIndicatorColor = Grey,
            disabledIndicatorColor = Grey,
            cursorColor = Secondary
        )
    }

}