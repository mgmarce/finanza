package com.grupo8.appfinanza;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Estadisticas extends AppCompatActivity {

    private Spinner spnMeses;
    private Button btnMesActual, btnMesAnterior, btnAtras;
    private View viewGraficaPastel;

    // Meses que se mostrarán en el Spinner (posición 0 es "Seleccionar mes")
    private final String[] meses = {
            "Seleccionar mes",
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };


    private final double[] ingresosPorMes = {
            800, 750, 900, 850, 920, 880,
            950, 970, 910, 930, 890, 910
    };

    private final double[] egresosPorMes = {
            500, 520, 610, 580, 600, 590,
            640, 650, 630, 620, 605, 615
    };

    // Guardamos el índice del mes actual (0 = Enero, 11 = Diciembre)
    private int indiceMesActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Usa tu layout activity_estadisticas.xml
        setContentView(R.layout.activity_estadisticas);

        // Referenciar vistas del XML
        spnMeses = findViewById(R.id.spnMeses);
        btnMesActual = findViewById(R.id.btnMesActual);
        btnMesAnterior = findViewById(R.id.btnMesAnterior);
        btnAtras = findViewById(R.id.btnAtras);
        viewGraficaPastel = findViewById(R.id.viewGraficaPastel);

        // Obtener mes actual del sistema (0 = Enero, 11 = Diciembre)
        Calendar hoy = Calendar.getInstance();
        indiceMesActual = hoy.get(Calendar.MONTH);

        configurarSpinnerMeses();
        configurarBotonesPeriodo();
        configurarBotonAtras();

        // Al iniciar: dejar "Seleccionar mes" y gráfica neutra
        spnMeses.setSelection(0);
        actualizarGraficaSinDatos();
    }

    /**
     * Configura el Spinner de meses con "Seleccionar mes" + 12 meses.
     */
    private void configurarSpinnerMeses() {
        ArrayAdapter<String> adapterMeses = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                meses
        );
        adapterMeses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMeses.setAdapter(adapterMeses);

        spnMeses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // position 0 = "Seleccionar mes" -> no mostramos datos
                if (position == 0) {
                    actualizarGraficaSinDatos();
                } else {
                    int indiceMes = position - 1; // para mapear a los arreglos 0..11
                    actualizarGraficaYResumen(indiceMes);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se usa
            }
        });
    }

    /**
     * Configura los botones "Mes actual" y "Mes anterior".
     */
    private void configurarBotonesPeriodo() {
        btnMesActual.setOnClickListener(v -> {
            // Selecciona en el spinner el mes actual (sumamos 1 por el "Seleccionar mes")
            spnMeses.setSelection(indiceMesActual + 1);
        });

        btnMesAnterior.setOnClickListener(v -> {
            // Mes anterior al actual (si es enero, pasa a diciembre)
            int indiceMesAnterior = (indiceMesActual + 11) % 12;
            spnMeses.setSelection(indiceMesAnterior + 1);
        });
    }

    /**
     * Configura el botón de atrás para regresar a la pantalla anterior.
     */
    private void configurarBotonAtras() {
        btnAtras.setOnClickListener(v -> finish());
    }

    /**
     * Se llama cuando el usuario aún no ha seleccionado un mes.
     * Deja la gráfica en modo "sin información".
     */
    private void actualizarGraficaSinDatos() {
        // Color gris clarito para indicar que no hay datos cargados
        viewGraficaPastel.setBackgroundColor(Color.parseColor("#DDDDDD"));
        viewGraficaPastel.setContentDescription("Sin datos seleccionados");
    }

    /**
     * Actualiza la "gráfica" y muestra un resumen analítico usando los datos del mes.
     *
     * @param indiceMes 0 = enero, 1 = febrero, ..., 11 = diciembre
     */
    private void actualizarGraficaYResumen(int indiceMes) {
        if (indiceMes < 0 || indiceMes >= ingresosPorMes.length) {
            actualizarGraficaSinDatos();
            return;
        }

        double ingresos = ingresosPorMes[indiceMes];
        double egresos = egresosPorMes[indiceMes];
        double balance = ingresos - egresos;

        // Porcentaje de egresos respecto a los ingresos
        double porcentajeGasto = ingresos == 0 ? 0 : (egresos / ingresos) * 100.0;

        // Color de la "gráfica":
        // Verde suave si el balance es positivo, rojo suave si es negativo.
        if (balance >= 0) {
            viewGraficaPastel.setBackgroundColor(Color.parseColor("#A5D6A7")); // verde claro
        } else {
            viewGraficaPastel.setBackgroundColor(Color.parseColor("#EF9A9A")); // rojo claro
        }

        String mes = meses[indiceMes + 1];
        String descripcion = "Mes: " + mes +
                ". Ingresos: $" + String.format("%.2f", ingresos) +
                ", Egresos: $" + String.format("%.2f", egresos) +
                ", Balance: $" + String.format("%.2f", balance) +
                ", Porcentaje gastado: " + String.format("%.1f", porcentajeGasto) + "%";
        viewGraficaPastel.setContentDescription(descripcion);

        // Resumen rápido (lo puedes quitar si no te gusta el Toast)
        String resumenToast = mes + "\n" +
                "Ingresos: $" + String.format("%.2f", ingresos) + "\n" +
                "Egresos: $" + String.format("%.2f", egresos) + "\n" +
                "Balance: $" + String.format("%.2f", balance) + "\n" +
                "Gastos: " + String.format("%.1f", porcentajeGasto) + "% de los ingresos";
        Toast.makeText(this, resumenToast, Toast.LENGTH_LONG).show();
    }
}

