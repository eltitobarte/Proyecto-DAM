package dam.proyecto.gestionguarderia.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConfigApp extends SQLiteOpenHelper {

    // Nombre de la tabla
    private final String table = "config";

    // Argumentos de consulta fijos
    private final String clausure = "id = ?";
    private final String[] row = new String[] { String.valueOf(1) };

    public ConfigApp (@Nullable Context context) {
        super(context, "config", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE config (id INTEGER PRIMARY KEY, user_id TEXT, user TEXT, lang TEXT, role TEXT, token TEXT, user_name TEXT, user_surname TEXT, user_phone TEXT, user_email TEXT)";
        db.execSQL(query);

        try {
            ContentValues values = new ContentValues();
            values.put("id", 1); // Fila unica
            values.put("lang", "es"); // Idioma por defecto
            values.put("role", "GUEST"); // Rol por defecto

            db.insert(this.table, null, values);
        } catch (Exception ignored) {}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {}

    /**
     * Metodo asignar el idioma de la aplicacion
     * @param lang
     * @return
     */
    public boolean setLanguage(String lang) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("lang", lang);
        int affected = db.update(this.table, values, this.clausure, this.row );

        db.close();
        return affected > 0;
    }

    /**
     * Metodo para obtener el idioma
     * @return
     */
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

    /**
     * Metodo para asignar el ID del usuario
     * @param user_id
     * @return
     */
    public boolean setUserId(int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("user_id", user_id);
        int affected = db.update(this.table, values, this.clausure, this.row );

        db.close();
        return affected > 0;
    }

    /**
     * Metodo para obtener el id del usuario, por defecto 0 (No logeado)
     * @return
     */
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

    /**
     * Metodo para guardar el nombre de usuario
     * @param user
     * @return
     */
    public boolean setUser(String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("user", user);
        int affected = db.update(this.table, values, this.clausure, this.row );

        db.close();
        return affected > 0;
    }

    /**
     * Metodo para obtener el nombre de usuario
     * @return
     */
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

    /**
     * Metodo para asignar el rol de usuario
     * @param role
     * @return
     */
    public boolean setRole(String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("role", role);
        int affected = db.update(this.table, values, this.clausure, this.row );

        db.close();
        return affected > 0;
    }

    /**
     * Metodo para obtener el rol de usuario, "GUEST" por defecto
     * @return
     */
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

    /**
     * Metodo para signar el token
     * @param token
     * @return
     */
    public boolean setToken(String token) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("token", token);
        int affected = db.update(this.table, values, this.clausure, this.row );

        db.close();
        return affected > 0;
    }

    /**
     * Metodo para obtener el token
     * Usado principalmente para las llamadas API/REST
     * @return
     */
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

    /**
     * Metodo para establecer el nombre del usuario
     * @param user_name
     * @return
     */
    public boolean setUserName(String user_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("user_name", user_name);
        int affected = db.update(this.table, values, this.clausure, this.row );

        db.close();
        return affected > 0;
    }

    /**
     * Metodo para obtener el nombre del usuario
     * @return
     */
    public String getUserName() {
        SQLiteDatabase db = this.getReadableDatabase();
        String userName = "";

        String query = "SELECT user_name FROM " + this.table + " WHERE id = ?";
        Cursor cursor = db.rawQuery(query, this.row);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                userName = cursor.getString(0);
            }
            cursor.close();
        }

        db.close();
        return userName;
    }

    /**
     * Metodo para establecer los apellidos del usuario
     * @param user_surname
     * @return
     */
    public boolean setUserSurname(String user_surname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("user_surname", user_surname);
        int affected = db.update(this.table, values, this.clausure, this.row );

        db.close();
        return affected > 0;
    }

    /**
     * Metodo para obtener los apellidos del usuario
     * @return
     */
    public String getUserSurname() {
        SQLiteDatabase db = this.getReadableDatabase();
        String userSurname = "";

        String query = "SELECT user_surname FROM " + this.table + " WHERE id = ?";
        Cursor cursor = db.rawQuery(query, this.row);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                userSurname = cursor.getString(0);
            }
            cursor.close();
        }

        db.close();
        return userSurname;
    }

    /**
     * Metodo para establecer el teléfono del usuario
     * @param user_phone
     * @return
     */
    public boolean setUserPhone(String user_phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("user_phone", user_phone);
        int affected = db.update(this.table, values, this.clausure, this.row );

        db.close();
        return affected > 0;
    }

    /**
     * Metodo para obtener el telefono del usuario
     * @return
     */
    public String getUserPhone() {
        SQLiteDatabase db = this.getReadableDatabase();
        String userPhone = "";

        String query = "SELECT user_phone FROM " + this.table + " WHERE id = ?";
        Cursor cursor = db.rawQuery(query, this.row);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                userPhone = cursor.getString(0);
            }
            cursor.close();
        }

        db.close();
        return userPhone;
    }

    /**
     * Metodo para establecer el correo electrónico del usuario
     * @param user_email
     * @return
     */
    public boolean setUserEmail(String user_email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("user_email", user_email);
        int affected = db.update(this.table, values, this.clausure, this.row );

        db.close();
        return affected > 0;
    }

    /**
     * Metodo para obtener el correo electrónico del usuario
     * @return
     */
    public String getUserEmail() {
        SQLiteDatabase db = this.getReadableDatabase();
        String userEmail = "";

        String query = "SELECT user_email FROM " + this.table + " WHERE id = ?";
        Cursor cursor = db.rawQuery(query, this.row);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                userEmail = cursor.getString(0);
            }
            cursor.close();
        }

        db.close();
        return userEmail;
    }

    /**
     * Metodo general para actualizar los datos de usuario
     * @param user
     * @param role
     * @param user_name
     * @param user_surname
     * @param user_phone
     * @param user_email
     * @return
     */
    public boolean updateUser(String user, String role, String user_name, String user_surname, String user_phone, String user_email) {
        try {
            this.setUser(user);
            this.setRole(role);
            this.setUserName(user_name);
            this.setUserSurname(user_surname);
            this.setUserPhone(user_phone);
            this.setUserEmail(user_email);
            return true;
        } catch (Exception ignored) {}

        return false;
    }

}
