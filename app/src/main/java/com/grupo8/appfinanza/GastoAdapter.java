package com.grupo8.appfinanza;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GastoAdapter extends ArrayAdapter<String> {

    Activity context;
    String[] categorias;
    String[] montos;
    String[] fechas;

    public GastoAdapter(Activity context, String[] categorias, String[] montos, String[] fechas) {
        super(context, R.layout.item_gasto, categorias);
        this.context = context;
        this.categorias = categorias;
        this.montos = montos;
        this.fechas = fechas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(R.layout.item_gasto, null, true);

        ImageView icon = row.findViewById(R.id.iconGasto);
        TextView tvNombre = row.findViewById(R.id.tvNombreGasto);
        TextView tvMonto = row.findViewById(R.id.tvMontoGasto);
        TextView tvFecha = row.findViewById(R.id.tvFechaGasto);

        tvNombre.setText(categorias[position]);
        tvMonto.setText("Monto: $" + montos[position]);
        tvFecha.setText("Fecha: " + fechas[position]);

        //icon.setImageResource(android.R.drawable.ic_menu_info_details);
        icon.setImageResource(R.drawable.gastos);
        return row;
    }
}
