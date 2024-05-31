package com.example.projet_dessin_interactif;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.PopupWindow;
import com.google.android.material.slider.Slider;
import android.view.LayoutInflater;
import android.content.Context;
import android.view.ViewGroup;
import android.graphics.Color;
import android.widget.Toast;


public class ViewDrawingActivity extends AppCompatActivity {
    private BDD dbHelper;
    private DrawingView drawingView;
    private PopupWindow colorPopupWindow;
    private int mode=0;

    private long dessinId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_drawing); // Charge le layout XML

        // Obtenir une référence à la vue de dessin
        drawingView = findViewById(R.id.drawingView);
        Button colorButton = findViewById(R.id.colorButton);
        dbHelper = new BDD(this);
        dessinId = getIntent().getLongExtra("dessin_id", -1);
        if (dessinId == -1) {
            Toast.makeText(this, "Erreur: ID du dessin non valide", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        colorButton.setOnClickListener(v -> showColorPopup(colorButton));

        SeekBar seekBar = findViewById(R.id.seekBar);

        // Configuration du SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                drawingView.setStrokeWidth(progress); // Mettre à jour l'épaisseur du trait
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        Slider colorPickerSlider;



        Button modeButton = findViewById(R.id.ModeButton);
        modeButton.setOnClickListener(v -> {
            drawingView.setMode(); // Activer ou désactiver le mode cercle dans la vue de dessin
            if(mode==0) {
                mode = 1;
                modeButton.setText("Mode Cercle");
            }else if(mode==1) {
                mode = 2;
                modeButton.setText("Mode Droite");
            }else if(mode==2) {
                mode = 0;
                modeButton.setText("Mode Libre");
            }
        });

        Button returnButton = findViewById(R.id.ReturnButton);
        returnButton.setOnClickListener(v -> {
            drawingView.goBack();
        });






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
        saveButton.setOnClickListener(v -> {
            // Obtenir le bitmap du dessin actuel
            Bitmap currentDrawing = drawingView.getBitmap();

            // Vérifier si le bitmap est null (aucun dessin)
            if (currentDrawing == null) {
                Toast.makeText(ViewDrawingActivity.this, "Aucun dessin à sauvegarder", Toast.LENGTH_SHORT).show();
                return;
            }

            // Enregistrer le dessin dans la base de données
            boolean success = dbHelper.saveDessin(dessinId, currentDrawing);
            if (success) {
                Toast.makeText(ViewDrawingActivity.this, "Dessin sauvegardé", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ViewDrawingActivity.this, "Échec de la sauvegarde du dessin", Toast.LENGTH_SHORT).show();
            }
        });

        Button quitButton = findViewById(R.id.quitButton);
        quitButton.setOnClickListener(v -> {
            Intent intent = new Intent(ViewDrawingActivity.this, HubActivity.class);
            startActivity(intent);
        });

    }
    private void showColorPopup(View anchorView) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View colorMenuView = inflater.inflate(R.layout.color_menu, null);

        colorPopupWindow = new PopupWindow(colorMenuView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        // Afficher la popup juste en dessous du bouton
        colorPopupWindow.showAsDropDown(anchorView, 0, 0);

        Button colorRed = colorMenuView.findViewById(R.id.colorRed);
        Button colorGreen = colorMenuView.findViewById(R.id.colorGreen);
        Button colorBlue = colorMenuView.findViewById(R.id.colorBlue);
        Button colorYellow = colorMenuView.findViewById(R.id.colorYellow);
        Button colorPurple = colorMenuView.findViewById(R.id.colorPurple);
        Button colorMagenta = colorMenuView.findViewById(R.id.colorMagenta);
        Button colorOrange = colorMenuView.findViewById(R.id.colorOrange);

        colorRed.setOnClickListener(v -> setColor(Color.RED));
        colorGreen.setOnClickListener(v -> setColor(Color.GREEN));
        colorBlue.setOnClickListener(v -> setColor(Color.BLUE));
        colorYellow.setOnClickListener(v -> setColor(Color.YELLOW));
        colorPurple.setOnClickListener(v -> setColor(Color.MAGENTA));
        colorMagenta.setOnClickListener(v -> setColor(Color.MAGENTA));
        colorOrange.setOnClickListener(v -> setColor(Color.parseColor("#FFA500")));
    }

    private void setColor(int color) {
        drawingView.setCurrentColor(color);
        if (colorPopupWindow != null && colorPopupWindow.isShowing()) {
            colorPopupWindow.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        // Laisser vide pour désactiver le bouton retour
    }
}

