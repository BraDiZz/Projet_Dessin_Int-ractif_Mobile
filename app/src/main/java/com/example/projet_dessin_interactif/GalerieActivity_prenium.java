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

public class GalerieActivity_prenium extends AppCompatActivity {

    private BDD dbHelper;
    private RecyclerView recyclerView;
    private DessinAdapter_prenium adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galerie);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new BDD(this);
        List<DessinModel> dessinsList = getDessins();
        adapter = new DessinAdapter_prenium(dessinsList,GalerieActivity_prenium.this); // Appeler le constructeur avec un seul paramètre
        recyclerView.setAdapter(adapter);
    }


    // Méthode pour récupérer les dessins depuis la base de données
    private List<DessinModel> getDessins() {
        List<DessinModel> dessinsList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT " + BDD.COLUMN_DESSIN_ID + ", " + BDD.COLUMN_DESSIN_NOM + ", " + BDD.COLUMN_PSEUDO + ", "  + BDD.COLUMN_DESSIN_IMAGE +
                " FROM " + BDD.TABLE_DESSIN + " INNER JOIN " + BDD.TABLE_NAME +
                " ON " + BDD.TABLE_DESSIN + "." + BDD.COLUMN_CREATEUR_ID + " = " +
                BDD.TABLE_NAME + "." + BDD.COLUMN_ID +
                " WHERE " + BDD.COLUMN_DESSIN_STATUT + " = 1"; // Utilisation de l'entier 1 pour les dessins publics

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

}
