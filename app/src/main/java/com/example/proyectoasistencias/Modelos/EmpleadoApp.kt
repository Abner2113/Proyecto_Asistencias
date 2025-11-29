package com.example.proyectoasistencias.Modelos

import com.google.gson.annotations.SerializedName

data class EmpleadoApp(
    @SerializedName("idTrabajador") val idTrabajador: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellidoP") val apellidoP: String,
    @SerializedName("apellidoM") val apellidoM: String,
    @SerializedName("puesto") val puesto: String?
) {
    val nombreCompleto: String
        get() = "$nombre $apellidoP $apellidoM".trim()
}
