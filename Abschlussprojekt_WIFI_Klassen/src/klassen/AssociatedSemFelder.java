package klassen;

/*Containerklasse, die eine Sammlung von in der DB einem Vokabel zugeordneten 
 * SemantischesFeld-Objektem in einer Array-List verwaltet und es ermöglicht, diese 
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

public class AssociatedSemFelder {

	/*
	 * Array-List für das Speichern und Handling von SemantischesFeld-Objekten, die
	 * in der DB einem Vokabel zugeordnet sind, wird deklariert
	 */
	private ArrayList<SemantischesFeld> associatedSemFelder;

	/*
	 * Der Konstruktor nimmt eine Array-List von SemantischesFeld-Objekten, die
	 * einem Vokabel zugeordnet sind, als Parameter und initialisiert sie.
	 */
	public AssociatedSemFelder(ArrayList<SemantischesFeld> associatedSemFelder) {
		this.associatedSemFelder = associatedSemFelder;
	}

	/*
	 * Nimmt eine XML-Zeichenfolge als Parameter, analysiert sie mit dem SAX-Parser
	 * und füllt die Array-List mit extrahierten SemantischesFeld-Objekten.
	 */
	public AssociatedSemFelder(String xmlString) {
		if (xmlString == null || xmlString.length() == 0)
			return;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			StringReader str = new StringReader(xmlString);
			XMLHandler xh = new XMLHandler();
			sp.parse(new InputSource(str), xh);
			associatedSemFelder = xh.getAssociatedSemFelder();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}

	}

	// Getter und Setter werden implementiert.
	public ArrayList<SemantischesFeld> getAssociatedSemFelder() {
		return associatedSemFelder;
	}

	public void setAssociatedSemFelder(ArrayList<SemantischesFeld> associatedSemFelder) {
		this.associatedSemFelder = associatedSemFelder;
	}

	/*
	 * Konvertiert die Sammlung von SemantischeFeld-Ojekten in XML, indem die
	 * toXML()-Methode für jedes SemantischesFeld-Objekt in der Array-List
	 * aufgerufen, um es in entsprechende XML-Tags zu verpacken.
	 */
	public String toXML() {
		StringBuilder sb = new StringBuilder(100);
		sb.append("<associatedsemfelder>");
		if (associatedSemFelder != null)
			for (SemantischesFeld sf : associatedSemFelder)
				sb.append(sf.toXML());
		sb.append("</associatedsemfelder>");
		return sb.toString();
	}

}
