package com.example.projet_dessin_interactif;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class InscriptionActivity extends AppCompatActivity {

    private Button btnSimple;
    private Button btnPrenium;

    private Button btnInscription;

    private EditText emailInput;
    private EditText pseudoInput;
    private EditText mdpInput;

    private EditText mdpVerifInput;

    private int bouton;

    private BDD database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        String email = getIntent().getStringExtra("email");
        String mdp = getIntent().getStringExtra("mdp");
        // Initialisation des boutons
        btnSimple = findViewById(R.id.buttonsimple);
        btnPrenium = findViewById(R.id.buttonprenium);
        btnInscription = findViewById(R.id.buttonInscrire);

        btnPrenium.setBackgroundColor(Color.BLUE);
        btnSimple.setBackgroundColor(Color.BLUE);

        emailInput = findViewById(R.id.email_input);
        pseudoInput = findViewById(R.id.pseudo_input);
        mdpInput = findViewById(R.id.password_input);
        mdpVerifInput = findViewById(R.id.password_verif_input);

        bouton=0;

        emailInput.setText(email);
        mdpInput.setText(mdp);

        database = new BDD(this);

        // Écouteur d'événements pour les deux boutons
        View.OnClickListener btnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;

                if(clickedButton==btnSimple){
                    btnPrenium.setBackgroundColor(Color.BLUE);
                    btnSimple.setBackgroundColor(Color.RED);
                    bouton=1;
                }else{
                    btnSimple.setBackgroundColor(Color.BLUE);
                    btnPrenium.setBackgroundColor(Color.RED);
                    bouton=2;
                }
            }
        };

        // Attribution de l'écouteur d'événements aux boutons
        btnSimple.setOnClickListener(btnClickListener);
        btnPrenium.setOnClickListener(btnClickListener);

        btnInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String pseudo = pseudoInput.getText().toString();
                String mdp = mdpInput.getText().toString();
                String mdp_verif = mdpVerifInput.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pseudo) || TextUtils.isEmpty(mdp) || TextUtils.isEmpty(mdp_verif) || bouton==0) {
                    // Afficher un message si un champ est vide
                    Toast.makeText(InscriptionActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }else {
                    if(mdp.equals(mdp_verif)){
                        boolean acc=database.registerUser(email,pseudo,mdp,bouton);
                        if(!acc){
                            Toast.makeText(InscriptionActivity.this, "Ce pseudo existe déjà", Toast.LENGTH_SHORT).show();
                        }else{
                            Intent intent = new Intent(InscriptionActivity.this, HubActivity.class);
                            startActivity(intent);
                        }

                    }else{
                        Toast.makeText(InscriptionActivity.this, "Les deux mdp ne correspondent pas", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }
}