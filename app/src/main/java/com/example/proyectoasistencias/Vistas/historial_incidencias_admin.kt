package com.example.proyectoasistencias.Vistas

import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoasistencias.Modelos.ApiResponse
import com.example.proyectoasistencias.Modelos.Incidencia
import com.example.proyectoasistencias.Modelos.RetrofitClient
import com.example.proyectoasistencias.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class historial_incidencias_admin : AppCompatActivity() {

    private lateinit var etBuscarIncidencias: EditText
    private lateinit var btnBuscarIncidencias: Button
    private lateinit var btnActualizarIncidencias: Button
    private lateinit var btnLimpiarIncidencias: Button
    private lateinit var listaIncidenciasAdmin: ListView

    private var periodo: String = "20253"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_incidencias_admin)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarIncidenciasAdmin)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        etBuscarIncidencias = findViewById(R.id.etBuscarIncidencias)
        btnBuscarIncidencias = findViewById(R.id.btnBuscarIncidencias)
        btnActualizarIncidencias = findViewById(R.id.btnActualizarIncidencias)
        btnLimpiarIncidencias = findViewById(R.id.btnLimpiarIncidencias)
        listaIncidenciasAdmin = findViewById(R.id.listaIncidenciasAdmin)

        btnBuscarIncidencias.setOnClickListener {
            val idTexto = etBuscarIncidencias.text.toString().trim()
            if (idTexto.isEmpty()) {
                Toast.makeText(this, "Ingresa el número de trabajador", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            cargarIncidencias(idTexto)
        }

        btnActualizarIncidencias.setOnClickListener {
            cargarIncidencias("0")
        }

        btnLimpiarIncidencias.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(this)
                .setTitle("Confirmar")
                .setMessage("¿Seguro que deseas eliminar TODO el historial de incidencias?")
                .setPositiveButton("Sí") { d, _ ->
                    limpiarHistorialIncidencias()
                    d.dismiss()
                }
                .setNegativeButton("Cancelar") { d, _ ->
                    d.dismiss()
                }
                .show()

            dialog.getButton(android.content.DialogInterface.BUTTON_POSITIVE)
                ?.setTextColor(Color.BLACK)
            dialog.getButton(android.content.DialogInterface.BUTTON_NEGATIVE)
                ?.setTextColor(Color.BLACK)
        }
        cargarIncidencias("0")
    }

    private fun cargarIncidencias(idTrabajador: String) {
        val call = RetrofitClient.api.obtenerIncidenciasTrabajador(
            idTrabajador,
            periodo
        )

        call.enqueue(object : Callback<ApiResponse<List<Incidencia>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Incidencia>>>,
                response: Response<ApiResponse<List<Incidencia>>>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@historial_incidencias_admin,
                        "Error de servidor: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val body = response.body()
                if (body == null) {
                    Toast.makeText(
                        this@historial_incidencias_admin,
                        "Respuesta vacía",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                if (!body.ok) {
                    Toast.makeText(
                        this@historial_incidencias_admin,
                        body.mensaje,
                        Toast.LENGTH_SHORT
                    ).show()
                    listaIncidenciasAdmin.adapter = null
                    return
                }

                val incidencias = body.datos ?: emptyList()

                if (incidencias.isEmpty()) {
                    Toast.makeText(
                        this@historial_incidencias_admin,
                        "No hay incidencias para mostrar",
                        Toast.LENGTH_SHORT
                    ).show()
                    listaIncidenciasAdmin.adapter = null
                    return
                }

                val items = incidencias.map { inc ->
                    val tipo = inc.tipoIncidenciaNombre ?: "Sin tipo"
                    val desc = inc.descripcion ?: "(sin descripción)"
                    "Trabajador: ${inc.idTrabajador}\n" +
                            "${inc.fecha}  |  $tipo\n$desc"
                }

                val adaptador = ArrayAdapter(
                    this@historial_incidencias_admin,
                    android.R.layout.simple_list_item_1,
                    items
                )

                listaIncidenciasAdmin.adapter = adaptador
            }

            override fun onFailure(call: Call<ApiResponse<List<Incidencia>>>, t: Throwable) {
                Toast.makeText(
                    this@historial_incidencias_admin,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun limpiarHistorialIncidencias() {
        val call = RetrofitClient.api.limpiarHistorialIncidencias("1")

        call.enqueue(object : Callback<ApiResponse<Any>> {
            override fun onResponse(
                call: Call<ApiResponse<Any>>,
                response: Response<ApiResponse<Any>>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@historial_incidencias_admin,
                        "Error de servidor: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val body = response.body()
                if (body == null) {
                    Toast.makeText(
                        this@historial_incidencias_admin,
                        "Respuesta vacía al limpiar",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                if (!body.ok) {
                    Toast.makeText(
                        this@historial_incidencias_admin,
                        body.mensaje,
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                listaIncidenciasAdmin.adapter = null
                Toast.makeText(
                    this@historial_incidencias_admin,
                    body.mensaje,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) {
                Toast.makeText(
                    this@historial_incidencias_admin,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
