package com.grupo8.appfinanza;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class FormAgregarGastos extends AppCompatActivity {

    private TextView tvCategoriaSeleccionada;
    private ImageView iconCategoria;
    private EditText etCantidad, etFecha, etHora;
    private ImageButton btnFecha, btnHora;
    private Button btnAgregar, btnAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_agregar_gastos);

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

        // --- Recuperar datos del gasto enviado desde MainActivity ---
        String nombreGasto = getIntent().getStringExtra("nombreGasto");
        int iconoGasto = getIntent().getIntExtra("iconoGasto", R.drawable.ic_educacion);

        tvCategoriaSeleccionada.setText(nombreGasto);
        iconCategoria.setImageResource(iconoGasto);

        // --- Acción del botón Atrás ---
        btnAtras.setOnClickListener(v -> finish());

        // --- Desplegar calendario al presionar el ícono ---
        btnFecha.setOnClickListener(v -> mostrarCalendario());
        // --- O también al tocar el campo de texto ---
        etFecha.setOnClickListener(v -> mostrarCalendario());

        // --- Desplegar selector de hora ---
        btnHora.setOnClickListener(v -> mostrarReloj());
        etHora.setOnClickListener(v -> mostrarReloj());

        // --- Botón Agregar (aquí solo cerramos por ahora) ---
        btnAgregar.setOnClickListener(v -> finish());
    }

    // Método para mostrar calendario visual
    private void mostrarCalendario() {
        Calendar c = Calendar.getInstance();
        int año = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                FormAgregarGastos.this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                (view, year, month, dayOfMonth) -> {
                    String fechaSeleccionada = String.format("%02d/%02d/%04d",
                            dayOfMonth, (month + 1), year);
                    etFecha.setText(fechaSeleccionada);
                },
                año, mes, dia
        );
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    // Método para mostrar reloj visual
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