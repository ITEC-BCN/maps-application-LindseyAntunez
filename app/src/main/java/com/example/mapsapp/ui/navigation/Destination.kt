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
    data class DetailMarker (var id: Int)

    @Serializable
    object Permisos : Destination()

    @Serializable
    object Drawer : Destination()


}

