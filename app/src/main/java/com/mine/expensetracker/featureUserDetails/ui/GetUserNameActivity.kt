package com.mine.expensetracker.featureUserDetails.ui

import android.os.Bundle
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mine.expensetracker.R
import com.mine.expensetracker.ui.theme.Black
import com.mine.expensetracker.ui.theme.Pink40
import com.mine.expensetracker.ui.theme.Secondary
import com.mine.expensetracker.ui.theme.ThinGrey
import com.mine.expensetracker.ui.theme.ThinThemePrimary
import com.mine.expensetracker.utils.AppUtils

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
            }
        }
    }
}

@Composable
fun GetUserDetails(onSubmit: (String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
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

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Your email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
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