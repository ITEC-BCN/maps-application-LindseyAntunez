package com.example.mapsapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.MyApp
import com.example.mapsapp.data.MarkerD
import com.google.maps.android.compose.Marker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OperacionesVM : ViewModel() {
    val database = MyApp.database
    private val _markerTitle = MutableLiveData<String>()
    val markerTitle = _markerTitle
    private val _markerDescrip = MutableLiveData<String>()
    val markerDescrip = _markerDescrip
    private val _markerImage = MutableLiveData<String>()
    val markerImage = _markerImage
    fun insertNewMarker(id: Int, title: String, descripcion: String, image: String) {
        val newMarker = MarkerD(
            id = id,
            title = title,
            descripcion =descripcion,
            image = image
        )
        CoroutineScope(Dispatchers.IO).launch {
            database.insertMarker(newMarker)
            database.getAllMarkers()
        }
    }
}
