package com.example.projet_dessin_interactif;

import android.content.Context;
import android.content.Intent;
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

    private Context c;

    // Constructeur de l'adaptateur
    public DessinAdapter(List<DessinModel> dessinsList, Context context) {
        this.dessinsList = dessinsList;
        this.c=context;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dessin, parent, false);
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

        // Gérer le clic sur le bouton "Modifier"
        holder.modifierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, ViewDrawingActivity.class);
                intent.putExtra("dessin_id", holder.id_img);
                c.startActivity(intent);
            }
        });
    }

    // Méthode pour obtenir le nombre total d'éléments dans la liste
    @Override
    public int getItemCount() {
        return dessinsList.size();
    }
}

