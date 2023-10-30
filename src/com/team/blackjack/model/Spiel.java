package com.team.blackjack.model;

import java.util.Scanner;

public class Spiel {

	public static void main(String[] args) {
		// Hier kannst du den Code zum Starten und Steuern des Spiels einfügen
		Scanner scanner = new Scanner(System.in);

		while (true) {

			int anzahlSpieler = 0;

			System.out.println("Willkommen beim Blackjack-Spiel!");

			while (anzahlSpieler < 1 || anzahlSpieler > 3) {
			    System.out.print("Bitte gib die Anzahl der Spieler ein (1-3): ");
			    try {
			        anzahlSpieler = scanner.nextInt();
			        scanner.nextLine();  // Diesen Zeilenumbruch hinzufügen

			        if (anzahlSpieler < 1 || anzahlSpieler > 3) {
			            System.out.println("Ungültige Eingabe. Bitte wähle zwischen 1 und 3 Spielern.");
			        }
			    } catch (Exception e) {
			        System.out.println("Bitte gib eine gültige Zahl ein.");
			        scanner.next(); // Fehlerhaften Input überspringen
			    }
			}

			// neues deck
			Deck deck = new Deck();
			deck.mischeDeck();

			System.out.println("Es spielen " + anzahlSpieler + " Spieler (plus Dealer).");

			// Liste von Spielern erstellen (plus ein Platz für den Dealer)
			Spieler[] spielerListe = new Spieler[anzahlSpieler + 1];

			// Spieler-Objekte initialisieren
			for (int i = 0; i < anzahlSpieler; i++) {
				spielerListe[i] = new Spieler();
			}

			// Dealer als letzten "Spieler" hinzufügen
			spielerListe[anzahlSpieler] = new Spieler(); // Dies ist der Dealer

			// Karten austeilen
			for (int i = 0; i < 2; i++) { // Jeder bekommt zwei Karten
				for (Spieler spieler : spielerListe) {
					Karte karte = deck.zieheKarte();
					spieler.addKarte(karte);
				}
			}

			// Attribut "istKarteOffen" für alle Spieler und den Dealer setzen
			for (Spieler spieler : spielerListe) {
				spieler.setKarteOffen(true);
			}

			// Die Hände jedes Spielers anzeigen
			for (int i = 0; i < spielerListe.length; i++) {
				if (i == anzahlSpieler) { // Für den Dealer
					System.out.println("Hand vom Dealer: ");
					System.out.println(spielerListe[i].zeigeHand());
				} else {
					System.out.println("Hand von Spieler " + (i + 1) + ":");
					System.out.println(spielerListe[i].zeigeHand());
				}
			}

			// Hit oder Stand der Spieler, bis sie aufhören oder "bust" sind.
			for (int i = 0; i < anzahlSpieler; i++) { // Nicht für den Dealer durchlaufen!
				Spieler aktuellerSpieler = spielerListe[i];
				boolean weitermachen = true;

				while (weitermachen && aktuellerSpieler.getPunktwert() < 21) {
					System.out.println("Hand von Spieler " + (i + 1) + ": " + aktuellerSpieler.getHand() + " (Wert: "
							+ aktuellerSpieler.getPunktwert() + ")");
					System.out.print("Wählen Sie: 'h' für hit oder 's' für stand: ");

					char entscheidung = scanner.next().charAt(0); // Erste Zeichen der Eingabe lesen
					switch (entscheidung) {
					case 'h':
						Karte gezogeneKarte = deck.zieheKarte();
						aktuellerSpieler.addKarte(gezogeneKarte);
						System.out.println("Sie haben eine " + gezogeneKarte + " gezogen.");
						System.out.println("Die Hand ist nun " + (i + 1) + ": " + aktuellerSpieler.getHand()
								+ " (Wert: " + aktuellerSpieler.getPunktwert() + ")");
						break;
					case 's':
						weitermachen = false;
						break;
					default:
						System.out.println("Ungültige Eingabe. Bitte geben Sie 'h' oder 's' ein.");
						break;
					}

					if (aktuellerSpieler.getPunktwert() > 21) {
						System.out.println("Bust! Ihre Punktzahl ist über 21.");
					}
				}
				System.out.println("============== nächster Spieler ==============");

			}
			// Dealer-logik - macht ja keine Entscheidungen
			Spieler dealer = spielerListe[anzahlSpieler]; // Annahme: Der Dealer ist das letzte Element in spielerListe

			// Dealer zeigt seine erste Karte
			System.out.println("Dealer zeigt: " + dealer.getHand().get(0));

			// Nun zieht der Dealer Karten, solange sein Punktwert unter 17 liegt
			while (dealer.getPunktwert() < 17) {
				Karte gezogeneKarte = deck.zieheKarte();
				dealer.addKarte(gezogeneKarte);
				System.out.println("Dealer zieht: " + gezogeneKarte);
			}

			// Nachdem der Dealer fertig ist, werden alle seine Karten und sein Punktwert
			// gezeigt
			System.out.println("Hand des Dealers: " + dealer.getHand() + " (Wert: " + dealer.getPunktwert() + ")");

			// wie geht die runde aus?
			int dealerPunktwert = dealer.getPunktwert();

			// Überprüfen, ob der Dealer überkauft hat
			boolean dealerBusted = (dealerPunktwert > 21);

			if (dealerBusted) {
				System.out.println("Dealer hat überkauft mit einem Wert von " + dealerPunktwert + "!");
			}

			// Vergleich des Punktwerts jedes Spielers mit dem des Dealers
			for (int i = 0; i < anzahlSpieler; i++) {
				Spieler aktuellerSpieler = spielerListe[i];
				int spielerPunktwert = aktuellerSpieler.getPunktwert();

				if (spielerPunktwert > 21) {
					System.out.println("Spieler " + (i + 1) + " hat überkauft und verliert!");
				} else if (dealerBusted || spielerPunktwert > dealerPunktwert) {
					System.out.println("Spieler " + (i + 1) + " gewinnt mit einem Wert von " + spielerPunktwert + "!");
				} else if (spielerPunktwert == dealerPunktwert) {
					System.out.println("Spieler " + (i + 1) + " und Dealer haben einen Push mit einem Wert von "
							+ spielerPunktwert + "!");
				} else {
					System.out.println("Spieler " + (i + 1) + " verliert gegen den Dealer mit einem Wert von "
							+ spielerPunktwert + "!");
				}
			}
			
			// noch ne runde?
			scanner.nextLine(); // Verbrauche den verbleibenden Zeilenumbruch
			System.out.print("Möchten Sie eine weitere Runde spielen? (j/n): ");
		    String entscheidung = scanner.nextLine().trim().toLowerCase();

		    if (!entscheidung.equals("j")) {
		        System.out.println("Danke fürs Spielen!");
		        break;
		    }

		    // Karten einsammeln und den Kartenstapel neu mischen
		    deck = new Deck();
		    deck.mischeDeck();
		    for (Spieler spieler : spielerListe) {
		        spieler.getHand().clear();
		    }
		    dealer.getHand().clear();
		}
		
	}
}
