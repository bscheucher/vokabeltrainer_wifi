package klassen;

/*Containerklasse, die eine Sammlung von SemantischesFeld-Instanzen in einer Array-List 
 * verwaltet und es ermöglicht, diese in das XML-Format zu konvertieren und 
 * auch XML-Daten zu analysieren, um die Sammlung zu füllen.*/

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SemantischeFelder {

	/*
	 * Array-List für das Speichern und Handling von SemantischesFeld-Objekten wird
	 * deklariert
	 */
	private ArrayList<SemantischesFeld> semantischeFelder;

	/*
	 * Der Konstruktor verwendet eine Array-List von SemantischesFeld-Objekten als
	 * Parameter und initialisiert sie.
	 */
	public SemantischeFelder(ArrayList<SemantischesFeld> semantischeFelder) {
		this.semantischeFelder = semantischeFelder;
	}

	/*
	 * Nimmt eine XML-Zeichenfolge als Parameter, analysiert sie mit dem SAX-Parser
	 * und füllt die Array-List mit extrahierten SemantischesFeld-Objekten.
	 */
	public SemantischeFelder(String xmlString) {
		if (xmlString == null || xmlString.length() == 0)
			return;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			StringReader str = new StringReader(xmlString);
			XMLHandler xh = new XMLHandler();
			sp.parse(new InputSource(str), xh);
			semantischeFelder = xh.getSemantischeFelder();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}

	}

	// Getter und Setter werden implementiert
	public ArrayList<SemantischesFeld> getSemantischeFelder() {
		return semantischeFelder;
	}

	public void setSemantischeFelder(ArrayList<SemantischesFeld> semantischeFelder) {
		this.semantischeFelder = semantischeFelder;
	}

	/*
	 * Konvertiert die Sammlung von SemantischesFeld-Ojekten in XML, indem die
	 * toXML()-Methode für jedes SemantisachesFeld-Objekt in der Array-List
	 * aufgerufen, um es in entsprechende XML-Tags zu verpacken.
	 */
	public String toXML() {
		StringBuilder sb = new StringBuilder(100);
		sb.append("<semantischefelder>");
		if (semantischeFelder != null)
			for (SemantischesFeld s : semantischeFelder)
				sb.append(s.toXML());
		sb.append("</semantischefelder>");
		return sb.toString();
	}
}
