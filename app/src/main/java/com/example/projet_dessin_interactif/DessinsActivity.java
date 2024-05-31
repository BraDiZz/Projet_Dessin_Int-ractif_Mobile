package com.example.projet_dessin_interactif;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class DessinsActivity extends AppCompatActivity {

    private BDD dbHelper;
    private RecyclerView recyclerView;
    private DessinAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dessins);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new BDD(this);
        List<DessinModel> dessinsList = getDessins();
        List<DessinModel> dessinsCollabList = getDessinsCollaborateur();
        dessinsList.addAll(dessinsCollabList);
        adapter = new DessinAdapter(dessinsList,DessinsActivity.this); // Appeler le constructeur avec un seul paramètre
        recyclerView.setAdapter(adapter);
    }


    // Méthode pour récupérer les dessins de l'utilisateur connecté depuis la base de données
    private List<DessinModel> getDessins() {
        List<DessinModel> dessinsList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Requête pour récupérer les dessins de l'utilisateur connecté uniquement
        int connectedUserId = BDD.getConnectedUserId();
        String query = "SELECT " + BDD.COLUMN_DESSIN_ID + ", " + BDD.COLUMN_DESSIN_NOM + ", " + BDD.COLUMN_PSEUDO + ", " + BDD.COLUMN_DESSIN_IMAGE +
                " FROM " + BDD.TABLE_DESSIN + " INNER JOIN " + BDD.TABLE_NAME +
                " ON " + BDD.TABLE_DESSIN + "." + BDD.COLUMN_CREATEUR_ID + " = " +
                BDD.TABLE_NAME + "." + BDD.COLUMN_ID +
                " WHERE " + BDD.COLUMN_CREATEUR_ID + " = " + connectedUserId;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String nomDessin = cursor.getString(cursor.getColumnIndex(BDD.COLUMN_DESSIN_NOM));
                String auteur = cursor.getString(cursor.getColumnIndex(BDD.COLUMN_PSEUDO));
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(BDD.COLUMN_DESSIN_IMAGE)); // Récupérer l'image en BLOB
                long acc_id = cursor.getLong(cursor.getColumnIndex(BDD.COLUMN_DESSIN_ID));
                dessinsList.add(new DessinModel(nomDessin, auteur, imageBytes,acc_id,true));
            } while (cursor.moveToNext());
            cursor.close(); // Fermer le curseur après utilisation
        }

        // Fermer la connexion à la base de données
        db.close();

        return dessinsList;
    }

    private List<DessinModel> getDessinsCollaborateur() {
        List<DessinModel> dessinsList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Requête pour récupérer les détails des dessins où la personne est collaborateur
        int connectedUserId = BDD.getConnectedUserId();

        String query = "SELECT " + BDD.TABLE_DESSIN + "." + BDD.COLUMN_DESSIN_ID + ", " +
                BDD.TABLE_DESSIN + "." + BDD.COLUMN_DESSIN_NOM + ", " +
                BDD.TABLE_NAME + "." + BDD.COLUMN_PSEUDO + ", " +
                BDD.TABLE_DESSIN + "." + BDD.COLUMN_DESSIN_IMAGE +
                " FROM " + BDD.TABLE_COLLABORATIONS +
                " INNER JOIN " + BDD.TABLE_NAME +
                " ON " + BDD.TABLE_COLLABORATIONS + "." + BDD.COLUMN_USER_ID + " = " +
                BDD.TABLE_NAME + "." + BDD.COLUMN_ID +
                " INNER JOIN " + BDD.TABLE_DESSIN +
                " ON " + BDD.TABLE_COLLABORATIONS + "." + BDD.COLUMN_DESSIN_C_ID + " = " +
                BDD.TABLE_DESSIN + "." + BDD.COLUMN_DESSIN_ID +
                " WHERE " + BDD.TABLE_COLLABORATIONS + "." + BDD.COLUMN_USER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(connectedUserId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String nomDessin = cursor.getString(cursor.getColumnIndex(BDD.COLUMN_DESSIN_NOM));
                String auteur = cursor.getString(cursor.getColumnIndex(BDD.COLUMN_PSEUDO));
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(BDD.COLUMN_DESSIN_IMAGE)); // Récupérer l'image en BLOB
                long acc_id = cursor.getLong(cursor.getColumnIndex(BDD.COLUMN_DESSIN_ID));
                dessinsList.add(new DessinModel(nomDessin, auteur, imageBytes, acc_id, false));
            } while (cursor.moveToNext());
            cursor.close(); // Fermer le curseur après utilisation
        }

        // Fermer la connexion à la base de données
        db.close();

        return dessinsList;
    }






}
