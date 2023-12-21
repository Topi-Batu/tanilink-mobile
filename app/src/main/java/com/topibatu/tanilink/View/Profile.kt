package com.topibatu.tanilink.View

import account_proto.AccountProto
import account_proto.AccountProto.AccountDetail
import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.orhanobut.hawk.Hawk
import com.patrykandpatrick.vico.core.extension.setFieldValue
import com.topibatu.tanilink.Util.Account
import com.topibatu.tanilink.Util.Photo
import com.topibatu.tanilink.View.components.AddressField
import com.topibatu.tanilink.View.components.BottomBar
import com.topibatu.tanilink.View.components.DatePickerDialogComponent
import com.topibatu.tanilink.View.components.TopBar
import io.grpc.StatusException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfilePage(navController: NavController) {
    val accountRPC = remember { Account() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val getProfileRes = remember { mutableStateOf<AccountDetail?>(null) }
    val editProfileRes = remember { mutableStateOf<AccountDetail?>(null) }

    // Gender Input Var
    var expanded by remember { mutableStateOf(false) }
    val genderList = arrayOf("Male", "Female")

    // Date of Birth Input Var
    var showDatePicker by remember { mutableStateOf(false) }

    // Address
    var showAddress by remember { mutableStateOf(false) }

    // TextField Value State
    val nameState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val phoneNumberState = remember { mutableStateOf("") }
    val pictureUrl = remember { mutableStateOf("") }
    val xp = remember { mutableStateOf("") }
    val genderState = remember { mutableStateOf(genderList[0]) }
    val date = remember { mutableStateOf("Date of Birth") }
    val roles = remember { mutableStateOf("") }

    // Default TextField Color
    val defaultTextFieldColor: TextFieldColors = OutlinedTextFieldDefaults.colors(
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
    )


    // Get Profile
    LaunchedEffect(null) {
        getProfileRes.value = try {
            accountRPC.getProfile()
        } catch (e: StatusException) {
            Toast.makeText(
                context,
                "Get Profile Failed: ${e.status.description}",
                Toast.LENGTH_SHORT
            ).show()
            null
        }

        val requestFailedPrompt = "Data Request Failed"
        nameState.value = getProfileRes.value?.fullName ?: requestFailedPrompt
        emailState.value = getProfileRes.value?.email ?: requestFailedPrompt
        phoneNumberState.value = getProfileRes.value?.phoneNumber ?: requestFailedPrompt
        pictureUrl.value = getProfileRes.value?.picture ?: requestFailedPrompt
        xp.value = getProfileRes.value?.xp.toString() ?: requestFailedPrompt
        genderState.value = getProfileRes.value?.gender ?: requestFailedPrompt
        date.value = getProfileRes.value?.dateOfBirth ?: requestFailedPrompt
        roles.value = getProfileRes.value?.roleList.toString() ?: requestFailedPrompt
    }


    // Toggle Status Edit or Save
    val isEdit = remember { mutableStateOf(true) }

    // Photo Upload Service
    var uri by remember { mutableStateOf<Uri?>(null) }
    val photoServices = remember { Photo() }
    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            uri = it
        }
    )

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .padding(all = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // back Icon
                Icon(
                    Icons.Filled.ArrowBack, "Back", modifier = Modifier
                        .size(38.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )
                // Name
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    "Profile",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.weight(1f)
                )

                // Log Out Icon
                Icon(
                    Icons.Filled.ExitToApp,
                    contentDescription = "Log Out",
                    tint = Color.Red,
                    modifier = Modifier
                        .size(38.dp)
                        .clickable {
                            navController.navigate("login")

                            // Delete Session
                            Hawk.delete("user-id");
                            Hawk.delete("access-token");

                            Toast.makeText(
                                context,
                                "Logged Out",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                )
            }
        },
        bottomBar = {
            BottomBar(navController, 3)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Check if it is save button and save the current state to backend
                    if (!isEdit.value) {
                        // Update account information to gRPC
                        scope.launch {
                            uri?.let {
                                // Upload image to firebase
                                val imageUrl =
                                    photoServices.uploadToStorage(it, context, Photo.Path.PhotoProfile).await()
                                pictureUrl.value = imageUrl
                            }

                            editProfileRes.value = try {
                                accountRPC.editProfile(
                                    fullName = nameState.value,
                                    phoneNumber = phoneNumberState.value,
                                    picture = pictureUrl.value,
                                    gender = genderState.value,
                                    dateOfBirth = date.value
                                )
                            } catch (e: StatusException) {
                                Toast.makeText(
                                    context,
                                    "Edit Profile Failed: ${e.status.description}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                null
                            }
                        }

                    }

                    // Toggle Status
                    isEdit.value = !isEdit.value
                }
            ) {
                if (isEdit.value) {
                    Icon(Icons.Filled.Edit, "Edit Profile")
                } else {
                    Icon(Icons.Filled.Done, "Save Profile")
                }
            }
        }
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 100.dp),
            modifier = Modifier
                .padding(top = 86.dp)
                .fillMaxSize()
        ) {
            items(count = 1) {
                // Profile Image
                AsyncImage(
                    model = if (uri != null) uri else getProfileRes.value?.picture,
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                        .clickable {
                            if (!isEdit.value) {
                                // Pick Image
                                singlePhotoPicker.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                        }
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Name
                OutlinedTextField(
                    value = nameState.value,
                    onValueChange = { nameState.value = it },
                    label = { Text(text = "Name") },
                    singleLine = true,
                    readOnly = isEdit.value,
                    shape = RoundedCornerShape(32.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Email
                OutlinedTextField(
                    value = emailState.value,
                    onValueChange = { },
                    label = { Text(text = "Email") },
                    enabled = false,
                    shape = RoundedCornerShape(32.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Phone Number
                OutlinedTextField(
                    value = phoneNumberState.value,
                    onValueChange = { phoneNumberState.value = it },
                    singleLine = true,
                    label = { Text(text = "Phone Number") },
                    readOnly = isEdit.value,
                    shape = RoundedCornerShape(32.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))

                // XP
                OutlinedTextField(
                    value = xp.value,
                    onValueChange = { },
                    label = { Text(text = "XP") },
                    enabled = false,
                    shape = RoundedCornerShape(32.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Gender
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        if (!isEdit.value) {
                            expanded = !expanded
                        }
                    }) {
                    OutlinedTextField(
                        value = genderState.value,
                        onValueChange = {},
                        label = { Text("Gender") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        shape = RoundedCornerShape(32.dp),
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
                                    genderState.value = item
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Date of Birth
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.clickable(onClick = {
                        if (!isEdit.value) showDatePicker = true
                    })
                ) {
                    OutlinedTextField(
                        value = date.value,
                        placeholder = { Text("Date of Birth") },
                        label = { Text("Date of Birth") },
                        enabled = false,
                        colors = defaultTextFieldColor,
                        shape = RoundedCornerShape(32.dp),
                        onValueChange = { },
                    )
                }
                if (showDatePicker) {
                    DatePickerDialogComponent(
                        datePattern = "MM/dd/yyyy",
                        onDateSelected = { date.value = it },
                        onDismiss = { showDatePicker = false }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Address
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.clickable(onClick = {
                        if (!isEdit.value) showAddress = true
                    })
                ) {
                    OutlinedTextField(
                        value = "Address", // TODO: Change this to be dynamic
                        placeholder = { Text("Address") },
                        label = { Text("Address") },
                        enabled = false,
                        colors = defaultTextFieldColor,
                        shape = RoundedCornerShape(32.dp),
                        onValueChange = { },
                    )
                }
                if (showAddress) {
                    AddressField(value = "", setShowDialog = {
                        showAddress = it
                    }) {
                        Log.i("HomePage", "HomePage : $it")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Roles
                OutlinedTextField(
                    value = roles.value,
                    onValueChange = { },
                    label = { Text(text = "Roles") },
                    enabled = false,
                    shape = RoundedCornerShape(32.dp),
                )
            }
        }

        // If Edit Profile Success
        editProfileRes.value?.let { response ->
            Toast.makeText(
                context,
                "Your Profile Has Been Updated",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}


