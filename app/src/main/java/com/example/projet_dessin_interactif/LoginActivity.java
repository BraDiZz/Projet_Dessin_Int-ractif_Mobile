package com.example.projet_dessin_interactif;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;

    private Button btnInscription;
    private BDD database;

    public static final String ERROR_EMAIL_NOT_FOUND = "Adresse e-mail non trouvée";
    public static final String ERROR_INCORRECT_PASSWORD = "Mot de passe incorrect";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Charge le layout de LoginActivity

        // Récupérer les références des champs de texte
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);

        btnInscription = findViewById(R.id.btn_inscription);

        // Initialiser la base de données
        database = new BDD(this);

        // Récupérer la référence du bouton
        Button loginButton = findViewById(R.id.buttonValider);

        // Ajouter un écouteur d'événements au bouton
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer les valeurs entrées
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                // Vérifier si les champs sont vides
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    // Afficher un message si un champ est vide
                    Toast.makeText(LoginActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else {
                    // Tous les champs sont remplis, procéder à la vérification dans la base de données
                    int acc = database.checkLogin(email, password);
                    if (acc == 0) {
                        Intent intent = new Intent(LoginActivity.this, HubActivity.class);
                        startActivity(intent);
                        //finish();
                    } else {
                        if(acc==1){
                            Toast.makeText(LoginActivity.this, ERROR_INCORRECT_PASSWORD, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this,ERROR_EMAIL_NOT_FOUND , Toast.LENGTH_SHORT).show();
                            btnInscription.setVisibility(View.VISIBLE);
                        }

                    }
                }
            }
        });

        btnInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                Intent intent = new Intent(LoginActivity.this, InscriptionActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("mdp", password);
                startActivity(intent);
            }
        });
    }
}
