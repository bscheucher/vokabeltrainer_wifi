package klassen;

/*Domain-Klasse, bildet den Bauplan für ein Vokabel-Objekt, enthält verschiedene Attribute, 
 * z.B. die Vokabel-ID, chinesische Schriftzeichen in vereinfachter und traditioneller Form, 
 * Pinyin-Aussprache, Bedeutung, Beschreibung und Worttyp. Die Klasse umfasst außerdem Konstruktoren, 
 * Getter und Setter für ihre Attribute sowie eine Methode zum Konvertieren des Vokabel-Objekts in 
 * einen XML-String und einen Komparator zum Vergleichen von Instanzen der Klasse.*/

import java.io.IOException;
import java.io.StringReader;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Vokabel implements Comparable<Vokabel> {

	// Instanzvariablen werden deklariert
	private int vokabelID;
	private String chinesischSimpl;
	private String chinesischTrad;
	private String pinyin;
	private String bedeutung;
	private String vokabelBeschreibung;
	private String wortart;

	// Konstruktor nimmt alle Instanzvariablen als Argumente und initialisiert sie
	public Vokabel(int vokabelID, String chinesischSimpl, String chinesischTrad, String pinyin, String bedeutung,
			String vokabelBeschreibung, String wortart) {
		super();
		this.vokabelID = vokabelID;
		this.chinesischSimpl = chinesischSimpl;
		this.chinesischTrad = chinesischTrad;
		this.pinyin = pinyin;
		this.bedeutung = bedeutung;
		this.vokabelBeschreibung = vokabelBeschreibung;
		this.wortart = wortart;
	}

	// argumentloser Konstruktor
	public Vokabel() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * Konstruktor verwendet eine XML-Zeichenfolge, um die Attribute aus XML-Daten,
	 * die von der XMLHandler-Klasse bereitgestellt werden, zu initialisieren.
	 */
	public Vokabel(String xmlString) {
		if (xmlString == null || xmlString.length() == 0)
			return;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			StringReader str = new StringReader(xmlString);
			XMLHandler xh = new XMLHandler();
			sp.parse(new InputSource(str), xh);
			vokabelID = xh.getVokabel().getVokabelID();
			chinesischSimpl = xh.getVokabel().getChinesischSimpl();
			chinesischTrad = xh.getVokabel().getChinesischTrad();
			pinyin = xh.getVokabel().getPinyin();
			bedeutung = xh.getVokabel().getBedeutung();
			vokabelBeschreibung = xh.getVokabel().getVokabelBeschreibung();
			wortart = xh.getVokabel().getWortart();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}

	}

	/*
	 * Für jedes Attribut gibt es Getter- und Setter-Methoden, mit denen Sie die
	 * Attributwerte abrufen und aktualisieren können.
	 */
	public int getVokabelID() {
		return vokabelID;
	}

	public void setVokabelID(int vokabelID) {
		this.vokabelID = vokabelID;
	}

	public String getChinesischSimpl() {
		return chinesischSimpl;
	}

	public void setChinesischSimpl(String chinesischSimpl) {
		this.chinesischSimpl = chinesischSimpl;
	}

	public String getChinesischTrad() {
		return chinesischTrad;
	}

	public void setChinesischTrad(String chinesischTrad) {
		this.chinesischTrad = chinesischTrad;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getBedeutung() {
		return bedeutung;
	}

	public void setBedeutung(String bedeutung) {
		this.bedeutung = bedeutung;
	}

	public String getVokabelBeschreibung() {
		return vokabelBeschreibung;
	}

	public void setVokabelBeschreibung(String vokabelBeschreibung) {
		this.vokabelBeschreibung = vokabelBeschreibung;
	}

	public String getWortart() {
		return wortart;
	}

	public void setWortart(String wortart) {
		this.wortart = wortart;
	}

	/*
	 * Diese Methode gibt einen String mit vereinfachten chinesischen Zeichen,
	 * Pinyin und Bedeutung des Vokabel-Objekts zurück.
	 */
	@Override
	public String toString() {
		return chinesischSimpl + " " + pinyin + " " + bedeutung;
	}

	/*
	 * Diese Methode implementiert das Comparable-Interface und vergleicht Vokabeln
	 * anhand ihrer Pinyin-Transkription mithilfe eines Komparators.
	 */
	@Override
	public int compareTo(Vokabel o) {
		Comparator<String> pinyinComparator = Comparator.nullsLast(Comparator.naturalOrder());
		return pinyinComparator.compare(this.pinyin, o.getPinyin());
	}

	// Diese Methode konvertiert das Vokabel-Objekt in eine XML-Zeichenfolge.
	public String toXML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<vokabel>");
		sb.append("<vokabelid>" + vokabelID + "</vokabelid>");
		sb.append("<chinesischsimpl>" + chinesischSimpl + "</chinesischsimpl>");
		sb.append("<chinesischtrad>" + chinesischTrad + "</chinesischtrad>");
		sb.append("<pinyin>" + pinyin + "</pinyin>");
		sb.append("<bedeutung>" + bedeutung + "</bedeutung>");
		sb.append("<vokabelbeschreibung>" + vokabelBeschreibung + "</vokabelbeschreibung>");
		sb.append("<wortart>" + wortart + "</wortart>");
		sb.append("</vokabel>");
		return sb.toString();
	}

}
