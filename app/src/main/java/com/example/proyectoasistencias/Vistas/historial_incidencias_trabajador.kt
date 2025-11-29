package com.example.proyectoasistencias.Vistas

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoasistencias.Modelos.ApiResponse
import com.example.proyectoasistencias.Modelos.Incidencia
import com.example.proyectoasistencias.Modelos.RetrofitClient
import com.example.proyectoasistencias.R
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class historial_incidencias_trabajador : AppCompatActivity() {

    private lateinit var listaIncidencias: ListView
    private var idTrabajador: Int = 0
    private var periodo: String = "20253"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_incidencias_trabajador)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarHistorialIncidencias)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        listaIncidencias = findViewById(R.id.listaIncidencias)

        idTrabajador = intent.getIntExtra("idTrabajador", 0)
        periodo = intent.getStringExtra("periodo") ?: "20253"

        if (idTrabajador == 0) {
            Toast.makeText(this, "Id de trabajador no recibido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        cargarIncidencias()
    }

    private fun cargarIncidencias() {
        val call = RetrofitClient.api.obtenerIncidenciasTrabajador(
            idTrabajador.toString(),
            periodo
        )

        call.enqueue(object : Callback<ApiResponse<List<Incidencia>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Incidencia>>>,
                response: Response<ApiResponse<List<Incidencia>>>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@historial_incidencias_trabajador,
                        "Error de servidor: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val body = response.body()
                if (body == null) {
                    Toast.makeText(
                        this@historial_incidencias_trabajador,
                        "Respuesta vacía",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                if (!body.ok) {
                    Toast.makeText(
                        this@historial_incidencias_trabajador,
                        body.mensaje,
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val incidencias = body.datos ?: emptyList()

                if (incidencias.isEmpty()) {
                    Toast.makeText(
                        this@historial_incidencias_trabajador,
                        "No hay incidencias registradas",
                        Toast.LENGTH_SHORT
                    ).show()
                    listaIncidencias.adapter = null
                    return
                }

                // Texto para cada fila del ListView
                val items = incidencias.map { inc ->
                    val tipo = inc.tipoIncidenciaNombre ?: "Sin tipo"
                    val desc = inc.descripcion ?: "(sin descripción)"
                    "${inc.fecha}  \n| $tipo\n$desc |"
                }

                val adaptador = ArrayAdapter(
                    this@historial_incidencias_trabajador,
                    android.R.layout.simple_list_item_1,
                    items
                )

                listaIncidencias.adapter = adaptador
            }

            override fun onFailure(call: Call<ApiResponse<List<Incidencia>>>, t: Throwable) {
                Toast.makeText(
                    this@historial_incidencias_trabajador,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
