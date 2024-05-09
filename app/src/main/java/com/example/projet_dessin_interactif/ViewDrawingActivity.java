package com.example.projet_dessin_interactif;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ViewDrawingActivity extends AppCompatActivity {

    private DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_drawing); // Charge le layout XML

        // Obtenir une référence à la vue de dessin
        drawingView = findViewById(R.id.drawingView);

        // Configurer le bouton "clear"
        Button clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.clearDrawing(); // Efface le dessin
            }
        });

        // Configurer le bouton "save"
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ajoutez ici la logique de sauvegarde du dessin
            }
        });
    }
}

