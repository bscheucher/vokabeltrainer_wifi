package client;

/*Die Klasse bietet eine JavaFX-Anwendung für eine Sprachlernübung, die das Lernen
 * der Bedeutungen von chinesischen Schriftzeichen unterstützt. 
 * Der Code erstellt ein Dialogfeld, in dem Benutzer Vokabeln üben können, 
 * indem sie aus mehreren Optionen die richtige Bedeutung eines bestimmten 
 * chinesischen Wortes auswählen. */

import java.util.ArrayList;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import klassen.Liste;
import klassen.Vokabel;
import klassen.VokabelnListe;

public class ListeUebungsDialog2 extends Dialog<ButtonType> {

	/*
	 * (in der Main-App gewähltes) Liste-Objekt wird für den Gebrauch in der
	 * gesamten Klasse deklariert und später im Konstruktor initialisiert
	 */
	Liste liste;

	// VokabelnListe-Objekt wird erstellt, um Vokabeln aus der DB auszulesen und zu
	// speichern
	VokabelnListe vl;

	/*
	 * Array-List für das Speichern und Handling der aus der DB eingelesenen
	 * Vokabeln in der Klasse wird initialisiert
	 */
	ArrayList<Vokabel> alVokabelnListe = new ArrayList<>();

	/*
	 * Array-List für das Speichern und Handling der gelösten Vokabeln wird
	 * initialisiert
	 */
	ArrayList<Vokabel> alKorrektGeloest = new ArrayList<>();

	/*
	 * Zufallsgenerator für das Holen der korrekten Lösung und der beiden falschen
	 * Lösungen aus alVokabelnListe wird initialisiert. Wird in den Methoden
	 * setVokabel(), setDistraktor1(), setDistraktor2(),
	 * distributeVokabelDistraktoren() und somit auch in setTask() verwendet.
	 */
	Random random = new Random();

	/*
	 * Vokabel-Objekte für die korrekte Lösung und die beiden falschen Lösungen
	 * ("Distraktoren") werden generiert
	 */
	Vokabel vokabel = new Vokabel();
	Vokabel distraktor1;
	Vokabel distraktor2;

	/* Strings für die richtige Bedeutung und die beiden falschen Bedeutungen werden
	 * deklariert */
	String bedeutung1;
	String bedeutung2;
	String bedeutung3;

	// Label für die Anzeige der chinesischen Schriftzeichen wird initialisiert.
	Label lblVokabel = new Label();

	/* Labels für die Anzeige der Multiple-Choice-Auswahlmöglichkeiten werden 
	 * initialisiert.*/
	Label lblBedeutung1 = new Label();
	Label lblBedeutung2 = new Label();
	Label lblBedeutung3 = new Label();

	// String für die getroffene Auswahl wird deklariert.
	String auswahl;

	// Variablen die Ermittlung statistischer Ergebnisse werden initialisiert
	int totalGuesses = 0;
	int correctGuesses = 0;
	int incorrectGuesses = 0;

	// Font für die Darstellung chinesischer Schriftzeichen wird initialisiert
	Font font = new Font("FangSong", 25);

	/*
	 * Der Konstruktor initialisiert den modalen Dialog, indem er ein Liste-Objekt
	 * als Parameter erhält. Verschiedene Felder werden initialisiert, darunter das
	 * Liste-Objekt, eine Array-List von Vokabeln und Randomisierungsvariablen.
	 * Innerhalb des Konstruktors werden mit einem Liste-Objekt verwundene Vokabeln
	 * aus der DB ausgelesen und die erforderlichen Datenstrukturen vorbereitet.
	 */
	public ListeUebungsDialog2(Liste liste) {

		// Liste-Objekt wird initialisiert
		this.liste = liste;

		/*
		 * Die mit der Liste verbundenen Vokabel-Elemente werden aus der Datenbank
		 * ausgelesen und in der Array-List alVokabelnListe gespeichert. Hierfür wird
		 * die REST_API-GET-Methode benutzt.
		 */
		try {
			String line = ServiceFunctions.get("vokabelnliste", String.valueOf(liste.getListeID()));
			vl = new VokabelnListe(line);
			for (Vokabel v : vl.getVokabelnListe())
				alVokabelnListe.add(v);
		} catch (VokabeltrainerException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.titleProperty().set("Fehler");
			alert.setContentText(e.toString());
			alert.showAndWait();
		}

		/*
		 * Mit dieser Methode wird ein zufällinges Vokabel-Element und zwei
		 * "Distraktoren" aus der Array-List alVokabelnListe geholt und nach dem
		 * Zufallsprinzip in der Multiple-Choice-Grid-Pane verteilt.
		 */
		setTask();

		/*
		 * Label für die Darstellung der chinesischen Schriftzeichen erhält Inhalt von
		 * zufällig gewähltem Vokabel-Element und der Font wird auf FangSong gesetzt.
		 */
		lblVokabel.setFont(font);
		lblVokabel.setText(vokabel.getChinesischSimpl());

		/* Check-Boxen für die Auswahl in der Multiple-Choice-Struktur werden
		 * initialisiert */
		CheckBox cb1 = new CheckBox();
		CheckBox cb2 = new CheckBox();
		CheckBox cb3 = new CheckBox();

		// Überprüfen-Button wird initialisiert
		Button btnUeberpruefen = new Button("Überprüfen");

		// Eine Grip-Pane wird initialisiert und mit div. GUI-Elementen gefüllt
		GridPane gp = new GridPane();
		gp.setHgap(5);
		gp.prefHeight(500);
		gp.prefWidth(800);
		gp.add(lblVokabel, 0, 0);
		gp.add(cb1, 0, 1);
		gp.add(lblBedeutung1, 1, 1);
		gp.add(cb2, 0, 2);
		gp.add(lblBedeutung2, 1, 2);
		gp.add(cb3, 0, 3);
		gp.add(lblBedeutung3, 1, 3);
		gp.add(btnUeberpruefen, 0, 4);

		/* Grid-Pane wird in die Dialog-Pane integriert, Größe der Dialog-Pane wird
		 * festgelegt*/
		this.getDialogPane().setContent(gp);
		this.getDialogPane().setPrefSize(500, 200);

		// Schließen-Button-Type wird initialisiert und in die Grid-Pane integriert
		ButtonType closeButton = new ButtonType("Schließen", ButtonBar.ButtonData.CANCEL_CLOSE);
		this.getDialogPane().getButtonTypes().add(closeButton);

		/*
		 * Der Event handler für den "Überprüfen" Button prüft, welche Check-Box
		 * aktiviert ist und vergleicht die ausgewählte Antwort mit der richtigen
		 * Antwort. Wenn die Antwort richtig ist, wird Feedback angezeigt und die
		 * Statistiken werden aktualisiert. Sind alle Aufgaben erledigt, wird eine
		 * abschließende Rückmeldung angezeigt und der Dialog geschlossen. Die
		 * Check-Box-Ecent-Handler stellen sicher, dass jeweils nur ein Check-Box
		 * ausgewählt werden kann.
		 */
		btnUeberpruefen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				totalGuesses++;
				// Welche CheckBox ist ausgewählt?
				if (cb1.isSelected()) {
					auswahl = bedeutung1;
				} else if (cb2.isSelected()) {
					auswahl = bedeutung2;
				} else if (cb3.isSelected()) {
					auswahl = bedeutung3;
				}

				if (auswahl.equals(vokabel.getBedeutung())) {
					showFeedbackDialog("Richtig!");
					correctGuesses++;
					alKorrektGeloest.add(vokabel);

					if (alKorrektGeloest.size() == alVokabelnListe.size()) {
						showFeedbackDialog("Alle Aufgaben in dieser Vokabelliste sind gelöst.\n" + "Statistik:\n"
								+ "Gesamte Versuche: " + totalGuesses + "\n" + "Richtige Versuche: " + correctGuesses
								+ "\n" + "Falsche Versuche: " + incorrectGuesses);
						getDialogPane().getScene().getWindow().hide(); // Nach Lösung aller Aufgaben wird Dialogfenster
																		// geschlossen.

					} else {
						setTask(); // Sollten nicht alle Vokabeln korrekt gelöst sein, wird eine neue Aufgabe
									// generiert.
					}
				} else {
					showFeedbackDialog("Falsch!");
					incorrectGuesses++;
				}
				// Alle Checkboxen werden zurückgesetzt.
				cb1.setSelected(false);
				cb2.setSelected(false);
				cb3.setSelected(false);

			}
		});

		// Event Handler für cb1
		cb1.setOnAction(event -> {
			if (cb1.isSelected()) {
				cb2.setSelected(false);
				cb3.setSelected(false);
			}
		});

		// Event Handler für cb2
		cb2.setOnAction(event -> {
			if (cb2.isSelected()) {
				cb1.setSelected(false);
				cb3.setSelected(false);
			}
		});

		// Event Handler für cb3
		cb3.setOnAction(event -> {
			if (cb3.isSelected()) {
				cb1.setSelected(false);
				cb2.setSelected(false);
			}
		});

	}

	/*
	 * Diese Methode erstellt einen Feed-Back-Dialog für Rückmeldungen wie z.B.
	 * richtige Auswahl, falsche Auswahl, korrekte Lösung aller Aufgaben und eine
	 * abschließende Statistik.
	 */
	private void showFeedbackDialog(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Feedback");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	/*
	 * Diese Methode generiert das korrekte Vokabel-Objekt für die
	 * Multiple-Choice-Übung und geht dabei folgendermaßen vor: Zuerst wird eine
	 * Array-List für nicht gelöste Vokabeln erstellt und mit Vokabeln gefüllt, die
	 * noch nicht in der Array-List alKorrektGeloest enthalten sind. Dann wird per
	 * Zufallsgenerator ein Vokabel aus der Array-List mit den nicht gelösten
	 * Vokabel-Elementen geholt.
	 */
	private void setVokabel() {
		// neue Liste für noch nicht gelöste Vokabeln
		ArrayList<Vokabel> alUngeloesteVokabeln = new ArrayList<>();

		/*
		 * alUngeloesteVokabeln wird mit Vokabeln gefüllt, die noch nicht in
		 * alKorrektGeloest enthalten sind
		 */
		for (Vokabel v : alVokabelnListe) {
			if (!alKorrektGeloest.contains(v)) {
				alUngeloesteVokabeln.add(v);
			}
		}

		// wenn alUngeloesteVokabeln nicht leer ist, wird per Zufall ein Vokabel gewählt
		if (!alUngeloesteVokabeln.isEmpty()) {
			int randomIndex = random.nextInt(alUngeloesteVokabeln.size());
			vokabel = alUngeloesteVokabeln.get(randomIndex);
			// Bedeutung des Vokabels wird geholt
			bedeutung1 = vokabel.getBedeutung();

		}
	}

	/*
	 * Diese Methode wählt die erste falsche Löung (Distraktor) für die
	 * Antwortoptionen aus. Der Distraktor wird zufällig aus der Array-List
	 * potentielleDistraktoren ausgewählt, aus der die richtige Antwort zuvor
	 * entfernt worden ist.
	 */
	private void setDistraktor1() {
		ArrayList<Vokabel> potenzielleDistraktoren = new ArrayList<>(alVokabelnListe);
		potenzielleDistraktoren.remove(vokabel); // Die korrekte Antwort wird von den potentiellen Distraktoren entfernt

		if (!potenzielleDistraktoren.isEmpty()) {
			int randomIndex = random.nextInt(potenzielleDistraktoren.size());
			distraktor1 = potenzielleDistraktoren.get(randomIndex);
			bedeutung2 = distraktor1.getBedeutung();
		}
	}

	/*
	 * Diese Methode wählt die zweite falsche Löung (Distraktor) für die
	 * Antwortoptionen aus. Der Distraktor wird zufällig aus der Array-List
	 * potentielleDistraktoren ausgewählt, aus der die richtige Antwort und die
	 * erste falsche Lösung zuvor entfernt worden sind.
	 */
	private void setDistraktor2() {
		ArrayList<Vokabel> potenzielleDistraktoren = new ArrayList<>(alVokabelnListe);
		potenzielleDistraktoren.remove(vokabel); // die korrekte Lösung wird eintfernt
		if (distraktor1 != null) {
			potenzielleDistraktoren.remove(distraktor1); // Distraktor 1 wird entfernt
		}

		if (!potenzielleDistraktoren.isEmpty()) {
			int randomIndex = random.nextInt(potenzielleDistraktoren.size());
			distraktor2 = potenzielleDistraktoren.get(randomIndex);
			bedeutung3 = distraktor2.getBedeutung();
		}
	}

	/*
	 * Diese Methode veteilt die drei Bedeutungen, die aus "Vokabel", "Distraktor1"
	 * und "Distraktor2" geholt worden sind. Die drei Bedeutungen werden in einen
	 * String-Array geschrieben, der mithilfe des Fisher-Yates-Verfahrens umsortiert
	 * wird
	 */
	private void distributeVokabelDistraktoren() {

		String[] distribute = { bedeutung1, bedeutung2, bedeutung3 };

		// Array wird mit dem Fisher-Yates-Verfahren umsortiert
		for (int i = distribute.length - 1; i > 0; i--) {
			int j = random.nextInt(i + 1);
			String temp = distribute[i];
			distribute[i] = distribute[j];
			distribute[j] = temp;
		}

		bedeutung1 = distribute[0];
		bedeutung2 = distribute[1];
		bedeutung3 = distribute[2];
	}

	/*
	 * Diese Methode implementiert die drei vorangehenden Methoden und setzt die
	 * Inhalte der Labels für die Multiple-Choice-GUI-Struktur.
	 */
	private void setTask() {
		setVokabel();
		setDistraktor1();
		setDistraktor2();
		distributeVokabelDistraktoren();
		lblVokabel.setText(vokabel.getChinesischSimpl());
		lblBedeutung1.setText(bedeutung1);
		lblBedeutung2.setText(bedeutung2);
		lblBedeutung3.setText(bedeutung3);

	}

}
