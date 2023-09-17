package client;

/*Diese Klasse besteht aus einem modalen Dialog, der dazu dient, Vokabel-Elemente neu zu erstellen 
 * bzw. bereits vorhandene Vokabel-Elemente zu aktualisieren. 
 * Als GUI wird JavaFX verwendet, die Vokabel-Elemente werden über die POST- und PUT-methode der REST API in
 * die DB geschrieben */

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import klassen.Vokabel;

public class VokabelDetailDialog extends Dialog<ButtonType> {

	/*Eine Hash Map wird statisch initialisiert und gefüllt, 
	um Vokale und Zahlen (Vokal + Töne 1-4) als Key durch die entsprechenden Unicode-Character-Codes als Value zu substituieren.*/
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

	public VokabelDetailDialog(Vokabel vokabel) {

		//GUI-Textfelder werden initialisiert und gefüllt
		TextField txtChinSimp = new TextField();
		txtChinSimp.setText(vokabel.getChinesischSimpl());
		TextField txtChinTrad = new TextField();
		txtChinTrad.setText(vokabel.getChinesischTrad());
		TextField txtPinyin = new TextField();
		txtPinyin.setText(vokabel.getPinyin());
		/*Das Pinyin-Textfeld erhält einen Listener, um zu überprüfen, 
		 * ob die Kombination Vokal + Zahl von 1-4 vorhanden ist und durch den 
		 * entsprechenden Pinyin-Wert zu ersetzen. */
		txtPinyin.textProperty().addListener((observable, oldValue, newValue) -> {
			/* Iteriert durch die Hash Map substitutions and ersetzt die Keys durch die
			entsprechenden Values*/
			for (Map.Entry<String, String> entry : substitutions.entrySet()) {
				newValue = newValue.replace(entry.getKey(), entry.getValue());
			}
			// Das Textfeld wird auf den aktualisierten Value gesetzt 
			txtPinyin.setText(newValue);
		});
		TextField txtBedeutung = new TextField();
		txtBedeutung.setText(vokabel.getBedeutung());
		TextField txtBeschreibung = new TextField();
		txtBeschreibung.setText(vokabel.getVokabelBeschreibung());

		//Eine Kombobox wird initialisiert und mit den entsprechenden Items (Wortarten) gefüllt
		ComboBox<String> cbWortart = new ComboBox<>();
		cbWortart.getItems().addAll("Nomen", "Verb", "Adjektiv", "Pronomen", "Präposition", "Konnektor", "Adverb",
				"Partikel", "Phrase");

		//Falls Wortart vorhanden, wird die Kombobox auf diesen Wert gesetzt
		if (vokabel.getWortart() != null)
			cbWortart.getSelectionModel().select(vokabel.getWortart());
		
		//Button für den Foreign-Key-Deletion-Dialog wird initialiert
		Button fkButton = new Button("Anzeigen");

		//Eine Gridpane wird erzeugt und mit Labels bzw. den oben erzeugten Textfeldern und einem Button gefüllt.
		GridPane gp = new GridPane();
		gp.add(new Label("Vokabel-ID"), 0, 0);
		gp.add(new Label(String.valueOf(vokabel.getVokabelID())), 1, 0);
		gp.add(new Label("Chinesisch vereinfacht"), 0, 1);
		gp.add(txtChinSimp, 1, 1);
		gp.add(new Label("Chinesisch traditionell"), 0, 2);
		gp.add(txtChinTrad, 1, 2);
		gp.add(new Label("Pinyin"), 0, 3);
		gp.add(txtPinyin, 1, 3);
		gp.add(new Label("Bedeutung"), 0, 4);
		gp.add(txtBedeutung, 1, 4);
		gp.add(new Label("Beschreibung"), 0, 5);
		gp.add(txtBeschreibung, 1, 5);
		gp.add(new Label("Wortart"), 0, 6);
		gp.add(cbWortart, 1, 6);
		gp.add(new Label("Zuordnung zu Listen / Feldern: "), 0, 7);
		gp.add(fkButton, 1, 7);

		/*Action-Handler für den Foreign-Key-Deletion-Button öffnet den Foreign-Key-Deletion-Dialog, 
		 * um eventuelle Foreign Keys anzuzeigen bzw. löschen zu können.*/
		fkButton.setOnAction(e -> {
			Optional<ButtonType> r = new FKDeletionDialog(vokabel).showAndWait();
		});
		
		//Gridpane wir in die Dialogpane integriert.
		this.getDialogPane().setContent(gp);
		//Buttontypes für speichern bzw. abbrechen werden erzeugt.
		ButtonType speichern = new ButtonType("Speichern", ButtonData.OK_DONE);
		ButtonType abbrechen = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);
		//Buttontypes werden in die Dialogpane integriert
		this.getDialogPane().getButtonTypes().addAll(speichern, abbrechen);
		//Speichern-Button wird mit entsprechendem Buttontype verbunden 
		Button btnSpeichern = (Button) this.getDialogPane().lookupButton(speichern);

		//Speichern-Button erhält Event-Filter, um zu gewährleisten, dass kein Textfeld leer ist.
		btnSpeichern.addEventFilter(ActionEvent.ACTION, e -> {
			if (txtChinSimp.getText() == null || (txtChinSimp.getText().length() == 0)) {
				new Alert(AlertType.ERROR, "Chinesisch vereinfacht darf nicht leer sein.").showAndWait();
				e.consume();
				return;
			}
			if (txtChinTrad.getText() == null || (txtChinTrad.getText().length() == 0)) {
				new Alert(AlertType.ERROR, "Chinesisch traditionell darf nicht leer sein.").showAndWait();
				e.consume();
				return;
			}
			if (txtPinyin.getText() == null || txtPinyin.getText().length() == 0) {
				new Alert(AlertType.ERROR, "Pinyin darf nicht leer sein.").showAndWait();
				e.consume();
				return;
			}
			if (txtBedeutung.getText() == null || txtBedeutung.getText().length() == 0) {
				new Alert(AlertType.ERROR, "Bedeutung darf nicht leer sein.").showAndWait();
				e.consume();
				return;
			}
			if (txtBeschreibung.getText() == null || txtBeschreibung.getText().length() == 0) {
				new Alert(AlertType.ERROR, "Beschreibung darf nicht leer sein.").showAndWait();
				e.consume();
				return;
			}
			if (cbWortart.getSelectionModel().isEmpty()) {
				new Alert(AlertType.ERROR, "Wortart muss ausgewählt werden.").showAndWait();
				e.consume();
				return;
			}
		});

		/*Result-Converter schreibt Werte mit POST- und PUT-Methoden der REST API in die DB,
		 * prüft zuvor anhand der Vokabel-Id,  ob Vokabel-Element neu erstellt worden oder bereits vorhanden ist, 
		 * um passende Spreichermethode zu ermitteln.*/
		this.setResultConverter(dialogButton -> {
			if (dialogButton == speichern) {
				vokabel.setChinesischSimpl(txtChinSimp.getText());
				vokabel.setChinesischTrad(txtChinTrad.getText());
				vokabel.setPinyin(txtPinyin.getText());
				vokabel.setBedeutung(txtBedeutung.getText());
				vokabel.setVokabelBeschreibung(txtBeschreibung.getText());
				vokabel.setWortart(cbWortart.getValue());

				if (vokabel.getVokabelID() == 0) {
					try {
						ServiceFunctions.post("vokabel", Long.toString(vokabel.getVokabelID()), vokabel.toXML());

					} catch (VokabeltrainerException e1) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.titleProperty().set("Fehler");
						alert.setContentText(e1.toString());
						alert.showAndWait();
					}

				} else {
					try {
						ServiceFunctions.put("vokabel", Long.toString(vokabel.getVokabelID()), vokabel.toXML());

					} catch (VokabeltrainerException e1) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.titleProperty().set("Fehler");
						alert.setContentText(e1.toString());
						alert.showAndWait();
					}
				}

			}

			return dialogButton;
		});

	}

}
