package com.example.proyectoasistencias.Modelos

import com.google.gson.annotations.SerializedName

data class TipoIncidencia(

    @SerializedName("id_tipoIncidencia")
    val idTipoIncidencia: Int,

    @SerializedName("Nombre")
    val nombre: String
)
