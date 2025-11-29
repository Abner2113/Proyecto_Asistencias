package com.example.proyectoasistencias.Modelos

import com.google.gson.annotations.SerializedName

data class Empleado(
    @SerializedName("Id_Trabajador") val idTrabajador: Int,
    @SerializedName("NombreCompleto") val nombreCompleto: String
)
