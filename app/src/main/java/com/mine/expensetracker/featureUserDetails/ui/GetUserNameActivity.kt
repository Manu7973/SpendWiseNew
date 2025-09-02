package com.mine.expensetracker.featureUserDetails.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.delay
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.expense.expensetracker.utils.CustomToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.mine.expensetracker.R
import com.mine.expensetracker.data.prefrences.SharedPref
import com.mine.expensetracker.featureHome.ui.MainActivity
import com.mine.expensetracker.ui.theme.Black
import com.mine.expensetracker.ui.theme.Pink40
import com.mine.expensetracker.ui.theme.Secondary
import com.mine.expensetracker.ui.theme.ThinGrey
import com.mine.expensetracker.ui.theme.ThinThemePrimary
import com.mine.expensetracker.utils.AppUtils
import com.mine.expensetracker.utils.Constants

class GetUserNameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppUtils.styleSystemBars(
            this@GetUserNameActivity,
            backgroundColor = ContextCompat.getColor(this, R.color.white),
            false, false
        )

        enableEdgeToEdge()
        setContent {
            GetUserDetails { name ->
                SharedPref.setString(this@GetUserNameActivity, Constants.USERNAME, name)
                val uid = SharedPref.getString(this@GetUserNameActivity, Constants.UID)
                if (uid != null) {
                    val db = FirebaseDatabase.getInstance().getReference("users")
                    val userMap = mapOf(
                        "name" to name
                    )

                    db.child(uid).updateChildren(userMap)
                        .addOnSuccessListener {
                            Log.d("UserNameActivity", "Name saved successfully")
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener {
                            Log.e("UserNameActivity", "Failed to save name", it)
                            CustomToast.showToast(this, "Failed to save name!", false)
                        }
                } else {
                    CustomToast.showToast(this, "User not logged in!", false)
                }
            }
        }
    }
}

@Composable
fun GetUserDetails(onSubmit: (String) -> Unit) {
    var name by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        ThinThemePrimary,
                        Secondary
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, start = 25.dp, end = 25.dp)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.register)
            )

            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(200.dp)
            )

            TypewriterText(context.getString(R.string.registerUser))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Your name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
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

//            OutlinedTextField(
//                value = email,
//                onValueChange = { email = it },
//                placeholder = { Text("Your email") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 20.dp),
//                shape = RoundedCornerShape(10.dp),
//                singleLine = true, colors = OutlinedTextFieldDefaults.colors(
//                    focusedBorderColor = Black,
//                    unfocusedBorderColor = Gray,
//                    cursorColor = Black,
//                    focusedLabelColor = Black,
//                    unfocusedLabelColor = Gray,
//                    unfocusedContainerColor = ThinGrey,
//                    focusedContainerColor = ThinGrey
//                )
//            )

            Button(
                onClick = {
                    onSubmit(name)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
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
                    text = "Continue",
                    color = Secondary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

            }
        }
    }
}

@Composable
fun TypewriterText(fullText: String) {
    var text by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        fullText.forEachIndexed { index, _ ->
            text = fullText.substring(0, index + 1)
            delay(60)
        }
    }

    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(top = 15.dp),
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        color = Pink40
    )
}