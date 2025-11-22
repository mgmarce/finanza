package com.grupo8.appfinanza;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Ingresos extends AppCompatActivity {

    Button btnGastos, btnIngresos, btnAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresos);

        btnGastos = findViewById(R.id.btnGastos);
        btnIngresos = findViewById(R.id.btnIngresos);
        btnAtras = findViewById(R.id.btnAtras);

        // Desactivar botón Ingresos (ya estás en esta pantalla)
        btnIngresos.setEnabled(false);
        btnIngresos.setAlpha(0.5f);

        // Botón Gastos → volver a MainActivity
        btnGastos.setOnClickListener(v -> {
            Intent intent = new Intent(Ingresos.this, MainActivity.class);
            // finish() antes para evitar apilar activities
            finish();
            startActivity(intent);
        });

        // Botón Atrás → regresar a la pantalla anterior
        btnAtras.setOnClickListener(v -> finish());
    }
}