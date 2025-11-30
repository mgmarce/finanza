package com.grupo8.appfinanza;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IngresoAdapter extends ArrayAdapter<String> {

    Activity context;
    String[] categorias;
    String[] montos;
    String[] fechas;

    public IngresoAdapter(Activity context, String[] categorias, String[] montos, String[] fechas) {
        super(context, R.layout.item_ingreso, categorias);
        this.context = context;
        this.categorias = categorias;
        this.montos = montos;
        this.fechas = fechas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(R.layout.item_ingreso, null, true);

        ImageView icon = row.findViewById(R.id.iconIngreso);
        TextView tvNombre = row.findViewById(R.id.tvNombreIngreso);
        TextView tvMonto = row.findViewById(R.id.tvMontoIngreso);
        TextView tvFecha = row.findViewById(R.id.tvFechaIngreso);

        tvNombre.setText(categorias[position]);
        tvMonto.setText("Monto: $" + montos[position]);
        tvFecha.setText("Fecha: " + fechas[position]);

        // Aquí pones tu icono .webp
        icon.setImageResource(R.drawable.ingresos);

        return row;
    }
}
