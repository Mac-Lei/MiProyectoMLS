package com.mac.miproyectomls;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "usuarios.db";
    private static final int DATABASE_VERSION = 1;

    // Tablas y columnas
    public static final String TABLE_ADMIN = "admin";
    public static final String TABLE_CLIENTE = "cliente";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    // Consultas para crear las tablas
    private static final String DATABASE_CREATE_ADMIN =
            "CREATE TABLE " + TABLE_ADMIN + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMAIL + " TEXT NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL);";

    private static final String DATABASE_CREATE_CLIENTE =
            "CREATE TABLE " + TABLE_CLIENTE + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMAIL + " TEXT NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tablas
        db.execSQL(DATABASE_CREATE_ADMIN);
        db.execSQL(DATABASE_CREATE_CLIENTE);

        // Insertar datos predeterminados directamente
        ContentValues adminValues = new ContentValues();
        adminValues.put(COLUMN_EMAIL, "maca@gmail.com");
        adminValues.put(COLUMN_PASSWORD, "maca1234");
        db.insert(TABLE_ADMIN, null, adminValues);

        ContentValues clienteValues = new ContentValues();
        clienteValues.put(COLUMN_EMAIL, "anto@gmail.com");
        clienteValues.put(COLUMN_PASSWORD, "anto1234");
        db.insert(TABLE_CLIENTE, null, clienteValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTE);
        onCreate(db);
    }

    // Verificar credenciales de Admin
    public boolean checkAdmin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ADMIN + " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // Verificar credenciales de Cliente
    public boolean checkCliente(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CLIENTE + " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }
}
