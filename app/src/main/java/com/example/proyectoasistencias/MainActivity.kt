package com.example.proyectoasistencias

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectoasistencias.Modelos.ApiResponse
import com.example.proyectoasistencias.Modelos.RetrofitClient
import com.example.proyectoasistencias.Modelos.Usuario
import com.example.proyectoasistencias.Vistas.usuario_administrador
import com.example.proyectoasistencias.Vistas.usuario_trabajador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var spPeriodo: Spinner
    private lateinit var spPuesto: Spinner

    private lateinit var edtIdTrabajador: EditText
    private lateinit var edtContrasena: EditText
    private lateinit var btnIniciarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        spPeriodo = findViewById(R.id.spPeriodo)
        spPuesto = findViewById(R.id.spPuesto)
        edtIdTrabajador = findViewById(R.id.etIdTrabajador)
        edtContrasena = findViewById(R.id.etContrasena)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)

        val opcionesPeriodo = arrayOf("20253")
        val adaptador1 = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            opcionesPeriodo
        )
        spPeriodo.adapter = adaptador1

        val opcionesPuesto = arrayOf("Empleado", "Administrador")
        val adaptador2 = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            opcionesPuesto
        )
        spPuesto.adapter = adaptador2
        btnIniciarSesion.setOnClickListener {
            hacerLogin()
        }
    }

    private fun hacerLogin() {
        val idTrabajador = edtIdTrabajador.text.toString().trim()
        val contrasena = edtContrasena.text.toString().trim()
        val periodo = spPeriodo.selectedItem.toString()
        val puesto = spPuesto.selectedItem.toString()

        if (idTrabajador.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Ingresa ID y contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        val call = RetrofitClient.api.login(periodo, puesto, idTrabajador, contrasena)

        call.enqueue(object : Callback<ApiResponse<List<Usuario>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Usuario>>>,
                response: Response<ApiResponse<List<Usuario>>>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity,
                        "Error de servidor: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val body = response.body()
                if (body == null) {
                    Toast.makeText(
                        this@MainActivity,
                        "Respuesta vacía del servidor",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                if (!body.ok) {
                    Toast.makeText(
                        this@MainActivity,
                        body.mensaje,
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val listaUsuarios = body.datos
                if (listaUsuarios.isNullOrEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        "No se encontró el usuario",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val usuario = listaUsuarios[0]
                when (usuario.rol) {
                    "Empleado" -> {
                        val intent = Intent(this@MainActivity, usuario_trabajador::class.java)
                        intent.putExtra("idTrabajador", usuario.idTrabajador)
                        intent.putExtra("nombreCompleto", usuario.nombreCompleto)
                        intent.putExtra("periodo", usuario.periodo)
                        intent.putExtra("rol", usuario.rol)

                        // NUEVOS EXTRAS
                        intent.putExtra("telefono", usuario.telefono)
                        intent.putExtra("email", usuario.email)
                        intent.putExtra("estado", usuario.estado)
                        intent.putExtra("municipio", usuario.municipio)
                        intent.putExtra("codigoPostal", usuario.codigoPostal)
                        intent.putExtra("colonia", usuario.colonia)
                        intent.putExtra("calle", usuario.calle)
                        intent.putExtra("numExterior", usuario.numExterior)
                        intent.putExtra("numInterior", usuario.numInterior)

                        startActivity(intent)
                    }

                    "Administrador" -> {
                        val intent = Intent(this@MainActivity, usuario_administrador::class.java)
                        intent.putExtra("idTrabajador", usuario.idTrabajador)
                        intent.putExtra("nombreCompleto", usuario.nombreCompleto)
                        intent.putExtra("periodo", usuario.periodo)
                        intent.putExtra("rol", usuario.rol)

                        // NUEVOS EXTRAS
                        intent.putExtra("telefono", usuario.telefono)
                        intent.putExtra("email", usuario.email)
                        intent.putExtra("estado", usuario.estado)
                        intent.putExtra("municipio", usuario.municipio)
                        intent.putExtra("codigoPostal", usuario.codigoPostal)
                        intent.putExtra("colonia", usuario.colonia)
                        intent.putExtra("calle", usuario.calle)
                        intent.putExtra("numExterior", usuario.numExterior)
                        intent.putExtra("numInterior", usuario.numInterior)

                        startActivity(intent)
                    }

                    else -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Rol no reconocido: ${usuario.rol}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }

            override fun onFailure(call: Call<ApiResponse<List<Usuario>>>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
