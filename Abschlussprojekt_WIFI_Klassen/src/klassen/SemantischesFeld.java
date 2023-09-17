package klassen;

/*Domain-Klasse, beinhaltet den Bauplan für ein SemantischeFeld-Objekt mit mehreren Konstruktoren,
 * Gettern und Settern und einer toXML()-Methode. SAX-Parsing wird implementiert, um
 * Objekte in XML und vice versa zu konvertieren.*/

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SemantischesFeld {

	// Instanzvariablen werden deklariert
	private int semFeldID;
	private String semFeldBezeichnung;
	private String semFeldBeschreibung;

	/*
	 * Konstruktor nimmt SemFeldID, semFeldBezeichnung und semFeldBeschreibung als
	 * Parameter und initialisiert die entsprechenden Properties.
	 */
	public SemantischesFeld(int semFeldID, String semFeldBezeichnung, String semFeldBeschreibung) {
		super();
		this.semFeldID = semFeldID;
		this.semFeldBezeichnung = semFeldBezeichnung;
		this.semFeldBeschreibung = semFeldBeschreibung;
	}

	public SemantischesFeld() {
		// parameterloser KOnstruktor
	}

	/*
	 * Konstruktor nimmt XML-Zeichenfolge und verwendet SAX-Parsing, um die
	 * Instanzvariablen „semFeldID“, „semFeldBezeichnung“ und „semFeldBeschreibung“
	 * aus XML zu extrahieren und zu initialisieren.
	 */
	public SemantischesFeld(String xmlString) {
		if (xmlString == null || xmlString.length() == 0)
			return;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			StringReader str = new StringReader(xmlString);
			XMLHandler xh = new XMLHandler();
			sp.parse(new InputSource(str), xh);
			semFeldID = xh.getSemFeld().getSemFeldID();
			semFeldBezeichnung = xh.getSemFeld().getSemFeldBezeichnung();
			semFeldBeschreibung = xh.getSemFeld().getSemFeldBeschreibung();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}

	}

	// Getter und Setter werden implementiert
	public int getSemFeldID() {
		return semFeldID;
	}

	public void setSemFeldID(int semFeldID) {
		this.semFeldID = semFeldID;
	}

	public String getSemFeldBezeichnung() {
		return semFeldBezeichnung;
	}

	public void setSemFeldBezeichnung(String semFeldBezeichnung) {
		this.semFeldBezeichnung = semFeldBezeichnung;
	}

	public String getSemFeldBeschreibung() {
		return semFeldBeschreibung;
	}

	public void setSemFeldBeschreibung(String semFeldBeschreibung) {
		this.semFeldBeschreibung = semFeldBeschreibung;
	}

	/*
	 * Konvertiert die Daten des Objekts in XML. Erstellt anhand der der
	 * Klassen-Properties eine XML-Zeichenfolge und verpackt sie in entsprechende
	 * XML-Tags.
	 */
	public String toXML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<semantischesfeld>");
		sb.append("<semfeldid>" + semFeldID + "</semfeldid>");
		sb.append("<semfeldbezeichnung>" + semFeldBezeichnung + "</semfeldbezeichnung>");
		sb.append("<semfeldbeschreibung>" + semFeldBeschreibung + "</semfeldbeschreibung>");
		sb.append("</semantischesfeld>");
		return sb.toString();
	}

}
