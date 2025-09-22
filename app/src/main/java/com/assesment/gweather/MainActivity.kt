package com.assesment.gweather

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.assesment.gweather.ui.auth.login.LoginScreen
import com.assesment.gweather.ui.auth.register.RegisterScreen
import com.assesment.gweather.ui.home.HomeScreen
import com.assesment.gweather.ui.model.Screen
import com.assesment.gweather.ui.theme.GWeatherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GWeatherTheme {
                @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation(viewModel: MainActivityViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    val startDestination = if (viewModel.isLoggedIn) {
        Screen.Home.route
    } else {
        Screen.Login.route
    }

    NavHost(
        navController = navController, startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(onNavigateToRegister = {
                navController.navigate(Screen.Register.route)
            }, onLoginSuccess = { user ->
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            })
        }

        composable(Screen.Register.route) {
            RegisterScreen(onRegisterSuccess = { user ->
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Register.route) { inclusive = true }
                }
            }, onNavigateBack = {
                navController.popBackStack()
            })
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onLogout = {
                    viewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                })
        }
    }
}