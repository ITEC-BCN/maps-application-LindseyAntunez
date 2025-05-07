package com.example.mapsapp

import android.app.Application
import com.example.mapsapp.data.MySupabaseClient

class MyApp: Application() {
    companion object {
        lateinit var database: MySupabaseClient
    }
    override fun onCreate() {
        super.onCreate()
        database = MySupabaseClient(
            supabaseUrl = "https://obdujrxdasdmsjlmumwl.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im9iZHVqcnhkYXNkbXNqbG11bXdsIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDY0Mzc2MTMsImV4cCI6MjA2MjAxMzYxM30.9s7eqnrbsCLHHXS1Unyb0_EJEcxLAy09kdVpXVbyWIk"
        )
    }
}
