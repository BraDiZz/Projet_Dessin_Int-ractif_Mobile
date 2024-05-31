package com.example.projet_dessin_interactif;

public class DessinModel {
    private String titre;
    private String auteur;
    private byte[] imageBytes; // Image sous forme de tableau de bytes

    public DessinModel(String titre, String auteur, byte[] imageBytes) {
        this.titre = titre;
        this.auteur = auteur;
        this.imageBytes = imageBytes;
    }

    public String getNom() {
        return titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }
}

