package klassen;

/*Diese Klasse ist ein Container zum Sammeln und Managen von Vokabel-Objekten. 
 * Sie enthält auch Methoden zum Bearbeiten und Konvertieren der Vokabel-Objekte 
 * ins XML-Format und vice versa.*/

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Vokabeln {

	/*
	 * Array-List für das Speichern und Handling von Vokabeln-Objekten wird
	 * deklariert.
	 */
	private ArrayList<Vokabel> vokabeln;

	/*
	 * Der Konstruktor verwendet eine ArrayList<Vokabel> als Parameter, die im
	 * Kostruktor initialisiert wird.
	 */
	public Vokabeln(ArrayList<Vokabel> vokabeln) {
		super();
		this.vokabeln = vokabeln;
	}

	/*
	 * Der Konstruktor nimmt eine XML-Zeichenfolge als Parameter, liest die
	 * XML-Daten mit dem SAX-Parser ein, inititialisiert die Array-List vokabeln und
	 * füllt sie gleichzeitig mit den eingelesenen Daten.
	 */
	public Vokabeln(String xmlString) {
		if (xmlString == null || xmlString.length() == 0)
			return;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			StringReader str = new StringReader(xmlString);
			XMLHandler xh = new XMLHandler();
			sp.parse(new InputSource(str), xh);
			vokabeln = xh.getVokabeln();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}

	}

	// Gibt eine Array-Liste von Vokabel-Objekten zurück.
	public ArrayList<Vokabel> getVokabeln() {
		return vokabeln;
	}

	// Setzt Inhalt einer Array-Liste von Vokabel-Objekten.
	public void setVokabeln(ArrayList<Vokabel> vokabeln) {
		this.vokabeln = vokabeln;
	}

	/*
	 * Konvertiert die Sammlung von Vokabel-Objekten in eine XML-Zeichenfolge.
	 * Iteriert durch die Sammlung und ruft die toXML()-Methode jedes
	 * Vokabel-Objekts auf, um es in das XML-Format zu konvertieren. Der
	 * resultierende XML-String wird in einen <vokabeln>-Tag eingeschlossen.
	 */
	public String toXML() {
		StringBuilder sb = new StringBuilder(100);
		sb.append("<vokabeln>");
		if (vokabeln != null)
			for (Vokabel v : vokabeln)
				sb.append(v.toXML());
		sb.append("</vokabeln>");
		return sb.toString();
	}

}
