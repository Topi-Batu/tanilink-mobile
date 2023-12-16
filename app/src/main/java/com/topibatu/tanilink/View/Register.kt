package com.topibatu.tanilink.View

import account_proto.AccountProto.RegisterRes
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.orhanobut.hawk.Hawk
import com.topibatu.tanilink.Util.Account
import com.topibatu.tanilink.View.components.DatePickerDialogComponent
import io.grpc.StatusException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalComposeUiApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("RememberReturnType")
@ExperimentalMaterial3Api
@Composable
fun RegisterPage(navController: NavController) {
    // TODO: change button color, get textfield state value, navigation from register -> login viceversa
    val accountRPC = remember { Account() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val nameState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val phoneNumberState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val confirmPasswordState = remember { mutableStateOf("") }

    // Gender Input Var
    var expanded by remember { mutableStateOf(false) }
    val genderList = arrayOf("Male", "Female")
    var genderState by remember { mutableStateOf(genderList[0]) }

    // Date of Birth Input Var
    var date by remember { mutableStateOf("Date of Birth") }
    var showDatePicker by remember { mutableStateOf(false) }


    val registerRes = remember { mutableStateOf<RegisterRes?>(null) }

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
            value = phoneNumberState.value,
            placeholder = { Text("Phone Number") },
            onValueChange = { phoneNumberState.value = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = genderState,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                genderList.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            genderState = item
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.clickable(onClick = {
                showDatePicker = true
            })
        ) {
            OutlinedTextField(
                value = date,
                placeholder = { Text("Date of Birth") },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = Color.Transparent,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPrefixColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledSuffixColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                onValueChange = { },
            )
        }
        if (showDatePicker) {
            DatePickerDialogComponent(
                onDateSelected = { date = it },
                onDismiss = { showDatePicker = false }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = passwordState.value,
            placeholder = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = {
                passwordState.value = it
            })
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPasswordState.value,
            placeholder = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = {
                confirmPasswordState.value = it
            })
        Spacer(modifier = Modifier.height(8.dp))

        // Button
        Button(onClick = {
            scope.launch {
                registerRes.value = try {
                    accountRPC.register(
                        name = nameState.value,
                        email = emailState.value,
                        phoneNumber = phoneNumberState.value,
                        gender = genderState,
                        dateOfBirth = date,
                        password = passwordState.value,
                        confirmPassword = confirmPasswordState.value
                    )
                } catch (e: StatusException) {
                    Toast.makeText(
                        context,
                        "Registration failed: ${e.status.description}",
                        Toast.LENGTH_SHORT
                    ).show()
                    null
                }

            }

        }) {
            Text(text = "Register")
        }

        // If Register Success
        registerRes.value?.let { response ->
            Toast.makeText(
                context,
                "Check Your Email Address Box",
                Toast.LENGTH_SHORT
            ).show()

            // Save Session
            Hawk.put("email", response.account.email);

            LaunchedEffect(response) {
                delay(2500) // Delay for 2 seconds
                navController.navigate("email_verification")
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
