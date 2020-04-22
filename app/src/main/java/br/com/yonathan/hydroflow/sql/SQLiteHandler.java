package br.com.yonathan.hydroflow.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "HydroFlow";
    private static final String TABLE_NAME = "User";

    // Table Columns for SQLite for Android
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CPF = "cpf";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_CEP = "cep";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_LOGIN_TABLE =
                "CREATE TABLE " + TABLE_NAME
                        + "("
                        + KEY_ID + " INTEGER PRIMARY KEY,"
                        + KEY_NAME + " TEXT,"
                        + KEY_CPF + " TEXT UNIQUE,"
                        + KEY_PHONE + " TEXT,"
                        + KEY_ADDRESS + " TEXT,"
                        + KEY_CEP + " TEXT,"
                        + KEY_EMAIL + " TEXT UNIQUE,"
                        + KEY_PASSWORD + " TEXT,"
                        + KEY_CREATED_AT + " TEXT,"
                        + KEY_UPDATED_AT + " TEXT"
                        + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "##### SQLite Table '" + TABLE_NAME + "' Created #####");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    // Storing user details in database
    public void addUser(String k00, String k01, String k02, String k03, String k04, String k05, String k06, String k07, String k08, String k09) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, k00);
        values.put(KEY_NAME, k01);
        values.put(KEY_CPF, k02);
        values.put(KEY_PHONE, k03);
        values.put(KEY_ADDRESS, k04);
        values.put(KEY_CEP, k05);
        values.put(KEY_EMAIL, k06);
        values.put(KEY_PASSWORD, k07);
        values.put(KEY_CREATED_AT, k08);
        values.put(KEY_UPDATED_AT, k09);

        // Inserting Row
        long ID = db.insert(TABLE_NAME, null, values);
        // Closing database connection
        db.close();

        Log.d(TAG, "##### New record into SQLite Table '" + TABLE_NAME + "' with ID: " + ID + " #####");
    }

    // Getting user data from SQLite
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            // This put the data on MainActivity
            user.put("ID", cursor.getString(0));
            user.put("NOME", cursor.getString(1));
            user.put("CPF", cursor.getString(2));
            user.put("EMAIL", cursor.getString(6));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "##### Fetching Data from SQLite Table '" + TABLE_NAME + "': " + user.toString() + " #####");

        return user;
    }

    // Delete all tables and creates them again
    public void deleteUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_NAME, null, null);
        db.close();
        Log.d(TAG, "##### Table '" + TABLE_NAME + "' Deleted from SQLite #####");
    }

}
