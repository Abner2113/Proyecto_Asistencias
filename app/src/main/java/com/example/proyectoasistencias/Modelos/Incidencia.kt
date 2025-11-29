package com.example.proyectoasistencias.Modelos

import com.google.gson.annotations.SerializedName

data class Incidencia(

    @SerializedName("Id_Incidencia")
    val idIncidencia: Int,

    @SerializedName("Id_Trabajador")
    val idTrabajador: Int,

    @SerializedName("fecha")
    val fecha: String, // "YYYY-MM-DD"

    @SerializedName("descripcion")
    val descripcion: String?,

    @SerializedName("id_tipoIncidencia")
    val idTipoIncidencia: Int,

    @SerializedName("tipoIncidenciaNombre")
    val tipoIncidenciaNombre: String?
)
