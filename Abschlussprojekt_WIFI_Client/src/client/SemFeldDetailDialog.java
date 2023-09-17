package client;

/*Diese Klasse besteht aus einem modalen Dialog, der dazu dient, SemantischesFeld-Elemente neu zu erstellen 
 * bzw. bereits vorhandene Elemente zu aktualisieren. 
 * Als GUI wird JavaFX verwendet, die SemantischesFeld-Elemente werden über die POST- und PUT-methode der REST API in
 * die DB geschrieben */

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import klassen.SemantischesFeld;

public class SemFeldDetailDialog extends Dialog<ButtonType>{

	public SemFeldDetailDialog(SemantischesFeld semantischesFeld) {
		
		//GUI-Textfelder werden initialisiert und gefüllt
		TextField bezeichnung = new TextField();
		bezeichnung.setText(semantischesFeld.getSemFeldBezeichnung());
		TextField beschreibung = new TextField();
		beschreibung.setText(semantischesFeld.getSemFeldBeschreibung());
		
		//Eine Gridpane wird erzeugt und mit Labels bzw. den oben erzeugten Textfeldern gefüllt.
		GridPane gp = new GridPane();
		gp.add( new Label("Liste-ID"), 0, 0);
		gp.add(new Label(String.valueOf(semantischesFeld.getSemFeldID())), 1, 0);
		gp.add(new Label("Bezeichnung"), 0, 1);
		gp.add(bezeichnung, 1, 1);
		gp.add(new Label("Beschreibung"), 0, 2);
		gp.add(beschreibung, 1, 2);
		
		//Gridpane wird in die Dialogpane integriert.
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
			if (bezeichnung.getText() == null || (bezeichnung.getText().length() == 0)) {
				new Alert(AlertType.ERROR, "Bezeichnung darf nicht leer sein.").showAndWait();
				e.consume();
				return;
			}
				
			if (beschreibung.getText() == null || beschreibung.getText().length() == 0) {
				new Alert(AlertType.ERROR, "Beschreibung darf nicht leer sein.").showAndWait();
				e.consume();
				return;
			}
			
		});
		
		/*Result-Converter schreibt Werte mit POST- und PUT-Methoden der REST API in die DB,
		 * prüft zuvor anhand der Vokabel-Id, ob SemantischesFeld-Element neu erstellt worden oder bereits vorhanden ist, 
		 * um passende Spreichermethode zu ermitteln.*/
		this.setResultConverter(dialogButton -> {
            if (dialogButton == speichern) {
                semantischesFeld.setSemFeldBezeichnung(bezeichnung.getText());
                semantischesFeld.setSemFeldBeschreibung(beschreibung.getText());
                
                    if (semantischesFeld.getSemFeldID() == 0) {
                    	try {
    						ServiceFunctions.post("semantischesfeld", Long.toString(semantischesFeld.getSemFeldID()), semantischesFeld.toXML());

    					} catch (VokabeltrainerException e1) {
    						Alert alert = new Alert(AlertType.ERROR);
    						alert.titleProperty().set("Fehler");
    						alert.setContentText(e1.toString());
    						alert.showAndWait();
    					}
                    } else {
                    	try {
    						ServiceFunctions.put("semantischesfeld", Long.toString(semantischesFeld.getSemFeldID()), semantischesFeld.toXML());

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
