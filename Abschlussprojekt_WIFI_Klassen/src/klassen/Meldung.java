package klassen;

/*Diese Klasse verarbeitet Text- und XML-Daten und enthält Methoden zum Parsen von XML und 
 * zum Konvertieren von Daten in und aus XML. Sie wird für Meldungen innerhalb der 
 * REST-API verwendet.*/

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Meldung {

	// Instanzvariable für den Meldungsinhalt wird deklariert
	private String text;

	// Parameterloser Default-Konstruktor
	public Meldung() {
	}

	/*
	 * Nimmt und initialisiert String text als Parameter. Wenn Parameter-String kein
	 * XML ist (nicht mit „<“ beginnt), wird der Wert direkt String text zugewiesen.
	 * Wenn String text ein XML ist, wird der String analysiert, um den Inhalt
	 * mithilfe der XMLHandler-Klasse zu extrahieren.
	 */
	public Meldung(String text) {
		super();
		if (text == null || text.length() == 0)
			return;
		if (!text.startsWith("<")) {
			this.text = text;
		} else {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			try {
				SAXParser sp = spf.newSAXParser();
				StringReader str = new StringReader(text);
				XMLHandler xh = new XMLHandler();
				sp.parse(new InputSource(str), xh);
				text = xh.getMeldung().getText();
			} catch (SAXException | ParserConfigurationException | IOException ie) {
				ie.printStackTrace();
			}
		}
	}

	/*
	 * Gibt eine XML-Darstellung des Meldung-Objekts zurück. Hüllt den Inhalt in
	 * XML.
	 */
	public String toXML() {
		return "<meldung><text>" + text + "</text></meldung>";
	}

	// Getter und Setter werden implementiert
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	// Gibt den Meldungs-Inhalt als String zurück
	@Override
	public String toString() {
		return text;
	}

}
