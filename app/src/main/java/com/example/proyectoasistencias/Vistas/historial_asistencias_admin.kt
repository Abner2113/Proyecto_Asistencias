package com.example.proyectoasistencias.Vistas

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoasistencias.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.proyectoasistencias.Modelos.ApiResponse
import com.example.proyectoasistencias.Modelos.Asistencia
import com.example.proyectoasistencias.Modelos.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.graphics.Color
import android.content.DialogInterface

class historial_asistencias_admin : AppCompatActivity() {

    private lateinit var etBuscar: EditText
    private lateinit var btnBuscar: Button
    private lateinit var btnActualizar: Button
    private lateinit var btnLimpiar: Button
    private lateinit var listaAsistencias: ListView

    private var periodo: String = "20253"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_asistencias_admin)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        etBuscar = findViewById(R.id.etBuscar)
        btnBuscar = findViewById(R.id.btnBuscar)
        btnActualizar = findViewById(R.id.btnActualizar)
        btnLimpiar = findViewById(R.id.btnLimpiar)
        listaAsistencias = findViewById(R.id.listaAsistencias)

        btnBuscar.setOnClickListener {
            val idTexto = etBuscar.text.toString().trim()

            if (idTexto.isEmpty()) {
                Toast.makeText(this, "Ingresa el número de trabajador", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            cargarAsistencias(idTexto)
        }

        btnActualizar.setOnClickListener {
            cargarAsistencias("0")
        }

        btnLimpiar.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(this)
                .setTitle("Confirmar")
                .setMessage("¿Seguro que deseas eliminar TODO el historial de asistencias?")
                .setPositiveButton("Sí") { d, _ ->
                    limpiarHistorial()
                    d.dismiss()
                }
                .setNegativeButton("Cancelar") { d, _ ->
                    d.dismiss()
                }
                .show()

            dialog.getButton(DialogInterface.BUTTON_POSITIVE)?.setTextColor(Color.BLACK)
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE)?.setTextColor(Color.BLACK)
        }
        cargarAsistencias("0")
    }

    private fun cargarAsistencias(idTrabajador: String) {
        val call = RetrofitClient.api.obtenerAsistenciasTrabajador(
            idTrabajador,
            periodo
        )

        call.enqueue(object : Callback<ApiResponse<List<Asistencia>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Asistencia>>>,
                response: Response<ApiResponse<List<Asistencia>>>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@historial_asistencias_admin,
                        "Error de servidor: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val body = response.body()
                if (body == null) {
                    Toast.makeText(
                        this@historial_asistencias_admin,
                        "Respuesta vacía",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                if (!body.ok) {
                    Toast.makeText(
                        this@historial_asistencias_admin,
                        body.mensaje,
                        Toast.LENGTH_SHORT
                    ).show()
                    listaAsistencias.adapter = null
                    return
                }

                val asistencias = body.datos ?: emptyList()

                if (asistencias.isEmpty()) {
                    Toast.makeText(
                        this@historial_asistencias_admin,
                        "No hay asistencias para mostrar",
                        Toast.LENGTH_SHORT
                    ).show()
                    listaAsistencias.adapter = null
                    return
                }

                val items = asistencias.map { a ->
                    "Trabajador: ${a.idTrabajador}\n" +
                            "${a.fecha}  |  Entrada: ${a.hEntrada}  -  Salida: ${a.hSalida}"
                }

                val adaptador = ArrayAdapter(
                    this@historial_asistencias_admin,
                    android.R.layout.simple_list_item_1,
                    items
                )

                listaAsistencias.adapter = adaptador
            }

            override fun onFailure(call: Call<ApiResponse<List<Asistencia>>>, t: Throwable) {
                Toast.makeText(
                    this@historial_asistencias_admin,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun limpiarHistorial() {
        val call = RetrofitClient.api.limpiarHistorialAsistencias("1")

        call.enqueue(object : Callback<ApiResponse<Any>> {
            override fun onResponse(
                call: Call<ApiResponse<Any>>,
                response: Response<ApiResponse<Any>>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@historial_asistencias_admin,
                        "Error de servidor: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val body = response.body()
                if (body == null) {
                    Toast.makeText(
                        this@historial_asistencias_admin,
                        "Respuesta vacía al limpiar",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                if (!body.ok) {
                    Toast.makeText(
                        this@historial_asistencias_admin,
                        body.mensaje,
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                listaAsistencias.adapter = null
                Toast.makeText(
                    this@historial_asistencias_admin,
                    body.mensaje,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) {
                Toast.makeText(
                    this@historial_asistencias_admin,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
