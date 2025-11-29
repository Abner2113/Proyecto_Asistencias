package com.example.proyectoasistencias.Vistas

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectoasistencias.Modelos.ApiResponse
import com.example.proyectoasistencias.Modelos.EmpleadoApp
import com.example.proyectoasistencias.Modelos.RetrofitClient
import com.example.proyectoasistencias.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class historial_usuarios_admin : AppCompatActivity() {

    private lateinit var listaUsuariosAdmin: ListView
    private val listaEmpleados = mutableListOf<EmpleadoApp>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historial_usuarios_admin)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarHistorialUsuarios)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_historial_usuarios_admin)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listaUsuariosAdmin = findViewById(R.id.listaUsuariosAdmin)

        cargarUsuarios()
    }

    private fun cargarUsuarios() {
        val call = RetrofitClient.api.obtenerUsuariosAdmin()

        call.enqueue(object : Callback<ApiResponse<List<EmpleadoApp>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<EmpleadoApp>>>,
                response: Response<ApiResponse<List<EmpleadoApp>>>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@historial_usuarios_admin,
                        "Error de servidor: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val body = response.body()
                if (body == null) {
                    Toast.makeText(
                        this@historial_usuarios_admin,
                        "Respuesta vacía",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                if (!body.ok) {
                    Toast.makeText(
                        this@historial_usuarios_admin,
                        body.mensaje,
                        Toast.LENGTH_SHORT
                    ).show()
                    listaEmpleados.clear()
                    listaUsuariosAdmin.adapter = null
                    return
                }

                val datos = body.datos ?: emptyList()

                if (datos.isEmpty()) {
                    Toast.makeText(
                        this@historial_usuarios_admin,
                        "No hay usuarios registrados",
                        Toast.LENGTH_SHORT
                    ).show()
                    listaEmpleados.clear()
                    listaUsuariosAdmin.adapter = null
                    return
                }

                listaEmpleados.clear()
                listaEmpleados.addAll(datos)

                val itemsTexto = listaEmpleados.map { e ->
                    // Lo que se muestra en la lista
                    val puesto = e.puesto ?: "Sin puesto"
                    "${e.idTrabajador} - ${e.nombreCompleto} ($puesto)"
                }

                val adapter = ArrayAdapter(
                    this@historial_usuarios_admin,
                    android.R.layout.simple_list_item_1,
                    itemsTexto
                )

                listaUsuariosAdmin.adapter = adapter
            }

            override fun onFailure(call: Call<ApiResponse<List<EmpleadoApp>>>, t: Throwable) {
                Toast.makeText(
                    this@historial_usuarios_admin,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
