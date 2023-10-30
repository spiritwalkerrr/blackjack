package com.team.blackjack.model;

import java.util.ArrayList;
import java.util.List;

public class Spieler {
    private List<Karte> hand = new ArrayList<>();
    private boolean istKarteOffen = false;  // Neues Attribut

    // Karte zur Hand des Spielers hinzufügen
    public void addKarte(Karte karte) {
        hand.add(karte);
    }

    // Gesamtpunktwert der Hand zurückgeben
    public int getPunktwert() {
        int punktwert = 0;
        int anzahlAsse = 0;

        for (Karte karte : hand) {
        	punktwert += karte.getWert().getPunktwert();

            if (karte.getWert() == Wert.ASS) {
                anzahlAsse++;
            }
        }

        // Asse können als 1 oder 11 gewertet werden, abhängig von der Gesamtpunktzahl
        while (punktwert > 21 && anzahlAsse > 0) {
            punktwert -= 10;  // Das Ass von 11 Punkten auf 1 Punkt reduzieren
            anzahlAsse--;
        }

        return punktwert;
    }
    public void setKarteOffen(boolean istKarteOffen) {
        this.istKarteOffen = istKarteOffen;
    }

    public boolean getIstKarteOffen() {
        return istKarteOffen;
    }
    

    // Hand des Spielers zurückgeben (zum Ausgeben oder für Spielentscheidungen)
    public List<Karte> getHand() {
        return hand;
    }

    @Override
    public String toString() {
        return hand.toString();
    }
    
    public String zeigeHand() {
        if (hand.isEmpty()) {
            return "Leere Hand";
        }

        if (istKarteOffen) {
            StringBuilder sb = new StringBuilder();
            sb.append("[Verdeckt], ");
            for (int i = 1; i < hand.size(); i++) {  // Starten bei der zweiten Karte
                sb.append(hand.get(i));
                if (i < hand.size() - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        } else {
            return hand.toString();  // Wenn keine Karte offen ist, die gesamte Hand zeigen
        }
    }
}