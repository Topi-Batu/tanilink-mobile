package com.topibatu.tanilink.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(navController: NavController) {
    // TODO: change button color, get textfield state value, navigation from register -> login viceversa
    val emailState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }


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
        Spacer(modifier = Modifier.height(16.dp))

        // Button
        Button(onClick = {
            // TODO: Request ke backend

            // Kalau berhasil login
            navController.navigate("main")
        }) {
            Text(text = "Login")
        }

        // Don't have an Acocunt?
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Don't Have an Account?",
            color = Color.Gray,
            modifier = Modifier
                .clickable {
                    navController.navigate("sign_up")
                }
        )
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
