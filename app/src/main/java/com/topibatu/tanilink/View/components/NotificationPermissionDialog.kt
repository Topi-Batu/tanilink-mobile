package com.topibatu.tanilink.View.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.core.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FirebaseMessagingNotificationPermissionDialog(
    showNotificationDialog: MutableState<Boolean>,
    notificationPermissionState: PermissionState
) {
    if (showNotificationDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showNotificationDialog.value = false
                notificationPermissionState.launchPermissionRequest()
            },
            title = { Text(text = "Notification Permission") },
            text = { Text(text = "Please allow this app to send you a notification") },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = null
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showNotificationDialog.value = false
                    notificationPermissionState.launchPermissionRequest()
                    Firebase.messaging.subscribeToTopic("main")
                }) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNotificationDialog.value = false }) {
                    Text(text = "Cancel", color = Color.Red)
                }
            }
        )
    }
}