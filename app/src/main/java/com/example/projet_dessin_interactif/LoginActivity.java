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
        setContentView(R.layout.activity_login);


        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);

        btnInscription = findViewById(R.id.btn_inscription);


        database = new BDD(this);

        Button loginButton = findViewById(R.id.buttonValider);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();


                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else {
                    int acc = database.checkLogin(LoginActivity.this,email, password);
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
