package com.example.mapsapp.ui.screens

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.core.graphics.scale
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.OperacionesVM
import java.io.File

@Composable
fun CreateMarkerScreen(navigateToDetail: (String) -> Unit, lat: Double, lon: Double) {
    val context = LocalContext.current





    val myViewModel = viewModel<OperacionesVM>()
    val markerTitle: String by myViewModel.markerTitle.observeAsState("")
    val markerDesc: String by myViewModel.markerDescrip.observeAsState("")
    val markerImage: String by myViewModel.markerImage.observeAsState("hola")
    val bitmap by myViewModel.bitmap.observeAsState()
    val uri by myViewModel.imageuri.observeAsState()
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && uri != null) {
                val stream = context.contentResolver.openInputStream(uri!!)
                stream?.use {
                    // Decodificar el flujo a un Bitmap
                    val originalBitmap = BitmapFactory.decodeStream(it)

                    // Obtener las dimensiones originales de la imagen
                    val originalWidth = originalBitmap.width
                    val originalHeight = originalBitmap.height

                    // Definir el aspect ratio (relación entre ancho y alto)
                    val aspectRatio = originalWidth.toFloat() / originalHeight.toFloat()

                    // Establecer el tamaño máximo que deseas para la imagen (por ejemplo, un ancho máximo)
                    val maxWidth = 800 // Puedes establecer el valor que prefieras

                    // Calcular el nuevo ancho y alto manteniendo el aspect ratio
                    val newWidth = maxWidth
                    val newHeight = (newWidth / aspectRatio).toInt()

                    // Redimensionar el bitmap mientras se mantiene el aspect ratio
                    val resizedBitmap = originalBitmap.scale(newWidth, newHeight)

                    // Establecer el Bitmap redimensionado en el ViewModel
                    myViewModel.setBitmap(resizedBitmap)
                } ?: run {
                    Log.e("TakePicture", "Error al abrir InputStream para la URI de la imagen.")
                }
            } else {
                Log.e("TakePicture", "La imagen no fue tomada o la URI de la imagen es nula.")
            }
        }
    Column(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxWidth().weight(0.4f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text("Create new marker", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            TextField(value = markerTitle, onValueChange = { myViewModel.editTitle(it) })
            TextField(value = markerDesc, onValueChange = { myViewModel.editDesc(it)})
            Button(onClick = {
                val uri = createImageUri(context)
                myViewModel.setImageUri(uri)
                launcher.launch(uri!!)
            }) {
                Text("Abrir Cámara")
            }

            bitmap?.let {
                Image(bitmap = it.asImageBitmap(), contentDescription = null,
                    modifier = Modifier.size(300.dp).clip(RoundedCornerShape(12.dp)),contentScale = ContentScale.Crop)
            }
            Button(onClick = { myViewModel.insertNewMarker(markerTitle, markerDesc, markerImage) }                 ) {
                Text("Insert")
            }
        }
    }

}

fun createImageUri(context: Context): Uri? {
    val file = File.createTempFile("temp_image_", ".jpg", context.cacheDir).apply {
        createNewFile()
        deleteOnExit()
    }
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}

