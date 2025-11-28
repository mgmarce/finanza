package com.grupo8.appfinanza;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.view.View;
import android.content.res.ColorStateList;

public class Categorias extends AppCompatActivity {
    private EditText etNombre;
    private RadioButton rbtnIngreso, rbtnGasto;
    private Button btnGuardar, btnSalir;
    private ImageView[] iconos; // arreglo de íconos
    private int iconoSeleccionado = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        etNombre = findViewById(R.id.editTextText);
        rbtnIngreso = findViewById(R.id.rbtnIngresoCategorias);
        rbtnGasto = findViewById(R.id.rbtnGastoCategorias);
        btnGuardar = findViewById(R.id.btnGuardarCategoria);
        btnSalir = findViewById(R.id.btnSalir);

        iconos = new ImageView[]{
                findViewById(R.id.imgCompras),
                findViewById(R.id.imgBanco),
                findViewById(R.id.imgHogar),
                findViewById(R.id.imgTransporte),
                findViewById(R.id.imgComida),
                findViewById(R.id.imgEntretenimiento),
                findViewById(R.id.imgSalud),
                findViewById(R.id.imgEducacion),
                findViewById(R.id.imgServicios),
                findViewById(R.id.imgMascotas),
                findViewById(R.id.imgRegalos),
                findViewById(R.id.imgImpuestos)
        };
        for (int i = 0; i < iconos.length; i++) {
            final int index = i; // índice del ícono actual
            iconos[i].setOnClickListener(v -> seleccionarIcono(index));
        }
        btnGuardar.setOnClickListener(v -> guardarCategoria());
        btnSalir.setOnClickListener(v -> finish());
    }

    private void seleccionarIcono(int index) {
        // Primero, limpiar la selección anterior
        for (int i = 0; i < iconos.length; i++) {
            iconos[i].setBackgroundResource(R.drawable.bg_icon_normal);
            iconos[i].setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_dark)));
        }

        // Luego marcar el nuevo ícono seleccionado
        iconos[index].setBackgroundResource(R.drawable.bg_icon_selected);
        iconos[index].setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

        // Guardar el índice del ícono seleccionado
        iconoSeleccionado = index;
    }

    private void guardarCategoria() {
        String nombre = etNombre.getText().toString().trim();
        String tipo = rbtnIngreso.isChecked() ? "Ingreso" : "Gasto";

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa un nombre de categoría", Toast.LENGTH_SHORT).show();
            return;
        }

        if (iconoSeleccionado == -1) {
            Toast.makeText(this, "Selecciona un ícono", Toast.LENGTH_SHORT).show();
            return;
        }

        // 💾 Guardar categoría usando SharedPreferences
        CategoriaManager.guardarCategoria(this, nombre, tipo, iconoSeleccionado);

        Toast.makeText(this,
                "Categoría guardada correctamente", Toast.LENGTH_SHORT).show();

        finish(); // salir y volver atrás
    }
}