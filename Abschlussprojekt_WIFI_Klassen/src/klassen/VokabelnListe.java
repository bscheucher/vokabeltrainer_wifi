package klassen;

/*Containerklasse, die eine Sammlung von in der DB einer Liste zugeordneten 
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

public class VokabelnListe {

	/*
	 * Array-List für das Speichern und Handling von Vokabel-Objekten, die einer
	 * Liste zugeornet sind, wird deklariert
	 */
	private ArrayList<Vokabel> alVokabelnListe;

	/*
	 * Der Konstruktor nimmt eine Array-List von Vokabel-Objekten als Parameter und
	 * initialisiert sie.
	 */
	public VokabelnListe(ArrayList<Vokabel> alVokabelnListe) {
		this.alVokabelnListe = alVokabelnListe;

	}

	/*
	 * Nimmt eine XML-Zeichenfolge als Parameter, analysiert sie mit dem SAX-Parser
	 * und füllt die Array-List mit extrahierten Vokabel-Objekten.
	 */
	public VokabelnListe(String xmlString) {

		if (xmlString == null || xmlString.length() == 0)
			return;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			StringReader str = new StringReader(xmlString);
			XMLHandler xh = new XMLHandler();
			sp.parse(new InputSource(str), xh);
			alVokabelnListe = xh.getVokabelnListe();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}

	}

	// Getter und Setter werden implementiert
	public ArrayList<Vokabel> getVokabelnListe() {
		return alVokabelnListe;
	}

	public void setVokabelnListe(ArrayList<Vokabel> alVokabelnListe) {
		this.alVokabelnListe = alVokabelnListe;
	}

	/*
	 * Konvertiert die Sammlung von Vokabel-Ojekten in XML, indem die
	 * toXML()-Methode für jedes Vokabel-Objekt in der Array-List aufgerufen, um es
	 * in entsprechende XML-Tags zu verpacken.
	 */
	public String toXML() {
		StringBuilder sb = new StringBuilder(100);
		sb.append("<vokabelnliste>");
		if (alVokabelnListe != null)
			for (Vokabel v : alVokabelnListe)
				sb.append(v.toXML());
		sb.append("</vokabelnliste>");
		return sb.toString();
	}

}
