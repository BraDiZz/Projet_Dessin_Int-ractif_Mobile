package com.example.projet_dessin_interactif;

import android.content.ContentValues;
import android.database.Cursor;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class BDD extends SQLiteOpenHelper {

    private static final String[] INITIAL_EMAILS = {"benjamin.serva34@gmail.com", "delvigne.brian@gmail.com"};
    private static final String[] INITIAL_PSEUDOS = {"ben", "brian"};
    private static final String[] INITIAL_PASSWORDS = {"test", "test2"};
    private static final int[] INITIAL_ACCOUNT_TYPES = {BDD.ACCOUNT_TYPE_SIMPLE, BDD.ACCOUNT_TYPE_SIMPLE};

    private static final String DATABASE_NAME = "DataBase";
    private static final int DATABASE_VERSION = 1;

    // Table name
    public static final String TABLE_NAME = "utilisateurs";

    // Table columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PSEUDO = "pseudo";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ACCOUNT_TYPE = "account_type";

    // Account types
    public static final int ACCOUNT_TYPE_SIMPLE = 1;
    public static final int ACCOUNT_TYPE_PREMIUM = 2;



    // Create table SQL query
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_EMAIL + " TEXT,"
                    + COLUMN_PSEUDO + " TEXT,"
                    + COLUMN_PASSWORD + " TEXT,"
                    + COLUMN_ACCOUNT_TYPE + " INTEGER"
                    + ")";

    public BDD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

        for (int i = 0; i < INITIAL_EMAILS.length; i++) {
            ContentValues values = new ContentValues(); // Créer une nouvelle instance à chaque itération
            values.put(COLUMN_EMAIL, INITIAL_EMAILS[i]);
            values.put(COLUMN_PSEUDO, INITIAL_PSEUDOS[i]);
            values.put(COLUMN_PASSWORD, INITIAL_PASSWORDS[i]);
            values.put(COLUMN_ACCOUNT_TYPE, INITIAL_ACCOUNT_TYPES[i]);
            db.insert(TABLE_NAME, null, values);
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public int checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Requête pour sélectionner l'utilisateur avec l'email donné
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        // Verifier si l'utilisateur avec l'email donné existe
        if (cursor != null && cursor.moveToFirst()) {
            // Si l'utilisateur existe, vérifier le mot de passe
            String storedPassword = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
            if (password.equals(storedPassword)) {
                // Le mot de passe correspond, connexion réussie
                cursor.close();
                return 0; // Aucune erreur
            } else {
                // Le mot de passe ne correspond pas
                cursor.close();
                return 1;
            }
        } else {
            // L'utilisateur avec l'email donné n'existe pas
            if (cursor != null) {
                cursor.close();
            }
            return 2;
        }
    }

    public boolean registerUser(String email, String pseudo, String password, int accountType) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Vérifier si le pseudo existe déjà dans la base de données
        Cursor pseudoCursor = db.query(TABLE_NAME, new String[]{COLUMN_PSEUDO}, COLUMN_PSEUDO + "=?", new String[]{pseudo}, null, null, null);
        if (pseudoCursor.getCount() > 0) {
            pseudoCursor.close();
            return false; // Le pseudo existe déjà
        }
        pseudoCursor.close();

        ContentValues values = new ContentValues();
        if(accountType==1){

            values.put(COLUMN_EMAIL, email);
            values.put(COLUMN_PSEUDO, pseudo);
            values.put(COLUMN_PASSWORD, password);
            values.put(COLUMN_ACCOUNT_TYPE, ACCOUNT_TYPE_SIMPLE);
        }else{
            values.put(COLUMN_EMAIL, email);
            values.put(COLUMN_PSEUDO, pseudo);
            values.put(COLUMN_PASSWORD, password);
            values.put(COLUMN_ACCOUNT_TYPE,ACCOUNT_TYPE_PREMIUM);
        }


        long result = db.insert(TABLE_NAME, null, values);

        return result!=-1;
    }

    // CRUD operations methods go here
}

