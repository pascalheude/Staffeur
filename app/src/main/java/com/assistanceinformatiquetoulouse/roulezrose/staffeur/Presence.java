package com.assistanceinformatiquetoulouse.roulezrose.staffeur;

// Enum Presence
public enum Presence {
    ABSENT (0),
    PRESENT (1),
    INDECIS (2),
    AUCUNE (3);
    private int pPresence;

    // Constructeur
    Presence(int presence) {
            this.pPresence = presence;
        }

    // Méthode toString
    public  String toString() {
        switch(pPresence) {
            case 0 :
                return("Absent");
            case 1 :
                return("Présent");
            case 2 :
                return("Indécis");
            case 3 :
                return("Aucune");
        }
        return("");
    }
}
