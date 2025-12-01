package com.example.proyectoasistencias.Vistas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoasistencias.R
import com.google.android.material.appbar.MaterialToolbar

class mapa_navegacion_trabajador : AppCompatActivity() {

    private var idTrabajador: Int = 0
    private var nombreCompleto: String = ""
    private var periodo: String = "20253"
    private var rol: String = "Empleado"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa_navegacion_trabajador)

        idTrabajador   = intent.getIntExtra("idTrabajador", 0)
        nombreCompleto = intent.getStringExtra("nombreCompleto") ?: ""
        periodo        = intent.getStringExtra("periodo") ?: "20253"
        rol            = intent.getStringExtra("rol") ?: "Empleado"

        val btnIrPerfilTrabajador      = findViewById<Button>(R.id.btnIrPerfilTrabajador)
        val btnIrAsistenciasTrabajador = findViewById<Button>(R.id.btnIrAsistenciasTrabajador)
        val btnIrIncidenciasTrabajador = findViewById<Button>(R.id.btnIrIncidenciasTrabajador)
        val btnIrHorarioTrabajador     = findViewById<Button>(R.id.btnIrHorarioTrabajador)

        btnIrPerfilTrabajador.setOnClickListener {
            finish()
        }

        btnIrAsistenciasTrabajador.setOnClickListener {
            irA(historial_asistencias_trabajador::class.java)
            finish()
        }

        btnIrIncidenciasTrabajador.setOnClickListener {
            irA(historial_incidencias_trabajador::class.java)
            finish()
        }

        btnIrHorarioTrabajador.setOnClickListener {
            irA(horario_trabajador::class.java)
            finish()
        }
    }
    private fun irA(destino: Class<*>) {
        val intent = Intent(this, destino)
        intent.putExtra("idTrabajador", idTrabajador)
        intent.putExtra("nombreCompleto", nombreCompleto)
        intent.putExtra("periodo", periodo)
        intent.putExtra("rol", rol)
        startActivity(intent)
    }
}