package com.topibatu.tanilink


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.orhanobut.hawk.Hawk
import com.topibatu.tanilink.View.CategoryListPage
import com.topibatu.tanilink.View.ChatPage
import com.topibatu.tanilink.View.ForgotPassword
import com.topibatu.tanilink.View.HomePage
import com.topibatu.tanilink.View.LoginPage
import com.topibatu.tanilink.View.PredictionPage
import com.topibatu.tanilink.View.ProductDetailPage
import com.topibatu.tanilink.View.ProfilePage
import com.topibatu.tanilink.View.RegisterPage
import com.topibatu.tanilink.ui.theme.TanilinkTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        // Initiate Session Manager
        Hawk.init(this).build();

        setContent {
            TanilinkTheme {

                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavHost(navController = navController, startDestination = "main") {
                    navigation(startDestination = "login", route = "login_signup"){
                        composable("login") { LoginPage(navController = navController) }
                        composable("sign_up") { RegisterPage(navController = navController) }
                        composable("forgot_password") { ForgotPassword(navController = navController) }
                    }

                    navigation(startDestination = "home", route = "main"){
                        composable("home") { HomePage(navController = navController) }
                        composable("prediction") { PredictionPage(navController = navController) }
                        composable("chat") { ChatPage(navController = navController)}
                        composable("profile"){ ProfilePage(navController = navController)}
                        composable("category_list"){ CategoryListPage(navController = navController)}
                        composable("product_detail"){ ProductDetailPage(navController = navController)}
//                      composable("cart") { CartPage(navController = navController) }
                    }
                }
            }

        }
    }
}

