package com.example.mapsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.navigation.Destination.Drawer
import com.example.mapsapp.ui.screens.MyDrawerMenu
import com.example.mapsapp.ui.screens.PermisosScreen

@Composable
fun ModalNavWrapper() {
    val navController = rememberNavController()
    NavHost(navController, Destination.Permisos) {
        composable<Destination.Permisos> {
            PermisosScreen(navController.navigate(Drawer))

        }
        composable<Destination.Drawer> {
            MyDrawerMenu()

        }

    }
}