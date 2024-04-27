package com.example.projet_dessin_interactif;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class GalerieActivity extends AppCompatActivity {

    private BDD dbHelper; // Instance de votre classe BDD pour accéder à la base de données

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galerie);

        // Récupérer la référence à la TextView pour afficher les dessins
        TextView Titre = findViewById(R.id.Titre);

        // Récupérer les données des dessins depuis la base de données
        dbHelper = new BDD(this); // Initialiser votre classe BDD
        List<String> dessins = getDessins(); // Méthode pour récupérer les dessins depuis la base de données

        // Construire le texte à afficher
        StringBuilder dessinsText = new StringBuilder();
        for (String dessin : dessins) {
            dessinsText.append(dessin).append("\n"); // Ajouter chaque dessin à la chaîne de texte
        }

        // Afficher les dessins dans la TextView
        Titre.setText(dessinsText.toString());
    }

    // Méthode pour récupérer les dessins depuis la base de données
    private List<String> getDessins() {
        List<String> dessinsList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT " + BDD.COLUMN_DESSIN_NOM + ", " + BDD.COLUMN_PSEUDO +
                " FROM " + BDD.TABLE_DESSIN + " INNER JOIN " + BDD.TABLE_NAME +
                " ON " + BDD.TABLE_DESSIN + "." + BDD.COLUMN_CREATEUR_ID + " = " +
                BDD.TABLE_NAME + "." + BDD.COLUMN_ID;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String nomDessin = cursor.getString(cursor.getColumnIndex(BDD.COLUMN_DESSIN_NOM));
                String auteur = cursor.getString(cursor.getColumnIndex(BDD.COLUMN_PSEUDO));
                String dessinInfo = nomDessin + " - " + auteur;
                dessinsList.add(dessinInfo);
            } while (cursor.moveToNext());
            cursor.close(); // Fermer le curseur après utilisation
        }

        // Fermer la connexion à la base de données
        db.close();

        return dessinsList;
    }
}
