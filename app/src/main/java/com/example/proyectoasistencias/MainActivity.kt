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
import com.example.proyectoasistencias.Contratos.LoginContract
import com.example.proyectoasistencias.Modelos.RetrofitClient
import com.example.proyectoasistencias.Modelos.Usuario
import com.example.proyectoasistencias.Presentadores.LoginPresenter
import com.example.proyectoasistencias.Vistas.usuario_administrador
import com.example.proyectoasistencias.Vistas.usuario_trabajador

class MainActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var spPeriodo: Spinner
    private lateinit var spPuesto: Spinner

    private lateinit var edtIdTrabajador: EditText
    private lateinit var edtContrasena: EditText
    private lateinit var btnIniciarSesion: Button

    private lateinit var presenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Vincular vistas
        spPeriodo = findViewById(R.id.spPeriodo)
        spPuesto = findViewById(R.id.spPuesto)
        edtIdTrabajador = findViewById(R.id.etIdTrabajador)
        edtContrasena = findViewById(R.id.etContrasena)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)

        // Llenar spinner de periodo
        val opcionesPeriodo = arrayOf("20253")
        val adaptador1 = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            opcionesPeriodo
        )
        spPeriodo.adapter = adaptador1

        // Llenar spinner de puesto
        val opcionesPuesto = arrayOf("Empleado", "Administrador")
        val adaptador2 = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            opcionesPuesto
        )
        spPuesto.adapter = adaptador2

        // Inicializar presenter (usa tu RetrofitClient.api existente)
        presenter = LoginPresenter(this, RetrofitClient.api)

        // Click del botón usando el presenter
        btnIniciarSesion.setOnClickListener {
            presenter.onClickIniciarSesion()
        }
    }

    // =======================
    // Implementación de LoginContract.View
    // =======================

    override fun obtenerIdTrabajador(): String = edtIdTrabajador.text.toString()

    override fun obtenerContrasena(): String = edtContrasena.text.toString()

    override fun obtenerPeriodo(): String = spPeriodo.selectedItem.toString()

    override fun obtenerPuesto(): String = spPuesto.selectedItem.toString()

    override fun mostrarProgreso(mostrar: Boolean) {
        btnIniciarSesion.isEnabled = !mostrar
    }

    override fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun navegarAUsuarioTrabajador(usuario: Usuario) {
        val intent = Intent(this, usuario_trabajador::class.java)

        intent.putExtra("idTrabajador", usuario.idTrabajador)
        intent.putExtra("nombreCompleto", usuario.nombreCompleto)
        intent.putExtra("periodo", usuario.periodo)
        intent.putExtra("rol", usuario.rol)

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

    override fun navegarAUsuarioAdministrador(usuario: Usuario) {
        val intent = Intent(this, usuario_administrador::class.java)

        intent.putExtra("idTrabajador", usuario.idTrabajador)
        intent.putExtra("nombreCompleto", usuario.nombreCompleto)
        intent.putExtra("periodo", usuario.periodo)
        intent.putExtra("rol", usuario.rol)

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

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
