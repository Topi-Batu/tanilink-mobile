package com.topibatu.tanilink.View

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.topibatu.tanilink.R
import com.topibatu.tanilink.Util.Account
import kotlinx.coroutines.launch

@SuppressLint("RememberReturnType")
@ExperimentalMaterial3Api
@Composable
fun RegisterPage(navController: NavController) {
    // TODO: change button color, get textfield state value, navigation from register -> login viceversa
    val accountRPC = remember { Account() }
    val scope = rememberCoroutineScope()

    val nameState = remember { mutableStateOf(TextFieldValue()) }
    val emailState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }

    val registerRes = remember { mutableStateOf<account_proto.Account.RegisterRes?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        // Title
        Text(text = "Sign Up", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Text Field
        OutlinedTextField(
            value = nameState.value,
            placeholder = { Text("Full Name") },
            onValueChange = {
                nameState.value = it
            })
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = emailState.value,
            placeholder = { Text("Email") },
            onValueChange = {
                emailState.value = it
            })
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = passwordState.value,
            placeholder = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = {
                passwordState.value = it
            })
        Spacer(modifier = Modifier.height(8.dp))

        // Button
        Button(onClick = {
            // TODO: Request ke backend
            scope.launch {
                registerRes.value = accountRPC.register(
                    name = nameState.value.toString(),
                    email = emailState.value.toString(),
                    password = passwordState.value.toString()
                )
            }

            Log.d("TEST", "TESTTTT")

            // kalau berhasil
//            navController.navigate("login")

        }) {
            Text(text = "Register")
        }

        registerRes.value?.let { response ->
            Text("Server Response: ", modifier = Modifier.padding(top = 10.dp))
            Text("Status: ${response.account.id}")
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
            painter = rememberAsyncImagePainter("https://firebasestorage.googleapis.com/v0/b/topibatu-2a076.appspot.com/o/assets%2Fdaun_register.png?alt=media&token=afae7d2b-068c-4bcd-850c-9bbd9089783b"),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .offset(boxWidth - 200.dp, 0.dp)
                .clip(MaterialTheme.shapes.small)
                .wrapContentSize(align = Alignment.TopEnd)
        )

        // Bottom Left Image
        Image(
            painter = rememberAsyncImagePainter("https://firebasestorage.googleapis.com/v0/b/topibatu-2a076.appspot.com/o/assets%2Fdaun_register.png?alt=media&token=afae7d2b-068c-4bcd-850c-9bbd9089783b"),
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