package com.grupo8.appfinanza;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class Gastos extends AppCompatActivity {

    Button btnGastos, btnIngresos, btnAtras, btnMostrarGastos;

    // 🔥 Íconos base de gastos (los que ya tienes en XML)
    private int[] iconosGastos = {
            R.drawable.ic_salud,
            R.drawable.ic_supermercado,
            R.drawable.ic_entretenimiento,
            R.drawable.ic_combustible,
            R.drawable.ic_educacion,
            R.drawable.ic_internet,
            R.drawable.ic_luz
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos);

        // -------------------------
        // FINDVIEWBYID
        // -------------------------
        btnGastos = findViewById(R.id.btnGastos);
        btnIngresos = findViewById(R.id.btnIngresos);
        btnAtras = findViewById(R.id.btnAtras);
        btnMostrarGastos = findViewById(R.id.btnMostrarGastos);

        // -------------------------
        // BOTONES SUPERIORES
        // -------------------------
        btnGastos.setEnabled(false);
        btnGastos.setAlpha(0.5f);

        btnIngresos.setOnClickListener(v -> {
            Intent intent = new Intent(Gastos.this, Ingresos.class);
            startActivity(intent);
        });

        btnAtras.setOnClickListener(v -> finish());

        btnMostrarGastos.setOnClickListener(v -> {
            Intent intent = new Intent(Gastos.this, MostrarGastos.class);
            startActivity(intent);
        });

        // -------------------------
        // CATEGORÍAS ESTÁTICAS
        // -------------------------

        findViewById(R.id.btnEducacionGastos).setOnClickListener(v -> abrirFormulario("Educación", R.drawable.ic_educacion));
        findViewById(R.id.btnSaludGasto).setOnClickListener(v -> abrirFormulario("Salud", R.drawable.ic_salud));
        findViewById(R.id.btnSupermercadoGasto).setOnClickListener(v -> abrirFormulario("Supermercado", R.drawable.ic_supermercado));
        findViewById(R.id.btnEntretenimientoGasto).setOnClickListener(v -> abrirFormulario("Entretenimiento", R.drawable.ic_entretenimiento));
        findViewById(R.id.btnCombustibleGasto).setOnClickListener(v -> abrirFormulario("Combustible", R.drawable.ic_combustible));
        findViewById(R.id.btnInternetIGastos).setOnClickListener(v -> abrirFormulario("Internet", R.drawable.ic_internet));
        findViewById(R.id.btnReciboLuzGasto).setOnClickListener(v -> abrirFormulario("Recibo de Luz", R.drawable.ic_luz));

        // -------------------------
        // 🔥 CARGAR CATEGORÍAS NUEVAS DESDE JSON
        // -------------------------
        cargarCategoriasDinamicas();
    }

    // ------------------------------------
    // ABRIR FORMULARIO PARA CUALQUIER GASTO
    // ------------------------------------
    private void abrirFormulario(String nombre, int icono) {
        Intent intent = new Intent(this, FormAgregarGastos.class);
        intent.putExtra("nombreGasto", nombre);
        intent.putExtra("iconoGasto", icono);
        startActivity(intent);
    }

    // ------------------------------------
    // 🔥 CARGAR TODAS LAS CATEGORÍAS DINÁMICAS GUARDADAS
    // ------------------------------------
    private void cargarCategoriasDinamicas() {
        try {
            JSONArray categorias = CategoriaManager.obtenerCategorias(this);

            for (int i = 0; i < categorias.length(); i++) {
                JSONObject obj = categorias.getJSONObject(i);

                if (obj.getString("tipo").equals("Gasto")) {
                    agregarCategoriaDinamica(
                            obj.getString("nombre"),
                            obj.getInt("icono")
                    );
                }
            }

        } catch (Exception ignored) {}
    }

    // ------------------------------------
    // 🔥 AGREGAR UNA CATEGORÍA NUEVA AL LAYOUT
    // ------------------------------------
    private void agregarCategoriaDinamica(String nombre, int icono) {

        LinearLayout contenedor = findViewById(R.id.layoutGastos);
        // cambia a layoutIngresos si estás en ingresos

        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.HORIZONTAL);
        item.setPadding(0, 16, 0, 16);
        item.setGravity(Gravity.CENTER_VERTICAL);

        // 🔹 Icono
        ImageView img = new ImageView(this);
        img.setImageResource(iconosGastos[icono]); // O iconosIngresos[]
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(60, 60);
        img.setLayoutParams(iconParams);

        // 🔹 Botón estilo idéntico a los del XML
        Button btn = new Button(this);
        btn.setText(nombre);

        // Ancho = wrap, alto = wrap, peso = 1
        LinearLayout.LayoutParams btnParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        btnParams.setMargins(16, 0, 0, 0); // igual que visualmente en XML
        btn.setLayoutParams(btnParams);

        // Colores iguales
        btn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.gray_light)));
        btn.setTextColor(getColor(R.color.black));

        // Fuente & estilo igual
        btn.setTextSize(14);
        btn.setAllCaps(false);
        btn.setTypeface(btn.getTypeface(), android.graphics.Typeface.BOLD);

        btn.setPadding(20, 20, 20, 20); // 🔥 hace visible el texto
        btn.setMinHeight(120); // 🔥 altura igual que los botones originales
        btn.setGravity(android.view.Gravity.CENTER); // 🔥 centra el texto


        // Acción del botón
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormAgregarGastos.class);
            intent.putExtra("nombreGasto", nombre);
            intent.putExtra("iconoGasto", iconosGastos[icono]);
            startActivity(intent);
        });

        // Añadir al layout
        item.addView(img);
        item.addView(btn);
        contenedor.addView(item);
    }
}