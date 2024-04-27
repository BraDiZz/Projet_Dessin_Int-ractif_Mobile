package com.example.projet_dessin_interactif;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Références aux boutons
        Button btnGallery = findViewById(R.id.btn_gallery);
        Button btnLoginSignup = findViewById(R.id.btn_login_signup);

        // Ajouter des événements de clic aux boutons
        btnGallery.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GalerieActivity.class);
            startActivity(intent);
        });

        btnLoginSignup.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent); // Lance LoginActivity
        });
    }
}

