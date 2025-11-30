package com.grupo8.appfinanza;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class FormularioAgregarIngresos extends AppCompatActivity {

    private TextView tvCategoriaSeleccionadaIngreso;
    private ImageView iconCategoriaIngreso;
    private EditText etCantidadIngreso, etFechaIngreso, etHoraIngreso;
    private ImageButton btnFechaIngreso, btnHoraIngreso;
    private Button btnAgregarIngreso, btnAtras;

    DatabaseHelper db; // BASE DE DATOS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_agregar_ingresos);

        db = new DatabaseHelper(this);

        // Vincular vistas
        tvCategoriaSeleccionadaIngreso = findViewById(R.id.tvCategoriaSeleccionadaIngreso);
        iconCategoriaIngreso = findViewById(R.id.iconCategoriaIngreso);
        etCantidadIngreso = findViewById(R.id.etCantidadIngreso);
        etFechaIngreso = findViewById(R.id.etFechaIngreso);
        etHoraIngreso = findViewById(R.id.etHoraIngreso);
        btnFechaIngreso = findViewById(R.id.btnFechaIngreso);
        btnHoraIngreso = findViewById(R.id.btnHoraIngreso);
        btnAgregarIngreso = findViewById(R.id.btnAgregarIngreso);
        btnAtras = findViewById(R.id.btnAtras);

        // Recuperar datos enviados desde Ingresos.java
        String nombreIngreso = getIntent().getStringExtra("nombreIngreso");
        int iconoIngreso = getIntent().getIntExtra("iconoIngreso", R.drawable.ic_salario);

        tvCategoriaSeleccionadaIngreso.setText(nombreIngreso);
        iconCategoriaIngreso.setImageResource(iconoIngreso);

        // Botón atrás
        btnAtras.setOnClickListener(v -> finish());

        // Calendario
        btnFechaIngreso.setOnClickListener(v -> mostrarCalendario());
        etFechaIngreso.setOnClickListener(v -> mostrarCalendario());

        // Reloj
        btnHoraIngreso.setOnClickListener(v -> mostrarReloj());
        etHoraIngreso.setOnClickListener(v -> mostrarReloj());

        // Botón Agregar (guardar en SQLite)
        btnAgregarIngreso.setOnClickListener(v -> guardarIngreso());
    }

    // ====================================================
    // GUARDAR INGRESO EN LA BASE DE DATOS
    // ====================================================
    private void guardarIngreso() {

        String categoria = tvCategoriaSeleccionadaIngreso.getText().toString();
        String cantidadStr = etCantidadIngreso.getText().toString().trim();
        String fechaStr = etFechaIngreso.getText().toString().trim();
        String horaStr = etHoraIngreso.getText().toString().trim();

        // Validar campos
        if (cantidadStr.isEmpty() || fechaStr.isEmpty() || horaStr.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_LONG).show();
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(cantidadStr);
        } catch (Exception e) {
            Toast.makeText(this, "Monto inválido", Toast.LENGTH_LONG).show();
            return;
        }

        // Convertir fecha: dd/MM/yyyy → yyyy-MM-dd
        String[] partes = fechaStr.split("/");
        String fechaSql = partes[2] + "-" + partes[1] + "-" + partes[0];

        // Guardar en base de datos
        boolean insertado = db.insertarIngreso(categoria, monto, fechaSql, horaStr);

        if (insertado) {
            Toast.makeText(this, "Ingreso agregado con éxito", Toast.LENGTH_LONG).show();

            // VOLVER a la pantalla de Ingresos correctamente
            Intent intent = new Intent(FormularioAgregarIngresos.this, Ingresos.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else {
            Toast.makeText(this, "Error al guardar el ingreso", Toast.LENGTH_LONG).show();
        }
    }

    // ====================================================
    // CALENDARIO
    // ====================================================
    private void mostrarCalendario() {
        Calendar c = Calendar.getInstance();
        int año = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                FormularioAgregarIngresos.this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                (view, year, month, dayOfMonth) -> {
                    String fechaSeleccionada =
                            String.format("%02d/%02d/%04d", dayOfMonth, (month + 1), year);
                    etFechaIngreso.setText(fechaSeleccionada);
                },
                año, mes, dia
        );
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    // ====================================================
    // SELECTOR DE HORA
    // ====================================================
    private void mostrarReloj() {
        Calendar c = Calendar.getInstance();
        int hora = c.get(Calendar.HOUR_OF_DAY);
        int minuto = c.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(
                FormularioAgregarIngresos.this,
                (view, hourOfDay, minute1) ->
                        etHoraIngreso.setText(String.format("%02d:%02d", hourOfDay, minute1)),
                hora, minuto, false
        );
        dialog.show();
    }
}