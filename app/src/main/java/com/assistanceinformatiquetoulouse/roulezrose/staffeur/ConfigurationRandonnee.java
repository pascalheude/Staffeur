package com.assistanceinformatiquetoulouse.roulezrose.staffeur;

import android.graphics.Color;

import java.util.HashMap;

// Class Randonnée
public class ConfigurationRandonnee {
    // Attributs privés
    private HashMap<Integer, Integer> pHashMapCouleur;
    private int pCouleurParDefaut;

    // Constructeur
    public ConfigurationRandonnee() {
        pHashMapCouleur = new HashMap<>();
        pCouleurParDefaut = Color.parseColor("#C0C0C0");
    }

    // Constructeur
    public ConfigurationRandonnee(HashMap<Integer, Integer> hashmap) {
        pHashMapCouleur = new HashMap<>(hashmap);
        pCouleurParDefaut = Color.parseColor("#C0C0C0");
    }

    // Méthode effacerRandonnee
    public void effacerRandonnee() {
        pHashMapCouleur.clear();
    }

    // Méthode ajouterCouleur
    // Ajouter une couleur aux HashMaps
    public void ajouterCouleur(int id, String couleur) {
        pHashMapCouleur.put(id, Color.parseColor(couleur));
    }
    public void ajouterCouleur(int id, int couleur) {
        pHashMapCouleur.put(id, couleur);
    }

    // Méthode lireCouleur
    // Lire une couleur à partir de l'id
    public int lireCouleur(int id) {
        if (pHashMapCouleur.containsKey(id)) {
            return (pHashMapCouleur.get(id));
        }
        else {
            return(pCouleurParDefaut);
        }
    }

    // Méthode lireListeCouleur
    public HashMap<Integer, Integer> lireListeCouleur() {
        return(pHashMapCouleur);
    }
}
