package com.example.proyectoasistencias.Vistas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoasistencias.R
import com.google.android.material.appbar.MaterialToolbar

class mapa_navegacion : AppCompatActivity() {

    private var idTrabajador: Int = 0
    private var nombreCompleto: String = ""
    private var periodo: String = "20253"
    private var rol: String = "Administrador"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa_navegacion)

        idTrabajador = intent.getIntExtra("idTrabajador", 0)
        nombreCompleto = intent.getStringExtra("nombreCompleto") ?: ""
        periodo = intent.getStringExtra("periodo") ?: "20253"
        rol = intent.getStringExtra("rol") ?: "Administrador"

        val btnIrPerfilAdmin      = findViewById<Button>(R.id.btnIrPerfilAdmin)
        val btnIrAsistenciasAdmin = findViewById<Button>(R.id.btnIrAsistenciasAdmin)
        val btnIrIncidenciasAdmin = findViewById<Button>(R.id.btnIrIncidenciasAdmin)
        val btnIrHorariosAdmin    = findViewById<Button>(R.id.btnIrHorariosAdmin)

        btnIrPerfilAdmin.setOnClickListener {
            finish()
        }

        btnIrAsistenciasAdmin.setOnClickListener {
            irA(historial_asistencias_admin::class.java)
            finish()
        }

        btnIrIncidenciasAdmin.setOnClickListener {
            irA(historial_incidencias_admin::class.java)
            finish()
        }

        btnIrHorariosAdmin.setOnClickListener {
            irA(horarios_admin::class.java)
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