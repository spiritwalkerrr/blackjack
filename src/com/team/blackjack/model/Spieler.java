package com.team.blackjack.model;

import java.util.ArrayList;
import java.util.List;

public class Spieler {
	private List<Karte> hand = new ArrayList<>();
	private boolean hatGesplittet = false;
	private boolean hatDoubleDownGemacht = false;
	private List<Karte> splitHand = new ArrayList<>();
	private boolean istKarteOffen = false; // Neues Attribut
	private int verfuegbaresGeld = 1000; // Neues Attribut
	private int aktuellerEinsatz;
	private boolean kannWetten = true;

	// Karte zur Hand des Spielers hinzufügen
	public void addKarte(Karte karte, String handWahl) {
	    if ("hand".equals(handWahl)) {
	        hand.add(karte);
	    } else if ("split".equals(handWahl)) {
	        splitHand.add(karte);
	    }
	}

	// Gesamtpunktwert der Hand zurückgeben
	public int getPunktwert(String handArt) {
	    int punktwert = 0;
	    int anzahlAsse = 0;
	    List<Karte> aktuelleHand = new ArrayList<>();

	    if ("hand".equals(handArt)) {
	        aktuelleHand = hand;
	    } else if ("split".equals(handArt)) {
	        aktuelleHand = splitHand;
	    }

	    for (Karte karte : aktuelleHand) {
	        punktwert += karte.getWert().getPunktwert();

	        if (karte.getWert() == Wert.ASS) {
	            anzahlAsse++;
	        }
	    }

	    // Asse können als 1 oder 11 gewertet werden, abhängig von der Gesamtpunktzahl
	    while (punktwert > 21 && anzahlAsse > 0) {
	        punktwert -= 10; // Das Ass von 11 Punkten auf 1 Punkt reduzieren
	        anzahlAsse--;
	    }

	    return punktwert;
	}

	// Hand des Spielers zurückgeben (zum Ausgeben oder für Spielentscheidungen)
	public List<Karte> getHand(String handArt) {
	    if ("split".equals(handArt)) {
	        return splitHand;
	    }
	    return hand;
	}

	@Override
	public String toString() {
		return hand.toString();
	}

	public String zeigeHand(String handWahl) {
	    List<Karte> gewaehlteHand;

	    // Entscheidung, welche Hand gewählt wird
	    if ("hand".equals(handWahl)) {
	        gewaehlteHand = hand;
	    } else if ("split".equals(handWahl)) {
	        gewaehlteHand = splitHand;
	    } else {
	        return "Ungültige Handauswahl";
	    }

	    if (gewaehlteHand.isEmpty()) {
	        return "Leere Hand";
	    }

	    if (istKarteOffen) {
	        StringBuilder sb = new StringBuilder();
	        sb.append("[Verdeckt], ");
	        for (int i = 1; i < gewaehlteHand.size(); i++) { // Starten bei der zweiten Karte
	            sb.append(gewaehlteHand.get(i));
	            if (i < gewaehlteHand.size() - 1) {
	                sb.append(", ");
	            }
	        }
	        return sb.toString();
	    } else {
	        return gewaehlteHand.toString(); // Wenn keine Karte offen ist, die gesamte Hand zeigen
	    }
	}

	public void leereHand() {
		hand.clear();
		splitHand.clear();
	}

	public int getVerfuegbaresGeld() {
		return verfuegbaresGeld;
	}

	public void setVerfuegbaresGeld(int betrag) {
		this.verfuegbaresGeld = betrag;
	}

	public void addiereGeld(int betrag) {
		this.verfuegbaresGeld += betrag;
	}

	public void subtrahiereGeld(int betrag) {
		if (betrag > this.verfuegbaresGeld) {
			throw new IllegalArgumentException("Spieler hat nicht genug Geld!");
		}
		this.verfuegbaresGeld -= betrag;
	}

	public int getAktuellerEinsatz() {
		return aktuellerEinsatz;
	}

	public void setAktuellerEinsatz(int aktuellerEinsatz) {
		this.aktuellerEinsatz = aktuellerEinsatz;
	}

	public boolean kannWetten() {
		return kannWetten;
	}

	public void setKannWetten(boolean kannWetten) {
		this.kannWetten = kannWetten;
	}
	
	public boolean getHatGesplittet() {
		return this.hatGesplittet;
	}
	public void setHatGesplittet(boolean hatGesplittet) {
        this.hatGesplittet = hatGesplittet;
    }
	// splittet hand
	public void split() {
	    if (hand.size() == 2) { // Voraussetzung ist, dass die ursprüngliche Hand nur zwei Karten hat
	        Karte karteZumSplit = hand.remove(1); // Entfernt die zweite Karte aus der ursprünglichen Hand
	        splitHand.add(karteZumSplit); // Fügt sie der gesplitteten Hand hinzu
	    }
	}
	public boolean getHatDoubleDownGemacht() {
	    return hatDoubleDownGemacht;
	}

	public void setHatDoubleDownGemacht(boolean hatDoubleDownGemacht) {
	    this.hatDoubleDownGemacht = hatDoubleDownGemacht;
	}

}