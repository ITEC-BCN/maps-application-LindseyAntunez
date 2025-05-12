package com.example.mapsapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.data.MarkerD
import com.example.mapsapp.viewmodels.OperacionesVM

@Composable
fun MarkerDetailScreen(markerID: Int, navigateBack: () -> Unit) {
    val myViewModel = viewModel<OperacionesVM>()
    myViewModel.getMarker(markerID)
    val markerTitle: String by myViewModel.markerTitle.observeAsState("")
    val markerDescripcion: String by myViewModel.markerDescrip.observeAsState("")
    val markerImagen: String by myViewModel.markerImage.observeAsState("foto")


    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = markerTitle, onValueChange = { myViewModel.editTitle(it) })
        TextField(value = markerDescripcion, onValueChange = { myViewModel.editDesc(it) })
        Button(onClick = {
            myViewModel.updateStudent(markerID, markerTitle, markerDescripcion,markerImagen )
            navigateBack()
        }) {
            Text("Update")
        }
    }


}

