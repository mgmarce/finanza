package com.grupo8.appfinanza;

import android.Manifest;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;

public class Estadisticas extends AppCompatActivity {

    private Spinner spnMeses;
    private Button btnMesActual, btnMesAnterior, btnAtras, btnGenerarReporte;
    private PieChart pieChart;
    private TextView tvTotalGastos, tvTotalIngresos, tvCatMasGasto, tvCatMasIngreso;

    private final String[] meses = {
            "Seleccionar mes",
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    private int indiceMesActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        spnMeses = findViewById(R.id.spnMeses);
        btnMesActual = findViewById(R.id.btnMesActual);
        btnMesAnterior = findViewById(R.id.btnMesAnterior);
        btnAtras = findViewById(R.id.btnAtras);
        btnGenerarReporte = findViewById(R.id.btnGenerarReporte);
        btnGenerarReporte.setOnClickListener(v -> seleccionarUbicacionTXT());


        pieChart = findViewById(R.id.pieChart);

        Calendar hoy = Calendar.getInstance();
        indiceMesActual = hoy.get(Calendar.MONTH);

        configurarSpinnerMeses();
        configurarBotones();
        configurarPieChartInicial();

        // ---- DATOS DINÁMICOS ----
        DatabaseHelper db = new DatabaseHelper(this);

        int mesActual = Calendar.getInstance().get(Calendar.MONTH) + 1;

        double totalGastos = db.obtenerTotalGastosMes(mesActual);
        double totalIngresos = db.obtenerTotalIngresosMes(mesActual);

        String catMasGasto = db.categoriaMasGasto();
        String catMasIngreso = db.categoriaMasIngreso();

        tvTotalGastos = findViewById(R.id.tvTotalGastos);
        tvTotalIngresos = findViewById(R.id.tvTotalIngresos);
        tvCatMasGasto = findViewById(R.id.tvCategoriaMasGasto);
        tvCatMasIngreso = findViewById(R.id.tvCategoriaMasIngreso);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 1);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == RESULT_OK) {

            if (data == null || data.getData() == null) {
                Toast.makeText(this, "No se seleccionó ubicación", Toast.LENGTH_SHORT).show();
                return;
            }

            android.net.Uri uri = data.getData();

            // ➤ Obtener mes seleccionado en el spinner
            int posicion = spnMeses.getSelectedItemPosition();

            if (posicion == 0) {
                Toast.makeText(this, "Seleccione un mes antes de generar reporte", Toast.LENGTH_SHORT).show();
                return;
            }

            int mesSeleccionado = posicion; // 1-12

            DatabaseHelper db = new DatabaseHelper(this);
            ArrayList<String> datos = db.obtenerReporteCompleto(mesSeleccionado);

            try {
                java.io.OutputStream outputStream = getContentResolver().openOutputStream(uri);
                StringBuilder contenido = new StringBuilder();

                contenido.append("=== REPORTE DE FINANZAS DEL MES DE ")
                        .append(meses[mesSeleccionado])
                        .append(" ===\n\n");

                for (String linea : datos) {
                    contenido.append(linea).append("\n");
                }

                outputStream.write(contenido.toString().getBytes());
                outputStream.close();

                Toast.makeText(this, "Reporte guardado correctamente", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }



    private void seleccionarUbicacionTXT() {

        // Validar mes
        int posicion = spnMeses.getSelectedItemPosition();
        if (posicion == 0) {
            Toast.makeText(this, "Seleccione un mes primero", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombreArchivo = "Reporte_" + meses[posicion] + ".txt";

        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_TITLE, nombreArchivo);

        startActivityForResult(intent, 200);
    }


    private void configurarSpinnerMeses() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, meses
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMeses.setAdapter(adapter);

        spnMeses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                if (position == 0) {
                    mostrarPieSinDatos();
                } else {
                    actualizarPieChart(position);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void configurarBotones() {
        btnMesActual.setOnClickListener(v ->
                spnMeses.setSelection(indiceMesActual + 1));

        btnMesAnterior.setOnClickListener(v -> {
            int mesAnterior = (indiceMesActual + 11) % 12;
            spnMeses.setSelection(mesAnterior + 1);
        });

        btnAtras.setOnClickListener(v -> finish());
    }

    private void configurarPieChartInicial() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setTransparentCircleRadius(0f);
        mostrarPieSinDatos();
    }

    private void mostrarPieSinDatos() {
        ArrayList<PieEntry> valores = new ArrayList<>();
        valores.add(new PieEntry(1f, "Sin datos"));

        PieDataSet dataSet = new PieDataSet(valores, "");
        dataSet.setColors(Color.LTGRAY);
        PieData data = new PieData(dataSet);
        data.setDrawValues(false);

        pieChart.setData(data);
        pieChart.invalidate();
    }

    // ---- DINÁMICO: CONSULTA BD PARA ACTUALIZAR EL PIECHART ----
    private void actualizarPieChart(int mesSeleccionado) {

        DatabaseHelper db = new DatabaseHelper(this);

        int mes = mesSeleccionado; // 1–12

        double ingresos = db.obtenerTotalIngresosMes(mes);
        double gastos = db.obtenerTotalGastosMes(mes);

        ArrayList<PieEntry> valores = new ArrayList<>();
        valores.add(new PieEntry((float) ingresos, "Ingresos"));
        valores.add(new PieEntry((float) gastos, "Gastos"));

        PieDataSet dataSet = new PieDataSet(valores, "Finanzas " + meses[mes]);
        ArrayList<Integer> colores = new ArrayList<>();
        colores.add(Color.parseColor("#4CAF50")); // verde
        colores.add(Color.parseColor("#F44336")); // rojo
        dataSet.setColors(colores);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();

        tvTotalGastos.setText("Total Gastos: $" + gastos);
        tvTotalIngresos.setText("Total Ingresos: $" + ingresos);
        tvCatMasGasto.setText("Categoría con más gastos: " + db.categoriaMasGasto());
        tvCatMasIngreso.setText("Categoría con más ingresos: " + db.categoriaMasIngreso());


        /*String resumen = meses[mes] + "\n" +
                "Ingresos: $" + ingresos + "\n" +
                "Gastos: $" + gastos + "\n" +
                "Balance: $" + (ingresos - gastos);

        Toast.makeText(this, resumen, Toast.LENGTH_LONG).show();*/
    }
}
