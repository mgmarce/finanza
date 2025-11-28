package com.grupo8.appfinanza;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "finanzas.db";
    private static final int DATABASE_VERSION = 2;

    // TABLA GASTOS
    public static final String TABLE_GASTOS = "gastos";
    public static final String COL_G_ID = "id";
    public static final String COL_G_CATEGORIA = "categoria";
    public static final String COL_G_MONTO = "monto";
    public static final String COL_G_FECHA = "fecha";
    public static final String COL_G_HORA = "hora";

    // TABLA INGRESOS
    public static final String TABLE_INGRESOS = "ingresos";
    public static final String COL_I_ID = "id";
    public static final String COL_I_CATEGORIA = "categoria";
    public static final String COL_I_MONTO = "monto";
    public static final String COL_I_FECHA = "fecha";
    public static final String COL_I_HORA = "hora";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createGastos = "CREATE TABLE " + TABLE_GASTOS + " (" +
                COL_G_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_G_CATEGORIA + " TEXT, " +
                COL_G_MONTO + " REAL, " +
                COL_G_FECHA + " TEXT, " +
                COL_G_HORA + " TEXT)";
        db.execSQL(createGastos);

        String createIngresos = "CREATE TABLE " + TABLE_INGRESOS + " (" +
                COL_I_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_I_CATEGORIA + " TEXT, " +
                COL_I_MONTO + " REAL, " +
                COL_I_FECHA + " TEXT, " +
                COL_I_HORA + " TEXT)";
        db.execSQL(createIngresos);

        // Datos quemados de ejemplo
        db.execSQL("INSERT INTO gastos (categoria, monto, fecha, hora) VALUES ('Salud', 20.50, '2025-01-02', '08:30')");
        db.execSQL("INSERT INTO gastos (categoria, monto, fecha, hora) VALUES ('Supermercado', 45.00, '2025-01-04', '13:10')");
        db.execSQL("INSERT INTO ingresos (categoria, monto, fecha, hora) VALUES ('Salario', 450.00, '2025-01-05', '09:00')");
        db.execSQL("INSERT INTO ingresos (categoria, monto, fecha, hora) VALUES ('Negocios', 120.00, '2025-01-06', '15:30')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GASTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGRESOS);
        onCreate(db);
    }

    // -------- GASTOS --------
    public boolean insertarGasto(String categoria, double monto, String fecha, String hora) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_G_CATEGORIA, categoria);
        cv.put(COL_G_MONTO, monto);
        cv.put(COL_G_FECHA, fecha);
        cv.put(COL_G_HORA, hora);
        long result = db.insert(TABLE_GASTOS, null, cv);
        return result != -1;
    }

    public Cursor obtenerGastos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_GASTOS + " ORDER BY fecha DESC, hora DESC", null);
    }

    // -------- INGRESOS --------
    public boolean insertarIngreso(String categoria, double monto, String fecha, String hora) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_I_CATEGORIA, categoria);
        cv.put(COL_I_MONTO, monto);
        cv.put(COL_I_FECHA, fecha);
        cv.put(COL_I_HORA, hora);
        long result = db.insert(TABLE_INGRESOS, null, cv);
        return result != -1;
    }

    public Cursor obtenerIngresos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_INGRESOS + " ORDER BY fecha DESC, hora DESC", null);
    }
}