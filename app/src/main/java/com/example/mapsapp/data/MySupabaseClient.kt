package com.example.mapsapp.data

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from

class MySupabaseClient() {
    lateinit var client: SupabaseClient

    constructor(supabaseUrl: String, supabaseKey: String) : this() {
        client = createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseKey
        ) {
            install(Postgrest)
        }
    }

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

    suspend fun insertMarker(markerD: MarkerD) {
        Log.d("Lindsey", "Entrar del InsertMarker")
        Log.d("Lindsey","httpURL: ${client.supabaseHttpUrl}")
        Log.d("Lindsey", "supabaseURL: ${client.supabaseUrl}")
        Log.d("Lindsey", "key: ${client.supabaseKey}")

        client.from("marcador").insert(markerD)

        Log.d("Lindsey", "Salir del InsertMarker")
    }

    suspend fun updateMarker(id:Int, title: String, descripcion: String, image: String) {
        client.from("marcador").update({
            set("title", title)
            set("descripcion", descripcion)
            set("image", image)
        }) { filter { eq("id", id) } }
    }

    suspend fun deleteMarker(id: Int) {
        client.from("marcador").delete { filter { eq("id", id) } }
    }


}
