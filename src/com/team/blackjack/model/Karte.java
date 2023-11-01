package com.team.blackjack.model;

public class Karte {

	private Farbe farbe;
	private Wert wert;

	public Karte(Farbe farbe, Wert wert) {
		this.farbe = farbe;
		this.wert = wert;
	}

	public Farbe getFarbe() {
		return farbe;
	}

	public Wert getWert() {
		return wert;
	}
	
	public int getPunkte() {
	    switch (this.wert) {
	        case ZWEI: return 2;
	        case DREI: return 3;
	        case VIER: return 4;
	        case FÜNF: return 5;
	        case SECHS: return 6;
	        case SIEBEN: return 7;
	        case ACHT: return 8;
	        case NEUN: return 9;
	        case ZEHN: 
	        case BUBE: 
	        case DAME: 
	        case KOENIG: return 10;
	        case ASS: return 11; 
	        default: return 0; // Dies sollte nicht passieren
	    }
	}

	@Override
	public String toString() {
		return wert + " von " + farbe;
	}

}

enum Farbe {
	HERZ, KARO, PIK, KREUZ
}

enum Wert {
	ZWEI(2), DREI(3), VIER(4), FÜNF(5), SECHS(6), SIEBEN(7), ACHT(8), NEUN(9), ZEHN(10), BUBE(10), DAME(10), KOENIG(10),
	ASS(11);

	private final int punktwert;

	Wert(int punktwert) {
		this.punktwert = punktwert;
	}

	public int getPunktwert() {
		return punktwert;
	}
}
