package com.example.mapsapp.ui.screens

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.core.graphics.scale
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.OperacionesVM
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateMarkerScreen(navigateToDetail: () -> Unit, lat: Double, lon: Double) {
    val context = LocalContext.current
    val myViewModel = viewModel<OperacionesVM>()
    val markerTitle by myViewModel.markerTitle.observeAsState("")
    val markerDesc by myViewModel.markerDescrip.observeAsState("")
    val bitmap by myViewModel.bitmap.observeAsState()
    var showDialog by remember { mutableStateOf(false) }

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            myViewModel.setImageUri(it)
            context.contentResolver.openInputStream(it)?.use { stream ->
                myViewModel.setBitmap(BitmapFactory.decodeStream(stream))
            }
        }
    }

    val launcherCamara = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            myViewModel.imageuri.value?.let { uri ->
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    myViewModel.setBitmap(BitmapFactory.decodeStream(stream))
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 🌍 Fondo de mapa interactivo
        AndroidView(factory = { context ->
            MapView(context).apply {
                onCreate(null)
                getMapAsync { googleMap ->
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lon), 12f))
                }
            }
        }, modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Nuevo marcador", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Gray)

            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.9f)) // 💠 Fondo semi-transparente
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = markerTitle,
                        onValueChange = { myViewModel.editTitle(it) },
                        label = { Text("Título") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = markerDesc,
                        onValueChange = { myViewModel.editDesc(it) },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                ElevatedButton(onClick = { showDialog = true }) {
                    Text("Seleccionar Foto", modifier = Modifier.padding(start = 8.dp))
                }

            }

            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(280.dp).clip(RoundedCornerShape(14.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔵 Botón flotante con efecto visual mejorado
            Button(
                onClick = {
                    myViewModel.insertNewMarker(markerTitle, markerDesc, lat, lon, bitmap)
                    navigateToDetail()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Guardar", tint = Color.White)
                Text("Guardar marcador", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp), color = Color.White)
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Selecciona una opción", fontWeight = FontWeight.Bold) },
            text = { Text("¿Quieres tomar una foto o elegir una desde la galería?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    val uri = createImageUri(context)
                    myViewModel.setImageUri(uri)
                    launcherCamara.launch(uri!!)
                }) {
                    Text("Tomar Foto", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false; pickImageLauncher.launch("image/*") }) {
                    Text("Elegir de Galería", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

fun createImageUri(context: Context): Uri? {
    val file = File.createTempFile("temp_image_", ".jpg", context.cacheDir).apply {
        createNewFile()
        deleteOnExit()
    }
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}