package com.example.mapsapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.data.MarkerD
import com.example.mapsapp.viewmodels.OperacionesVM

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MarkerListScreen(navigateToDetail: (Int) -> Unit) {
    val myViewModel = viewModel<OperacionesVM>()
    val markerList by myViewModel.markerList.observeAsState(emptyList<MarkerD>())
    myViewModel.getAllMarkers()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 100.dp)) {
        stickyHeader {
            Box(modifier = Modifier.fillMaxWidth().background(Color.LightGray)) {
                Text("Marcadores", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp), textAlign = TextAlign.Center)
            }
        }

        items(markerList, key = { it.id }) { marker ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { dismissValue ->
                    if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                        myViewModel.deleteMarker(marker.id, marker.image)
                        true
                    } else {
                        false
                    }
                }
            )

            AnimatedVisibility(visible = dismissState.targetValue == SwipeToDismissBoxValue.Settled) {
                SwipeToDismissBox(
                    state = dismissState,
                    backgroundContent = {
                        Box(
                            Modifier.padding(8.dp).background(Color.Red.copy(alpha = 0.9f)).fillMaxWidth().height(150.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.White)
                        }
                    }
                ) {
                    MarkerItem(marker) { navigateToDetail(marker.id) }
                }
            }
        }
    }
}

@Composable
fun MarkerItem(marker: MarkerD, navigateToDetail: (Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxSize().padding(8.dp)
            .clickable { navigateToDetail(marker.id) },
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(marker.title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E8B57))
                Text("Tap para ver detalles", fontSize = 14.sp, color = Color.Gray)
            }
            Icon(Icons.Default.LocationOn, contentDescription = "Marcador", tint = Color(0xFF73C2FB))
        }
    }
}