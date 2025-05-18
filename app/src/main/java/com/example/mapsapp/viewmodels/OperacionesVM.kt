package com.example.mapsapp.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.SupabaseApplication
import com.example.mapsapp.data.MarkerD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class OperacionesVM : ViewModel() {
    val database = SupabaseApplication.supabase
    private val _markerTitle = MutableLiveData<String>()
    val markerTitle = _markerTitle
    private val _markerID = MutableLiveData<String>()
    val markerID = _markerID
    private val _markerDescrip = MutableLiveData<String>()
    val markerDescrip = _markerDescrip
    private val _markerImage = MutableLiveData<String>()
    val markerImage = _markerImage
    private var _selectedMarker: MarkerD? = null
    private val _imageUri = MutableLiveData<Uri?>(null)
    val imageuri = _imageUri
    private var _bitmap = MutableLiveData<Bitmap>(null)
    val bitmap = _bitmap
    private val _markerList = MutableLiveData<List<MarkerD>>()
    val markerList = _markerList
    private var _selectedMarkerD: MarkerD? = null
    private val _showLoading = MutableLiveData<Boolean>(true)
    val showLoading = _showLoading

    fun getAllMarkers() {
        CoroutineScope(Dispatchers.IO).launch {
            val databaseStudents = database.getAllMarkers()
            withContext(Dispatchers.Main) {
                _markerList.value = databaseStudents
                _showLoading.value = false  // Se desactiva la carga al finalizar
            }
        }
    }

    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    fun setBitmap(bit: Bitmap) {
        _bitmap.value = bit
    }

    fun editTitle(value: String) {
        _markerTitle.value = value
    }

    fun editDesc(value: String) {
        _markerDescrip.value = value
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateMarker(id: Int, title: String, descripcion: String, image: Bitmap?, _markerImageName: String?) {
        if (image != null) {
            val stream = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 0, stream)
            CoroutineScope(Dispatchers.IO).launch {
                val imageName = database.uploadImage(stream.toByteArray())
                database.updateMarker(
                    id = id,
                    title = title,
                    descripcion = descripcion,
                    imageName.toString(), stream.toByteArray()
                )
                database.deleteImage(_markerImageName!!)
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                database.updateMarker(
                    id = id,
                    title = title,
                    descripcion = descripcion,
                    _markerImageName.toString(), null
                )
            }
        }
    }

    fun getMarker(id: Int) {
        if (_selectedMarker == null) {
            CoroutineScope(Dispatchers.IO).launch {
                val marker = database.getMarker(id)
                withContext(Dispatchers.Main) {
                    _selectedMarker = marker
                    _markerTitle.value = marker.title
                    _markerDescrip.value = marker.descripcion
                    _markerImage.value = marker.image
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertNewMarker(title: String, descripcion: String, latitud: Double, longitud: Double, bitmap: Bitmap?) {
        // Activamos la carga
        _showLoading.postValue(true)
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
        CoroutineScope(Dispatchers.IO).launch {
            val imageName = database.uploadImage(stream.toByteArray())
            database.insertMarker(title, descripcion, latitud, longitud, imageName)
            // Obtenemos la lista actualizada de marcadores
            val updatedMarkers = database.getAllMarkers()
            withContext(Dispatchers.Main) {
                _markerList.value = updatedMarkers  // Se actualiza la lista, lo que refrescará el mapa
                _showLoading.value = false           // Desactivamos la carga
            }
        }
    }

    fun deleteMarker(id: Int, image: String) {
        CoroutineScope(Dispatchers.IO).launch {
            database.deleteImage(image)
            database.deleteMarker(id)
            val updatedMarkers = database.getAllMarkers()
            withContext(Dispatchers.Main) {
                _markerList.value = updatedMarkers
            }
        }
    }
}
