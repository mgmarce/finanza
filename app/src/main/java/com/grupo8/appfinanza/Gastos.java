package com.grupo8.appfinanza;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AppCompatActivity;

public class Gastos extends AppCompatActivity {
    Button btnGastos, btnIngresos, btnAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos);

        btnGastos = findViewById(R.id.btnGastos);
        btnIngresos = findViewById(R.id.btnIngresos);
        btnAtras = findViewById(R.id.btnAtras);

        // Desactivar botón Gastos al inicio (porque ya está en esta pantalla)
        btnGastos.setEnabled(false);
        btnGastos.setAlpha(0.5f); // efecto visual de desactivado

        // Botón Ingresos → abre la segunda Activity
        btnIngresos.setOnClickListener(v -> {
            Intent intent = new Intent(Gastos.this, Ingresos.class);
            startActivity(intent);
        });

        // Botón Atrás → cierra la app o vuelve al menú anterior si lo hubiera
        btnAtras.setOnClickListener(v -> finish());

        // Ejemplo para botón Educación
        Button btnEducacion = findViewById(R.id.btnEducacionGastos); // o el ID que uses
        btnEducacion.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormAgregarGastos.class);
            intent.putExtra("nombreGasto", "Educación");
            intent.putExtra("iconoGasto", R.drawable.ic_educacion);
            startActivity(intent);
        });

        // Salud
        Button btnSalud = findViewById(R.id.btnSaludGasto);
        btnSalud.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormAgregarGastos.class);
            intent.putExtra("nombreGasto", "Salud");
            intent.putExtra("iconoGasto", R.drawable.ic_salud);
            startActivity(intent);
        });

        // --- Supermercado ---
        Button btnSupermercado = findViewById(R.id.btnSupermercadoGasto);
        btnSupermercado.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormAgregarGastos.class);
            intent.putExtra("nombreGasto", "Supermercado");
            intent.putExtra("iconoGasto", R.drawable.ic_supermercado);
            startActivity(intent);
        });

// --- Entretenimiento ---
        Button btnEntretenimiento = findViewById(R.id.btnEntretenimientoGasto);
        btnEntretenimiento.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormAgregarGastos.class);
            intent.putExtra("nombreGasto", "Entretenimiento");
            intent.putExtra("iconoGasto", R.drawable.ic_entretenimiento);
            startActivity(intent);
        });

// --- Combustible ---
        Button btnCombustible = findViewById(R.id.btnCombustibleGasto);
        btnCombustible.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormAgregarGastos.class);
            intent.putExtra("nombreGasto", "Combustible");
            intent.putExtra("iconoGasto", R.drawable.ic_combustible);
            startActivity(intent);
        });

// --- Internet ---
        Button btnInternet = findViewById(R.id.btnInternetIGastos);
        btnInternet.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormAgregarGastos.class);
            intent.putExtra("nombreGasto", "Internet");
            intent.putExtra("iconoGasto", R.drawable.ic_internet);
            startActivity(intent);
        });

// --- Recibo de Luz ---
        Button btnLuz = findViewById(R.id.btnReciboLuzGasto);
        btnLuz.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormAgregarGastos.class);
            intent.putExtra("nombreGasto", "Recibo de Luz");
            intent.putExtra("iconoGasto", R.drawable.ic_luz);
            startActivity(intent);
        });
    }
}