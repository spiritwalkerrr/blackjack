package com.team.blackjack.model;

import java.util.Scanner;

public class Spiel {

	public static void main(String[] args) {
		boolean spielNeustart = true;
		Scanner scanner = new Scanner(System.in);
		// spiel neustarten
		while (spielNeustart) {
			// Hier kannst du den Code zum Starten und Steuern des Spiels einfügen

			int anzahlSpieler = 0;

			System.out.println("Willkommen beim Blackjack-Spiel!");

			while (anzahlSpieler < 1 || anzahlSpieler > 3) {
				System.out.print("Bitte gib die Anzahl der Spieler ein (1-3): ");
				try {
					anzahlSpieler = scanner.nextInt();
					scanner.nextLine(); // Diesen Zeilenumbruch hinzufügen

					if (anzahlSpieler < 1 || anzahlSpieler > 3) {
						System.out.println("Ungültige Eingabe. Bitte wähle zwischen 1 und 3 Spielern.");
					}
				} catch (Exception e) {
					System.out.println("Bitte gib eine gültige Zahl ein.");
					scanner.next(); // Fehlerhaften Input überspringen
				}
			}

			// Spieler-Objekte initialisieren
			Spieler[] spielerListe = new Spieler[anzahlSpieler + 1]; // +1 für den Dealer

			for (int i = 0; i < anzahlSpieler; i++) {
				spielerListe[i] = new Spieler();
			}

			// Dealer als letzten "Spieler" hinzufügen
			spielerListe[anzahlSpieler] = new Spieler(); // Dies ist der Dealer

			System.out.println("Es spielen " + anzahlSpieler + " Spieler (plus Dealer).");

			// code einer runde
			while (true) {

				// neues deck
				Deck deck = new Deck();
				deck.mischeDeck();

				// Karten austeilen
				for (int i = 0; i < 2; i++) { // Jeder bekommt zwei Karten
					for (Spieler spieler : spielerListe) {
						if (spieler.kannWetten()) { // ausser spieler, die nicht wetten können
							Karte karte = deck.zieheKarte();
							spieler.addKarte(karte, "hand");
						}
					}
				}

				

				// Die Hände jedes Spielers anzeigen
				for (int i = 0; i < spielerListe.length; i++) {
					if (i == anzahlSpieler) { // Für den Dealer
						System.out.println("Hand vom Dealer: ");
						System.out.println(spielerListe[i].zeigeHand("hand"));
					} else {
						System.out.println("Hand von Spieler " + (i + 1) + ":");
						System.out.println(spielerListe[i].zeigeHand("hand"));
					}
				}
				// spieler können "geld" wetten
				for (int i = 0; i < anzahlSpieler; i++) {
					// nur spieler, die noch wetten können
					if (!spielerListe[i].kannWetten()) {
						System.out.println(
								"Spieler " + (i + 1) + " kann nicht wetten und wird diese Runde übersprungen.");
						continue; // überspringt den aktuellen Durchlauf der Schleife und fährt mit dem nächsten
									// Spieler fort
					}
					int maxWette = Math.min(500, spielerListe[i].getVerfuegbaresGeld());
					// festlegen der wette
					int wette = 0;
					while (wette < 100 || wette > maxWette) {
					    System.out.println("Spieler " + (i + 1) + ", Sie haben " + spielerListe[i].getVerfuegbaresGeld()
					            + " verfügbar. Wie viel möchten Sie setzen? (Mindesteinsatz: 100, Maximaleinsatz: " + maxWette + ")");
					    try {
					        wette = scanner.nextInt();
					        scanner.nextLine();
					        if (wette < 100 || wette > maxWette) {
					            System.out.println(
					                    "Ungültige Wette. Bitte setzen Sie einen Betrag zwischen 100 und " + maxWette + ".");
					        }
					    } catch (Exception e) {
					        System.out.println("Bitte geben Sie eine gültige Wette ein.");
					        scanner.nextLine();  // Hier sollte es scanner.nextLine() statt scanner.next() sein, um den gesamten Zeilenumbruch zu konsumieren
					    }
					}
					spielerListe[i].setAktuellerEinsatz(wette);
					spielerListe[i].subtrahiereGeld(wette);
				}
				System.out.println("Wetteinsätze für diese Runde:");
				for (int i = 0; i < anzahlSpieler; i++) {
					System.out.println("Spieler " + (i + 1) + ": " + spielerListe[i].getAktuellerEinsatz() + " Geld.");
				}

				// Hit oder Stand der Spieler, bis sie aufhören oder "bust" sind.
				for (int i = 0; i < anzahlSpieler; i++) { // Nicht für den Dealer durchlaufen!
					Spieler aktuellerSpieler = spielerListe[i];
					// spieler ausschliessen, die nicht wetten können
					if (!aktuellerSpieler.kannWetten()) {
						System.out.println(
								"Spieler " + (i + 1) + " hat nicht genug Geld und wird diese Runde übersprungen.");
						System.out.println("============== nächster Spieler ==============");
						continue; // überspringt den aktuellen Durchlauf der Schleife und fährt mit dem nächsten
									// Spieler fort
					}

					// Überprüfen, ob der Spieler genug Geld hat, um zu verdoppeln
					if (aktuellerSpieler.getVerfuegbaresGeld() >= 2 * aktuellerSpieler.getAktuellerEinsatz()) {

					    // Überprüfen, ob Split möglich ist
					    boolean kannSplitten = !aktuellerSpieler.getHatGesplittet() && aktuellerSpieler.getHand("hand").size() == 2
					            && aktuellerSpieler.getHand("hand").get(0).getWert() == aktuellerSpieler.getHand("hand").get(1).getWert()
					            && aktuellerSpieler.getHand("hand").get(0).getWert() != Wert.ASS;
					    System.out.println("Hand von Spieler: " + aktuellerSpieler.zeigeHand("hand") + " (Wert: "
								+ aktuellerSpieler.getPunktwert("hand") + ")");

					    String optionen = "Möchten Sie double down (d) oder nein (n)? ";
					    if (kannSplitten) {
					        optionen = "Möchten Sie splitten (s), " + optionen;
					    }

					    System.out.print(optionen);
					    char entscheidung = scanner.next().charAt(0);
					    scanner.nextLine();

					    switch (entscheidung) {
					        case 's':
					            aktuellerSpieler.split();
					            // Hier können Sie auch den aktuellen Einsatz des Spielers verdoppeln, da er
					            Karte gezogeneKarte1 = deck.zieheKarte();
					            aktuellerSpieler.addKarte(gezogeneKarte1, "hand");
					            System.out.println("Zur ersten Hand hinzugefügte Karte: " + gezogeneKarte1);

					            // Eine Karte zur gesplitteten Hand hinzufügen
					            Karte gezogeneKarte2 = deck.zieheKarte();
					            aktuellerSpieler.addKarte(gezogeneKarte2, "split");
					            System.out.println("Zur zweiten Hand hinzugefügte Karte: " + gezogeneKarte2);
					            aktuellerSpieler.setHatGesplittet(true);
					            break;
					        case 'd':
					        	// Den Einsatz des Spielers verdoppeln
					            int aktuellerEinsatz = aktuellerSpieler.getAktuellerEinsatz();
					            aktuellerSpieler.setAktuellerEinsatz(2 * aktuellerEinsatz);
					            aktuellerSpieler.subtrahiereGeld(aktuellerEinsatz); // den zusätzlichen Einsatz vom verfügbaren Geld des Spielers abziehen

					            // Dem Spieler eine zusätzliche Karte geben
					            Karte gezogeneKarte = deck.zieheKarte();
					            aktuellerSpieler.addKarte(gezogeneKarte, "hand");
					            System.out.println("Zusätzliche Karte für Double Down: " + gezogeneKarte);
					            System.out.println("Hand von Spieler: " + aktuellerSpieler.zeigeHand("hand") + " (Wert: "
										+ aktuellerSpieler.getPunktwert("hand") + ")");

					            // Den Double Down Status des Spielers auf true setzen
					            aktuellerSpieler.setHatDoubleDownGemacht(true);
					            break;
					        case 'n':
					            // Weitermachen ohne Split oder Double Down.
					            break;
					        default:
					            System.out.println("Ungültige Auswahl. Bitte geben Sie 's', 'd' oder 'n' ein.");
					            break;
					    }
					}

					// hit oder stand?

					if (!aktuellerSpieler.getHatDoubleDownGemacht()) {
					    // Für die Haupt-Hand:
					    hitOderStand(aktuellerSpieler, deck, "hand");

					    // Wenn der Spieler gesplittet hat, dann auch für die splitHand:
					    if (aktuellerSpieler.getHatGesplittet()) {
					        hitOderStand(aktuellerSpieler, deck, "split");
					    }
					}
					System.out.println("============== nächster Spieler ==============");

				}
				// Dealer-logik - macht ja keine Entscheidungen
				Spieler dealer = spielerListe[anzahlSpieler]; // Annahme: Der Dealer ist das letzte Element in
																// spielerListe

				// Dealer zeigt seine erste Karte
				System.out.println("Dealer zeigt: " + dealer.getHand("hand").get(0));

				// Nun zieht der Dealer Karten, solange sein Punktwert unter 17 liegt
				while (dealer.getPunktwert("hand") < 17) {
					Karte gezogeneKarte = deck.zieheKarte();
					dealer.addKarte(gezogeneKarte, "hand");
					System.out.println("Dealer zieht: " + gezogeneKarte);
				}

				// Nachdem der Dealer fertig ist, werden alle seine Karten und sein Punktwert
				// gezeigt
				System.out.println(
						"Hand des Dealers: " + dealer.getHand("hand") + " (Wert: " + dealer.getPunktwert("hand") + ")");

				// wie geht die runde aus?
				int dealerPunktwert = dealer.getPunktwert("hand");

				// Überprüfen, ob der Dealer überkauft hat
				boolean dealerBusted = (dealerPunktwert > 21);

				if (dealerBusted) {
					System.out.println("Dealer hat überkauft mit einem Wert von " + dealerPunktwert + "!");
				}

				// Vergleich des Punktwerts jedes Spielers mit dem des Dealers
				for (int i = 0; i < anzahlSpieler; i++) {
					Spieler aktuellerSpieler = spielerListe[i];

					// Für beide Hände: hand und split
					for (String handArt : new String[] { "hand", "split" }) {
						// Wenn der Spieler nicht gesplittet hat und die aktuelle handArt "split" ist,
						// überspringen
						if (!aktuellerSpieler.getHatGesplittet() && "split".equals(handArt)) {
							continue;
						}

						int spielerPunktwert = aktuellerSpieler.getPunktwert(handArt);
						int einsatz = aktuellerSpieler.getAktuellerEinsatz();

						if (spielerPunktwert > 21) {
							System.out.println("Spieler " + (i + 1) + " (" + handArt + ") hat überkauft und verliert "
									+ einsatz + " Geld!");
						} else if (dealerBusted || spielerPunktwert > dealerPunktwert) {
							System.out.println("Spieler " + (i + 1) + " (" + handArt + ") gewinnt mit einem Wert von "
									+ spielerPunktwert + " und erhält " + (2 * einsatz) + " Geld zurück!");
							aktuellerSpieler.addiereGeld(2 * einsatz); // Einsatz verdoppeln
						} else if (spielerPunktwert == dealerPunktwert) {
							System.out.println("Spieler " + (i + 1) + " (" + handArt
									+ ") und Dealer haben einen Push mit einem Wert von " + spielerPunktwert
									+ ". Spieler " + (i + 1) + " erhält seinen Einsatz von " + einsatz
									+ " Geld zurück.");
							aktuellerSpieler.addiereGeld(einsatz); // Einsatz zurück
						} else {
							System.out.println("Spieler " + (i + 1) + " (" + handArt
									+ ") verliert gegen den Dealer mit einem Wert von " + spielerPunktwert
									+ " und verliert " + einsatz + " Geld!");
						}
					}
				}
				// hand jedes spielers leeren
				for (Spieler spieler : spielerListe) {
					spieler.leereHand();
					spieler.setHatGesplittet(false);
					spieler.setHatDoubleDownGemacht(false);
				}
				// checken, ob spieler noch genug geld haben
				for (Spieler spieler : spielerListe) {
					if (spieler.getVerfuegbaresGeld() >= 100) {
						spieler.setKannWetten(true);
					} else {
						spieler.setKannWetten(false);
					}
				}

				// rundenende
				boolean spielBeenden = false;
				int aktiveSpieler = 0;
				// schauen, ob alle verloren haben oder ein spieler 5000+ hat (und damit das
				// spiel gewinnt)
				for (int i = 0; i < spielerListe.length; i++) {
					Spieler spieler = spielerListe[i];
					if (spieler.getVerfuegbaresGeld() > 5000) {
						System.out.println("Spieler " + (i + 1) + " hat mehr als 5000! Das Casino wirft Sie raus!");
						spielBeenden = true;
					}
					if (spieler.kannWetten()) {
						aktiveSpieler++;
					}
				}

				if (aktiveSpieler == 0) {
					System.out.println("Alle Spieler sind pleite! Das Casino gewinnt!");
					spielBeenden = true;
				}

				if (spielBeenden) {
					break; // Das beendet den aktuellen Rundenschleifen-Durchlauf und fährt mit dem
							// Programm nach dem Schleifenblock fort.
				}
				// spieler können das spiel unterbrechen und damit ein neues beginnen.
				System.out.print("Möchten Sie eine weitere Runde spielen? (j/n): ");
				String entscheidung = scanner.nextLine().trim().toLowerCase();
				if (!entscheidung.equals("j")) {
					break;
				}
			}
			// ausgeben, wieviel geld die spieler hatten.
			for (int i = 0; i < spielerListe.length - 1; i++) { // -1 length wegen dealer
				System.out.println("Spieler " + (i + 1) + " hat zum Schluss "
						+ (spielerListe[i + 1]).getVerfuegbaresGeld() + " übrig.");
			}
			System.out.print("Möchten Sie das gesamte Spiel neu starten? (j/n): ");
			String neustartEntscheidung = scanner.nextLine().trim().toLowerCase();

			if (!neustartEntscheidung.equals("j")) {
				spielNeustart = false;
				System.out.println("Danke fürs Spielen!");
			}

		}

	}

	private static void hitOderStand(Spieler spieler, Deck deck, String handArt) {
		boolean weitermachen = true;
		Scanner scanner = new Scanner(System.in);

		while (weitermachen && spieler.getPunktwert(handArt) < 21) {
			System.out.println("Hand (" + handArt + ") von Spieler: " + spieler.zeigeHand(handArt) + " (Wert: "
					+ spieler.getPunktwert(handArt) + ")");
			System.out.print("Wählen Sie: 'h' für hit oder 's' für stand: ");

			char entscheidung = scanner.next().charAt(0);
			switch (entscheidung) {
			case 'h':
				Karte gezogeneKarte = deck.zieheKarte();
				spieler.addKarte(gezogeneKarte, handArt);
				System.out.println("Sie haben eine " + gezogeneKarte + " gezogen.");

				if (spieler.getPunktwert(handArt) == 21) {
					System.out.println("Glückwunsch! Sie haben genau 21 Punkte.");
					weitermachen = false;
				} else if (spieler.getPunktwert(handArt) > 21) {
					System.out.println("Bust! Ihre Punktzahl ist über 21.");
					weitermachen = false;
				}
				break;
			case 's':
				weitermachen = false;
				break;
			default:
				System.out.println("Ungültige Eingabe. Bitte geben Sie 'h' oder 's' ein.");
				break;
			}
		}
	}

}
