package com.grupo8.appfinanza;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class Ingresos extends AppCompatActivity {

    Button btnGastos, btnIngresos, btnAtras, btnVerIngresos;

    // Íconos válidos para ingresos (4)
    private int[] iconosIngresos = {
            R.drawable.ic_salario,
            R.drawable.ic_ingresos_extra,
            R.drawable.ic_negocios,
            R.drawable.ic_otros
    };

    LinearLayout contenedorIngresos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresos);

        btnGastos = findViewById(R.id.btnGastos);
        btnIngresos = findViewById(R.id.btnIngresos);
        btnAtras = findViewById(R.id.btnAtras);
        btnVerIngresos = findViewById(R.id.btnVerIngresos);

        contenedorIngresos = findViewById(R.id.layoutIngresos);

        if (contenedorIngresos == null) {
            return; // evita crash si el ID no existe
        }

        // Desactivar Ingresos (estás aquí)
        btnIngresos.setEnabled(false);
        btnIngresos.setAlpha(0.5f);

        // Ir a Gastos
        btnGastos.setOnClickListener(v -> {
            startActivity(new Intent(Ingresos.this, Gastos.class));
            finish();
        });

        // Ver ingresos registrados
        btnVerIngresos.setOnClickListener(v -> {
            startActivity(new Intent(Ingresos.this, MostrarIngresos.class));
        });

        btnAtras.setOnClickListener(v -> finish());

        // -------------------------
        // Categorías Fijas
        // -------------------------
        findViewById(R.id.btnSalarioIngresos).setOnClickListener(v -> abrirFormulario("Salario", R.drawable.ic_salario));
        findViewById(R.id.btnIngresosExtras).setOnClickListener(v -> abrirFormulario("Ingresos Extra", R.drawable.ic_ingresos_extra));
        findViewById(R.id.btnNegociosIngresos).setOnClickListener(v -> abrirFormulario("Negocios", R.drawable.ic_negocios));
        findViewById(R.id.btnOtrosIngresos).setOnClickListener(v -> abrirFormulario("Otros", R.drawable.ic_otros));

        // -------------------------
        // Categorías dinámicas
        // -------------------------
        cargarCategoriasDinamicas();
    }


    // Abre formulario de agregar ingresos
    private void abrirFormulario(String nombre, int icono) {
        Intent intent = new Intent(this, FormularioAgregarIngresos.class);
        intent.putExtra("nombreIngreso", nombre);
        intent.putExtra("iconoIngreso", icono);
        startActivity(intent);
    }


    // Leer categorías desde memoria
    private void cargarCategoriasDinamicas() {
        try {
            JSONArray categorias = CategoriaManager.obtenerCategorias(this);

            for (int i = 0; i < categorias.length(); i++) {
                JSONObject obj = categorias.getJSONObject(i);

                if (!obj.getString("tipo").equals("Ingreso"))
                    continue;

                int iconIndex = obj.getInt("icono");

                // 🟢 ARREGLO IMPORTANTE: Validación de índice
                if (iconIndex < 0 || iconIndex >= iconosIngresos.length)
                    iconIndex = 0;

                agregarCategoriaDinamica(obj.getString("nombre"), iconIndex);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Crea visualmente una categoría dinámica
    private void agregarCategoriaDinamica(String nombre, int iconIndex) {

        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.HORIZONTAL);
        item.setPadding(0, 16, 0, 16);
        item.setGravity(android.view.Gravity.CENTER_VERTICAL);

        // Icono
        ImageView img = new ImageView(this);
        img.setImageResource(iconosIngresos[iconIndex]);
        img.setLayoutParams(new LinearLayout.LayoutParams(60, 60));

        // Botón
        Button btn = new Button(this);
        btn.setText(nombre);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        params.setMargins(16, 0, 0, 0);
        btn.setLayoutParams(params);

        btn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.gray_light)));
        btn.setTextColor(getColor(R.color.black));
        btn.setAllCaps(false);
        btn.setPadding(20, 20, 20, 20);
        btn.setTextSize(14);

        btn.setOnClickListener(v -> abrirFormulario(nombre, iconosIngresos[iconIndex]));

        item.addView(img);
        item.addView(btn);
        contenedorIngresos.addView(item);
    }
}