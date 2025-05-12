package com.example.mapsapp.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.MyApp
import com.example.mapsapp.data.MarkerD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OperacionesVM : ViewModel() {
    val database = MyApp.database
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



    fun getAllStudents() {
        CoroutineScope(Dispatchers.IO).launch {
            val databaseStudents = database.getAllMarkers()
            withContext(Dispatchers.Main) {
                _markerList.value = databaseStudents
            }
        }
    }


    fun setImageUri(uri: Uri?){
        _imageUri.value = uri

    }

    fun setBitmap(bit: Bitmap){
        _bitmap.value = bit

    }
    fun editTitle(value: String) {
        _markerTitle.value = value
    }

    fun editDesc(value: String) {
        _markerDescrip.value = value
    }

    fun updateStudent(id: Int, title: String, descripcion: String, image: String) {
        CoroutineScope(Dispatchers.IO).launch {
            database.updateMarker(
                id = id,
                title = title,
                descripcion = descripcion,
                image = image
            )
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

    fun insertNewMarker(title: String, descripcion: String, image: String) {
        val newMarker = MarkerD(
            title = title,
            descripcion = descripcion,
            image = image
        )
        CoroutineScope(Dispatchers.IO).launch {
            database.insertMarker(newMarker)
            database.getAllMarkers()
        }
    }

    fun deleteMarker(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            database.deleteMarker(id)
            database.getAllMarkers()
        }
    }

}
