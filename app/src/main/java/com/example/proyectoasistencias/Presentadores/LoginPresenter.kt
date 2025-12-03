package com.example.proyectoasistencias.Presentadores

import com.example.proyectoasistencias.Contratos.LoginContract
import com.example.proyectoasistencias.Modelos.ApiResponse
import com.example.proyectoasistencias.Modelos.ApiServer
import com.example.proyectoasistencias.Modelos.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter(
    private var view: LoginContract.View?,
    private val api: ApiServer
) : LoginContract.Presenter {

    override fun onClickIniciarSesion() {
        val v = view ?: return

        val idTrabajador = v.obtenerIdTrabajador().trim()
        val contrasena = v.obtenerContrasena().trim()
        val periodo = v.obtenerPeriodo()
        val puesto = v.obtenerPuesto()

        if (idTrabajador.isEmpty() || contrasena.isEmpty()) {
            v.mostrarError("Ingresa ID y contraseña")
            return
        }

        v.mostrarProgreso(true)

        val call = api.login(periodo, puesto, idTrabajador, contrasena)
        call.enqueue(object : Callback<ApiResponse<List<Usuario>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Usuario>>>,
                response: Response<ApiResponse<List<Usuario>>>
            ) {
                val vista = view ?: return
                vista.mostrarProgreso(false)

                if (!response.isSuccessful) {
                    vista.mostrarError("Error de respuesta del servidor")
                    return
                }

                val body = response.body()
                if (body == null) {
                    vista.mostrarError("Respuesta vacía del servidor")
                    return
                }

                if (!body.ok) {
                    vista.mostrarError(body.mensaje)
                    return
                }

                val listaUsuarios = body.datos
                if (listaUsuarios.isNullOrEmpty()) {
                    vista.mostrarError("No se encontró el usuario")
                    return
                }

                val usuario = listaUsuarios[0]

                when (usuario.rol) {
                    "Empleado" -> vista.navegarAUsuarioTrabajador(usuario)
                    "Administrador" -> vista.navegarAUsuarioAdministrador(usuario)
                    else -> vista.mostrarError("Rol desconocido: ${usuario.rol}")
                }
            }

            override fun onFailure(
                call: Call<ApiResponse<List<Usuario>>>,
                t: Throwable
            ) {
                val vista = view ?: return
                vista.mostrarProgreso(false)
                vista.mostrarError("Error de conexión: ${t.message}")
            }
        })
    }

    override fun onDestroy() {
        view = null
    }
}
