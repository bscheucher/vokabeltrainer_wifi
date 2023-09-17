package client;

/*Diese Klasse ist für JavaFX-basierte Benutzeroberflächenkomponenten konzipiert
 * und umschließt eine Instanz der SemantischesFeld-Klasse. 
 * Sie ist Teil einer grafischen Benutzeroberfläche-(GUI)-Implementierung 
 * zur Anzeige von bzw. Interaktion mit SemantischesFeld-Objekten.*/

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import klassen.SemantischesFeld;

public class SemantischesFeldFX {

	/*
	 * Als Instanzvariablen finden sich hier modellSemFeld: Eine Instanzvariable,
	 * die ein Objekt der SemantischesFeld-Klasse enthält. semFeldID,
	 * semFeldBezeichnung und semFeldBeschreibung sind SimpleIntegerProperty- und
	 * SimpleStringProperty-Variablen, die zum Binden der Werte aus dem
	 * SemantischesFeld-Objekt verwendet werden.
	 */
	private SemantischesFeld modellSemFeld;
	private SimpleIntegerProperty semFeldID;
	private SimpleStringProperty semFeldBezeichnung;
	private SimpleStringProperty semFeldBeschreibung;

	/*
	 * Der Konstruktor verwendet ein SemantischesFeld-Objekt als Argument und
	 * initialisiert die Instanzvariablen mit den Werten aus dem bereitgestellten
	 * SemantischesFeld-Objekt.
	 */
	public SemantischesFeldFX(SemantischesFeld modellSemFeld) {
		this.modellSemFeld = modellSemFeld;
		semFeldID = new SimpleIntegerProperty(modellSemFeld.getSemFeldID());
		semFeldBezeichnung = new SimpleStringProperty(modellSemFeld.getSemFeldBezeichnung());
		semFeldBeschreibung = new SimpleStringProperty(modellSemFeld.getSemFeldBeschreibung());
	}

	/*
	 * Getter- und Setter-Methoden für jede der Instanzvariablen definiert. Diese
	 * Methoden bieten Zugriff auf die Eigenschaften des SemantischesFeld-Objekts
	 * und ermöglichen eine ordnungsgemäße Bindung mit JavaFX-UI-Komponenten.
	 * Außerdem gibt es Methoden, die SimpleIntegerProperty- oder
	 * SimpleStringProperty-Objekte für die jeweiligen Properties zurückgeben, um
	 * diese an die JavaFX-UI-Komponenten zu binden.
	 */

	public SemantischesFeld getModellSemFeld() {
		return modellSemFeld;
	}

	public void setModellSemFeld(SemantischesFeld modellSemFeld) {
		this.modellSemFeld = modellSemFeld;
	}

	public final SimpleIntegerProperty semFeldIDProperty() {
		return this.semFeldID;
	}

	public final int getSemFeldID() {
		return this.semFeldIDProperty().get();
	}

	public final void setSemFeldID(final int semFeldID) {
		this.semFeldIDProperty().set(semFeldID);
	}

	public final SimpleStringProperty semFeldBezeichnungProperty() {
		return this.semFeldBezeichnung;
	}

	public final String getSemFeldBezeichnung() {
		return this.semFeldBezeichnungProperty().get();
	}

	public final void setSemFeldBezeichnung(final String semFeldBezeichnung) {
		this.semFeldBezeichnungProperty().set(semFeldBezeichnung);
	}

	public final SimpleStringProperty semFeldBeschreibungProperty() {
		return this.semFeldBeschreibung;
	}

	public final String getSemFeldBeschreibung() {
		return this.semFeldBeschreibungProperty().get();
	}

	public final void setSemFeldBeschreibung(final String semFeldBeschreibung) {
		this.semFeldBeschreibungProperty().set(semFeldBeschreibung);
	}

	/*
	 * Die toString()-Methode gibt den Wert der semFeldBezeichnung-Eigenschaft
	 * zurück. Sie wird verwendet, um den Namen des semantischen Feldes im
	 * FKDeletionDialog anzuzeigen.
	 */
	@Override
	public String toString() {
		return semFeldBezeichnung.get();
	}

}
