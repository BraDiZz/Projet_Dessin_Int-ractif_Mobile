package com.example.projet_dessin_interactif;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.widget.Toast;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class BDD extends SQLiteOpenHelper {
    private static int connected = -1;

    private static final String[] INITIAL_EMAILS = {"benjamin.serva34@gmail.com", "delvigne.brian@gmail.com"};
    private static final String[] INITIAL_PSEUDOS = {"ben", "brian"};
    private static final String[] INITIAL_PASSWORDS = {"test", "test2"};
    private static final int[] INITIAL_ACCOUNT_TYPES = {BDD.ACCOUNT_TYPE_PREMIUM, BDD.ACCOUNT_TYPE_SIMPLE};

    private static final String[] INITIAL_NOM = {"dessin1", "dessin2","dessin3"};
    private static final int[] INITIAL_STATUT = {BDD.STATUT_PUBLIC, BDD.STATUT_PUBLIC, BDD.STATUT_PRIVEE};

    private static final int[] INITIAL_CREATEUR = {1,2,1};

    private static final String DATABASE_NAME = "DataBase";
    private static final int DATABASE_VERSION = 4;

    // Table names and columns
    public static final String TABLE_NAME = "utilisateurs";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PSEUDO = "pseudo";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ACCOUNT_TYPE = "account_type";
    public static final String TABLE_DESSIN = "dessin";
    public static final String COLUMN_DESSIN_ID = "dessin_id";
    public static final String COLUMN_DESSIN_NOM = "nom";
    public static final String COLUMN_DESSIN_STATUT = "statut";
    public static final String COLUMN_CREATEUR_ID = "createur_id";
    public static final int ACCOUNT_TYPE_SIMPLE = 1;
    public static final int ACCOUNT_TYPE_PREMIUM = 2;
    public static final int STATUT_PUBLIC = 1;
    public static final int STATUT_PRIVEE = 2;

    // SQL queries for creating tables
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_EMAIL + " TEXT,"
                    + COLUMN_PSEUDO + " TEXT,"
                    + COLUMN_PASSWORD + " TEXT,"
                    + COLUMN_ACCOUNT_TYPE + " INTEGER"
                    + ")";
    public static final String COLUMN_DESSIN_IMAGE = "image";
    private static final String CREATE_DESSIN_TABLE =
            "CREATE TABLE " + TABLE_DESSIN + "("
                    + COLUMN_DESSIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DESSIN_NOM + " TEXT,"
                    + COLUMN_DESSIN_STATUT + " INTEGER,"
                    + COLUMN_DESSIN_IMAGE + " BLOB,"
                    + COLUMN_CREATEUR_ID + " INTEGER,"
                    + "FOREIGN KEY(" + COLUMN_CREATEUR_ID + ") REFERENCES " + TABLE_NAME + "(" + COLUMN_ID + ")"
                    + ")";

    // Constantes pour la table de jointure collaborations
    public static final String TABLE_COLLABORATIONS = "collaborations";
    public static final String COLUMN_COLLABORATION_ID = "collaboration_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_DESSIN_C_ID = "dessin_id";

    // Requête de création de table pour la table de jointure collaborations
    private static final String CREATE_COLLABORATIONS_TABLE =
            "CREATE TABLE " + TABLE_COLLABORATIONS + "("
                    + COLUMN_COLLABORATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USER_ID + " INTEGER,"
                    + COLUMN_DESSIN_C_ID + " INTEGER,"
                    + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_NAME + "(" + COLUMN_ID + "),"
                    + "FOREIGN KEY(" + COLUMN_DESSIN_C_ID + ") REFERENCES " + TABLE_DESSIN + "(" + COLUMN_DESSIN_C_ID + ")"
                    + ")";
    public BDD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_DESSIN_TABLE);

        // Insert initial data
        for (int i = 0; i < INITIAL_EMAILS.length; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_EMAIL, INITIAL_EMAILS[i]);
            values.put(COLUMN_PSEUDO, INITIAL_PSEUDOS[i]);
            values.put(COLUMN_PASSWORD, INITIAL_PASSWORDS[i]);
            values.put(COLUMN_ACCOUNT_TYPE, INITIAL_ACCOUNT_TYPES[i]);
            db.insert(TABLE_NAME, null, values);
        }

        for (int i = 0; i < INITIAL_NOM.length; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_DESSIN_NOM, INITIAL_NOM[i]);
            values.put(COLUMN_DESSIN_STATUT, INITIAL_STATUT[i]);
            values.put(COLUMN_CREATEUR_ID, INITIAL_CREATEUR[i]);
            values.put(COLUMN_DESSIN_IMAGE, (byte[]) null);
            db.insert(TABLE_DESSIN, null, values);
        }
    }

    public long createDessin(Context context, String nom, int statut, int createurId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESSIN_NOM, nom);
        values.put(COLUMN_DESSIN_STATUT, statut);
        values.put(COLUMN_CREATEUR_ID, createurId);
        values.put(COLUMN_DESSIN_IMAGE, (byte[]) null); // Convertir le bitmap en tableau de bytes

        long id = db.insert(TABLE_DESSIN, null, values);
        db.close();
        if (id == -1) {
            Toast.makeText(context, "id du dessin non chargé", Toast.LENGTH_SHORT).show();
        }

        return id;
    }

    public boolean saveDessin(long dessinId, Bitmap image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESSIN_IMAGE, getBitmapAsByteArray(image)); // Convertir le bitmap en tableau de bytes

        int affectedRows = db.update(TABLE_DESSIN, values, COLUMN_DESSIN_ID + " = ?", new String[]{String.valueOf(dessinId)});
        db.close();
        return affectedRows > 0;
    }
    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DESSIN);
        onCreate(db);
    }

    public int checkLogin(Context context,String email, String password) {
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
                connected=cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                cursor.close();

                //Toast.makeText(context, "Connected user ID: " + connected, Toast.LENGTH_LONG).show();

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
    public static int getConnectedUserId() {
        // Utiliser l'attribut connected pour obtenir l'ID de l'utilisateur connecté
        return connected;
    }

    public int getTypeAccount() {
        // Récupérer l'ID de l'utilisateur connecté
        int userId = getConnectedUserId();

        // Vérifier si l'utilisateur est connecté
        if (userId != -1) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = null;

            try {
                // Requête pour obtenir le type de compte de l'utilisateur connecté
                String query = "SELECT " + COLUMN_ACCOUNT_TYPE +
                        " FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_ID + " = ?";
                cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

                // Vérifier si le curseur contient des données
                if (cursor != null && cursor.moveToFirst()) {
                    // Récupérer le type de compte de l'utilisateur
                    int accountType = cursor.getInt(cursor.getColumnIndex(COLUMN_ACCOUNT_TYPE));
                    return accountType;
                }
            } catch (Exception e) {
                e.printStackTrace(); // Journaliser l'exception pour le débogage
            } finally {
                if (cursor != null) {
                    cursor.close(); // Fermer le curseur après utilisation
                }
                db.close(); // Fermer la connexion à la base de données
            }
        }

        // Si aucun utilisateur n'est connecté ou si une erreur s'est produite, retourner -1
        return -1;
    }


    public static void deconnexion(){
        connected = -1;
    }

    public List<String> getDessinsUtilisateur(int userId) {
        List<String> dessinsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_DESSIN_NOM +
                " FROM " + TABLE_DESSIN +
                " WHERE " + COLUMN_CREATEUR_ID + " = " + userId;

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String nomDessin = cursor.getString(cursor.getColumnIndex(COLUMN_DESSIN_NOM));
                    dessinsList.add(nomDessin);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
        } finally {
            if (cursor != null) {
                cursor.close(); // Fermer le curseur après utilisation
            }
            db.close(); // Fermer la connexion à la base de données
        }

        return dessinsList;
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

        if (result != -1) {
            // L'insertion a réussi, donc récupérer l'ID inséré
            Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
            if (cursor.moveToFirst()) {
                connected = cursor.getInt(0); // Récupérer l'ID inséré
            }
            cursor.close();
        }
        return result!=-1;
    }

    public Bitmap downloadImage(long dessinId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Bitmap bitmap = null;

        try {
            // Requête pour sélectionner l'image à partir de l'ID du dessin
            String query = "SELECT " + COLUMN_DESSIN_IMAGE +
                    " FROM " + TABLE_DESSIN +
                    " WHERE " + COLUMN_DESSIN_ID + " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(dessinId)});

            // Vérifier si le curseur contient des données
            if (cursor != null && cursor.moveToFirst()) {
                // Récupérer l'image en tant que tableau de bytes
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(COLUMN_DESSIN_IMAGE));
                if (imageBytes != null) {
                    // Convertir le tableau de bytes en bitmap
                    bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Journaliser l'exception pour le débogage
        } finally {
            db.close(); // Fermer la connexion à la base de données
        }

        return bitmap;
    }

    public Bitmap getDessinImage(long dessinId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_DESSIN_IMAGE + " FROM " + TABLE_DESSIN +
                " WHERE " + COLUMN_DESSIN_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(dessinId)});

        Bitmap image = null;
        if (cursor != null && cursor.moveToFirst()) {
            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(COLUMN_DESSIN_IMAGE));
            image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            cursor.close();
        }

        db.close();
        return image;
    }



}
