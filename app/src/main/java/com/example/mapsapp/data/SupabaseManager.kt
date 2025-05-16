package com.example.mapsapp.data

import android.os.Build
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.mapsapp.BuildConfig
import com.example.mapsapp.utils.AuthState
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.FileUploadResponse
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class SupabaseManager {

    lateinit var client: SupabaseClient
    lateinit var storage: Storage
    private val supabaseUrl = BuildConfig.SUPABASE_URL
    private val supabaseKey = BuildConfig.SUPABASE_KEY

    constructor() {
        client = createSupabaseClient(supabaseUrl = supabaseUrl, supabaseKey = supabaseKey) {
            install(Postgrest)
            install(Storage)
            install(Auth) { autoLoadFromStorage = true }
        }
        storage = client.storage
    }

    suspend fun signUpWithEmail(emailValue: String, passwordValue: String): AuthState {
        try {
            client.auth.signUpWith(
                provider = io.github.jan.supabase.auth.providers.builtin.Email
            ) {
                email = emailValue
                password = passwordValue
            }
            return AuthState.Authenticated
        } catch (e: Exception) {
            return AuthState.Error(e.localizedMessage)
        }
    }

    suspend fun signInWithEmail(emailValue: String, passwordValue: String): AuthState {
        try {
            client.auth.signInWith(io.github.jan.supabase.auth.providers.builtin.Email) {
                email = emailValue
                password = passwordValue
            }
            return AuthState.Authenticated
        } catch (e: Exception) {
            return AuthState.Error(e.localizedMessage)
        }
    }


    fun retrieveCurrentSession(): UserSession? {
        val session = client.auth.currentSessionOrNull()
        return session
    }

    fun refreshSession(): AuthState {
        try {
            client.auth.currentSessionOrNull()
            return AuthState.Authenticated
        } catch (e: Exception) {
            return AuthState.Error(e.localizedMessage)
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun uploadImage(imageFile: ByteArray): String {
        val fechaHoraActual = LocalDateTime.now()
        val formato = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val imageName = storage.from("images")
            .upload(path = "image_${fechaHoraActual.format(formato)}.png", data = imageFile)
        return buildImageUrl(imageFileName = imageName.path)
    }

    fun buildImageUrl(imageFileName: String) =
        "${this.supabaseUrl}/storage/v1/object/public/images/${imageFileName}"


    suspend fun getAllMarkers(): List<MarkerD> {
        return client.from("marcador").select().decodeList<MarkerD>()
    }

    suspend fun getMarker(id: Int): MarkerD {
        return client.from("marcador").select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<MarkerD>()
    }


    suspend fun insertMarker(
        title: String,
        descripcion: String,
        latitud: Double,
        longitud: Double,
        imageName: String
    ) {
        Log.d("Lindsey", "Entrar del InsertMarker")
        Log.d("Lindsey", "httpURL: ${client.supabaseHttpUrl}")
        Log.d("Lindsey", "supabaseURL: ${client.supabaseUrl}")
        Log.d("Lindsey", "key: ${client.supabaseKey}")
        val newMarker = MarkerD(
            title = title,
            descripcion = descripcion,
            image = imageName,
            latitud = latitud,
            longitud = longitud
        )
        client.from("marcador").insert(newMarker)

        Log.d("Lindsey", "Salir del InsertMarker")
    }


    suspend fun updateMarker(
        id: Int,
        title: String,
        descripcion: String,
        imageName: String,
        imageFile: ByteArray?
    ) {
        var newImageName: FileUploadResponse? = null
        if (imageFile != null) {
            newImageName = storage.from("images").update(path = imageName, data = imageFile)
        }



        client.from("marcador").update({
            set("title", title)
            set("descripcion", descripcion)
            if (newImageName != null) {
                set("image", buildImageUrl(imageFileName = newImageName!!.path))
            }
        }) { filter { eq("id", id) } }
    }

    suspend fun deleteMarker(id: Int) {
        client.from("marcador").delete { filter { eq("id", id) } }
    }

    suspend fun deleteImage(imageName: String) {
        val imgName =
            imageName.removePrefix("https://aobflzinjcljzqpxpcxs.supabase.co/storage/v1/object/public/images/")
        client.storage.from("images").delete(imgName)
    }


}
