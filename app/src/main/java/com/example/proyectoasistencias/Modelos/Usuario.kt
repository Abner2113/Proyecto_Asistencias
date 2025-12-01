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
    val nombreCompletoServidor: String? = null,

    @SerializedName("Telefono")
    val telefono: String,

    @SerializedName("Email")
    val email: String,

    @SerializedName("Estado")
    val estado: String,

    @SerializedName("Municipio")
    val municipio: String,

    @SerializedName("CodigoPostal")
    val codigoPostal: String,

    @SerializedName("Colonia")
    val colonia: String,

    @SerializedName("Calle")
    val calle: String,

    @SerializedName("NumExterior")
    val numExterior: String,

    @SerializedName("NumInterior")
    val numInterior: String?
) {
    // Este lo sigues usando en MainActivity
    val nombreCompleto: String
        get() = "$nombre $apellidoPaterno $apellidoMaterno"
}
