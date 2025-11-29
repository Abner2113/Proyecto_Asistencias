package com.example.proyectoasistencias.Vistas

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoasistencias.Modelos.ApiResponse
import com.example.proyectoasistencias.Modelos.Empleado
import com.example.proyectoasistencias.Modelos.RetrofitClient
import com.example.proyectoasistencias.R
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class horarios_admin : AppCompatActivity() {

    private lateinit var etIdTrabajador: EditText
    private lateinit var btnIrEditarHorario: Button
    private lateinit var listaEmpleados: ListView

    private var empleados: List<Empleado> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horarios_admin)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarHorarioAdmin)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        etIdTrabajador = findViewById(R.id.etIdTrabajadorHorarioAdmin)
        btnIrEditarHorario = findViewById(R.id.btnIrEditarHorario)
        listaEmpleados = findViewById(R.id.listaEmpleadosHorarioAdmin)

        cargarEmpleados()

        btnIrEditarHorario.setOnClickListener {
            val idTexto = etIdTrabajador.text.toString().trim()

            if (idTexto.isEmpty()) {
                Toast.makeText(this, "Ingresa el número de trabajador", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val idInt = idTexto.toIntOrNull()
            if (idInt == null) {
                Toast.makeText(this, "El ID debe ser numérico", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, horarios_usuarios_admin::class.java)
            intent.putExtra("idTrabajador", idInt)
            startActivity(intent)
        }

        listaEmpleados.setOnItemClickListener { _, _, position, _ ->
            val empleadoSeleccionado = empleados[position]
            etIdTrabajador.setText(empleadoSeleccionado.idTrabajador.toString())
            Toast.makeText(
                this,
                "Seleccionado: ${empleadoSeleccionado.nombreCompleto}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun cargarEmpleados() {
        val call = RetrofitClient.api.obtenerEmpleados("1")

        call.enqueue(object : Callback<ApiResponse<List<Empleado>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Empleado>>>,
                response: Response<ApiResponse<List<Empleado>>>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@horarios_admin,
                        "Error de servidor: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val body = response.body()
                if (body == null) {
                    Toast.makeText(
                        this@horarios_admin,
                        "Respuesta vacía",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                if (!body.ok) {
                    Toast.makeText(
                        this@horarios_admin,
                        body.mensaje,
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                empleados = body.datos ?: emptyList()

                if (empleados.isEmpty()) {
                    Toast.makeText(
                        this@horarios_admin,
                        "No hay empleados registrados",
                        Toast.LENGTH_SHORT
                    ).show()
                    listaEmpleados.adapter = null
                    return
                }

                val items = empleados.map { e ->
                    "${e.idTrabajador}  -  ${e.nombreCompleto}"
                }

                val adaptador = ArrayAdapter(
                    this@horarios_admin,
                    android.R.layout.simple_list_item_1,
                    items
                )

                listaEmpleados.adapter = adaptador
            }

            override fun onFailure(call: Call<ApiResponse<List<Empleado>>>, t: Throwable) {
                Toast.makeText(
                    this@horarios_admin,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
