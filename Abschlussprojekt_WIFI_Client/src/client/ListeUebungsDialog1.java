package client;

/*Diese Klasse bietet eine JavaFX-Anwendung für zwei Sprachlernübungen, 
 * um chinesische Schriftzeichen (Hanzi) und deren Aussprache (Pinyin) zu üben. 
 * Der Code erstellt ein Dialogfenster mit einer Tab-Pane und zwei Tabs.  
 * Benutzer können ihre Antworten eingeben und das Programm gibt Feedback zur Richtigkeit.*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import klassen.Liste;
import klassen.Vokabel;
import klassen.VokabelnListe;

public class ListeUebungsDialog1 extends Dialog<ButtonType> {

	/*
	 * Eine Hash Map wird statisch initialisiert und gefüllt, um Vokale und Zahlen
	 * (Vokal + Töne 1-4) als Key durch die entsprechenden Unicode-Character-Codes
	 * als Value zu substituieren.
	 */
	private static final Map<String, String> substitutions;

	static {
		substitutions = new HashMap<>();
		// Substitutionen Vokal + Ton-Nummer durch entsprechende Characters
		substitutions.put("a1", "\u0101");
		substitutions.put("a2", "\u00E1");
		substitutions.put("a3", "\u01CE");
		substitutions.put("a4", "\u00E0");

		substitutions.put("e1", "\u0113");
		substitutions.put("e2", "\u00E9");
		substitutions.put("e3", "\u011B");
		substitutions.put("e4", "\u00E8");

		substitutions.put("i1", "\u012B");
		substitutions.put("i2", "\u00ED");
		substitutions.put("i3", "\u01D0");
		substitutions.put("i4", "\u00EC");

		substitutions.put("o1", "\u014D");
		substitutions.put("o2", "\u00F3");
		substitutions.put("o3", "\u01D2");
		substitutions.put("o4", "\u00F2");

		substitutions.put("u1", "\u016B");
		substitutions.put("u2", "\u00FA");
		substitutions.put("u3", "\u01D4");
		substitutions.put("u4", "\u00F9");

		substitutions.put("\u00FC" + "1", "\u01D6");
		substitutions.put("\u00FC" + "2", "\u01D8");
		substitutions.put("\u00FC" + "3", "\u01DA");
		substitutions.put("\u00FC" + "4", "\u01DC");
	}

	// "Repository" wird deklariert.
	VokabelnListe vl;

	/*
	 * Array-List für das Handling der Vokabel-Elemente in dieser Klasse wird
	 * initialisiert
	 */
	ArrayList<Vokabel> alVokabelnListe;

	// Zufallsgenerator und Variable für Indexpositionen werden generiert
	Random random = new Random();
	int randomIndex;

	// Variablen für statistische Informationen werden initialisiert
	int totalGuessesPinyin = 0;
	int correctGuessesPinyin = 0;
	int incorrectGuessesPinyin = 0;
	int totalGuessesHanzi = 0;
	int correctGuessesHanzi = 0;
	int incorrectGuessesHanzi = 0;

	// 2 Vokabel-Objekte für das Handling von Zufallsvokabeln werden generiert.*/
	Vokabel randomVokabel1;
	Vokabel randomVokabel2;

	// 2 Listen für das Speichern korrekt gelöster Vokabeln werden generiert
	ArrayList<Vokabel> geloesteVokabeln1 = new ArrayList<>();
	ArrayList<Vokabel> geloesteVokabeln2 = new ArrayList<>();

	// Font für die Darstellung der chinesischen Schriftzeichen wird initialisiert
	Font font = new Font("FangSong", 25);

	/*
	 * Der Konstruktor initialisiert die UI-Komponenten und die Logik für zwei
	 * Registerkarten: eine zum Schreiben von Pinyin-Zeichen und eine zum Schreiben
	 * von Hanzi-Zeichen.
	 */
	public ListeUebungsDialog1(Liste liste) {

		// Array-List für das Handling der eingelesenen Vokabeln wird initialisiert
		alVokabelnListe = new ArrayList<>();

		/*
		 * Die dem semantischen zugeordneten Vokabeln werden aus der Datenbank
		 * eingelesen und in der Array-List alVokabelnSemFeld gespeichert.
		 */
		leseVokabelnListe(liste.getListeID());

		/* Eine zufällige Index-Position wird generiert. */
		randomIndex = random.nextInt(alVokabelnListe.size());

		// Eine Tab-Pane wird initialisiert
		TabPane tabPane = new TabPane();

		// Registerkarte für die erste Übungsvariante wird erstellt
		Tab piyinSchreibenTab = new Tab("Pinyin schreiben");

		// Aus der bereitgestellten Liste wird ein zufälliges Vokabularwort ausgewählt.
		randomVokabel1 = alVokabelnListe.get(randomIndex);

		/*
		 * Chinesische Zeichen des Zufallsvokabels wird wird in Lable mit Font
		 * "FangSong" dargestellt.
		 */
		Label lblChinSimpl = new Label(randomVokabel1.getChinesischSimpl());
		lblChinSimpl.setFont(font);

		// Textfeld zur Eingabe der Pinyin-Transkription wird generiert
		TextField txtPinyin = new TextField();
		txtPinyin.textProperty().addListener((observable, oldValue, newValue) -> {
			/*
			 * Das Pinyin-Textfeld erhält einen Listener, um zu überprüfen, ob die
			 * Kombination Vokal + Zahl von 1-4 vorhanden ist und durch den entsprechenden
			 * Pinyin-Wert zu ersetzen.
			 */
			for (Map.Entry<String, String> entry : substitutions.entrySet()) {
				newValue = newValue.replace(entry.getKey(), entry.getValue());
			}
			// Textfeld wird aktualisiert
			txtPinyin.setText(newValue);
		});

		// Überprüfen-Button wird initialisiert
		Button checkPinyin = new Button("Überprüfen");

		// Grid-Pane wird initialisiert und gefüllt
		GridPane gp1 = new GridPane();
		gp1.setHgap(10);
		gp1.add(new Label("Chinesisches Zeichen"), 0, 0);
		gp1.add(lblChinSimpl, 1, 0);
		gp1.add(new Label("Pinyin"), 0, 1);
		gp1.add(txtPinyin, 1, 1);
		gp1.add(checkPinyin, 2, 1);

		/*
		 * Überprüfen-Button erhält Action-Handler: TotalGuessesPinyin-Zähler wird
		 * inkrementiert, um die Gesamtzahl der vom Benutzer unternommenen Versuche zu
		 * ermitteln. Eingabe aus txtPinyin wird abgerufen und mit dem korrekten Pinyin
		 * von randomVokabel1 verglichen. Wenn dEingabe mit Lösungn übereinstimmt, wird
		 * Feedback-Dialog mit der Meldung „Richtig!“ angezeigt,
		 * „correctGuessesPinyin“-Zähler inkrementiert und der Wert von randomVokabel1
		 * zur Liste geloesteVokabeln1 hinzugefügt. Wenn geloesteVokabeln1 und
		 * alVokabelnSemFeld die gleiche Größe aufweisen, (d.h. alle Aufgabwen gelöst
		 * sind), wird ein Feedback-Dialog mit Statistiken angezeigt und die
		 * Registerkarte aus der Tab-Pane entfernt. Wenn die Listen nicht gleich groß
		 * sind, wird ein neues Zufallsvokabel aus der Liste alVokabelnSemfeld erzeugt,
		 * das noch nicht in geloesteVokabeln1 enthalten ist. Wenn die Eingabe falsch
		 * ist, wird ein Feedback-Dialog mit dem Text "Falsch!" angezeigt und die
		 * Variable incorrectGuesses1 inkrementiert.
		 */
		checkPinyin.setOnAction(event -> {
			totalGuessesPinyin++;
			String userPinyin = txtPinyin.getText();
			if (userPinyin.equals(randomVokabel1.getPinyin())) {
				showFeedbackDialog("Richtig!");
				correctGuessesPinyin++;
				geloesteVokabeln1.add(randomVokabel1);
				if (geloesteVokabeln1.size() == alVokabelnListe.size()) {
					showFeedbackDialog("Alle Pinyin-Aufgaben in dieser Vokabelliste sind gelöst.\n" + "Statistik:\n"
							+ "Gesamte Versuche: " + totalGuessesPinyin + "\n" + "Richtige Versuche: "
							+ correctGuessesPinyin + "\n" + "Falsche Versuche: " + incorrectGuessesPinyin);
					tabPane.getTabs().remove(piyinSchreibenTab);
					return;
				}
				while (geloesteVokabeln1.contains(randomVokabel1)) {
					randomIndex = random.nextInt(alVokabelnListe.size());
					randomVokabel1 = alVokabelnListe.get(randomIndex);
				}

				txtPinyin.clear();
				lblChinSimpl.setText(randomVokabel1.getChinesischSimpl());
			} else {
				showFeedbackDialog("Falsch!");
				incorrectGuessesPinyin++;
			}
		});

		// Grid-Pane wird in Registerkarte integriert
		piyinSchreibenTab.setContent(gp1);

		// Registerkarte wird in Tab-Pane integriert
		tabPane.getTabs().add(piyinSchreibenTab);

		// Registerkarten werden ohne Close-Icon angezeigt
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		// Höhe der Tab-Pane wird festgesetzt
		tabPane.setTabMinHeight(30);

		// Registerkarte für die zweite Übungsvariante wird generiert
		Tab hanziSchreibenTab = new Tab("Chinesisches Zeichen schreiben");

		// Aus der bereitgestellten Liste wird ein zufälliges Vokabularwort ausgewählt.
		randomVokabel2 = alVokabelnListe.get(randomIndex);

		// Zwei Labels zeigen Pinyin-Transkription und Bedeutung des Zufallsvokabels an
		Label lblPinyin = new Label(randomVokabel2.getPinyin());
		Label lblBedeutung = new Label(randomVokabel2.getBedeutung());

		/*
		 * Ein Textfeld für die Eingabe der Lösung (chinesische Zeichen des
		 * Zufalls-Vokabelelements) wird generiert. FOnt wird auf FangSong gesetzt.
		 */
		TextField txtChin = new TextField();
		txtChin.setFont(font);

		// Überprüfen-Button wird initialisiert
		Button checkChin = new Button("Überprüfen");

		// Grid-Pane wird initialisiert und gefüllt
		GridPane gp2 = new GridPane();
		gp2.setHgap(10);
		gp2.add(new Label("Pinyin"), 0, 0);
		gp2.add(lblPinyin, 1, 0);
		gp2.add(new Label("Übersetzung"), 0, 1);
		gp2.add(lblBedeutung, 1, 1);
		gp2.add(new Label("Hanzi"), 0, 2);
		gp2.add(txtChin, 1, 2);
		gp2.add(checkChin, 2, 2);

		/*
		 * Überprüfen-Button erhält Action-Handler: TotalGuessesPinyin-Zähler wird
		 * inkrementiert, um die Gesamtzahl der vom Benutzer unternommenen Versuche zu
		 * ermitteln. Eingegebene des Benutzers aus txtChin wird abgerufen und wird mit
		 * der korrekten Lösung von randomVokabel2 verglichen. Wenn die Eingegabe Pinyin
		 * mit der Lösung übereinstimmt, wird Feedback-Dialog mit der Meldung „Richtig!“
		 * angezeigt, „correctGuessesHanzi“-Zähler wird inkrementiert und der Wert von
		 * randomVokabel2 zur Liste geloesteVokabeln2 hinzugefügt. Wenn
		 * geloesteVokabeln2 und alVokabelnSemFeld die gleiche Größe aufweisen, (d.h.
		 * alle Aufgabwen gelöst sind), wird ein Feedback-Dialog mit Statistiken
		 * angezeigt und die Registerkarte aus der Tab-Pane entfernt. Wenn die Listen
		 * nicht gleich groß sind, wird ein neues Zufallsvokabel aus der Liste
		 * alVokabelnSemfeld erzeugt, das noch nicht in geloesteVokabeln1 enthalten ist.
		 * Wenn die Eingabe falsch ist, wird ein Feedback-Dialog mit dem Text "Falsch!"
		 * angezeigt und die Variable incorrectGuesses2 inkrementiert.
		 */
		checkChin.setOnAction(event -> {
			totalGuessesHanzi++;
			String userHanzi = txtChin.getText();
			if (userHanzi.equals(randomVokabel2.getChinesischSimpl())) {
				showFeedbackDialog("Richtig!");
				correctGuessesHanzi++;
				geloesteVokabeln2.add(randomVokabel2);
				if (geloesteVokabeln2.size() == alVokabelnListe.size()) {
					showFeedbackDialog("Alle Hanzi-Aufgaben in dieser Vokabelliste sind gelöst.\n" + "Statistik:\n"
							+ "Gesamte Versuche: " + totalGuessesHanzi + "\n" + "Richtige Versuche: "
							+ correctGuessesHanzi + "\n" + "Falsche Versuche: " + incorrectGuessesHanzi);
					tabPane.getTabs().remove(hanziSchreibenTab);
					return;
				}

				while (geloesteVokabeln2.contains(randomVokabel2)) {
					randomIndex = random.nextInt(alVokabelnListe.size());
					randomVokabel2 = alVokabelnListe.get(randomIndex);
				}
				txtChin.clear();
				lblPinyin.setText(randomVokabel2.getPinyin());
				lblBedeutung.setText(randomVokabel2.getBedeutung());
			} else {
				showFeedbackDialog("Falsch!");
				incorrectGuessesHanzi++;
			}
		});

		// Grid-Pane wird in Registerkarte integriert
		hanziSchreibenTab.setContent(gp2);

		// Registerkarte wird in Tab-Pane integriert
		tabPane.getTabs().add(hanziSchreibenTab);

		// Registerkarten erhalten kein Closing-Icon
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

		// Höhe der Tab-Pane wird festgesetzt
		tabPane.setTabMinHeight(30);

		// Tab-Pane wird in Dialog-Pane integriert
		this.getDialogPane().setContent(tabPane);

		// Schließen-Button-Type wird erzeugt und in die Dialog-Pane integriert
		ButtonType closeButton = new ButtonType("Schließen", ButtonBar.ButtonData.CANCEL_CLOSE);
		this.getDialogPane().getButtonTypes().add(closeButton);
	}

	/*
	 * Diese Methoden liest mithilfe des Parameters SemFeldId die Vokabeln ein, die
	 * dem semantischen Feld zugeordnet sind, und speichert diese in der Array-List
	 * alVokabelnSemFeld ab. Die Rest-API-GET-Methode wird für die Datenbank-Query
	 * benutzt.
	 */
	private void leseVokabelnListe(int listeID) {

		try {
			String line = ServiceFunctions.get("vokabelnliste", String.valueOf(listeID));
			vl = new VokabelnListe(line);
			for (Vokabel v : vl.getVokabelnListe())
				alVokabelnListe.add(v);
		} catch (VokabeltrainerException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.titleProperty().set("Fehler");
			alert.setContentText(e.toString());
			alert.showAndWait();
		}
	}

	// Diese Methode generiert einen JavaFX-Feedbackdialog
	private void showFeedbackDialog(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Feedback");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
