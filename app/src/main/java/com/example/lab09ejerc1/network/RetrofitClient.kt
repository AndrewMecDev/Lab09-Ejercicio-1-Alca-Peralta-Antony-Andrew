package com.example.lab09ejerc1.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Objeto singleton para crear y gestionar la instancia de Retrofit.
object RetrofitClient {
    // URL base de la API de DummyJSON
    private const val BASE_URL = "https://dummyjson.com/"

    // Creación perezosa (lazy) de la instancia de Retrofit.
    // Se creará solo una vez cuando se acceda por primera vez.
    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            // Usa Gson para convertir la respuesta JSON a objetos Kotlin
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Crea una implementación de la interfaz ApiService
        retrofit.create(ApiService::class.java)
    }
}
