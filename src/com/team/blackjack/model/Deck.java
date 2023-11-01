package com.team.blackjack.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

	private List<Karte> karten;

	public Deck() {
		this.karten = new ArrayList<>();

		// Für jede Farbe und jeden Wert eine Karte erstellen und zum Deck hinzufügen
		for (Farbe farbe : Farbe.values()) {
			for (Wert wert : Wert.values()) {
				karten.add(new Karte(farbe, wert));
			}
		}

		mischeDeck();
	}

	public void mischeDeck() {
		Collections.shuffle(karten);
	}

	public Karte zieheKarte() {
		if (karten.isEmpty()) {
			throw new IllegalStateException("Das Deck ist leer.");
		}

		return karten.remove(0); // Die oberste Karte ziehen
	}

	public int verbleibendeKarten() {
		return karten.size();
	}
}

