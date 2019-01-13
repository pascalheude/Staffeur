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

    // Méthode ecrirePresence
    public void ecrirePresence(Presence presence) {
        this.pPresence = presence;
    }

    // Méthode equals
    public boolean equals(PresenceRandonnee presence_randonnee) {
        if ((this.pPresence == presence_randonnee.pPresence) &&
            (this.pRandoDate == presence_randonnee.pRandoDate) &&
            (this.pRandoId == presence_randonnee.pRandoId)) {
            return(true);
        }
        else {
            return(false);
        }
    }
}
