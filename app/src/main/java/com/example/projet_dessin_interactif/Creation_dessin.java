package com.example.projet_dessin_interactif;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Creation_dessin extends AppCompatActivity {

    private BDD dbHelper;
    int radio=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_dessin);  // Charger le layout associé

        dbHelper = new BDD(this);

        // Références aux éléments de l'activité
        TextView nom_du_dessin = findViewById(R.id.nom_du_dessin);
        EditText champ_dessin = findViewById(R.id.champ_dessin);

        TextView collaborateurs = findViewById(R.id.collaborateurs);
        Spinner collaborateursSpinner = findViewById(R.id.collaborateurs_spinner);
        TextView adresse_label = findViewById(R.id.adresse_label);
        EditText adresse_collaborateurs = findViewById(R.id.adresse_collaborateurs);
        Button btnAjouter = findViewById(R.id.btn_ajouter);
        Button btnCreate = findViewById(R.id.btn_create);

        RadioGroup radioGroup = findViewById(R.id.radio_group);
        RadioButton radioPublic = findViewById(R.id.radio_public);
        RadioButton radioPrivate = findViewById(R.id.radio_prive);

        // Initialisation de la liste pour le Spinner
        ArrayList<String> collaborateursList = new ArrayList<>();
        collaborateursList.add("Collaborateur 1");
        collaborateursList.add("Collaborateur 2");

        // Créez l'ArrayAdapter avec la liste
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                collaborateursList  // Initialisation avec des valeurs par défaut
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        collaborateursSpinner.setAdapter(adapter);

        btnAjouter.setOnClickListener(v -> {
            String newName = adresse_collaborateurs.getText().toString().trim();
            if (!newName.isEmpty()) {
                collaborateursList.add(newName);  // Ajouter à la liste
                adapter.notifyDataSetChanged();  // Mettre à jour le Spinner
                Toast.makeText(this, "Ajouté : " + newName, Toast.LENGTH_SHORT).show();
                adresse_collaborateurs.setText("");  // Effacer le champ
            } else {
                Toast.makeText(this, "Entrez un nom valide", Toast.LENGTH_SHORT).show();
            }
        });

        radioPrivate.setOnClickListener(v -> {
            radio = 2;
        });
        radioPublic.setOnClickListener(v -> {
            radio = 1;
        });

        btnCreate.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            String dessinNom = champ_dessin.getText().toString().trim();
            if (!dessinNom.isEmpty()) {

                if (selectedId == -1) {
                    // Aucun bouton radio n'est sélectionné
                    Toast.makeText(this, "Veuillez sélectionner la disponibilité de votre salon", Toast.LENGTH_SHORT).show();
                } else {
                    int acc_id=dbHelper.getConnectedUserId();

                    long id_dessin = dbHelper.createDessin(this,dessinNom,radio,acc_id); // Utilisez l'ID utilisateur approp

                    // Optionnel : Démarrer une nouvelle activité après la création du dessin
                    Intent intent = new Intent(this, ViewDrawingActivity.class);
                    intent.putExtra("dessin_id", id_dessin);
                    startActivity(intent);
                }

            } else {
                Toast.makeText(this, "Entrez un nom de dessin valide", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
