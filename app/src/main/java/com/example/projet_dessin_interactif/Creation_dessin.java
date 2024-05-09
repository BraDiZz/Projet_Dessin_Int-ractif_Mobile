package com.example.projet_dessin_interactif;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class Creation_dessin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_dessin);  // Charger le layout associé

        // Références aux éléments de l'activité
        TextView nom_du_dessin = findViewById(R.id.nom_du_dessin);
        EditText champ_dessin = findViewById(R.id.champ_dessin);

        TextView collaborateurs = findViewById(R.id.collaborateurs);
        Spinner collaborateursSpinner = findViewById(R.id.collaborateurs_spinner);
        TextView adresse_label = findViewById(R.id.adresse_label);
        EditText adresse_collaborateurs = findViewById(R.id.adresse_collaborateurs);
        Button btnAjouter = findViewById(R.id.btn_ajouter);
        Button btnCreate = findViewById(R.id.btn_create);

        //int selectedId = radioGroup.getCheckedRadioButtonId();



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

        btnCreate.setOnClickListener(v -> {
            Intent intent = new Intent(this, ViewDrawingActivity.class); // Classe du canvas
            startActivity(intent); // Démarrer l'activité du dessin
        });
    }
}
