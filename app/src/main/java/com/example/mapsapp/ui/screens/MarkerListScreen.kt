package com.example.mapsapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
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
fun MarkerListScreen(navigateToDetail: (Int) -> Unit){
    val myViewModel = viewModel<OperacionesVM>()
    val markerList by myViewModel.markerList.observeAsState(emptyList<MarkerD>())
    myViewModel.getAllMarkers()
    LazyColumn(Modifier.fillMaxWidth().padding(top= 150.dp)) {
        items(markerList) { marker ->
            val dissmissState = rememberSwipeToDismissBoxState(
                confirmValueChange = {
                    if (it == SwipeToDismissBoxValue.EndToStart) {
                        myViewModel.deleteMarker(marker.id, marker.image)
                        true
                    } else { false }
                }
            )
            SwipeToDismissBox(state = dissmissState, backgroundContent = {
                Box(Modifier.fillMaxSize().background(Color.Red),contentAlignment = Alignment.BottomEnd) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }) {
                MarkerItem(marker) { navigateToDetail(marker.id) }
            }
        }
    }



}

@Composable
fun MarkerItem(marker: MarkerD, navigateToDetail: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth().background(Color.LightGray).border(width = 2.dp, Color.DarkGray)
            .clickable { navigateToDetail(marker.id) }) {
        Row(Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(marker.title, fontSize = 28.sp, fontWeight = FontWeight.Bold)

        }
    }
}
