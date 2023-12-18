package com.topibatu.tanilink.View

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.protobuf.Empty
import com.orhanobut.hawk.Hawk
import com.topibatu.tanilink.Util.Account
import io.grpc.StatusException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPassword(navController: NavController) {
    // TODO: change button color, get textfield state value, navigation from register -> login viceversa
    val accountRPC = remember { Account() }
    val scope = rememberCoroutineScope()

    val emailState = remember { mutableStateOf(TextFieldValue()) }

    val context = LocalContext.current
    val forgotPasswordRes = remember { mutableStateOf<Empty?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        // Title
        Text(text = "Forgot Password", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(2.dp))

        // Description
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Please enter your email address to", fontSize = 15.sp)
            Text(text = "receive verification code", fontSize = 15.sp)
        }
        Spacer(modifier = Modifier.height(42.dp))

        // Text Field
        OutlinedTextField(
            value = emailState.value,
            placeholder = { Text("Email") },
            onValueChange = {
                emailState.value = it
            })
        Spacer(modifier = Modifier.height(16.dp))

        // Button
        Button(onClick = {
            scope.launch {

                forgotPasswordRes.value = try {
                    accountRPC.forgotPassword(
                        email = emailState.value.text
                    )
                } catch (e: StatusException) {
                    Toast.makeText(
                        context,
                        "Failed: ${e.status.description}",
                        Toast.LENGTH_SHORT
                    ).show()
                    null
                }
            }

        }) {
            Text(text = "Send")
        }

        // If Forget Password Request is Success
        forgotPasswordRes.value?.let { response ->
            Toast.makeText(
                context,
                "Forget Password Email Has Been Sent",
                Toast.LENGTH_SHORT
            ).show()

            LaunchedEffect(response) {
                delay(2500) // Delay for 2 seconds
                navController.navigate("login")
            }
        }

    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val boxWidth = with(LocalDensity.current) { constraints.maxWidth.toDp() }
        val boxHeight = with(LocalDensity.current) { constraints.maxHeight.toDp() }

        // Top Right Image
        Image(
            painter = rememberAsyncImagePainter("https://firebasestorage.googleapis.com/v0/b/topibatu-2a076.appspot.com/o/assets%2Fdaun_forget_password.png?alt=media&token=aee0277e-e8ad-4104-8ab1-04f0705c93e1"),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .offset(boxWidth - 200.dp, 0.dp)
                .clip(MaterialTheme.shapes.small)
                .wrapContentSize(align = Alignment.TopEnd)
        )

        // Bottom Left Image
        Image(
            painter = rememberAsyncImagePainter("https://firebasestorage.googleapis.com/v0/b/topibatu-2a076.appspot.com/o/assets%2Fdaun_forget_password.png?alt=media&token=aee0277e-e8ad-4104-8ab1-04f0705c93e1"),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .offset(0.dp, boxHeight - 200.dp)
                .clip(MaterialTheme.shapes.small)
                .wrapContentSize(align = Alignment.BottomStart)
                .rotate(180.0F)
        )
    }
}