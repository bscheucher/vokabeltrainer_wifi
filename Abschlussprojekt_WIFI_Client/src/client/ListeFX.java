package client;

/*Diese Klasse ist für JavaFX-basierte Benutzeroberflächenkomponenten konzipiert
 * und umschließt eine Instanz der Liste-Klasse. 
 * Sie ist Teil einer grafischen Benutzeroberfläche-(GUI)-Implementierung 
 * zur Anzeige von bzw. Interaktion mit Listenobjekten.*/

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import klassen.Liste;

public class ListeFX {

	/*
	 * Als Instanzvariablen finden sich hier modellListe: Eine Instanzvariable, die
	 * ein Objekt der Liste-Klasse enthält. listeID, listeBezeichnung und
	 * listeBeschreibung sind SimpleIntegerProperty- und
	 * SimpleStringProperty-Variablen, die zum Binden der Werte aus dem Liste-Objekt
	 * verwendet werden.
	 */
	private Liste modellListe;
	private SimpleIntegerProperty listeID;
	private SimpleStringProperty listeBezeichnung;
	private SimpleStringProperty listeBeschreibung;

	/*
	 * Der Konstruktor verwendet ein Liste-Objekt als Argument und initialisiert die
	 * Instanzvariablen mit den Werten aus dem bereitgestellten Liste-Objekt.
	 */
	public ListeFX(Liste modellListe) {
		this.modellListe = modellListe;
		listeID = new SimpleIntegerProperty(modellListe.getListeID());
		listeBezeichnung = new SimpleStringProperty(modellListe.getListeBezeichnung());
		listeBeschreibung = new SimpleStringProperty(modellListe.getListeBeschreibung());
	}

	/*
	 * Getter- und Setter-Methoden für jede der Instanzvariablen definiert. 
	 * Diese Methoden bieten Zugriff auf die Eigenschaften des
	 * Liste-Objekts und ermöglichen eine ordnungsgemäße Bindung mit
	 * JavaFX-UI-Komponenten. Außerdem gibt es Methoden, die SimpleIntegerProperty-
	 * oder SimpleStringProperty-Objekte für die jeweiligen Properties zurückgeben,
	 * um diese an die JavaFX-UI-Komponenten zu binden.
	 */

	public Liste getModellListe() {
		return modellListe;
	}

	public void setModellListe(Liste modellListe) {
		this.modellListe = modellListe;
	}

	public final SimpleIntegerProperty listeIDProperty() {
		return this.listeID;
	}

	public final int getListeID() {
		return this.listeIDProperty().get();
	}

	public final void setListeID(final int listeID) {
		this.listeIDProperty().set(listeID);
	}

	public final SimpleStringProperty listeBezeichnungProperty() {
		return this.listeBezeichnung;
	}

	public final String getListeBezeichnung() {
		return this.listeBezeichnungProperty().get();
	}

	public final void setListeBezeichnung(final String listeBezeichnung) {
		this.listeBezeichnungProperty().set(listeBezeichnung);
	}

	public final SimpleStringProperty listeBeschreibungProperty() {
		return this.listeBeschreibung;
	}

	public final String getListeBeschreibung() {
		return this.listeBeschreibungProperty().get();
	}

	public final void setListeBeschreibung(final String listeBeschreibung) {
		this.listeBeschreibungProperty().set(listeBeschreibung);
	}

	/*
	 * Die toString()-Methode gibt den Wert der listeBezeichnung-Eigenschaft zurück.
	 * Sie wird verwendet, um den Namen der Liste im FKDeletionDialog anzuzeigen.
	 */
	@Override
	public String toString() {
		return listeBezeichnung.get();
	}

}
