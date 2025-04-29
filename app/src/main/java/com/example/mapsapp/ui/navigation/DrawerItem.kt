package com.example.mapsapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector

enum class DrawerItem (
    val text: String,
    val route: Destination
    ) {
    MAP("Map", Destination.Map),
    LIST("List", Destination.List),


}