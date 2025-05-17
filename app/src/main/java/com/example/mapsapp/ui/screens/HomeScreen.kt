package com.example.mapsapp.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng

@Composable
fun HomeScreen(navigateToRegister: () -> Unit, navigateToLogIn: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // 🌐 Fondo inspirado en mapas con líneas y puntos
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val gridColor = Color(0xFF00A8E8).copy(alpha = 0.2f)

            // Líneas simulando un mapa digital
            for (i in 1..6) {
                drawLine(gridColor, Offset(i * (size.width / 7f), 0f), Offset(i * (size.width / 7f), size.height), strokeWidth = 2f)
                drawLine(gridColor, Offset(0f, i * (size.height / 7f)), Offset(size.width, i * (size.height / 7f)), strokeWidth = 2f)
            }

            // Puntos representando nodos de un mapa digital
            for (x in 1..8) {
                for (y in 1..6) {
                    drawCircle(gridColor, radius = 5f, center = Offset(x * (size.width / 9f), y * (size.height / 7f)))
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¡Explora el mundo!",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFFD2D2D2),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(Color.Black.copy(alpha = 0.85f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { navigateToRegister() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(Color(0xFF53BAE1)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Registrarse", modifier = Modifier.padding(start = 8.dp), color = Color.Black)
                    }

                    Button(
                        onClick = { navigateToLogIn() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(Color(0xFF53BAE1)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Iniciar sesión", modifier = Modifier.padding(start = 8.dp), color = Color.Black)
                    }
                }
            }
        }
    }
}


