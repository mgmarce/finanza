package com.grupo8.appfinanza;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CategoriaManager {

    private static final String PREFS = "categorias_prefs";
    private static final String KEY = "categorias";

    public static void guardarCategoria(Context ctx, String nombre, String tipo, int icono) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        String jsonString = prefs.getString(KEY, "[]");

        try {
            JSONArray lista = new JSONArray(jsonString);
            JSONObject nueva = new JSONObject();

            nueva.put("nombre", nombre);
            nueva.put("tipo", tipo);
            nueva.put("icono", icono);

            lista.put(nueva);

            prefs.edit().putString(KEY, lista.toString()).apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static JSONArray obtenerCategorias(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String jsonString = prefs.getString(KEY, "[]");

        try {
            return new JSONArray(jsonString);
        } catch (JSONException e) {
            return new JSONArray();
        }
    }
}