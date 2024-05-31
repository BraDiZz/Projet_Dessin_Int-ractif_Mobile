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
        adapter = new DessinAdapter(dessinsList); // Appeler le constructeur avec un seul paramètre
        recyclerView.setAdapter(adapter);
    }


    // Méthode pour récupérer les dessins de l'utilisateur connecté depuis la base de données
    private List<DessinModel> getDessins() {
        List<DessinModel> dessinsList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Requête pour récupérer les dessins de l'utilisateur connecté uniquement
        int connectedUserId = BDD.getConnectedUserId();
        String query = "SELECT " + BDD.COLUMN_DESSIN_NOM + ", " + BDD.COLUMN_PSEUDO + ", " + BDD.COLUMN_DESSIN_IMAGE +
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
                dessinsList.add(new DessinModel(nomDessin, auteur, imageBytes));
            } while (cursor.moveToNext());
            cursor.close(); // Fermer le curseur après utilisation
        }

        // Fermer la connexion à la base de données
        db.close();

        return dessinsList;
    }
}
