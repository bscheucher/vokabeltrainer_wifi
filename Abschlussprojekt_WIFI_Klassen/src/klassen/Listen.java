package klassen;

/*Containerklasse, die eine Sammlung von Liste-Instanzen in einer Array-List 
 * verwaltet und es ermöglicht, diese in das XML-Format zu konvertieren und 
 * auch XML-Daten zu analysieren, um die Container-Klasse zu füllen.*/

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Listen {

	/*
	 * Array-List für das Speichern und Handling von Liste-Objekten wird deklariert
	 */
	private ArrayList<Liste> listen;

	/*
	 * Der Konstruktor verwendet eine Array-List von Liste-Objekten als Parameter
	 * und initialisiert sie.
	 */
	public Listen(ArrayList<Liste> listen) {
		super();
		this.listen = listen;
	}

	/*
	 * Nimmt eine XML-Zeichenfolge als Parameter, analysiert sie mit dem SAX-Parser
	 * und füllt die Array-List mit extrahierten Liste-Objekten.
	 */
	public Listen(String xmlString) {
		if (xmlString == null || xmlString.length() == 0)
			return;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			StringReader str = new StringReader(xmlString);
			XMLHandler xh = new XMLHandler();
			sp.parse(new InputSource(str), xh);
			listen = xh.getListen();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}

	}

	// Getter und Setter werden implementiert
	public ArrayList<Liste> getListen() {
		return listen;
	}

	public void setListen(ArrayList<Liste> listen) {
		this.listen = listen;
	}

	/*
	 * Konvertiert die Sammlung von Liste-Ojekten in XML, indem die toXML()-Methode
	 * für jedes Liste-Objekt in der Array-List aufgerufen, um es in entsprechende
	 * XML-Tags zu verpacken.
	 */
	public String toXML() {
		StringBuilder sb = new StringBuilder(100);
		sb.append("<listen>");
		if (listen != null)
			for (Liste l : listen)
				sb.append(l.toXML());
		sb.append("</listen>");
		return sb.toString();
	}

}
