package com.example.proyectoasistencias.Vistas

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoasistencias.Modelos.ApiResponse
import com.example.proyectoasistencias.Modelos.Horario
import com.example.proyectoasistencias.Modelos.RetrofitClient
import com.example.proyectoasistencias.R
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class horario_trabajador : AppCompatActivity() {

    private lateinit var listaHorario: ListView
    private var idTrabajador: Int = 0
    private var periodo: String = "20253"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horario_trabajador)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarHorarioTrabajador)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        listaHorario = findViewById(R.id.listaHorario)

        idTrabajador = intent.getIntExtra("idTrabajador", 0)
        periodo = intent.getStringExtra("periodo") ?: "20253"

        if (idTrabajador == 0) {
            Toast.makeText(this, "Id de trabajador no recibido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        cargarHorario()
    }

    private fun cargarHorario() {
        val call = RetrofitClient.api.obtenerHorarioTrabajador(
            idTrabajador.toString(),
            periodo
        )

        call.enqueue(object : Callback<ApiResponse<List<Horario>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Horario>>>,
                response: Response<ApiResponse<List<Horario>>>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@horario_trabajador,
                        "Error de servidor: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val body = response.body()
                if (body == null) {
                    Toast.makeText(
                        this@horario_trabajador,
                        "Respuesta vacía",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                if (!body.ok) {
                    Toast.makeText(
                        this@horario_trabajador,
                        body.mensaje,
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val horarios = body.datos ?: emptyList()

                if (horarios.isEmpty()) {
                    Toast.makeText(
                        this@horario_trabajador,
                        "No hay horario asignado",
                        Toast.LENGTH_SHORT
                    ).show()
                    listaHorario.adapter = null
                    return
                }

                val items = horarios.map { h ->
                    val dia = h.nombreDia ?: "Día"
                    "$dia  \n| Entrada: ${h.hEntrada}  -  Salida: ${h.hSalida} |"
                }

                val adaptador = ArrayAdapter(
                    this@horario_trabajador,
                    android.R.layout.simple_list_item_1,
                    items
                )

                listaHorario.adapter = adaptador
            }

            override fun onFailure(call: Call<ApiResponse<List<Horario>>>, t: Throwable) {
                Toast.makeText(
                    this@horario_trabajador,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
