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

    @Override
    public String toString() {
        return wert + " von " + farbe;
    }

}

enum Farbe {
    HERZ, KARO, PIK, KREUZ
}

enum Wert {
    ZWEI(2), DREI(3), VIER(4), FÜNF(5), SECHS(6), SIEBEN(7), ACHT(8), NEUN(9), ZEHN(10), BUBE(10), DAME(10), KOENIG(10), ASS(11);

    private final int punktwert;

    Wert(int punktwert) {
        this.punktwert = punktwert;
    }

    public int getPunktwert() {
        return punktwert;
    }
}
