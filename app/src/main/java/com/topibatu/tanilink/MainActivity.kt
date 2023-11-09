package com.topibatu.tanilink

import android.graphics.ColorSpace
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.topibatu.tanilink.View.LoginPage
import com.topibatu.tanilink.View.RegisterPage
import com.topibatu.tanilink.ui.theme.TanilinkTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TanilinkTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Tani Link") },
                            colors = TopAppBarDefaults.mediumTopAppBarColors(
                                // FIX: color scheme
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                ) { padding ->
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") { LoginPage(navController = navController) }
                        composable("sign_up") { RegisterPage(navController = navController) }
                    }
                }
            }
        }
    }
}



