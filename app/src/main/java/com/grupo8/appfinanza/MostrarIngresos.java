package com.grupo8.appfinanza;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MostrarIngresos extends AppCompatActivity {

    DatabaseHelper db;
    ListView listViewIngresos;
    Button btnRegresarIngresos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_ingresos);

        db = new DatabaseHelper(this);
        listViewIngresos = findViewById(R.id.listViewIngresos);
        btnRegresarIngresos = findViewById(R.id.btnRegresarIngresos);

        cargarIngresos();

        btnRegresarIngresos.setOnClickListener(v -> finish());
    }

    /*private void cargarIngresos() {
        Cursor cursor = db.obtenerIngresos();   // ← método del DatabaseHelper

        if (cursor.getCount() == 0) {
            String[] vacio = { "No hay ingresos registrados." };
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    vacio
            );
            listViewIngresos.setAdapter(adapter);
            return;
        }

        String[] lista = new String[cursor.getCount()];
        int i = 0;

        while (cursor.moveToNext()) {
            String categoria = cursor.getString(1);
            double monto = cursor.getDouble(2);
            String fecha = cursor.getString(3);
            String hora = cursor.getString(4);

            lista[i] =
                    "Categoría: " + categoria + "\n" +
                            "Monto: $" + monto + "\n" +
                            "Fecha: " + fecha + "\n" +
                            "Hora: " + hora;
            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                lista
        );
        listViewIngresos.setAdapter(adapter);
    }*/

    private void cargarIngresos() {
        Cursor cursor = db.obtenerIngresos();

        if (cursor.getCount() == 0) {
            String[] vacio = { "No hay ingresos registrados." };
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, vacio);
            listViewIngresos.setAdapter(adapter);
            return;
        }

        int total = cursor.getCount();

        String[] categorias = new String[total];
        String[] montos = new String[total];
        String[] fechas = new String[total];

        int i = 0;

        while (cursor.moveToNext()) {
            categorias[i] = cursor.getString(1);
            montos[i] = String.valueOf(cursor.getDouble(2));
            fechas[i] = cursor.getString(3);
            i++;
        }

        IngresoAdapter adapter = new IngresoAdapter(this, categorias, montos, fechas);
        listViewIngresos.setAdapter(adapter);
    }

}