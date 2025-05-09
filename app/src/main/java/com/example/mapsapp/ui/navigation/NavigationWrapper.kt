package com.example.mapsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mapsapp.ui.screens.CreateMarkerScreen
import com.example.mapsapp.ui.screens.MapScreen
import com.example.mapsapp.ui.screens.MarkerListScreen

@Composable
fun NavigationWrapper(navigateNext: NavHostController, Modifer: Modifier) {
    NavHost(navigateNext, Destination.Map) {
        composable<Destination.Map> {
            MapScreen(
                navigateToCreate = { lat, lon-> navigateNext.navigate(Destination.CreateMarker(lat , lon)) }
            )

        }
        composable<Destination.List> {
            MarkerListScreen()

        }
        composable<Destination.CreateMarker> { backStackEntry ->
            val create = backStackEntry.toRoute<Destination.CreateMarker>()
            CreateMarkerScreen(
                navigateToDetail = { navigateNext.navigate(Destination.Map) },
                lat= create.lat,
                lon= create.lon
            )

        }

        composable<Destination.List> {
            MarkerListScreen()

        }

    }
}