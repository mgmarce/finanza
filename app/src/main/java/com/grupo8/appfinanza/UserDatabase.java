package com.grupo8.appfinanza;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "usuarios.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_USERS = "users";

    public UserDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_USERS + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT," +
                        "email TEXT UNIQUE," +
                        "password TEXT," +         // NULL si se registró con Google
                        "google INTEGER" +         // 1 = Google, 0 = normal
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // -----------------------
    //         DAO
    // -----------------------

    public long insertUser(String name, String email, String password, boolean google) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        values.put("google", google ? 1 : 0);

        return db.insert(TABLE_USERS, null, values);
    }

    public boolean existsByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM " + TABLE_USERS + " WHERE email = ? LIMIT 1",
                new String[]{email}
        );

        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public Cursor getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE email = ?",
                new String[]{email}
        );
    }
}
