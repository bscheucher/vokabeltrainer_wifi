package klassen;

/*Die Klasse XMLHandler wird für die XML-Analyse mithilfe des SAX-Parsers (Simple API for XML) 
 * verwendet, um ein XML-Dokument zu analysieren und Informationen zu extrahieren, um verschiedene 
 * Datenstrukturen der Vokabeltrainer-App zu füllen. Der Code erstellt und füllt Java-Objekte 
 * basierend auf der XML-Struktur und ermöglicht es der App, strukturiert mit den analysierten Daten 
 * zu arbeiten. Der Zweck dieser analysierten Objekte besteht darin, mit den Daten innerhalb der 
 * Java-Anwendung zu interagieren und diese zu bearbeiten.*/

import java.util.ArrayList;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler {

	// Instanzvariablen und Array-Lists für das Speichern und Handling von Objekten
	// werden deklariert
	private String text;
	private Liste liste;
	private SemantischesFeld semantischesFeld;
	private Vokabel vokabel;
	private Meldung meldung;
	private ArrayList<Vokabel> vokabeln;
	private ArrayList<Vokabel> vokabelnListe;
	private ArrayList<Vokabel> vokabelnSemFeld;
	private ArrayList<Liste> listen;
	private ArrayList<SemantischesFeld> semantischeFelder;
	private ArrayList<Liste> associatedListen;
	private ArrayList<SemantischesFeld> associatedSemFelder;

	/*
	 * Diese Methode wird aufgerufen, wenn der Parser auf den Anfang eines
	 * XML-Elements stößt. Abhängig vom Namen des Elements (gekennzeichnet durch
	 * qName) initialisiert die Methode entsprechend unterschiedliche Objekte oder
	 * Array-Lists.
	 */
	@SuppressWarnings("exports")
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		switch (qName.toUpperCase()) {
		case "VOKABEL":
			vokabel = new Vokabel();
			break;
		case "VOKABELN":
			vokabeln = new ArrayList<>();
			break;
		case "LISTE":
			liste = new Liste();
			break;
		case "LISTEN":
			listen = new ArrayList<>();
			break;
		case "SEMANTISCHESFELD":
			semantischesFeld = new SemantischesFeld();
			break;
		case "SEMANTISCHEFELDER":
			semantischeFelder = new ArrayList<>();
			break;
		case "VOKABELNLISTE":
			vokabelnListe = new ArrayList<>();
			break;
		case "VOKABELNSEMFELD":
			vokabelnSemFeld = new ArrayList<>();
			break;
		case "ASSOCIATEDLISTEN":
			associatedListen = new ArrayList<>();
			break;
		case "ASSOCIATEDSEMFELDER":
			associatedSemFelder = new ArrayList<>();
			break;
		case "MELDUNG":
			meldung = new Meldung();
			break;
		}
	}

	/*
	 * Diese Methode wird aufgerufen, wenn der Parser auf das Ende eines
	 * XML-Elements stößt. Abhängig vom Namen des Elements (qName) extrahiert die
	 * Methode Daten und legt die Attribute der zu erstellenden Objekte fest.
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName.toUpperCase()) {
		case "VOKABELID":
			vokabel.setVokabelID(Integer.parseInt(text));
			break;
		case "CHINESISCHSIMPL":
			vokabel.setChinesischSimpl(text);
			break;
		case "CHINESISCHTRAD":
			vokabel.setChinesischTrad(text);
			break;
		case "PINYIN":
			vokabel.setPinyin(text);
			break;
		case "BEDEUTUNG":
			vokabel.setBedeutung(text);
			break;
		case "VOKABELBESCHREIBUNG":
			vokabel.setVokabelBeschreibung(text);
			break;
		case "WORTART":
			vokabel.setWortart(text);
			break;
		case "VOKABEL":
			if (vokabeln != null)
				vokabeln.add(vokabel);
			if (vokabelnListe != null)
				vokabelnListe.add(vokabel);
			if (vokabelnSemFeld != null)
				vokabelnSemFeld.add(vokabel);
			break;
		case "LISTEID":
			liste.setListeID(Integer.parseInt(text));
			break;
		case "LISTEBEZEICHNUNG":
			liste.setListeBezeichnung(text);
			break;
		case "LISTEBESCHREIBUNG":
			liste.setListeBeschreibung(text);
			break;
		case "LISTE":
			if (listen != null)
				listen.add(liste);
			if (associatedListen != null)
				associatedListen.add(liste);
			break;
		case "SEMFELDID":
			semantischesFeld.setSemFeldID(Integer.parseInt(text));
			break;
		case "SEMFELDBEZEICHNUNG":
			semantischesFeld.setSemFeldBezeichnung(text);
			break;
		case "SEMFELDBESCHREIBUNG":
			semantischesFeld.setSemFeldBeschreibung(text);
			break;
		case "SEMANTISCHESFELD":
			if (semantischeFelder != null)
				semantischeFelder.add(semantischesFeld);
			if (associatedSemFelder != null)
				associatedSemFelder.add(semantischesFeld);
			break;

		}
	}

	/*
	 * Diese Methode wird aufgerufen, wenn der Parser auf Zeichendaten innerhalb
	 * eines Elements stößt. Es setzt die Variable text auf den Inhalt der
	 * gefundenen Zeichen.
	 */
	public void characters(char ch[], int start, int length) throws SAXException {
		text = new String(ch, start, length);
	}

	// Getter werden implementiert
	public Vokabel getVokabel() {
		return vokabel;
	}

	public ArrayList<Vokabel> getVokabeln() {
		return vokabeln;
	}

	public ArrayList<Vokabel> getVokabelnListe() {
		return vokabelnListe;
	}

	public ArrayList<Vokabel> getVokabelnSemFeld() {
		return vokabelnSemFeld;
	}

	public Liste getListe() {
		return liste;
	}

	public ArrayList<Liste> getListen() {
		return listen;
	}

	public SemantischesFeld getSemFeld() {
		return semantischesFeld;
	}

	public ArrayList<SemantischesFeld> getSemantischeFelder() {
		return semantischeFelder;
	}

	public ArrayList<Liste> getAssociatedListen() {
		return associatedListen;
	}

	public ArrayList<SemantischesFeld> getAssociatedSemFelder() {
		return associatedSemFelder;
	}

	public Meldung getMeldung() {
		return meldung;
	}

	// Definiert Konstanten für XML-Prolog-Deklarationen mit unterschiedlichen
	// Kodierungen.
	public static final String XML_PROLOG_ISO8859 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\"?>";
	public static final String XML_PROLOG_UTF8 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";

}
