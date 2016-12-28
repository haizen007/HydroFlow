package online.hydroflow.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "HydroFlow";

    // Login table name
    private static final String TABLE_USER = "User";

    // Login Table Columns for SQLite into Android
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CPF = "cpf";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_CEP = "cep";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_CREATED_AT = "created_at";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_LOGIN_TABLE =
                "CREATE TABLE " + TABLE_USER
                        + "("
                        + KEY_ID            + " INTEGER PRIMARY KEY,"
                        + KEY_NAME          + " TEXT,"
                        + KEY_CPF           + " TEXT UNIQUE,"
                        + KEY_PHONE         + " TEXT,"
                        + KEY_ADDRESS       + " TEXT,"
                        + KEY_CEP           + " TEXT,"
                        + KEY_EMAIL         + " TEXT UNIQUE,"
                        + KEY_PASSWORD      + " TEXT,"
                        + KEY_CREATED_AT    + " TEXT"
                        + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "DB Table Created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     */
    public void addUser(String id, String nome, String cpf, String telefone, String endereco, String cep, String email, String senha, String data_criado) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_NAME, nome);
        values.put(KEY_CPF, cpf);
        values.put(KEY_PHONE, telefone);
        values.put(KEY_ADDRESS, endereco);
        values.put(KEY_CEP, cep);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, senha);
        values.put(KEY_CREATED_AT, data_criado);

        // Inserting Row
        long ID = db.insert(TABLE_USER, null, values);
        // Closing database connection
        db.close();

        Log.d(TAG, "New user inserted into SQLite: " + ID);
    }

    /**
     * Getting user data from SQLite
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row, this put the data on MainActivity
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
//            user.put("created_at", cursor.getString(3));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from SQLite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from SQLite");
    }

}
