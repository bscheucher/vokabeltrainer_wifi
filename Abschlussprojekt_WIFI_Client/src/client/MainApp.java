package client;

/*Diese Java-Klasse definiert eine JavaFX-Anwendung für einen Chinesisch-Vokabeltrainer. 
 * Die Anwendung erstellt ein grafisches User-Interface (GUI) mit drei Table Views und Schaltflächen, die es Benutzern ermöglichen, 
 * chinesische Vokabeln zu verwalten, Listen von Vokabeln zu erstellen und mit semantischen 
 * Feldern zu arbeiten. 
 * */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import klassen.Liste;
import klassen.Listen;
import klassen.SemantischeFelder;
import klassen.SemantischesFeld;
import klassen.Vokabel;
import klassen.Vokabeln;
import klassen.VokabelnListe;
import klassen.VokabelnSemFeld;

public class MainApp extends Application {

	/*
	 * Verschiedene Listen und ObservableLists werden deklariert bzw. initialisiert,
	 * die zur Verwaltung von Vokabeln, Listen, semantischen Feldern und deren
	 * Anzeigen im GUI dienen.
	 */
	ArrayList<Vokabel> alVokabeln; // benötigt für die Suchfunktion

	ObservableList<VokabelFX> olVokabeln = FXCollections.observableArrayList(); // benötigt, um alle Vokabeln der DB in
																				// Table-View anzuzeigen

	ObservableList<ListeFX> olListen = FXCollections.observableArrayList(); // benötigt, um alle Listen in Table-View
																			// anzuzeigen

	ObservableList<SemantischesFeldFX> olSemantischeFelder = FXCollections.observableArrayList(); // benötigt, um alle
																									// semantischen
																									// Felder in Table-
																									// View anzuzeigen

	ObservableList<VokabelFX> olVokabelnListe = FXCollections.observableArrayList(); // benötigt, um alle Vokabeln einer
																						// Liste in Table-View
																						// anzuzeigen

	ObservableList<VokabelFX> olVokabelnSemFeld = FXCollections.observableArrayList(); // benötigt, um alle Vokabeln
																						// eines semantischen Feldes in
																						// Table-View anzuzeigen

	/*
	 * Eine Hash-Map wird statisch initialisiert und gefüllt, um Vokale und Zahlen
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

	// Main-Klasse mit Launch-Methode
	public static void main(String[] args) {

		launch(args);
	}

	// Start-Methode mit JavaFX-GUI-Elementen
	@Override
	public void start(Stage arg0) throws Exception {

		// Mittlere Table-View mit div. Spalten, um Vokabeln anzuzeigen (aus olVokabeln,
		// olVokabelnListe, olVokabelnSemfeld)
		TableColumn<VokabelFX, Integer> colID = new TableColumn<>("ID");
		colID.setCellValueFactory(new PropertyValueFactory<>("vokabelID"));
		colID.setPrefWidth(50);
		TableColumn<VokabelFX, String> colChin = new TableColumn<>("Chinesisch 1");
		colChin.setCellValueFactory(new PropertyValueFactory<>("chinesischSimpl"));
		colChin.setPrefWidth(100);
		TableColumn<VokabelFX, String> colChinTrad = new TableColumn<>("Chinesisch 2");
		colChinTrad.setCellValueFactory(new PropertyValueFactory<>("chinesischTrad"));
		colChinTrad.setPrefWidth(100);
		TableColumn<VokabelFX, String> colPinyin = new TableColumn<>("Pinyin");
		colPinyin.setCellValueFactory(new PropertyValueFactory<>("pinyin"));
		colPinyin.setPrefWidth(150);
		TableColumn<VokabelFX, String> colBedeutung = new TableColumn<>("Bedeutung");
		colBedeutung.setCellValueFactory(new PropertyValueFactory<>("bedeutung"));
		colBedeutung.setPrefWidth(150);
		TableColumn<VokabelFX, String> colBeschreibung = new TableColumn<>("Beschreibung");
		colBeschreibung.setCellValueFactory(new PropertyValueFactory<>("vokabelBeschreibung"));
		colBeschreibung.setPrefWidth(150);
		TableColumn<VokabelFX, String> colWortart = new TableColumn<>("Wortart");
		colWortart.setCellValueFactory(new PropertyValueFactory<>("wortart"));
		colWortart.setPrefWidth(100);

		// default-mäßig zeigt die mittlere Table View den Inhalt von olVokabeln
		TableView<VokabelFX> tvVokabeln = new TableView<>(olVokabeln);
		// Spalten werden in die Table View integriert
		tvVokabeln.getColumns().addAll(colID, colChin, colChinTrad, colPinyin, colBedeutung, colBeschreibung,
				colWortart);
		// nur ein Vokabel-Element kann gleichzeitig ausgewählt sein
		tvVokabeln.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		leseVokabeln(); // diese Methode ruft die GET-Methode der REST Api und füllt olVokabeln mit dem
						// Inhalt der VOKABEL-Tabelle der DB

		// Buttons für die Realisierung von CRUD-Operationen werden initialisiert, by
		// default disabled
		Button vokabelNeu = new Button("Neu");
		Button vokabelBearbeiten = new Button("Bearbeiten");
		vokabelBearbeiten.setDisable(true);
		Button vokabelEntfernen = new Button("Enfernen");
		vokabelEntfernen.setDisable(true);
		Button vokabelAnzeigen = new Button("Anzeigen");
		// Buttons werden in einer HBox zusammengefasst
		HBox hbVokabelButtons1 = new HBox(vokabelNeu, vokabelBearbeiten, vokabelEntfernen, vokabelAnzeigen);

		// Textfeld für für die Suchfunktion wird initialisiert
		TextField suchenInput = new TextField();
		/*
		 * Das suchenInput-Textfeld erhält einen Listener, um zu überprüfen, ob die
		 * Kombination Vokal + Zahl von 1-4 vorhanden ist und durch den entsprechenden
		 * Pinyin-Wert zu ersetzen.
		 */
		suchenInput.textProperty().addListener((observable, oldValue, newValue) -> {
			// Durch Hash Map substitutions iterieren, Key durch entsprechenden Value
			// ersetzen
			for (Map.Entry<String, String> entry : substitutions.entrySet()) {
				newValue = newValue.replace(entry.getKey(), entry.getValue());
			}
			// Textfeld wird auf den dem Key zugeordneten Value gesetzt
			suchenInput.setText(newValue);
		});

		// Button und Textfeld für die Suchfunktion werden in einer HBox zusammengefasst
		Button suchenButton = new Button("Suchen");
		HBox hbVokabelButtons2 = new HBox(suchenInput, suchenButton);

		// Leere HBox für evtl. weitere GUI-Elemente bzw. als Platzhalter aus
		// Symmetriegründen
		HBox hbVokabelButtons3 = new HBox();

		// Change-Listener für Bearbeiten- und Entfernen-Buttons (wenn Element in
		// tvVokabeln ausgewählt ist, wird Button aktiviert)
		tvVokabeln.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<VokabelFX>() {
			@Override
			public void changed(ObservableValue<? extends VokabelFX> arg0, VokabelFX arg1, VokabelFX arg2) {
				if (arg2 != null) {
					vokabelBearbeiten.setDisable(false);
					vokabelEntfernen.setDisable(false);
				} else {
					vokabelBearbeiten.setDisable(true);
					vokabelEntfernen.setDisable(true);
				}
			}
		});

		// Action-Handler für Neu-Button, öffnet modalen Dialog, aktualisert olVokabeln
		vokabelNeu.setOnAction(e -> {
			Optional<ButtonType> r = new VokabelDetailDialog(new Vokabel()).showAndWait();
			if (r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE)
				leseVokabeln();
		});

		// Action-Handler Bearbeiten-Button, öffnet modalen Dialog, aktualisiert
		// olVokabeln, setzt den Inhalt von tvVokabeln auf olVokabeln
		vokabelBearbeiten.setOnAction(e -> {
			VokabelFX selectedVokabel = tvVokabeln.getSelectionModel().getSelectedItem();
			if (selectedVokabel != null) {
				Optional<ButtonType> r = new VokabelDetailDialog(selectedVokabel.getModellVokabel()).showAndWait();
				if (r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) {
					leseVokabeln();
					tvVokabeln.setItems(olVokabeln);
				}
			}
		});

		// Action-Handler für Entfernen-Button, ruft DELETE-Methode der REST API, bei
		// FOREIGN KEY CONSTRAINT VIOLATION Fehlermeldung
		vokabelEntfernen.setOnAction(e -> {
			VokabelFX selectedVokabel = tvVokabeln.getSelectionModel().getSelectedItem();
			try {
				ServiceFunctions.delete("vokabel", String.valueOf(selectedVokabel.getVokabelID()));
				olVokabeln.remove(selectedVokabel);
			} catch (VokabeltrainerException e1) {
				System.out.println(e1);
				Alert alert = new Alert(AlertType.ERROR);
				alert.titleProperty().set("Fehler");
				alert.setContentText(
						"Vokabeln müssen vor dem endgültigen Löschen zuerst aus Listen und Feldern entfernt werden!");
				alert.showAndWait();
			}
		});

		// Action-Handler für Button, der den Inhalt von tvVokabeln auf olVokabeln setzt
		vokabelAnzeigen.setOnAction(e -> {
			leseVokabeln();
			tvVokabeln.setItems(olVokabeln);
		});

		// Action-Handler für Suchen-Button
		suchenButton.setOnAction(e -> searchVokabeln(suchenInput.getText()));

		// Linke Table-View, um alle in der DB vorhandenen Vokabellisten anzuzeigen (aus
		// olListen)
		TableColumn<ListeFX, Integer> colListeID = new TableColumn<>("ID");
		colListeID.setCellValueFactory(new PropertyValueFactory<>("listeID"));
		colListeID.setPrefWidth(50);
		TableColumn<ListeFX, String> colListeBezeichnug = new TableColumn<>("Liste");
		colListeBezeichnug.setCellValueFactory(new PropertyValueFactory<>("listeBezeichnung"));
		colListeBezeichnug.setPrefWidth(150);
		TableColumn<ListeFX, String> colListeBeschreibung = new TableColumn<>("");
		colListeBeschreibung.setCellValueFactory(new PropertyValueFactory<>("listeBeschreibung"));
		colListeBeschreibung.setPrefWidth(50);
		leseListen();
		TableView<ListeFX> tvListen = new TableView<>(olListen);

		// Spalten werden in die Table-View integriert
		tvListen.getColumns().addAll(colListeID, colListeBezeichnug, colListeBeschreibung);
		// Nur eine einzige Liste kann gleichzeitig in der Table-View gewählt werden
		tvListen.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		// Buttons für CRUD-Operationen werden initialisiert, by default disabled
		Button listeNeu = new Button("Neu");
		Button listeBearbeiten = new Button("Bearbeiten");
		listeBearbeiten.setDisable(true);
		Button listeEntfernen = new Button("Entfernen");
		listeEntfernen.setDisable(true);
		// Buttons werden in HBox zusammengefasst
		HBox hbListeButtons1 = new HBox(listeNeu, listeBearbeiten, listeEntfernen);

		// Button, um den Inhalt von tvVokabel (mittlere Table-View) auf olVokabelnLite
		// zu setzen, by default disabled
		Button listeShowVokabeln = new Button("Anzeigen");
		listeShowVokabeln.setDisable(true);

		// Buttons für das Öffnen der Listen-Übungsdialoge werden initialisiert, by
		// default disabled
		Button listeUeben1 = new Button("Üben 1");
		listeUeben1.setDisable(true);
		Button listeUeben2 = new Button("Üben 2");
		listeUeben2.setDisable(true);
		// Buttons werden in HBox zusammengefasst
		HBox hbListeButtons2 = new HBox(listeShowVokabeln, listeUeben1, listeUeben2);

		// Button für das Einfügen eines Vokabels in eine Liste wird initialisiert, by
		// default diabled
		Button listeAddVokabel = new Button("Vokabel hinzufügen");
		listeAddVokabel.setDisable(true);

		// Button für das Entfernen eines Vokabels aus einer Liste wird initialisiert,
		// by default disabled
		Button listeDeleteVokabel = new Button("Vokabel entfernen");
		listeDeleteVokabel.setDisable(true);
		// Buttons werden in HBox zusammengefasst
		HBox hbListeButtons3 = new HBox(listeAddVokabel, listeDeleteVokabel);

		// Change-Listener für Liste-Buttons, werden aktiviert wenn Liste ausgewählt
		tvListen.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ListeFX>() {
			@Override
			public void changed(ObservableValue<? extends ListeFX> arg0, ListeFX arg1, ListeFX arg2) {
				if (arg2 != null) {
					listeBearbeiten.setDisable(false);
					listeEntfernen.setDisable(false);
					listeAddVokabel.setDisable(false);
					listeShowVokabeln.setDisable(false);
					listeDeleteVokabel.setDisable(false);
					listeUeben1.setDisable(false);
					listeUeben2.setDisable(false);

				} else {
					listeBearbeiten.setDisable(true);
					listeEntfernen.setDisable(true);
					listeAddVokabel.setDisable(true);
					listeShowVokabeln.setDisable(true);
					listeDeleteVokabel.setDisable(true);
					listeUeben1.setDisable(true);
					listeUeben2.setDisable(true);
				}
			}
		});
		// Action-Handler für Neue-Liste-Button, öffnet modalen Dialog, olListe wird
		// aktualisiert
		listeNeu.setOnAction(e -> {
			Optional<ButtonType> r = new ListeDetailDialog(new Liste()).showAndWait();
			if (r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE)
				leseListen();
		});
		// Action-Handler für Liste-Bearbeiten-Button, öffnet modalen Dialog, olListe
		// wird aktualisiert
		listeBearbeiten.setOnAction(e -> {
			Optional<ButtonType> r = new ListeDetailDialog(
					tvListen.getSelectionModel().getSelectedItem().getModellListe()).showAndWait();
			if (r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE)
				leseListen();
		});
		// Action-Handler für Liste-Enfernen-Button, öffnet modalen Dialog, bei FORGEIGN
		// KEY CONSTRAINT VIOLATION Fehlermeldung
		listeEntfernen.setOnAction(e -> {
			ListeFX selectedListe = tvListen.getSelectionModel().getSelectedItem();
			try {
				ServiceFunctions.delete("liste", String.valueOf(selectedListe.getListeID()));
				olListen.remove(selectedListe);
			} catch (VokabeltrainerException e1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.titleProperty().set("Fehler");
				alert.setContentText(e1.toString());
				alert.showAndWait();
			}

		});

		// Actionhandler für Vokabel-zur-Liste-hinzufügen-Button, ruft
		// REST-API-POST-Methode, Liste und Vokabel müssen gewählt sein, sonst
		// Fehlermeldung
		listeAddVokabel.setOnAction(e -> {
			VokabelFX selectedVokabel = tvVokabeln.getSelectionModel().getSelectedItem();
			ListeFX selectedListe = tvListen.getSelectionModel().getSelectedItem();

			if (selectedVokabel != null && selectedListe != null) {
				int vokabelId = selectedVokabel.getVokabelID();
				int listeId = selectedListe.getListeID();
				Vokabel vokabel = selectedVokabel.getModellVokabel();

				try {
					ServiceFunctions.post("vokabelnliste", String.valueOf(vokabelId), String.valueOf(listeId),
							vokabel.toXML());
				} catch (VokabeltrainerException e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.titleProperty().set("Fehler");
					alert.setContentText(e1.toString());
					alert.showAndWait();
				}
				olVokabelnListe.clear();
				leseVokabelnListe(listeId);
				tvVokabeln.setItems(olVokabelnListe);

			} else {
				showErrorMessage("Auswahl nicht vollständig", "Vokabel und Liste müssen gewählt werden.");
			}
		});

		// Actionhandler für Vokabel-aus-Liste-entfernen-Button, ruft
		// REST-API-DELETE-Methode, Liste und Vokabel müssen gewählt sein, sonst
		// Fehlermeldung
		listeDeleteVokabel.setOnAction(e -> {
			VokabelFX selectedVokabel = tvVokabeln.getSelectionModel().getSelectedItem();
			ListeFX selectedListe = tvListen.getSelectionModel().getSelectedItem();
			if (selectedVokabel != null && selectedListe != null) {
				try {
					ServiceFunctions.delete("vokabelnliste", String.valueOf(selectedVokabel.getVokabelID()),
							String.valueOf(selectedListe.getListeID()));
					olVokabelnListe.clear();
					leseVokabelnListe(selectedListe.getListeID());
					tvVokabeln.setItems(olVokabelnListe);
				} catch (VokabeltrainerException e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.titleProperty().set("Fehler");
					alert.setContentText(e.toString());
					alert.showAndWait();
				}
			} else {
				showErrorMessage("Auswahl nicht vollständig", "Vokabel und Liste müssen gewählt werden.");

			}
		});

		// Action-Handler für Vokabeln-Liste-Anzeigen-Button, setzt den Inhalt von
		// tvVokabeln auf olVokabelnListe
		listeShowVokabeln.setOnAction(e -> {
			olVokabelnListe.clear();
			ListeFX selectedListe = tvListen.getSelectionModel().getSelectedItem();
			leseVokabelnListe(selectedListe.getListeID());
			tvVokabeln.setItems(olVokabelnListe);
		});

		/*
		 * Action-Handler für Übungsdialog1-Button, macht den Inhalt von tvVokabeln
		 * unsichtbar, öffnet modalen Dialog, nach Schließen des Dialogs
		 * tvVokabeln-Items wieder sichtbar, wenn Liste ohne Inhalt Fehlermeldung
		 */
		listeUeben1.setOnAction(e -> {
			tvVokabeln.setVisible(false);
			ListeFX selectedListe = tvListen.getSelectionModel().getSelectedItem();
			try {
				Optional<ButtonType> r = new ListeUebungsDialog1(selectedListe.getModellListe()).showAndWait();
			} catch (IllegalArgumentException ex) {
				showErrorMessage("Invalid Argument", "Die Liste ist leer.");

			}
			tvVokabeln.setVisible(true);
		});

		/*
		 * Action-Handler für Übungsdialog2-Button, macht den Inhalt von tvVokabeln
		 * unsichtbar, öffnet modalen Dialog, nach Schließen des Dialogs
		 * tvVokabeln-Items wieder sichtbar, wenn Liste ohne Inhalt Fehlermeldung
		 */
		listeUeben2.setOnAction(e -> {
			tvVokabeln.setVisible(false);
			ListeFX selectedListe = tvListen.getSelectionModel().getSelectedItem();
			try {
				Optional<ButtonType> r = new ListeUebungsDialog2(selectedListe.getModellListe()).showAndWait();
			} catch (IllegalArgumentException ex) {
				showErrorMessage("Invalid Argument", "Die Liste ist leer.");
			}
			tvVokabeln.setVisible(true);
		});

		// Rechte Table-View, um alle in der DB vorhandenen semantischen Felder
		// anzuzeigen (aus olSemantischeFelder)
		TableColumn<SemantischesFeldFX, Integer> colSemFeldID = new TableColumn<>("ID");
		colSemFeldID.setCellValueFactory(new PropertyValueFactory<>("semFeldID"));
		colSemFeldID.setPrefWidth(50);
		TableColumn<SemantischesFeldFX, String> colSemFeldBezeichnung = new TableColumn<>("Semant. Feld");
		colSemFeldBezeichnung.setCellValueFactory(new PropertyValueFactory<>("semFeldBezeichnung"));
		colSemFeldBezeichnung.setPrefWidth(150);
		TableColumn<SemantischesFeldFX, String> colSemFeldBeschreibung = new TableColumn<>("");
		colSemFeldBeschreibung.setCellValueFactory(new PropertyValueFactory<>("semFeldBeschreibung"));
		colSemFeldBeschreibung.setPrefWidth(50);

		// Table-View wird initialisiert und mit den Items aus olSemantischeFelder
		// gefüllt
		TableView<SemantischesFeldFX> tvSemFelder = new TableView<>(olSemantischeFelder);
		// Spalten werden in die Table-View integriert
		tvSemFelder.getColumns().addAll(colSemFeldID, colSemFeldBezeichnung, colSemFeldBeschreibung);
		tvSemFelder.setPrefWidth(100);
		// olSemantischeFelder wird Inhalt aus Tabelle SemantischesFeld in DB gefüllt
		leseSemFelder();
		// Buttons werden initialisiert, by default disabled
		Button semFeldNeu = new Button("Neu");
		Button semFeldBearbeiten = new Button("Bearbeiten");
		semFeldBearbeiten.setDisable(true);
		Button semFeldEntfernen = new Button("Enfernen");
		semFeldEntfernen.setDisable(true);
		HBox hbSemFeldButtons1 = new HBox(semFeldNeu, semFeldBearbeiten, semFeldEntfernen);

		Button semFeldShowVokabeln = new Button("Anzeigen");
		semFeldShowVokabeln.setDisable(true);
		Button semFeldUeben1 = new Button("Üben 1");
		semFeldUeben1.setDisable(true);
		Button semFeldUeben2 = new Button("Üben 2");
		semFeldUeben2.setDisable(true);
		HBox hbSemFeldButtons2 = new HBox(semFeldShowVokabeln, semFeldUeben1, semFeldUeben2);

		Button semFeldAddVokabel = new Button("Vokabel hinzufügen");
		semFeldAddVokabel.setDisable(true);
		Button semFeldDeleteVokabel = new Button("Vokabel entfernen");
		semFeldDeleteVokabel.setDisable(true);
		// Buttons werden in HBox zusammengefasst
		HBox hbSemFeldButtons3 = new HBox(semFeldAddVokabel, semFeldDeleteVokabel);

		// Change Listener für Buttons, wenn semantisches Feld in tvSemFelder ausgewählt
		// ist, werden Buttons aktiviert
		tvSemFelder.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SemantischesFeldFX>() {
			@Override
			public void changed(ObservableValue<? extends SemantischesFeldFX> arg0, SemantischesFeldFX arg1,
					SemantischesFeldFX arg2) {
				if (arg2 != null) {
					semFeldBearbeiten.setDisable(false);
					semFeldEntfernen.setDisable(false);
					semFeldAddVokabel.setDisable(false);
					semFeldShowVokabeln.setDisable(false);
					semFeldDeleteVokabel.setDisable(false);
					semFeldUeben1.setDisable(false);
					semFeldUeben2.setDisable(false);

				} else {
					semFeldBearbeiten.setDisable(true);
					semFeldEntfernen.setDisable(true);
					semFeldAddVokabel.setDisable(true);
					semFeldShowVokabeln.setDisable(true);
					semFeldDeleteVokabel.setDisable(true);
					semFeldUeben1.setDisable(true);
					semFeldUeben2.setDisable(true);
				}
			}
		});

		// Action-Handler Neues-Semantisches-Feld-Button, modaler Dialog wird geöffnet,
		// olSemFelder wird aktualisiert
		semFeldNeu.setOnAction(e -> {
			Optional<ButtonType> r = new SemFeldDetailDialog(new SemantischesFeld()).showAndWait();
			if (r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE)
				leseSemFelder();
		});
		// Action-Handler Semantisches-Feld-bearbeiten-Button, modaler Dialog wird
		// geöffnet, olSemFelder wird aktualisiert
		semFeldBearbeiten.setOnAction(e -> {
			Optional<ButtonType> r = new SemFeldDetailDialog(
					tvSemFelder.getSelectionModel().getSelectedItem().getModellSemFeld()).showAndWait();
			if (r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE)
				leseSemFelder();
		});

		/*
		 * Action-Handler für Semantisches-Feld-entfernen-Button, ruft
		 * REST-API-DELETE-Methode, bei FOREIGN KEY CONSTRAINT VIOLATION Fehlermeldung
		 */
		semFeldEntfernen.setOnAction(e -> {
			SemantischesFeldFX selectedSemFeld = tvSemFelder.getSelectionModel().getSelectedItem();
			try {
				ServiceFunctions.delete("semantischesfeld", String.valueOf(selectedSemFeld.getSemFeldID()));
				olSemantischeFelder.remove(selectedSemFeld);
			} catch (VokabeltrainerException e1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.titleProperty().set("Fehler");
				alert.setContentText(e1.toString());
				alert.showAndWait();
			}

		});

		/*
		 * Actionhandler für Vokabel-zum-semantischen-Feld-hinzufügen-Button, ruft
		 * REST-API-POST-Methode, semantisches Feld und Vokabel müssen gewählt sein,
		 * sonst Fehlermeldung
		 */
		semFeldAddVokabel.setOnAction(e -> {
			VokabelFX selectedVokabel = tvVokabeln.getSelectionModel().getSelectedItem();
			SemantischesFeldFX selectedSemFeld = tvSemFelder.getSelectionModel().getSelectedItem();
			if (selectedVokabel != null && selectedSemFeld != null) {
				int vokabelId = selectedVokabel.getVokabelID();
				int semFeldId = selectedSemFeld.getSemFeldID();
				Vokabel vokabel = selectedVokabel.getModellVokabel();

				try {
					ServiceFunctions.post("vokabelnsemfeld", String.valueOf(vokabelId), String.valueOf(semFeldId),
							vokabel.toXML());
				} catch (VokabeltrainerException e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.titleProperty().set("Fehler");
					alert.setContentText(e1.toString());
					alert.showAndWait();
				}
				olVokabelnSemFeld.clear();
				leseVokabelnSemFeld(semFeldId);
				tvVokabeln.setItems(olVokabelnSemFeld);

			} else {
				showErrorMessage("Auswahl nicht vollständig", "Vokabel und Semantisches Feld müssen gewählt werden.");

			}
		});

		/*
		 * Actionhandler für Vokabel-aus-semantischem-Feld-entfernen-Button, ruft
		 * REST-API-DELETE-Methode, semantisches Feld und Vokabel müssen gewählt sein,
		 * sonst Fehlermeldung
		 */
		semFeldDeleteVokabel.setOnAction(e -> {
			VokabelFX selectedVokabel = tvVokabeln.getSelectionModel().getSelectedItem();
			SemantischesFeldFX selectedSemFeld = tvSemFelder.getSelectionModel().getSelectedItem();
			if (selectedVokabel != null && selectedSemFeld != null) {
				try {
					ServiceFunctions.delete("vokabelnsemfeld", String.valueOf(selectedVokabel.getVokabelID()),
							String.valueOf(selectedSemFeld.getSemFeldID()));
					olVokabelnSemFeld.clear();
					leseVokabelnSemFeld(selectedSemFeld.getSemFeldID());
					tvVokabeln.setItems(olVokabelnSemFeld);
				} catch (VokabeltrainerException e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.titleProperty().set("Fehler");
					alert.setContentText(e.toString());
					alert.showAndWait();
				}
			} else {
				showErrorMessage("Auswahl nicht vollständig", "Vokabel und semantisches Feld müssen gewählt werden.");

			}
		});

		// Action Handler für, um den Inhalt von tvVokabeln auf olVokabelnSemFeld zu
		// setzen
		semFeldShowVokabeln.setOnAction(e -> {
			olVokabelnSemFeld.clear();
			SemantischesFeldFX selectedSemFeld = tvSemFelder.getSelectionModel().getSelectedItem();
			leseVokabelnSemFeld(selectedSemFeld.getSemFeldID());
			tvVokabeln.setItems(olVokabelnSemFeld);
		});

		/*
		 * Action-Handler für Übungsdialog1-Button, macht den Inhalt von tvVokabeln
		 * unsichtbar, öffnet modalen Dialog, nach Schließen des Dialogs
		 * tvVokabeln-Items wieder sichtbar, wenn Liste ohne Inhalt Fehlermeldung
		 */

		semFeldUeben1.setOnAction(e -> {
			tvVokabeln.setVisible(false);
			SemantischesFeldFX selectedSemFeld = tvSemFelder.getSelectionModel().getSelectedItem();
			try {
				Optional<ButtonType> r = new SemFeldUebungsDialog1(
						tvSemFelder.getSelectionModel().getSelectedItem().getModellSemFeld()).showAndWait();
			} catch (IllegalArgumentException ex) {
				showErrorMessage("Invalid Argument", "Das Feld ist leer.");
			}

			tvVokabeln.setVisible(true);
		});

		/*
		 * Action-Handler für Übungsdialog2-Button, macht den Inhalt von tvVokabeln
		 * unsichtbar, öffnet modalen Dialog, nach Schließen des Dialogs
		 * tvVokabeln-Items wieder sichtbar, wenn Liste ohne Inhalt Fehlermeldung
		 */
		semFeldUeben2.setOnAction(e -> {
			tvVokabeln.setVisible(false);
			SemantischesFeldFX selectedSemFeld = tvSemFelder.getSelectionModel().getSelectedItem();
			try {
				Optional<ButtonType> r = new SemFeldUebungsDialog2(selectedSemFeld.getModellSemFeld()).showAndWait();
			} catch (IllegalArgumentException ex) {
				showErrorMessage("Invalid Argument", "Das Feld ist leer.");
			}
			tvVokabeln.setVisible(true);
		});

		// Table-Views und Buttons werden in eine Grid-Pane integriert, um
		// Main-App-GUI aufzubauen
		GridPane gp = new GridPane();
		gp.add(tvListen, 0, 0);
		gp.add(tvVokabeln, 1, 0);
		gp.add(tvSemFelder, 2, 0);
		gp.add(hbListeButtons1, 0, 1);
		gp.add(hbVokabelButtons1, 1, 1);
		gp.add(hbSemFeldButtons1, 2, 1);
		gp.add(hbListeButtons2, 0, 2);
		gp.add(hbVokabelButtons2, 1, 2);
		gp.add(hbSemFeldButtons2, 2, 2);
		gp.add(hbListeButtons3, 0, 3);
		gp.add(hbVokabelButtons3, 1, 3);
		gp.add(hbSemFeldButtons3, 2, 3);

		gp.setPadding(new Insets(5));
		gp.setHgap(10);
		gp.setVgap(10);

		// Grid-Pane und somit alle GUI-Elemente werden in die Scene integriert
		arg0.setScene(new Scene(gp));
		arg0.setTitle("Chinesisch-Vokabeltrainer");
		arg0.show();

	}

	// Diese Methode ruft die REST-API-GET-Methode und füllt olVokabeln mit dem
	// Inhalt der Tabelle "Vokabel" in der DB
	private void leseVokabeln() {

		try {
			String line = ServiceFunctions.get("vokabeln", null);
			Vokabeln vv = new Vokabeln(line);
			olVokabeln.clear();
			for (Vokabel v : vv.getVokabeln())
				olVokabeln.add(new VokabelFX(v));
		} catch (VokabeltrainerException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.titleProperty().set("Fehler");
			alert.setContentText(e.toString());
			alert.showAndWait();
		}
	}

	// Diese Methode ruft die REST-API-GET-Methode und füllt olListen mit dem Inhalt
	// der Tabelle "Liste" in der DB
	private void leseListen() {

		try {
			String line = ServiceFunctions.get("listen", null);
			Listen ll = new Listen(line);
			olListen.clear();
			for (Liste l : ll.getListen())
				olListen.add(new ListeFX(l));
		} catch (VokabeltrainerException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.titleProperty().set("Fehler");
			alert.setContentText(e.toString());
			alert.showAndWait();
		}
	}

	// Diese Methode ruft die REST-API-GET-Methode und füllt olSemantischeFelder mit
	// dem Inhalt der Tabelle "SemantischesFeld" in der DB
	private void leseSemFelder() {

		try {
			String line = ServiceFunctions.get("semantischefelder", null);
			SemantischeFelder ss = new SemantischeFelder(line);
			olSemantischeFelder.clear();
			for (SemantischesFeld s : ss.getSemantischeFelder())
				olSemantischeFelder.add(new SemantischesFeldFX(s));

		} catch (VokabeltrainerException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.titleProperty().set("Fehler");
			alert.setContentText(e.toString());
			alert.showAndWait();
		}
	}

	// Diese Methode ruft die REST-API-GET-Methode und füllt olVokabelnListe mit dem
	// Inhalt der Tabelle "VokabelListe" in der DB
	private void leseVokabelnListe(int listeID) {

		try {
			String line = ServiceFunctions.get("vokabelnliste", String.valueOf(listeID));
			VokabelnListe vl = new VokabelnListe(line);
			for (Vokabel v : vl.getVokabelnListe())
				olVokabelnListe.add(new VokabelFX(v));
		} catch (VokabeltrainerException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.titleProperty().set("Fehler");
			alert.setContentText(e.toString());
			alert.showAndWait();
		}
	}

	// Diese Methode ruft die REST-API-GET-Methode und füllt olVokabelnSemFeld mit
	// dem Inhalt der Tabelle "VokabelSemFeld" in der DB
	private void leseVokabelnSemFeld(int semFeldID) {

		try {
			String line = ServiceFunctions.get("vokabelnsemfeld", String.valueOf(semFeldID));
			VokabelnSemFeld vs = new VokabelnSemFeld(line);
			for (Vokabel v : vs.getVokabelnSemFeld())
				olVokabelnSemFeld.add(new VokabelFX(v));
		} catch (VokabeltrainerException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.titleProperty().set("Fehler");
			alert.setContentText(e.toString());
			alert.showAndWait();
		}
	}

	/*
	 * Diese Methode ruft die REST-API-GET-Methode, füllt olVokabeln mit dem Inhalt
	 * der Tabelle "Vokabel" in der DB und sucht nach einem durch den User
	 * eingegebenen String-Input in den Vokabel-Objekten
	 */
	private void searchVokabeln(String query) {
		olVokabeln.clear();
		try {
			String line = ServiceFunctions.get("vokabeln", null);
			Vokabeln vv = new Vokabeln(line);
			alVokabeln = vv.getVokabeln();
		} catch (VokabeltrainerException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.titleProperty().set("Fehler");
			alert.setContentText(e.toString());
			alert.showAndWait();
		}

		for (Vokabel vokabel : alVokabeln) {
			if (vokabel.getChinesischSimpl().contains(query) || vokabel.getChinesischTrad().contains(query)
					|| vokabel.getPinyin().contains(query) || vokabel.getBedeutung().contains(query)
					|| vokabel.getVokabelBeschreibung().contains(query) || vokabel.getWortart().contains(query)) {
				olVokabeln.add(new VokabelFX(vokabel));
			}
		}
	}

	// Diese Methode erzeugt eine Fehlermeldung und wird für Fehler bei der Auswahl
	// von Table-View-Elementen benutzt
	private void showErrorMessage(String messageHeader, String messageText) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Fehler!");
		alert.setHeaderText(messageHeader);
		alert.setContentText(messageText);
		alert.showAndWait();
	}
}
