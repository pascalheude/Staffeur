package com.assistanceinformatiquetoulouse.roulezrose.staffeur;

import java.util.HashMap;
import java.util.Map;

// Enum Presence
public enum Presence {
    ABSENT (0),
    PRESENT (1),
    INDECIS (2),
    AUCUNE (3);
    private int pPresence;
    private static Map pMap = new HashMap<>();

    // Constructeur
    Presence(int presence) {
            this.pPresence = presence;
        }

    static {
        for (Presence presence : Presence.values()) {
            pMap.put(presence.pPresence, presence);
        }
    }

    // Méthode getPresenceAsInt
    public int getPresenceAsInt() {
        return(pPresence);
    }

    public static Presence valueOf(int presence) {
        return((Presence) pMap.get(presence));
    }
}