package com.example.mapsapp.data

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
        return client.from("MarkerD").select().decodeList<MarkerD>()
    }

    suspend fun getMarker(id: Int): MarkerD {
        return client.from("MarkerD").select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<MarkerD>()
    }

    suspend fun insertMarker(markerD: MarkerD) {
        client.from("MarkerD").insert(markerD)
    }

    suspend fun updateMarker(id:Int, title: String, descripcion: String, image: String) {
        client.from("MarkerD").update({
            set("title", title)
            set("descripcion", descripcion)
            set("image", image)
        }) { filter { eq("id", id) } }
    }

    suspend fun deleteMarker(id: Int) {
        client.from("MarkerD").delete { filter { eq("id", id) } }
    }


}
