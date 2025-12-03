package com.example.proyectoasistencias.Contratos

import com.example.proyectoasistencias.Modelos.Usuario

interface LoginContract {

    interface View {
        fun obtenerIdTrabajador(): String
        fun obtenerContrasena(): String
        fun obtenerPeriodo(): String
        fun obtenerPuesto(): String

        fun mostrarProgreso(mostrar: Boolean)
        fun mostrarError(mensaje: String)
        fun navegarAUsuarioTrabajador(usuario: Usuario)
        fun navegarAUsuarioAdministrador(usuario: Usuario)
    }

    interface Presenter {
        fun onClickIniciarSesion()
        fun onDestroy()
    }
}
