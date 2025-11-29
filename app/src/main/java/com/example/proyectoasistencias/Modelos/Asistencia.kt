package com.example.proyectoasistencias.Modelos

import com.google.gson.annotations.SerializedName

data class Asistencia(

    @SerializedName("Id_Asistencia")
    val idAsistencia: Int,

    @SerializedName("Id_Trabajador")
    val idTrabajador: Int,

    @SerializedName("H_Entrada")
    val hEntrada: String,   // "HH:MM:SS"

    @SerializedName("H_Salida")
    val hSalida: String,    // "HH:MM:SS"

    @SerializedName("fecha")
    val fecha: String       // "YYYY-MM-DD"
)
