package client;

/* Modaler Dialog, mit zwei JavaFX-List-Views. Klasse bietet die Möglichkeit,
 * Foreign Keys, die Vokabeln mit Listen bzw. semantischen Feldern verbinden,
 * aus der DB zu löschen.
 * */

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import klassen.AssociatedListen;
import klassen.AssociatedSemFelder;
import klassen.Liste;
import klassen.SemantischesFeld;
import klassen.Vokabel;

public class FKDeletionDialog extends Dialog<ButtonType> {

	// Vokabel-Objekt wird deklariert und weiter unten im Konstruktor initialisiert
	Vokabel vokabel;

	/*
	 * Observable Lists dienen zum Speichern und Handling von Listen und
	 * semantischen Feldern für die List-Views
	 */
	ObservableList<ListeFX> olListen = FXCollections.observableArrayList();
	ObservableList<SemantischesFeldFX> olSemFelder = FXCollections.observableArrayList();


	/*
	 * Der Konstruktor initialisiert den Dialog, indem er die GUI-Komponenten,
	 * Event-Handler für mit Buttons assozierte Aktionen und den Inhalt der
	 * DialogPane einrichtet.
	 */
	public FKDeletionDialog(Vokabel vokabel) {

		// Instanz-Vokabel-Objekt wird initialisiert
		this.vokabel = vokabel;

		// olListen wird mit Listen-Objekten aus der Datenbank gefüllt
		leseListen();

		/*
		 * List-View zum Anzeigen der mit dem Instanz-Vokabel verbundenen Listen wird
		 * erstellt, gefüllt, mit Single-Auswahlmodus versehen
		 */
		ListView lvListen = new ListView<>();
		lvListen.setItems(olListen);
		lvListen.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		// olSemFelder wird mit SemantischesFeld-Objekten aus der Datenbank gefüllt
		leseSemFelder();

		/*
		 * List-View zum Anzeigen der mit dem Instanz-Vokabel verbundenen semantischen
		 * Feldern wird erstellt, gefüllt, mit Single-Auswahlmodus versehen
		 */
		ListView lvSemFelder = new ListView<>();
		lvSemFelder.setItems(olSemFelder);
		lvSemFelder.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		/*
		 * Zwei Buttons zum Entfernen der Foreign Keys, die Listen und semantische
		 * Felder mit dem Instanz-Vokabel verbinden, werden erstellt
		 */
		Button btnListeEntfernen = new Button("Entfernen");
		Button btnSemFeldEntfernen = new Button("Entfernen");

		// Grid-Pane wird erstellt und mit Labels, List-Views und Buttons gefüllt
		GridPane gp = new GridPane();
		gp.add(new Label(vokabel.getChinesischSimpl()), 0, 0);
		gp.add(new Label(vokabel.getPinyin()), 1, 0);
		gp.add(new Label("Listen"), 0, 1);
		gp.add(new Label("Semantische Felder"), 1, 1);
		gp.add(lvListen, 0, 2);
		gp.add(lvSemFelder, 1, 2);
		gp.add(btnListeEntfernen, 0, 3);
		gp.add(btnSemFeldEntfernen, 1, 3);

		/*
		 * Action-Handler ruft das ausgewählte ListeFX-Element aus der List-View ab
		 * löscht mithilfe der ServiceFunctions.delete-Methode die Zuordnung zwischen
		 * dem Vokabel und der ausgewählten Liste aus der DB.
		 */
		btnListeEntfernen.setOnAction(e -> {
			ListeFX selectedListe = (ListeFX) lvListen.getSelectionModel().getSelectedItem();
			try {
				ServiceFunctions.delete("vokabelnliste", String.valueOf(vokabel.getVokabelID()),
						String.valueOf(selectedListe.getListeID()));
			} catch (VokabeltrainerException e1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.titleProperty().set("Fehler");
				alert.setContentText(e.toString());
				alert.showAndWait();
			}
			leseListen();
		});

		/*
		 * Action-Handler ruft das ausgewählte SemantischesFeldFX-Element aus der
		 * List-View ab und löscht mithilfe der ServiceFunctions.delete-Methode die
		 * Zuordnung zwischen dem Vokabel und dem ausgewählten semantischen Feld aus der
		 * DB.
		 */
		btnSemFeldEntfernen.setOnAction(e -> {
			SemantischesFeldFX selectedSemFeld = (SemantischesFeldFX) lvSemFelder.getSelectionModel().getSelectedItem();
			try {
				ServiceFunctions.delete("vokabelnsemfeld", String.valueOf(vokabel.getVokabelID()),
						String.valueOf(selectedSemFeld.getSemFeldID()));
			} catch (VokabeltrainerException e1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.titleProperty().set("Fehler");
				alert.setContentText(e.toString());
				alert.showAndWait();
			}
			leseSemFelder();
		});

		/*
		 * Grid-Pane wird in Dialog-Pane integriert, Schließen-Button-Type wird erstellt
		 * und in Dialog-Pane integriert
		 */
		this.getDialogPane().setContent(gp);
		ButtonType schliessen = new ButtonType("Schließen", ButtonData.CANCEL_CLOSE);
		this.getDialogPane().getButtonTypes().addAll(schliessen);

	}

	/* Mit Vokabel assoziierte Liste-Objekte werden über ein AssociatedListen-Objekt aus der DB eingelesen 
	 * und als ListeFX-Objekte in der Observable List olListen gespeichert*/
	private void leseListen() {

		try {
			String line = ServiceFunctions.get("associatedlisten", String.valueOf(vokabel.getVokabelID()));
			AssociatedListen al = new AssociatedListen(line);
			olListen.clear();
			for (Liste l : al.getAssociatedListen())
				olListen.add(new ListeFX(l));

		} catch (VokabeltrainerException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.titleProperty().set("Fehler");
			alert.setContentText(e.toString());
			alert.showAndWait();
		}

	}

	/* Mit Vokabel assoziierte SemantischesFeld-Objekte werden über ein AssociatedSemfelder-Objekt aus der DB eingelesen 
	 * und als SemantischesFeldFX-Objekt in der Observable List olSemfelder gespeichert*/
	private void leseSemFelder() {

		try {
			String line = ServiceFunctions.get("associatedsemfelder", String.valueOf(vokabel.getVokabelID()));
			AssociatedSemFelder as = new AssociatedSemFelder(line);
			olSemFelder.clear();
			for (SemantischesFeld s : as.getAssociatedSemFelder())
				olSemFelder.add(new SemantischesFeldFX(s));

		} catch (VokabeltrainerException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.titleProperty().set("Fehler");
			alert.setContentText(e.toString());
			alert.showAndWait();
		}
	}
}
