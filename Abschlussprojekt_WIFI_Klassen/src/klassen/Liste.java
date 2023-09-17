package klassen;

/*Domain-Klasse, beinhaltet den Bauplan für ein Liste-Objekt mit mehreren Konstruktoren,
 * Gettern und Settern und einer toXML()-Methode. SAX-Parsing wird implementiert, um
 * Objekte in XML und vice versa zu konvertieren.*/

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Liste {

	// Instanzvariablen werden deklariert
	private int listeID;
	private String listeBezeichnung;
	private String listeBeschreibung;

	/*
	 * Konstruktor nimmt listeID, listeBezeichnung und listeBeschreibung als
	 * Parameter und initialisiert die entsprechenden Properties.
	 */
	public Liste(int listeID, String listeBezeichnung, String listeBeschreibung) {
		super();
		this.listeID = listeID;
		this.listeBezeichnung = listeBezeichnung;
		this.listeBeschreibung = listeBeschreibung;
	}

	public Liste() {
		// parameterloser Konstruktor
	}

	/*
	 * Konstruktor nimmt XML-Zeichenfolge und verwendet SAX-Parsing, um die
	 * Instanzvariablen „listeID“, „listeBezeichnung“ und „listeBeschreibung“ aus
	 * XML zu extrahieren und zu initialisieren.
	 */
	public Liste(String xmlString) {
		if (xmlString == null || xmlString.length() == 0)
			return;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			StringReader str = new StringReader(xmlString);
			XMLHandler xh = new XMLHandler();
			sp.parse(new InputSource(str), xh);
			listeID = xh.getListe().getListeID();
			listeBezeichnung = xh.getListe().getListeBezeichnung();
			listeBeschreibung = xh.getListe().getListeBeschreibung();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}

	}

	// Getter und Setter werden implementiert
	public int getListeID() {
		return listeID;
	}

	public void setListeID(int listeID) {
		this.listeID = listeID;
	}

	public String getListeBezeichnung() {
		return listeBezeichnung;
	}

	public void setListeBezeichnung(String listeBezeichnung) {
		this.listeBezeichnung = listeBezeichnung;
	}

	public String getListeBeschreibung() {
		return listeBeschreibung;
	}

	public void setListeBeschreibung(String listeBeschreibung) {
		this.listeBeschreibung = listeBeschreibung;
	}

	/*
	 * Konvertiert die Daten des Objekts in XML. Erstellt anhand der der
	 * Klassen-Properties eine XML-Zeichenfolge und verpackt sie in entsprechende
	 * XML-Tags.
	 */
	public String toXML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<liste>");
		sb.append("<listeid>" + listeID + "</listeid>");
		sb.append("<listebezeichnung>" + listeBezeichnung + "</listebezeichnung>");
		sb.append("<listebeschreibung>" + listeBeschreibung + "</listebeschreibung>");
		sb.append("</liste>");
		return sb.toString();
	}

}
