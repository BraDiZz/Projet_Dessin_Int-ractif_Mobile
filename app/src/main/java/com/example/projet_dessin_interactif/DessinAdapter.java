package com.example.projet_dessin_interactif;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DessinAdapter extends RecyclerView.Adapter<DessinAdapter.DessinViewHolder> {

    private List<DessinModel> dessinsList;

    // Constructeur de l'adaptateur
    public DessinAdapter(List<DessinModel> dessinsList) {
        this.dessinsList = dessinsList;
    }

    // ViewHolder pour les dessins
    public class DessinViewHolder extends RecyclerView.ViewHolder {
        TextView nomTextView;
        TextView auteurTextView;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dessin, parent, false);
        return new DessinViewHolder(view);
    }

    // Méthode pour lier les données à chaque ViewHolder
    @Override
    public void onBindViewHolder(@NonNull DessinViewHolder holder, int position) {
        DessinModel dessin = dessinsList.get(position);
        holder.nomTextView.setText(dessin.getNom());
        holder.auteurTextView.setText(dessin.getAuteur());
        // Charger l'image depuis le tableau de bytes dans dessin.getImageBytes() et l'afficher dans imageView
        byte[] imageBytes = dessin.getImageBytes();
        if (imageBytes != null) {
            holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
        }

        // Gérer le clic sur le bouton "Modifier"
        holder.modifierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ouvrir le dessin dans une nouvelle activité ou effectuer une action spécifique
                // Tu devras implémenter cette fonctionnalité en fonction de tes besoins
            }
        });
    }

    // Méthode pour obtenir le nombre total d'éléments dans la liste
    @Override
    public int getItemCount() {
        return dessinsList.size();
    }
}

