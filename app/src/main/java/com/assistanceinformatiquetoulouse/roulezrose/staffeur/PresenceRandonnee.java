package com.assistanceinformatiquetoulouse.roulezrose.staffeur;

import java.io.Serializable;
import java.util.Date;

// Classe PresenceRandonnee
public class PresenceRandonnee implements Serializable {
    // Attributs privés
    int pRandoId;
    Date pRandoDate;
    Presence pPresence;

    // Constructeur
    public PresenceRandonnee(int rando_id, Date date, Presence presence) {
        this.pRandoId = rando_id;
        this.pRandoDate = date;
        this.pPresence = presence;
    }

    // Méthode lireRandonneeId
    public int lireRandonneeId() {
        return(this.pRandoId);
    }

    // Méthode lireDate
    public Date lireDate() {
        return(this.pRandoDate);
    }

    // Méthode lirePresence
    public Presence lirePresence() {
        return(this.pPresence);
    }
}
