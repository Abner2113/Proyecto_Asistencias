package com.example.proyectoasistencias.Modelos

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(

    @SerializedName("ok")
    val ok: Boolean,

    @SerializedName("mensaje")
    val mensaje: String,

    @SerializedName("datos")
    val datos: T?
)
