package com.example.proyectoasistencias.Vistas

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoasistencias.R
import com.google.android.material.appbar.MaterialToolbar

class usuario_administrador : AppCompatActivity() {


    private lateinit var txtBienvenidaAdmin: TextView
    private lateinit var txtIdTrabajadorAdmin: TextView
    private lateinit var txtRolAdmin: TextView
    private lateinit var txtPeriodoAdmin: TextView

    private var idTrabajador: Int = 0
    private var nombreCompleto: String = ""
    private var rol: String = ""
    private var periodo: String = "20253"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario_administrador)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarAdmin)
        setSupportActionBar(toolbar)

        txtBienvenidaAdmin = findViewById(R.id.txtBienvenidaAdmin)
        txtIdTrabajadorAdmin = findViewById(R.id.txtIdTrabajadorAdmin)
        txtRolAdmin = findViewById(R.id.txtRolAdmin)
        txtPeriodoAdmin = findViewById(R.id.txtPeriodoAdmin)

        idTrabajador = intent.getIntExtra("idTrabajador", 0)
        nombreCompleto = intent.getStringExtra("nombreCompleto") ?: ""
        rol = intent.getStringExtra("rol") ?: "Administrador"
        periodo = intent.getStringExtra("periodo") ?: "20253"

        if (nombreCompleto.isNotEmpty()) {
            txtBienvenidaAdmin.text = "¡Bienvenido, $nombreCompleto!"
        } else {
            txtBienvenidaAdmin.text = "¡Bienvenido, Administrador!"
        }

        txtIdTrabajadorAdmin.text = "ID trabajador: $idTrabajador"
        txtRolAdmin.text = "Rol: $rol"
        txtPeriodoAdmin.text = "Periodo actual: $periodo"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_opciones, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.opcion_perfil -> {
                true
            }
            R.id.opcion_usuarios -> {
                val intent = Intent(this, historial_usuarios_admin::class.java)
                startActivity(intent)
                true
            }
            R.id.opcion_asistencias -> {
                val intent = Intent(this, historial_asistencias_admin::class.java)
                startActivity(intent)
                true
            }
            R.id.opcion_incidencias -> {
                val intent = Intent(this, historial_incidencias_admin::class.java)
                startActivity(intent)
                true
            }
            R.id.opcion_horarios -> {
                val intent = Intent(this, horarios_admin::class.java)
                startActivity(intent)
                true
            }
            R.id.opcion_cerrar_sesion -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}