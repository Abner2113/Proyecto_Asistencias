package com.example.proyectoasistencias.Vistas

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    private lateinit var txtBienvenida: TextView
    private lateinit var txtIdTrabajador: TextView
    private lateinit var txtRolTrabajador: TextView
    private lateinit var txtPeriodo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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

        txtBienvenida.text = "¡Bienvenido, $nombreCompleto!"
        txtIdTrabajador.text = "ID trabajador: $idTrabajador"
        txtRolTrabajador.text = "Rol: $rol"
        txtPeriodo.text = "Periodo actual: $periodo"
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
