package com.example.mapsapp.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mapsapp.R
import com.example.mapsapp.viewmodels.OperacionesVM
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MarkerDetailScreen(markerID: Int, navigateBack: () -> Unit) {
    val context = LocalContext.current
    val myViewModel = viewModel<OperacionesVM>()

    LaunchedEffect(markerID) {
        Log.d("Lindsey", "Voy a por el marker")
        myViewModel.getMarker(markerID)
    }

    val markerTitle by myViewModel.markerTitle.observeAsState("")
    val markerDescripcion by myViewModel.markerDescrip.observeAsState("")
    val bitmap by myViewModel.bitmap.observeAsState()
    val markerImageName by myViewModel.markerImage.observeAsState()


    val uri by myViewModel.imageuri.observeAsState()

    val launcherCamara =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && uri != null) {
                val stream = context.contentResolver.openInputStream(uri!!)
                stream?.use {
                    val originalBitmap = BitmapFactory.decodeStream(it)
                    val resizedBitmap = Bitmap.createScaledBitmap(
                        originalBitmap,
                        800,
                        (800 * (originalBitmap.height.toFloat() / originalBitmap.width)).toInt(),
                        true
                    )

                    myViewModel.setBitmap(resizedBitmap)
                } ?: Log.e("TakePicture", "Error al abrir InputStream para la URI de la imagen.")
            } else {
                Log.e("TakePicture", "La imagen no fue tomada o la URI de la imagen es nula.")
            }
        }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = markerTitle, onValueChange = { myViewModel.editTitle(it) })
        TextField(value = markerDescripcion, onValueChange = { myViewModel.editDesc(it) })

        Box(
            modifier = Modifier.size(300.dp).clip(RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Log.d("Lindsey", "Comprobando si es null: ${bitmap == null}")

            if (markerImageName != null) {
                Log.d("Lindsey", "La imagen esta en $markerImageName")
                AsyncImage(markerImageName,contentDescription = null,  modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop)

            } else {
                Image(
                    painter = painterResource(id = R.drawable.placeholder_image),
                    contentDescription = "Imagen no disponible",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Button(onClick = {
            val newUri = createImageUri(context)
            myViewModel.setImageUri(newUri)
            launcherCamara.launch(newUri!!)
        }) {
            Text("Abrir Cámara")
        }

        Button(onClick = {
            myViewModel.updateMarker(markerID, markerTitle, markerDescripcion, bitmap,markerImageName)
            navigateBack()
        }) {
            Text("Actualizar")
        }
    }
}