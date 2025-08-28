package com.mine.expensetracker.featureLogin.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expense.expensetracker.utils.CustomToast

import androidx.compose.ui.text.withStyle
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModelProvider
import com.mine.expensetracker.data.prefrences.SharedPref
import com.mine.expensetracker.R
import com.mine.expensetracker.featureHome.ui.MainActivity
import com.mine.expensetracker.featureLogin.repositery.LoginRepository
import com.mine.expensetracker.featureLogin.viewModelFactory.LoginViewModelFactory
import com.mine.expensetracker.featureLogin.viewmodel.LoginViewModel
import com.mine.expensetracker.featureUserDetails.ui.GetUserNameActivity
import com.mine.expensetracker.ui.theme.Black
import com.mine.expensetracker.ui.theme.Grey
import com.mine.expensetracker.ui.theme.Pink40
import com.mine.expensetracker.ui.theme.Secondary
import com.mine.expensetracker.ui.theme.TextBlack
import com.mine.expensetracker.ui.theme.ThemePrimary
import com.mine.expensetracker.ui.theme.ThinGrey
import com.mine.expensetracker.utils.AppUtils
import com.mine.expensetracker.utils.Constants
import kotlin.jvm.java

//Login activity
class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.handleGoogleSignInResult(result.data)
        if (viewModel.loginSuccess) {
            proceed(2) // loginType = 2 for Google login
        } else {
            viewModel.errorMessage?.let {
                CustomToast.showToast(this, it, false)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.styleSystemBars(
            this@LoginActivity,
            backgroundColor = Secondary.toArgb(),
            false, false
        )

        //ViewModelFactory
        val factory = LoginViewModelFactory(
            LoginRepository(this)
        )

        //ViewModel
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            Main(viewModel, onProceed = { loginType ->
                proceed(loginType)
            })
        }
    }

    //Move to next screen
    fun proceed(loginType: Int) {
        SharedPref.setInt(this, Constants.LOGIN_TYPE, loginType)
        if(loginType==1){
            val intent = Intent(this, GetUserNameActivity::class.java)
            this.startActivity(intent)
        }else{
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }
    }

    //Main screen
    @Composable
    fun Main(
        viewModel: LoginViewModel,
        onProceed: (Int) -> Unit
    ) {
        var showLoginScreen by remember { mutableStateOf(false) }
        val context = LocalContext.current

        if (showLoginScreen) {
            //Callbacks
            LoginClick(
                onLoginClick = { email, pass ->
                    viewModel.loginWithEmail(email, pass)
                    if (viewModel.loginSuccess) {
                        onProceed(1)
                    } else {
                        viewModel.errorMessage?.let {
                            CustomToast.showToast(context, it, false)
                        }
                    }
                },
                onRegisterClick = {
                    CustomToast.showToast(
                        context,
                        context.getString(R.string.featureUnavailable),
                        false
                    )
                },
                onBackClick = {
                    showLoginScreen = false
                },
                onForgotPassword = {
                    CustomToast.showToast(
                        context,
                        context.getString(R.string.passwordResetInfo),
                        false
                    )
                },
                onGoogleLoginClick = {
                    googleSignInLauncher.launch(viewModel.getGoogleSignInIntent())
                })
        } else {
            SelectLoginType(
                onLoginClick = {
                    showLoginScreen = true
                },
                onRegisterClick = {
                    CustomToast.showToast(
                        context,
                        context.getString(R.string.featureUnavailable),
                        false
                    )
                },
                onGuestClick = {
                    SharedPref.setString(context, Constants.UID, "")
                    SharedPref.setString(context, Constants.USERNAME, "Guest")
                    onProceed(3)
                }
            )
        }
    }

    //Login type selection
    @Composable
    fun SelectLoginType(
        onLoginClick: () -> Unit,
        onRegisterClick: () -> Unit,
        onGuestClick: () -> Unit
    ) {
        val context = LocalContext.current
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Secondary),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .background(Secondary),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.login_background),
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Login Background"
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = onLoginClick,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(height = 50.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        ),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Secondary,
                            containerColor = Black, disabledContainerColor = Gray
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Login",
                            color = Secondary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                    }

                    OutlinedButton(
                        onClick = onRegisterClick,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(top = 20.dp)
                            .height(height = 50.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 3.dp,
                            pressedElevation = 8.dp
                        ),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Black,
                            containerColor = Secondary, disabledContainerColor = Gray
                        ),
                        border = BorderStroke(1.dp, Black),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Register",
                            color = Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    TextButton(onClick = onGuestClick, modifier = Modifier.padding(top = 60.dp)) {
                        Text(
                            text = context.getString(R.string.loginAsGuest),
                            color = Pink40,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
    }

    //Login button click
    @Composable
    fun LoginClick(
        onLoginClick: (String, String) -> Unit,
        onBackClick: () -> Unit,
        onRegisterClick: () -> Unit, onForgotPassword: () -> Unit, onGoogleLoginClick: () -> Unit
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordVisibility by remember { mutableStateOf(false) }
        val context = LocalContext.current

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Secondary)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 25.dp, top = 47.dp, end = 25.dp)
                    .align(Alignment.TopStart)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Secondary)
                        .border(1.dp, Black, RoundedCornerShape(10.dp))
                        .clickable(onClick = onBackClick),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow_back_svg),
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = context.getString(R.string.loginWelcome),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Left,
                    color = TextBlack
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text(context.getString(R.string.loginUserEmail)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true, colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Black,
                        unfocusedBorderColor = Gray,
                        cursorColor = Black,
                        focusedLabelColor = Black,
                        unfocusedLabelColor = Gray,
                        unfocusedContainerColor = ThinGrey,
                        focusedContainerColor = ThinGrey
                    )
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text(context.getString(R.string.loginPassword)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Black,
                        unfocusedBorderColor = Gray,
                        cursorColor = Black,
                        focusedLabelColor = Black,
                        unfocusedLabelColor = Gray,
                        unfocusedContainerColor = ThinGrey,
                        focusedContainerColor = ThinGrey
                    ),
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisibility)
                            painterResource(id = R.drawable.eye_open_svg)
                        else
                            painterResource(id = R.drawable.eye_close_svg)

                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            Icon(
                                painter = image,
                                contentDescription = if (passwordVisibility) "Hide password" else "Show password",
                                tint = Gray
                            )
                        }
                    },
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onForgotPassword,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(top = 3.dp),
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        Text(
                            context.getString(R.string.forgotPassword),
                            color = Grey,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.End
                        )
                    }
                }


                Button(
                    onClick = {
                        onLoginClick(email, password)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .height(height = 50.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Secondary,
                        containerColor = Black, disabledContainerColor = Gray
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = context.getString(R.string.login),
                        color = Secondary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        context.getString(R.string.loginWith),
                        modifier = Modifier
                            .padding(top = 40.dp, bottom = 35.dp)
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Grey,
                        textAlign = TextAlign.Center
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(50.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Secondary)
                            .border(0.5.dp, Grey, RoundedCornerShape(10.dp))
                            .clickable(onClick = onGoogleLoginClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google_svg),
                            contentDescription = "Google Login",
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            bottom = 30.dp
                        ),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Gray)) {
                                append("Don't have an account? ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = ThemePrimary,
                                    fontWeight = FontWeight.Medium
                                )
                            ) {
                                append("Register Now")
                            }
                        },
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .clickable { onRegisterClick() }
                    )

                }
            }
        }
    }
}