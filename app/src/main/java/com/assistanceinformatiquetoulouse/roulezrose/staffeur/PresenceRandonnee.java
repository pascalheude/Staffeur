package com.assistanceinformatiquetoulouse.roulezrose.staffeur;

import java.io.Serializable;
import java.util.Date;

// Classe PresenceRandonnee
public class PresenceRandonnee implements Serializable {
    // Attributs privés
    int pRandoId;
    Date pRandoDate;
    int pRandoType;
    int pPresence;

    // Constructeur
    public PresenceRandonnee(int rando_id, Date date, int rando_type, int presence) {
        this.pRandoId = rando_id;
        this.pRandoDate = date;
        this.pRandoType = rando_type;
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
    public int lirePresence() {
        return(this.pPresence);
    }

    // Méthode ecrirePresence
    public void ecrirePresence(int presence) {
        this.pPresence = presence;
    }

    // Méthode lireTypeRandonnee
    public int lireTypeRandonnee() {
        return(this.pRandoType);
    }

    // Méthode equals
    public boolean equals(PresenceRandonnee presence_randonnee) {
        if ((this.pPresence == presence_randonnee.pPresence) &&
            (this.pRandoDate.compareTo(presence_randonnee.pRandoDate) == 0) &&
            (this.pRandoType == presence_randonnee.pRandoType) &&
            (this.pRandoId == presence_randonnee.pRandoId)) {
            return(true);
        }
        else {
            return(false);
        }
    }

    // Méthode clone
    public PresenceRandonnee clone() {
        PresenceRandonnee lPresenceRandonnee = new PresenceRandonnee(this.pRandoId,
                                                                     this.pRandoDate,
                                                                     this.pRandoType,
                                                                     this.pPresence);
        return(lPresenceRandonnee);
    }
}