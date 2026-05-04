package dam.proyecto.gestionguarderia.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConfigApp extends SQLiteOpenHelper {

    private final String table = "config";

    private final String clausure = "id = ?";
    private final String[] row = new String[] { String.valueOf(1) };

    public ConfigApp (@Nullable Context context) {
        super(context, "config", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE config (id INTEGER PRIMARY KEY, user_id TEXT, user TEXT, lang TEXT, role TEXT, token TEXT)";
        db.execSQL(query);

        try {
            ContentValues values = new ContentValues();
            values.put("id", 1);
            values.put("lang", "es");
            values.put("role", "GUEST");

            db.insert(this.table, null, values);
        } catch (Exception ignored) {}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {}

    public boolean setLanguage(String lang) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("lang", lang);
        int affected = db.update(this.table, values, this.clausure, this.row );

        db.close();
        return affected > 0;
    }

    public String getLanguage() {
        SQLiteDatabase db = this.getReadableDatabase();
        String lang = "es"; // Idioma por defecto

        String query = "SELECT lang FROM " + this.table + " WHERE id = ?";
        Cursor cursor = db.rawQuery(query, this.row);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                lang = cursor.getString(0);
            }
            cursor.close();
        }

        db.close();
        return lang;
    }

    public boolean setUserId(int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("user_id", user_id);
        int affected = db.update(this.table, values, this.clausure, this.row );

        db.close();
        return affected > 0;
    }

    public int getUserId() {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = 0;

        String query = "SELECT user_id FROM " + this.table + " WHERE id = ?";
        Cursor cursor = db.rawQuery(query, this.row);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                userId = cursor.getInt(0);
            }
            cursor.close();
        }

        db.close();
        return userId;
    }

    public boolean setUser(String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("user", user);
        int affected = db.update(this.table, values, this.clausure, this.row );

        db.close();
        return affected > 0;
    }

    public String getUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        String user = "";

        String query = "SELECT user FROM " + this.table + " WHERE id = ?";
        Cursor cursor = db.rawQuery(query, this.row);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                user = cursor.getString(0);
            }
            cursor.close();
        }

        db.close();
        return user;
    }

    public boolean setRole(String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("role", role);
        int affected = db.update(this.table, values, this.clausure, this.row );

        db.close();
        return affected > 0;
    }

    public String getRole() {
        SQLiteDatabase db = this.getReadableDatabase();
        String role = "GUEST"; // Role por defecto

        String query = "SELECT user FROM " + this.table + " WHERE id = ?";
        Cursor cursor = db.rawQuery(query, this.row);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                role = cursor.getString(0);
            }
            cursor.close();
        }

        db.close();
        return role;
    }

    public boolean setToken(String token) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("token", token);
        int affected = db.update(this.table, values, this.clausure, this.row );

        db.close();
        return affected > 0;
    }

    public String getToken() {
        SQLiteDatabase db = this.getReadableDatabase();
        String token = "";

        String query = "SELECT user FROM " + this.table + " WHERE id = ?";
        Cursor cursor = db.rawQuery(query, this.row);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                token = cursor.getString(0);
            }
            cursor.close();
        }

        db.close();
        return token;
    }

}
