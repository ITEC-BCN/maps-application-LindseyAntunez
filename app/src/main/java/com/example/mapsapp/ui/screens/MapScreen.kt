package com.example.mapsapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.OperacionesVM
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(modifier: Modifier = Modifier, navigateToCreate: (Double, Double) -> Unit) {
    val myViewModel = viewModel<OperacionesVM>()
    val marcadorList by myViewModel.markerList.observeAsState(emptyList())
    val showLoading by myViewModel.showLoading.observeAsState(true)

    // Invoca getAllMarkers() al iniciar la composición
    LaunchedEffect(Unit) {
        myViewModel.getAllMarkers()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Muestra siempre el mapa
        val itb = LatLng(41.4534225, 2.1837151)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(itb, 17f)
        }
        GoogleMap(
            modifier = modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLongClick = { latLng ->
                navigateToCreate(latLng.latitude, latLng.longitude)
            }
        ) {
            marcadorList.forEach { marcador ->
                Marker(
                    state = MarkerState(position = LatLng(marcador.latitud, marcador.longitud)),
                    title = marcador.title,
                    snippet = marcador.descripcion
                )
            }
            Marker(
                state = MarkerState(position = itb),
                title = "ITB",
                snippet = "Marcador fijo"
            )
        }

        // Si aún se están cargando los datos, muestra el indicador
        if (showLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

        }
    }
}