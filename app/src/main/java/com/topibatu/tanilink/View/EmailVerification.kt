package com.topibatu.tanilink.View

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailVerificationPage(navController: NavController) {
    // TODO: change button color, get textfield state value, navigation from register -> login viceversa
    val accountRPC = remember { Account() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val isEmailVerifiedRes = remember { mutableStateOf<Empty?>(null) }
    val resendVerificationEmailRes = remember { mutableStateOf<Empty?>(null) }

    val email = Hawk.get<String>("email")

    // Resent Cooldown Timer
    var times by remember { mutableStateOf(60) }
    var timeLeft by remember { mutableStateOf(0) }

    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            // Title
            Text(text = "Email Verify Status", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            // Texts
            Text(
                text = "We sent an email to \n" +
                        "${email}", fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Please check your email! \n" +
                        "If you don’t see it, you may need to \n" +
                        "check your spam folder.",
                fontSize = 12.sp,
                lineHeight = 14.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(42.dp))

            Text(
                text = "Still can’t find the email? No problem.",
                fontSize = 12.sp,
                lineHeight = 14.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Button
            Button(onClick = {
                scope.launch {

                    isEmailVerifiedRes.value = try {
                        accountRPC.isEmailConfirmed(
                            email = email
                        )
                    } catch (e: StatusException) {
                        Toast.makeText(
                            context,
                            "Email is not verified: ${e.status.description}",
                            Toast.LENGTH_SHORT
                        ).show()
                        null
                    }
                }
            }) {
                Text(text = "Check")
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Check if there is cooldown
            if (timeLeft == 0) { // No cooldown
                Text(
                    text = "Resent Verification Email",
                    modifier = Modifier.clickable {
                        scope.launch {

                            resendVerificationEmailRes.value = try {
                                accountRPC.resendVerificationEmail(
                                    email = email
                                )
                            } catch (e: StatusException) {
                                Toast.makeText(
                                    context,
                                    "Email is not verified: ${e.status.description}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                null
                            }

                            // Add Cooldown
                            timeLeft = times
                            while (timeLeft > 0) {
                                delay(1000L)
                                timeLeft--
                            }

                        }
                    }
                )
            } else { // Cooldown
                Text(text = "Resent Verification Email in $timeLeft seconds")
            }


            // If email is verified
            isEmailVerifiedRes.value?.let { response ->
                Toast.makeText(
                    context,
                    "Your Email Is Verified",
                    Toast.LENGTH_SHORT
                ).show()

                LaunchedEffect(response) {
                    delay(2500) // Delay for 2 seconds
                    navController.navigate("login")
                }
            }

            // If resend email verification success
            resendVerificationEmailRes.value?.let { response ->
                Toast.makeText(
                    context,
                    "New Verification Email Has Been Sent",
                    Toast.LENGTH_SHORT
                ).show()
                resendVerificationEmailRes.value = null
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
            painter = rememberAsyncImagePainter("https://firebasestorage.googleapis.com/v0/b/topibatu-2a076.appspot.com/o/assets%2Fdaun_login.png?alt=media&token=c66edc3e-3028-4b81-8e47-e654c9f79a98"),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .offset(boxWidth - 200.dp, 0.dp)
                .clip(MaterialTheme.shapes.small)
                .wrapContentSize(align = Alignment.TopEnd)
        )

        // Bottom Left Image
        Image(
            painter = rememberAsyncImagePainter("https://firebasestorage.googleapis.com/v0/b/topibatu-2a076.appspot.com/o/assets%2Fdaun_login.png?alt=media&token=c66edc3e-3028-4b81-8e47-e654c9f79a98"),
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