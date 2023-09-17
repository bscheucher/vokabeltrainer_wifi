package klassen;

/*Containerklasse, die eine Sammlung von in der DB einem semantischen Feld zugeordneten 
 * Vokabel-Objektem in einer Array-List verwaltet und es ermöglicht, diese 
 * in das XML-Format zu konvertieren und außerdem auch XML-Daten zu analysieren, 
 * um die Containerklasse zu füllen.*/

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class VokabelnSemFeld {

	/*
	 * Array-List für das Speichern und Handling von Vokabel-Objekten, die einem
	 * semantischen Feld zugeordnet sind, wird deklariert
	 */
	private ArrayList<Vokabel> alVokabelnSemFeld;

	/*
	 * Der Konstruktor nimmt eine Array-List von Vokabel-Objekten als Parameter und
	 * initialisiert sie.
	 */
	public VokabelnSemFeld(ArrayList<Vokabel> alVokabelnSemFeld) {
		this.alVokabelnSemFeld = alVokabelnSemFeld;

	}

	/*
	 * Nimmt eine XML-Zeichenfolge als Parameter, analysiert sie mit dem SAX-Parser
	 * und füllt die Array-List mit extrahierten Vokabel-Objekten.
	 */
	public VokabelnSemFeld(String xmlString) {

		if (xmlString == null || xmlString.length() == 0)
			return;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			StringReader str = new StringReader(xmlString);
			XMLHandler xh = new XMLHandler();
			sp.parse(new InputSource(str), xh);
			alVokabelnSemFeld = xh.getVokabelnSemFeld();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}

	}

	// Getter und Setter werden implementiert
	public ArrayList<Vokabel> getVokabelnSemFeld() {
		return alVokabelnSemFeld;
	}

	public void setVokabelnSemFeld(ArrayList<Vokabel> alVokabelnSemFeld) {
		this.alVokabelnSemFeld = alVokabelnSemFeld;
	}

	/*
	 * Konvertiert die Sammlung von Vokabel-Ojekten in XML, indem die
	 * toXML()-Methode für jedes Vokabel-Objekt in der Array-List aufgerufen, um es
	 * in entsprechende XML-Tags zu verpacken.
	 */
	public String toXML() {
		StringBuilder sb = new StringBuilder(100);
		sb.append("<vokabelnsemfeld>");
		if (alVokabelnSemFeld != null)
			for (Vokabel v : alVokabelnSemFeld)
				sb.append(v.toXML());
		sb.append("</vokabelnsemfeld>");
		return sb.toString();
	}

}
