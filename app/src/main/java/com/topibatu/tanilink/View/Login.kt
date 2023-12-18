package com.topibatu.tanilink.View

import account_proto.AccountProto.LoginRes
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
import com.orhanobut.hawk.Hawk
import com.topibatu.tanilink.Util.Account
import io.grpc.StatusException
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(navController: NavController) {
    // TODO: change button color, get textfield state value, navigation from register -> login viceversa
    val accountRPC = remember { Account() }
    val scope = rememberCoroutineScope()

    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    val context = LocalContext.current
    val loginRes = remember { mutableStateOf<LoginRes?>(null) }

    // Check if user already logged in
    if(Hawk.get<String>("access-token") != null) {
        navController.navigate("home")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        // Title
        Text(text = "Login", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Text Field
        OutlinedTextField(
            value = emailState.value,
            placeholder = { Text("Email") },
            onValueChange = {
                emailState.value = it
            })
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = passwordState.value,
            placeholder = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = {
                passwordState.value = it
            })
        Spacer(modifier = Modifier.height(12.dp))

        // Forget Password
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 40.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            Text(
                text = "Forget Password?",
                modifier = Modifier.clickable {
                    navController.navigate("forgot_password")
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Button
        Button(onClick = {
            scope.launch {

                loginRes.value = try {
                    accountRPC.login(
                        email = emailState.value,
                        password = passwordState.value
                    )
                } catch (e: StatusException) {
                    Toast.makeText(
                        context,
                        "Login failed: ${e.status.description}",
                        Toast.LENGTH_SHORT
                    ).show()
                    null
                }
            }
        }) {
            Text(text = "Login")
        }

        // Don't have an Acocunt?
        Spacer(modifier = Modifier.height(32.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable {
                    navController.navigate("sign_up")
                }
        ) {
            Text(
                text = "Don't Have an Account?",
                color = Color.Gray,
            )
            Text(
                text = "Sign Up",
                color = Color.Gray,
                textDecoration = TextDecoration.Underline
            )
        }

        // If Login Success
        loginRes.value?.let { response ->
            Toast.makeText(
                context,
                "Login Success",
                Toast.LENGTH_SHORT
            ).show()

            // Save Session
            Hawk.put("user-id", response.account.id);
            Hawk.put("access-token", response.tokens.accessToken);

            LaunchedEffect(response) {
                delay(500) // Delay for 0.5 seconds
                navController.navigate("home")
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
