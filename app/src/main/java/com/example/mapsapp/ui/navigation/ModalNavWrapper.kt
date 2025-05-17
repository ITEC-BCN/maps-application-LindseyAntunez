package com.example.mapsapp.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.navigation.Destination.Drawer
import com.example.mapsapp.ui.navigation.Destination.Home
import com.example.mapsapp.ui.navigation.Destination.LogIn
import com.example.mapsapp.ui.navigation.Destination.Permisos
import com.example.mapsapp.ui.navigation.Destination.Register
import com.example.mapsapp.ui.screens.HomeScreen
import com.example.mapsapp.ui.screens.LogInScreen
import com.example.mapsapp.ui.screens.MyDrawerMenu
import com.example.mapsapp.ui.screens.PermisosScreen
import com.example.mapsapp.ui.screens.RegisterScreen

@Composable
fun ModalNavWrapper() {
    val navController = rememberNavController()
    NavHost(navController, Destination.Home) {

        composable<Destination.Home> {
            HomeScreen(
                navigateToRegister = { navController.navigate(Register) },
                navigateToLogIn = { navController.navigate(Destination.LogIn) }
            )
        }

        composable<LogIn> {
            LogInScreen({ navController.navigate(Permisos) })

        }

        composable<Permisos> {
            PermisosScreen(navController.navigate(Drawer))

        }
        composable<Drawer> {
            MyDrawerMenu {
                navController.navigate(Destination.Home) {
                    popUpTo<Home> { inclusive = true }
                }


            }

        }
        composable<Register> {
            RegisterScreen {
                navController.navigate(Destination.Home) {
                    popUpTo<Home> { inclusive = true }
                }
            }


        }
    }
}