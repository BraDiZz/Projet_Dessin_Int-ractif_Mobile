package com.example.projet_dessin_interactif;

public class DessinModel {
    private String titre;
    private String auteur;
    private byte[] imageBytes; // Image sous forme de tableau de bytes

    private long dessin_id;

    public DessinModel(String titre, String auteur, byte[] imageBytes,long id) {
        this.titre = titre;
        this.auteur = auteur;
        this.imageBytes = imageBytes;
        this.dessin_id=id;
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

    public long getDessinId(){ return dessin_id;}
}

