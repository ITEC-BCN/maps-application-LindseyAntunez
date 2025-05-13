package com.example.mapsapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
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
    var myViewModel = viewModel<OperacionesVM>()
    var marcadorList = myViewModel.markerList.observeAsState(emptyList())
    myViewModel.getAllMarkers()
    Column(modifier.fillMaxSize()) {
        val itb = LatLng(41.4534225, 2.1837151)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(itb, 17f)
        }
        GoogleMap(
            modifier = modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLongClick = { navigateToCreate(it.latitude, it.longitude)
            }
        ) {
            marcadorList.value.forEach { marcador ->
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

    }
}
