package com.example.proyectoasistencias.Vistas

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoasistencias.Modelos.ApiResponse
import com.example.proyectoasistencias.Modelos.Asistencia
import com.example.proyectoasistencias.Modelos.RetrofitClient
import com.example.proyectoasistencias.R
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class historial_asistencias_trabajador : AppCompatActivity() {
    private lateinit var listaAsistencias: ListView
    private var idTrabajador: Int = 0
    private var periodo: String = "20253"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_asistencias_trabajador)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarHistorialAsistencias)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        listaAsistencias = findViewById(R.id.listaAsistencias)

        idTrabajador = intent.getIntExtra("idTrabajador", 0)
        periodo = intent.getStringExtra("periodo") ?: "20253"

        if (idTrabajador == 0) {
            Toast.makeText(this, "Id de trabajador no recibido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        cargarAsistencias()
    }

    private fun cargarAsistencias() {
        val call = RetrofitClient.api.obtenerAsistenciasTrabajador(
            idTrabajador.toString(),
            periodo
        )

        call.enqueue(object : Callback<ApiResponse<List<Asistencia>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Asistencia>>>,
                response: Response<ApiResponse<List<Asistencia>>>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@historial_asistencias_trabajador,
                        "Error de servidor: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val body = response.body()
                if (body == null) {
                    Toast.makeText(
                        this@historial_asistencias_trabajador,
                        "Respuesta vacía",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                if (!body.ok) {
                    Toast.makeText(
                        this@historial_asistencias_trabajador,
                        body.mensaje,
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val asistencias = body.datos ?: emptyList()

                if (asistencias.isEmpty()) {
                    Toast.makeText(
                        this@historial_asistencias_trabajador,
                        "No hay asistencias registradas",
                        Toast.LENGTH_SHORT
                    ).show()
                    listaAsistencias.adapter = null
                    return
                }

                val items = asistencias.map { a ->
                    "${a.fecha}  \n| Entrada: ${a.hEntrada}  -  Salida: ${a.hSalida} |"
                }

                val adaptador = ArrayAdapter(
                    this@historial_asistencias_trabajador,
                    android.R.layout.simple_list_item_1,
                    items
                )

                listaAsistencias.adapter = adaptador
            }

            override fun onFailure(call: Call<ApiResponse<List<Asistencia>>>, t: Throwable) {
                Toast.makeText(
                    this@historial_asistencias_trabajador,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}