package com.example.projet_dessin_interactif;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class DessinAdapter_prenium extends RecyclerView.Adapter<DessinAdapter_prenium.DessinViewHolder> {

    private List<DessinModel> dessinsList;

    private BDD dbHelper;

    private Context c;

    // Constructeur de l'adaptateur
    public DessinAdapter_prenium(List<DessinModel> dessinsList, Context context) {

        this.dessinsList = dessinsList;

        c=context;
        dbHelper = new BDD(context);
    }

    // ViewHolder pour les dessins
    public class DessinViewHolder extends RecyclerView.ViewHolder {
        TextView nomTextView;
        TextView auteurTextView;
        long id_img;
        ImageView imageView;
        Button modifierButton;

        public DessinViewHolder(@NonNull View itemView) {
            super(itemView);
            nomTextView = itemView.findViewById(R.id.titreTextView);
            auteurTextView = itemView.findViewById(R.id.auteurTextView);
            imageView = itemView.findViewById(R.id.imageView);
            modifierButton = itemView.findViewById(R.id.modifierButton);
        }
    }

    // Méthode pour créer de nouveaux ViewHolder
    @NonNull
    @Override
    public DessinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dessin_prenium, parent, false);
        return new DessinViewHolder(view);
    }

    // Méthode pour lier les données à chaque ViewHolder
    @Override
    public void onBindViewHolder(@NonNull DessinViewHolder holder, int position) {
        DessinModel dessin = dessinsList.get(position);
        holder.nomTextView.setText(dessin.getNom());
        holder.auteurTextView.setText(dessin.getAuteur());
        holder.id_img = dessin.getDessinId();
        // Charger l'image depuis le tableau de bytes dans dessin.getImageBytes() et l'afficher dans imageView
        byte[] imageBytes = dessin.getImageBytes();
        if (imageBytes != null) {
            holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
        }

        // gérer le bouton télécharger
        holder.modifierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Télécharger l'image en utilisant l'ID du dessin
                Bitmap image = dbHelper.downloadImage(holder.id_img);

                // Vérifier si l'image a été téléchargée avec succès
                if (image != null) {
                    // Enregistrer l'image dans le stockage externe de l'appareil
                    if (saveImageToExternalStorage(c, image)) {
                        Toast.makeText(c, "L'image a été téléchargée avec succès", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(c, "Impossible de télécharger l'image", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(c, "Impossible de télécharger l'image", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

// Méthode pour enregistrer une image Bitmap dans le stockage externe de l'appareil
        private boolean saveImageToExternalStorage(Context context, Bitmap image) {
            // Vérifier si le stockage externe est disponible en écriture
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // Créer un répertoire pour les images dans le stockage externe
                File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "NomDeVotreApplication");
                if (!directory.exists()) {
                    directory.mkdirs(); // Créer le répertoire s'il n'existe pas
                }

                // Créer un fichier dans le répertoire avec un nom unique
                File file = new File(directory, "image.jpg");

                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.close();
                    // Actualiser la galerie pour afficher l'image nouvellement enregistrée
                    MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, null);
                    return true; // L'image a été enregistrée avec succès
                } catch (IOException e) {
                    e.printStackTrace(); // Affichez l'erreur dans le logcat pour le débogage
                    return false; // Impossible d'enregistrer l'image
                }
            } else {
                return false; // Le stockage externe n'est pas disponible en écriture
            }
        }





    // Méthode pour obtenir le nombre total d'éléments dans la liste
    @Override
    public int getItemCount() {
        return dessinsList.size();
    }
}

