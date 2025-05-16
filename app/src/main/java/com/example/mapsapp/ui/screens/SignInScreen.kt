package com.example.mapsapp.ui.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.data.MarkerD
import com.example.mapsapp.utils.AuthState
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.example.mapsapp.viewmodels.AuthViewModel
import com.example.mapsapp.viewmodels.AuthViewModelFactory


@Composable
fun SignInScreen(navigateToHome: () -> Unit){
    val context = LocalContext.current
    val viewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(SharedPreferencesHelper(context)))
    val authState by viewModel.authState.observeAsState()
    val showError by viewModel.showError.observeAsState(false)
    if(authState == AuthState.Authenticated){
        navigateToHome()
    }
    else{
        if (showError) {
            val errorMessage = (authState as AuthState.Error).message
            if (errorMessage!!.contains("invalid_credentials")) {
                Toast.makeText(context, "Invalid credentials", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "An error has ocurred", Toast.LENGTH_LONG).show()
            }
            viewModel.errorMessageShowed()
        }
//Construim Interfície d'Usuari

    }


}