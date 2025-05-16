package com.example.mapsapp.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.navigation.Destination.Drawer
import com.example.mapsapp.ui.screens.HomeScreen
import com.example.mapsapp.ui.screens.MyDrawerMenu
import com.example.mapsapp.ui.screens.PermisosScreen
import com.example.mapsapp.ui.screens.RegisterScreen

@Composable
fun ModalNavWrapper() {
    val navController = rememberNavController()
    NavHost(navController, Destination.Home) {

        composable<Destination.Home> {
            HomeScreen(
                navigateToRegister = { navController.navigate(Destination.Register) },
                navigateToLogin = { navController.navigate(Destination.LogIn) }
            )
        }

        composable<Destination.Permisos> {
            PermisosScreen(navController.navigate(Drawer))

        }
        composable<Drawer> {
            MyDrawerMenu()

        }

        composable<Destination.Register> {
            RegisterScreen({navController.navigate(Destination.Permisos)})
        }


    }
}