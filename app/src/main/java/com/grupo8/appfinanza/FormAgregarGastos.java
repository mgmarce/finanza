package com.grupo8.appfinanza;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class FormAgregarGastos extends AppCompatActivity {

    private TextView tvCategoriaSeleccionada;
    private ImageView iconCategoria;
    private EditText etCantidad, etFecha, etHora;
    private ImageButton btnFecha, btnHora;
    private Button btnAgregar, btnAtras;

    DatabaseHelper db;  // ← NUEVO: Base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_agregar_gastos);

        // --- Instancia de la base de datos ---
        db = new DatabaseHelper(this);

        // --- Vincular vistas ---
        tvCategoriaSeleccionada = findViewById(R.id.tvCategoriaSeleccionada);
        iconCategoria = findViewById(R.id.iconCategoria);
        etCantidad = findViewById(R.id.etCantidad);
        etFecha = findViewById(R.id.etFecha);
        etHora = findViewById(R.id.etHora);
        btnFecha = findViewById(R.id.btnFecha);
        btnHora = findViewById(R.id.btnHora);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnAtras = findViewById(R.id.btnAtras);

        // --- Recuperar datos enviados desde Gastos ---
        String nombreGasto = getIntent().getStringExtra("nombreGasto");
        int iconoGasto = getIntent().getIntExtra("iconoGasto", R.drawable.ic_educacion);

        tvCategoriaSeleccionada.setText(nombreGasto);
        iconCategoria.setImageResource(iconoGasto);

        // --- Botón Atrás ---
        btnAtras.setOnClickListener(v -> finish());

        // --- Calendario ---
        btnFecha.setOnClickListener(v -> mostrarCalendario());
        etFecha.setOnClickListener(v -> mostrarCalendario());

        // --- Reloj ---
        btnHora.setOnClickListener(v -> mostrarReloj());
        etHora.setOnClickListener(v -> mostrarReloj());

        // --- BOTÓN AGREGAR ---
        btnAgregar.setOnClickListener(v -> guardarGasto());
    }

    // ----------------------------------------------------------
    // MÉTODO PARA GUARDAR EN LA BASE DE DATOS
    // ----------------------------------------------------------
    private void guardarGasto() {

        String categoria = tvCategoriaSeleccionada.getText().toString();
        String cantidadStr = etCantidad.getText().toString().trim();
        String fechaStr = etFecha.getText().toString().trim();
        String horaStr = etHora.getText().toString().trim();

        // Validaciones
        if (cantidadStr.isEmpty() || fechaStr.isEmpty() || horaStr.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_LONG).show();
            return;
        }

        double cantidad;
        try {
            cantidad = Double.parseDouble(cantidadStr);
        } catch (Exception e) {
            Toast.makeText(this, "Cantidad inválida", Toast.LENGTH_LONG).show();
            return;
        }

        // Convertir fecha de dd/MM/yyyy → yyyy-MM-dd
        String[] partes = fechaStr.split("/");
        String fechaSql = partes[2] + "-" + partes[1] + "-" + partes[0];

        // Guardar en SQLite
        boolean insertado = db.insertarGasto(categoria, cantidad, fechaSql, horaStr);

        if (insertado) {
            Toast.makeText(this, "Gasto agregado con éxito", Toast.LENGTH_LONG).show();
            finish(); // volver a la pantalla anterior
        } else {
            Toast.makeText(this, "Error al guardar el gasto", Toast.LENGTH_LONG).show();
        }
    }

    // ----------------------------------------------------------
    // FECHA
    // ----------------------------------------------------------
    private void mostrarCalendario() {
        Calendar c = Calendar.getInstance();
        int año = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                FormAgregarGastos.this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                (view, year, month, dayOfMonth) -> {
                    String fecha = String.format("%02d/%02d/%04d",
                            dayOfMonth, (month + 1), year);
                    etFecha.setText(fecha);
                },
                año, mes, dia
        );
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    // ----------------------------------------------------------
    // HORA
    // ----------------------------------------------------------
    private void mostrarReloj() {
        Calendar c = Calendar.getInstance();
        int hora = c.get(Calendar.HOUR_OF_DAY);
        int minuto = c.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(
                FormAgregarGastos.this,
                (view, hourOfDay, minute1) ->
                        etHora.setText(String.format("%02d:%02d", hourOfDay, minute1)),
                hora, minuto, false
        );
        dialog.show();
    }
}