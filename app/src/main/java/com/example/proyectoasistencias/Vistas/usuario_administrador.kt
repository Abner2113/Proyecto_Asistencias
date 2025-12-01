package com.example.proyectoasistencias.Vistas

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
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

    // NUEVOS CAMPOS
    private var telefono: String = ""
    private var email: String = ""
    private var estado: String = ""
    private var municipio: String = ""
    private var codigoPostal: String = ""
    private var colonia: String = ""
    private var calle: String = ""
    private var numExterior: String = ""
    private var numInterior: String? = null

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

        telefono = intent.getStringExtra("telefono") ?: ""
        email = intent.getStringExtra("email") ?: ""
        estado = intent.getStringExtra("estado") ?: ""
        municipio = intent.getStringExtra("municipio") ?: ""
        codigoPostal = intent.getStringExtra("codigoPostal") ?: ""
        colonia = intent.getStringExtra("colonia") ?: ""
        calle = intent.getStringExtra("calle") ?: ""
        numExterior = intent.getStringExtra("numExterior") ?: ""
        numInterior = intent.getStringExtra("numInterior")

        txtBienvenidaAdmin.text = nombreCompleto

        val info = buildString {
            appendLine("ID trabajador: $idTrabajador")
            appendLine("Rol: $rol")
            appendLine("Periodo actual: $periodo")
            appendLine()
            appendLine("Teléfono: $telefono")
            appendLine("Email: $email")
            appendLine()
            appendLine("Dirección:")
            appendLine("${calle} #$numExterior")
            appendLine("Col. $colonia, C.P. $codigoPostal")
            appendLine("$municipio, $estado")
            appendLine("Num. interior: ${numInterior ?: "N/A"}")
        }

        txtIdTrabajadorAdmin.text = info

        txtRolAdmin.visibility = View.GONE
        txtPeriodoAdmin.visibility = View.GONE
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
            R.id.menuMapaNavegacionAdmin -> {
                val intent = Intent(this, mapa_navegacion::class.java)
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