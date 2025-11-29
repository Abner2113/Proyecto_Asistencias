package com.example.proyectoasistencias.Modelos

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL =
        "https://net06usa.nethostingsac.com/~bcorpsov/ProyectoAsistencias/php/"

    val api: ApiServer by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServer::class.java)
    }
}