package com.example.proyectoasistencias.Vistas

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectoasistencias.MainActivity
import com.example.proyectoasistencias.R
import com.google.android.material.appbar.MaterialToolbar

class usuario_trabajador : AppCompatActivity() {


    private var idTrabajador: Int = 0
    private var nombreCompleto: String = ""
    private var periodo: String = "20253"
    private var rol: String = ""

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

    private lateinit var txtBienvenida: TextView
    private lateinit var txtIdTrabajador: TextView
    private lateinit var txtRolTrabajador: TextView
    private lateinit var txtPeriodo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario_trabajador)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtBienvenida = findViewById(R.id.txtBienvenida)
        txtIdTrabajador = findViewById(R.id.txtIdTrabajador)
        txtRolTrabajador = findViewById(R.id.txtRolTrabajador)
        txtPeriodo = findViewById(R.id.txtPeriodo)

        idTrabajador = intent.getIntExtra("idTrabajador", 0)
        nombreCompleto = intent.getStringExtra("nombreCompleto") ?: ""
        periodo = intent.getStringExtra("periodo") ?: "20253"
        rol = intent.getStringExtra("rol") ?: "Empleado"

        telefono = intent.getStringExtra("telefono") ?: ""
        email = intent.getStringExtra("email") ?: ""
        estado = intent.getStringExtra("estado") ?: ""
        municipio = intent.getStringExtra("municipio") ?: ""
        codigoPostal = intent.getStringExtra("codigoPostal") ?: ""
        colonia = intent.getStringExtra("colonia") ?: ""
        calle = intent.getStringExtra("calle") ?: ""
        numExterior = intent.getStringExtra("numExterior") ?: ""
        numInterior = intent.getStringExtra("numInterior")

        txtBienvenida.text = nombreCompleto

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

        txtIdTrabajador.text = info

        txtRolTrabajador.visibility = View.GONE
        txtPeriodo.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_opciones_trabajador, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.opcion_perfil_trabajador -> {
                true
            }

            R.id.opcion_MisAsistencias -> {
                val intent = Intent(this, historial_asistencias_trabajador::class.java)
                intent.putExtra("idTrabajador", idTrabajador)
                intent.putExtra("periodo", periodo)
                startActivity(intent)
                true
            }

            R.id.opcion_MisIncidencias -> {
                val intent = Intent(this, historial_incidencias_trabajador::class.java)
                intent.putExtra("idTrabajador", idTrabajador)
                intent.putExtra("periodo", periodo)
                startActivity(intent)
                true
            }

            R.id.opcion_MiHorario -> {
                val intent = Intent(this, horario_trabajador::class.java)
                intent.putExtra("idTrabajador", idTrabajador)
                intent.putExtra("periodo", periodo)
                startActivity(intent)
                true
            }
            R.id.menuMapaNavegacion -> {
                val intent = Intent(this, mapa_navegacion_trabajador::class.java)
                intent.putExtra("idTrabajador", idTrabajador)
                intent.putExtra("periodo", periodo)
                startActivity(intent)
                true
            }

            R.id.opcion_cerrar_sesion -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finishAffinity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
