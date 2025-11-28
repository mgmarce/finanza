package com.grupo8.appfinanza;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MostrarGastos extends AppCompatActivity {

    DatabaseHelper db;
    ListView listViewGastos;
    Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_gastos);

        db = new DatabaseHelper(this);
        listViewGastos = findViewById(R.id.listViewGastos);
        btnRegresar = findViewById(R.id.btnRegresar);

        cargarGastos();

        btnRegresar.setOnClickListener(v -> finish());
    }

    private void cargarGastos() {
        Cursor cursor = db.obtenerGastos();

        if (cursor.getCount() == 0) {
            String[] vacio = { "No hay gastos registrados." };
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, vacio);
            listViewGastos.setAdapter(adapter);
            return;
        }

        // Construimos la lista
        StringBuilder sb;
        String[] lista = new String[cursor.getCount()];
        int i = 0;

        while (cursor.moveToNext()) {
            sb = new StringBuilder();

            sb.append("Categoría: ").append(cursor.getString(1)).append("\n")
                    .append("Monto: $").append(cursor.getDouble(2)).append("\n")
                    .append("Fecha: ").append(cursor.getString(3)).append("\n")
                    .append("Hora: ").append(cursor.getString(4));

            lista[i] = sb.toString();
            i++;
        }

        // Adaptador para mostrar los datos
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, lista);

        listViewGastos.setAdapter(adapter);
    }
}