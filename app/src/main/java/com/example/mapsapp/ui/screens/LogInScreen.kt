package com.example.mapsapp.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.utils.AuthState
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.example.mapsapp.viewmodels.AuthViewModel
import com.example.mapsapp.viewmodels.AuthViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LogInScreen(navigateToHome: () -> Unit) {
    val context = LocalContext.current
    val viewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(SharedPreferencesHelper(context)))
    val authState by viewModel.authState.observeAsState()
    val showError by viewModel.showError.observeAsState(false)
    val email by viewModel.email.observeAsState("")
    val password by viewModel.password.observeAsState("")

    if (authState == AuthState.Authenticated) {
        navigateToHome()
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            // 🌍 Fondo con mapa dinámico
            AndroidView(factory = { context ->
                MapView(context).apply {
                    onCreate(null)
                    getMapAsync { googleMap ->
                        googleMap.uiSettings.isZoomControlsEnabled = false
                        googleMap.uiSettings.isMapToolbarEnabled = false
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(- 38.1828, -3.687), 12f))
                    }
                }
            }, modifier = Modifier.fillMaxSize())

            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Bienvenido de nuevo!!",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.85f)) // 💠 Fondo semi-transparente
                ) {
                    Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { viewModel.editEmail(it) },
                            label = { Text("Correo Electrónico") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { viewModel.editPassword(it) },
                            label = { Text("Contraseña") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        )

                        Button(
                            onClick = { viewModel.signIn() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(Color(0xFF2E8B57)) // Verde mapa
                        ) {
                            Text("Iniciar sesión", modifier = Modifier.padding(start = 8.dp), color = Color.White)
                        }
                    }
                }

                if (showError) {
                    val errorMessage = (authState as AuthState.Error).message
                    Text(
                        text = if (errorMessage!!.contains("invalid_credentials")) "Credenciales inválidas" else "Ha ocurrido un error.",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    viewModel.errorMessageShowed()
                }
            }
        }
    }
}