package com.example.proyectoasistencias.Modelos

import com.google.gson.annotations.SerializedName

data class Horario(

    @SerializedName("Id_Horario")
    val idHorario: Int,

    @SerializedName("Id_Trabajador")
    val idTrabajador: Int,

    @SerializedName("H_Entrada")
    val hEntrada: String,   // "HH:MM:SS"

    @SerializedName("H_Salida")
    val hSalida: String,    // "HH:MM:SS"

    @SerializedName("dia")
    val nombreDia: String?,

    @SerializedName("Clave_Periodo")
    val clavePeriodo: String?
)
