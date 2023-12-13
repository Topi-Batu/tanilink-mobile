package com.topibatu.tanilink.View.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

@Composable
fun BottomBar(navController: NavController, activeIndex: Int) {
    data class NavigationItem(
        val text: String,
        val path: String,
        val icon: ImageVector
    )
    var selectedItem by remember { mutableIntStateOf(activeIndex) }
    val items = listOf(
        NavigationItem("Home", "home", Icons.Filled.Home),
        NavigationItem("Chat", "chat", Icons.Filled.Email),
        NavigationItem("Prediction", "prediction", Icons.Filled.Check),
        NavigationItem("Profile", "profile", Icons.Filled.Person)
    )
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.text) },
                label = { Text(item.text) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.path)
                }
            )
        }
    }
}