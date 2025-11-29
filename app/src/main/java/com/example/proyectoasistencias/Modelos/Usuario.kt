package com.example.proyectoasistencias.Modelos

import com.google.gson.annotations.SerializedName

data class Usuario(

    @SerializedName("Id_Trabajador")
    val idTrabajador: Int,

    @SerializedName("Nombre")
    val nombre: String,

    @SerializedName("Apellido_Paterno")
    val apellidoPaterno: String,

    @SerializedName("Apellido_Materno")
    val apellidoMaterno: String,

    @SerializedName("puesto")
    val rol: String,

    @SerializedName("periodo")
    val periodo: String,

    @SerializedName("nombreCompleto")
    val nombreCompletoServidor: String? = null
) {
    val nombreCompleto: String
        get() = "$nombre $apellidoPaterno $apellidoMaterno"
}
