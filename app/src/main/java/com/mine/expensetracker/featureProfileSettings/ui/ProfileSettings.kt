package com.mine.expensetracker.featureProfileSettings.ui

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.Gson
import com.mine.expensetracker.R
import com.mine.expensetracker.data.prefrences.SharedPref
import com.mine.expensetracker.featureProfileSettings.data.ProfileDataX
import com.mine.expensetracker.featureProfileSettings.data.ProfileSetting
import com.mine.expensetracker.featureProfileSettings.viewmodel.ProfileViewModel
import com.mine.expensetracker.ui.theme.Blue
import com.mine.expensetracker.ui.theme.Secondary
import com.mine.expensetracker.ui.theme.TextBlack
import com.mine.expensetracker.ui.theme.ThemePrimary
import com.mine.expensetracker.ui.theme.ThinGrey
import com.mine.expensetracker.ui.theme.ThinThemePrimary
import com.mine.expensetracker.utils.AppUtils
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.expense.expensetracker.utils.CustomToast
import com.mine.expensetracker.FeatureExtSettings.ui.CustomExtSettings
import com.mine.expensetracker.ui.theme.Grey
import com.mine.expensetracker.ui.theme.ThemeBG
import com.mine.expensetracker.utils.Constants
import com.mine.expensetracker.utils.CustomDialogs

//Profile and settings screen
class ProfileSettings() {

    @Composable
    fun ShowProfile(
        activity: Activity,
        viewModel: ProfileViewModel = viewModel()
    ) {
        AppUtils.styleSystemBars(activity, backgroundColor = ThinGrey.toArgb(), true, false)

        val context = LocalContext.current
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                viewModel.saveProfileImage(context, it)
                CustomToast.showToast(context, context.getString(R.string.profilePicStatus), false)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(ThinGrey)
        ) {
            val context = LocalContext.current
            HeaderProfile(context, viewModel, modifier = Modifier.weight(0.35f), onClick = {
                launcher.launch("image/*")
            })
            FooterProfile(activity, context, modifier = Modifier.weight(0.5f))
        }
    }

    @Composable
    fun HeaderProfile(
        context: Context,
        viewModel: ProfileViewModel,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        val loginType = SharedPref.getInt(context, Constants.LOGIN_TYPE)
        val profileImageUri by viewModel.profileImageUri.collectAsState()
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .background(ThinGrey),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                // Profile image
                if (profileImageUri != null) {
                    Log.d("ProfileViewModel", "HeaderProfile: $profileImageUri")
                    Image(
                        painter = rememberAsyncImagePainter(profileImageUri),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(ThemePrimary),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.dummy_display_pic),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                IconButton(
                    onClick = {
                        onClick()
                    },
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(ThinThemePrimary)
                        .align(Alignment.BottomEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // User Name below the profile pic
            Text(
                text = SharedPref.getString(context, Constants.USERNAME), fontSize = 18.sp,
                fontStyle = FontStyle.Normal, fontWeight = FontWeight.Bold,
                color = TextBlack
            )
            if (loginType != 3) {
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .background(
                            color = ThinThemePrimary,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = SharedPref.getString(context, Constants.EMAIL),
                        fontSize = 15.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal,
                        color = Grey
                    )
                }

            }
        }
    }

    @Composable
    fun FooterProfile(
        activity: Activity,
        context: Context,
        modifier: Modifier = Modifier
    ) {
        val getData = Constants.PROFILE_SCREEN
        val gson = Gson()
        val profileDatax = gson.fromJson(getData, ProfileDataX::class.java)
        var showLogoutDialog by remember { mutableStateOf(false) }
        var selectedScreen by remember { mutableStateOf("main_pop") }

        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .background(ThinGrey),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .wrapContentHeight()
                    .background(
                        color = Secondary,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top
                ) {
                    val filteredList = profileDatax.profileSettings
                        .filter { it.isActive }
                        .sortedBy { it.order }
                    items(
                        filteredList,
                        key = { it.id }
                    ) { lists ->
                        ShowListItems(lists, onClick = {
                            when (lists.id) {
                                1 -> {
                                    selectedScreen = "edit_profile"
                                }

                                2 -> {
                                    selectedScreen = "Settings"
                                }

                                3 -> {
                                    selectedScreen = "Notifications"
                                }

                                4 -> {
                                    selectedScreen = "Share"
                                }

                                5 -> {
                                    showLogoutDialog = true
                                }

                                6 -> {
                                    selectedScreen = "About"
                                }

                                else -> {
                                    CustomToast.showToast(
                                        context,
                                        context.getString(R.string.invalidRequest),
                                        false
                                    )
                                    Log.d("Profile", "Clicked item with id: ${lists.id}")
                                }
                            }
                        })
                        if (lists != filteredList.last()) {
                            Divider(color = Color.LightGray, thickness = 0.5.dp)
                        }
                    }
                }
            }
        }

        when (selectedScreen) {
            "edit_profile" -> CustomExtSettings().EditProfile(context, onBack = {
                selectedScreen = "main_pop"
            })

            "About" -> CustomExtSettings().About(context, onBack = {
                selectedScreen = "main_pop"
            })

            "Settings" -> CustomExtSettings().SettingsScreen(context, onBack = {
                selectedScreen = "main_pop"
            })

            "Notifications" -> CustomExtSettings().Notifications(context, onBack = {
                selectedScreen = "main_pop"
            })

            "Share" -> {
                CustomToast.showToast(
                    context,
                    context.getString(R.string.featureUnavailable),
                    false
                )
            }
        }

        if (showLogoutDialog) {
            CustomDialogs().CustomLogoutDialog(context, negative = {
                showLogoutDialog = false
            }, positive = {
                showLogoutDialog = true
                AppUtils().logout(context)
            })
        }
    }

    @Composable
    fun ShowListItems(data: ProfileSetting, onClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                }
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val iconRes = when (data.displayName) {
                    "Edit Profile" -> R.drawable.profile_edit
                    "Settings" -> R.drawable.setting
                    "Notifications" -> R.drawable.notification
                    "Share" -> R.drawable.share
                    "About" -> R.drawable.information
                    "Logout" -> R.drawable.logout
                    else -> R.drawable.information
                }

                if (data.category != "danger") {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = data.displayName,
                        tint = ThemePrimary,
                        modifier = Modifier
                            .size(32.dp)
                            .padding(end = 12.dp)
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.logout),
                        contentDescription = data.displayName,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(32.dp)
                            .padding(end = 12.dp)
                    )
                }

                // Label
                Text(
                    text = data.displayName,
                    fontSize = 16.sp,
                    fontWeight = if (data.category == "danger") FontWeight.Bold else FontWeight.Normal,
                    color = if (data.category == "danger") Color.Red else Color.Black
                )
            }

            if (data.isOpenable) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Open",
                    tint = ThemePrimary
                )
            }
        }
    }
}