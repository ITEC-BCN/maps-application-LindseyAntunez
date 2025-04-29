package com.example.mapsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mapsapp.ui.screens.MapScreen
import com.example.mapsapp.ui.screens.MarkerListScreen

@Composable
fun NavigationWrapper(navigateNext: NavHostController, Modifer: Modifier) {
    NavHost(navigateNext, Destination.Map) {
        composable<Destination.Map> {
            MapScreen()

        }
        composable<Destination.List> {
            MarkerListScreen()

        }

    }
}