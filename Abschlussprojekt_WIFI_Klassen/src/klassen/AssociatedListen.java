
package klassen;

/*Containerklasse, die eine Sammlung von in der DB einem Vokabel zugeordneten 
 * Liste-Objektem in einer Array-List verwaltet und es ermöglicht, diese 
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

public class AssociatedListen {

	/*
	 * Array-List für das Speichern und Handling von Liste-Objekten, die in der DB
	 * einem Vokabel zugeordnet sind, wird deklariert
	 */
	private ArrayList<Liste> associatedListen;

	/*
	 * Der Konstruktor nimmt eine Array-List von Liste-Objekten als Parameter und
	 * initialisiert sie.
	 */
	public AssociatedListen(ArrayList<Liste> associatedListen) {
		this.associatedListen = associatedListen;
	}

	/*
	 * Nimmt eine XML-Zeichenfolge als Parameter, analysiert sie mit dem SAX-Parser
	 * und füllt die Array-List mit extrahierten Liste-Objekten.
	 */
	public AssociatedListen(String xmlString) {
		if (xmlString == null || xmlString.length() == 0)
			return;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			StringReader str = new StringReader(xmlString);
			XMLHandler xh = new XMLHandler();
			sp.parse(new InputSource(str), xh);
			associatedListen = xh.getAssociatedListen();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}

	}

	// Getter und Setter werden implementiert
	public ArrayList<Liste> getAssociatedListen() {
		return associatedListen;
	}

	public void setAssociatedListen(ArrayList<Liste> associatedListen) {
		this.associatedListen = associatedListen;
	}

	/*
	 * Konvertiert die Sammlung von Liste-Ojekten in XML, indem die toXML()-Methode
	 * für jedes Vokabel-Objekt in der Array-List aufgerufen, um es in entsprechende
	 * XML-Tags zu verpacken.
	 */
	public String toXML() {
		StringBuilder sb = new StringBuilder(100);
		sb.append("<associatedlisten>");
		if (associatedListen != null)
			for (Liste al : associatedListen)
				sb.append(al.toXML());
		sb.append("</associatedlisten>");
		return sb.toString();
	}
}
