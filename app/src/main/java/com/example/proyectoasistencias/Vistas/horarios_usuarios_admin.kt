package com.example.proyectoasistencias.Vistas

import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectoasistencias.Modelos.ApiResponse
import com.example.proyectoasistencias.Modelos.Horario
import com.example.proyectoasistencias.Modelos.RetrofitClient
import com.example.proyectoasistencias.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class horarios_usuarios_admin : AppCompatActivity() {

    private lateinit var tvIdTrabajador: TextView
    private lateinit var spinnerDia: Spinner
    private lateinit var spinnerPeriodo: Spinner
    private lateinit var etHoraEntrada: EditText
    private lateinit var etHoraSalida: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnRegresar: Button
    private lateinit var listaHorariosListView: ListView

    private var idTrabajador: Int = 0
    private var periodoSeleccionado: String = "20253"

    private val listaHorarios = mutableListOf<Horario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_horarios_usuarios_admin)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvIdTrabajador = findViewById(R.id.tvIdTrabajador)
        spinnerDia = findViewById(R.id.spinnerDia)
        spinnerPeriodo = findViewById(R.id.spinnerPeriodo)
        etHoraEntrada = findViewById(R.id.etHoraEntrada)
        etHoraSalida = findViewById(R.id.etHoraSalida)
        btnGuardar = findViewById(R.id.btnBuscar)
        btnRegresar = findViewById(R.id.regresar)
        listaHorariosListView = findViewById(R.id.listaEmpleadosHorarioAdmin)

        etHoraEntrada.setOnClickListener {
            mostrarTimePicker(etHoraEntrada)
        }

        etHoraSalida.setOnClickListener {
            mostrarTimePicker(etHoraSalida)
        }

        idTrabajador = intent.getIntExtra("idTrabajador", 0)

        if (idTrabajador == 0) {
            Toast.makeText(this, "Id de trabajador no recibido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        tvIdTrabajador.text = idTrabajador.toString()

        configurarSpinners()

        listaHorariosListView.setOnItemClickListener { _, _, position, _ ->
            if (position in listaHorarios.indices) {
                val h = listaHorarios[position]
                val dia = h.nombreDia ?: ""
                seleccionarDiaEnSpinner(dia)
                etHoraEntrada.setText(h.hEntrada)
                etHoraSalida.setText(h.hSalida)
            }
        }

        cargarHorario()

        btnGuardar.setOnClickListener {
            val diaSeleccionado = spinnerDia.selectedItem?.toString() ?: ""
            val periodo = spinnerPeriodo.selectedItem?.toString() ?: "20253"
            val hEntrada = etHoraEntrada.text.toString().trim()
            val hSalida = etHoraSalida.text.toString().trim()

            if (diaSeleccionado.isEmpty() || hEntrada.isEmpty() || hSalida.isEmpty()) {
                Toast.makeText(this, "Completa día y horas de entrada/salida", Toast.LENGTH_SHORT)
                    .show()
            } else {
                guardarHorarioEnServidor(
                    idTrabajador = idTrabajador,
                    periodo = periodo,
                    dia = diaSeleccionado,
                    hEntrada = hEntrada,
                    hSalida = hSalida
                )
            }
        }

        btnRegresar.setOnClickListener {
            finish()
        }
    }

    private fun configurarSpinners() {

        val dias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")
        val adaptadorDias = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            dias
        )
        adaptadorDias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDia.adapter = adaptadorDias


        val periodos = listOf("20253")
        val adaptadorPeriodos = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            periodos
        )
        adaptadorPeriodos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPeriodo.adapter = adaptadorPeriodos

        spinnerPeriodo.setSelection(0)
        periodoSeleccionado = "20253"
    }

    private fun seleccionarDiaEnSpinner(dia: String) {
        val count = spinnerDia.adapter?.count ?: 0
        for (i in 0 until count) {
            val item = spinnerDia.adapter.getItem(i)?.toString()
            if (item.equals(dia, ignoreCase = true)) {
                spinnerDia.setSelection(i)
                return
            }
        }
    }

    private fun cargarHorario() {
        periodoSeleccionado = spinnerPeriodo.selectedItem?.toString() ?: "20253"

        val call = RetrofitClient.api.obtenerHorarioTrabajador(
            idTrabajador.toString(),
            periodoSeleccionado
        )

        call.enqueue(object : Callback<ApiResponse<List<Horario>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Horario>>>,
                response: Response<ApiResponse<List<Horario>>>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@horarios_usuarios_admin,
                        "Error de servidor: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val body = response.body()
                if (body == null) {
                    Toast.makeText(
                        this@horarios_usuarios_admin,
                        "Respuesta vacía",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                if (!body.ok) {
                    Toast.makeText(
                        this@horarios_usuarios_admin,
                        body.mensaje,
                        Toast.LENGTH_SHORT
                    ).show()
                    listaHorarios.clear()
                    listaHorariosListView.adapter = null
                    return
                }

                val horarios = body.datos ?: emptyList()

                if (horarios.isEmpty()) {
                    Toast.makeText(
                        this@horarios_usuarios_admin,
                        "No hay horario asignado para este trabajador",
                        Toast.LENGTH_SHORT
                    ).show()
                    listaHorarios.clear()
                    listaHorariosListView.adapter = null
                    return
                }

                listaHorarios.clear()
                listaHorarios.addAll(horarios)

                val itemsTexto = listaHorarios.map { h ->
                    val dia = h.nombreDia ?: "Día"
                    "$dia  |  Entrada: ${h.hEntrada}  -  Salida: ${h.hSalida}"
                }

                val adapter = ArrayAdapter(
                    this@horarios_usuarios_admin,
                    android.R.layout.simple_list_item_1,
                    itemsTexto
                )
                listaHorariosListView.adapter = adapter
            }

            override fun onFailure(call: Call<ApiResponse<List<Horario>>>, t: Throwable) {
                Toast.makeText(
                    this@horarios_usuarios_admin,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun guardarHorarioEnServidor(
        idTrabajador: Int,
        periodo: String,
        dia: String,
        hEntrada: String,
        hSalida: String
    ) {
        val call = RetrofitClient.api.actualizarHorarioTrabajador(
            idTrabajador.toString(),
            periodo,
            dia,
            hEntrada,
            hSalida
        )

        call.enqueue(object : Callback<ApiResponse<Any>> {
            override fun onResponse(
                call: Call<ApiResponse<Any>>,
                response: Response<ApiResponse<Any>>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@horarios_usuarios_admin,
                        "Error de servidor: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val body = response.body()
                if (body == null) {
                    Toast.makeText(
                        this@horarios_usuarios_admin,
                        "Respuesta vacía del servidor",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                if (body.ok) {
                    Toast.makeText(
                        this@horarios_usuarios_admin,
                        body.mensaje,
                        Toast.LENGTH_SHORT
                    ).show()
                    cargarHorario()
                } else {
                    Toast.makeText(
                        this@horarios_usuarios_admin,
                        body.mensaje,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) {
                Toast.makeText(
                    this@horarios_usuarios_admin,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun mostrarTimePicker(editText: EditText) {
        val cal = java.util.Calendar.getInstance()
        val horaActual = cal.get(java.util.Calendar.HOUR_OF_DAY)
        val minutoActual = cal.get(java.util.Calendar.MINUTE)

        val dialog = android.app.TimePickerDialog(
            this@horarios_usuarios_admin,
            { _, hourOfDay, minute ->
                val horaFormateada = String.format("%02d:%02d", hourOfDay, minute)
                editText.setText(horaFormateada)
            },
            horaActual,
            minutoActual,
            true
        )
        dialog.show()
    }
}
