package com.example.mapsapp.data

import kotlinx.serialization.Serializable

@Serializable
data class MarkerD(
    var id: Int = 0,
    var title: String,
    var descripcion: String,
    var image: String

)
