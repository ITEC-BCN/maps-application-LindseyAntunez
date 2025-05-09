package com.example.mapsapp.ui.navigation

import kotlinx.serialization.Serializable


sealed class Destination {
    @Serializable
    object Map : Destination()

    @Serializable
    object List : Destination()

    @Serializable
    data class CreateMarker (var lat: Double, var lon: Double)

    @Serializable
    object DetailMarker : Destination()

    @Serializable
    object Permisos : Destination()

    @Serializable
    object Drawer : Destination()


}

