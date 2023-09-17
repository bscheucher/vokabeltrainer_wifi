package client;

/*Diese Klasse ist für JavaFX-basierte Benutzeroberflächenkomponenten konzipiert
 * und umschließt eine Instanz der Vokabel-Klasse. 
 * Sie ist Teil einer grafischen Benutzeroberfläche-(GUI)-Implementierung 
 * zur Anzeige von bzw. Interaktion mit Vokabel-Objekten.*/


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import klassen.Vokabel;
import javafx.beans.property.SimpleObjectProperty;

public class VokabelFX {
	
	/*Es werden mehrere Instanzvariablen deklariert, darunter Simple Properties für 
	 * verschiedene Attribute eines Vokabel-Objekts: 
	 * modellVokabel: Eine Instanz der Vokabel-Klasse. 
	 * vokabelID, chinesischSimpl, chinesischTrad, pinyin, bedeutung, 
	 * vokabelBeschreibung und wortart: Dies sind Instanzen der 
	 * SimpleIntegerProperty- und SimpleStringProperty-Klassen von JavaFX, 
	 * mit denen observable Properties für Integer- und String-Werte erstellt 
	 * werden können.*/
	private Vokabel modellVokabel;
	private SimpleIntegerProperty vokabelID;
	private SimpleStringProperty chinesischSimpl;
	private SimpleStringProperty chinesischTrad;
	private SimpleStringProperty pinyin;
	private SimpleStringProperty bedeutung;
	private SimpleStringProperty vokabelBeschreibung;
	private SimpleStringProperty wortart;
	
	
	/* Der Konstruktor verwendet ein Vokabel-Objekt als Parameter und initialisiert ein 
	 * SimpleIntegerProperty und mehrere SimpleStringProperties mit den Werten, die 
	 * aus dem Parameter modellVokabel abgerufen werden.*/
	public VokabelFX(Vokabel modellVokabel) {
		super();
		this.modellVokabel = modellVokabel;
		vokabelID = new SimpleIntegerProperty(modellVokabel.getVokabelID());
		chinesischSimpl = new SimpleStringProperty(modellVokabel.getChinesischSimpl());
		chinesischTrad = new SimpleStringProperty(modellVokabel.getChinesischTrad());
		pinyin = new SimpleStringProperty(modellVokabel.getPinyin());
		bedeutung = new SimpleStringProperty(modellVokabel.getBedeutung());
		vokabelBeschreibung = new SimpleStringProperty(modellVokabel.getVokabelBeschreibung());
		wortart = new SimpleStringProperty(modellVokabel.getWortart());
	}
	
	/*
	 * Getter- und Setter-Methoden für jede der Instanzvariablen definiert. Diese
	 * Methoden bieten Zugriff auf die Eigenschaften des SemantischesFeld-Objekts
	 * und ermöglichen eine ordnungsgemäße Bindung mit JavaFX-UI-Komponenten.
	 * Außerdem gibt es Methoden, die SimpleIntegerProperty- oder
	 * SimpleStringProperty-Objekte für die jeweiligen Properties zurückgeben, um
	 * diese an die JavaFX-UI-Komponenten zu binden.
	 */

	public Vokabel getModellVokabel() {
		return modellVokabel;
	}

	public void setModellVokabel(Vokabel modellVokabel) {
		this.modellVokabel = modellVokabel;
	}

	

	public final SimpleIntegerProperty vokabelIDProperty() {
        return this.vokabelID;
    }    
 
    public final int getVokabelID() {
        return this.vokabelIDProperty().get();
    }    
 
    public final void setVokabelID(final int vokabelID) {
        this.vokabelIDProperty().set(vokabelID);
    }
    
    public final SimpleStringProperty chinesischSimplProperty() {
    	return this.chinesischSimpl;
    }
    
    public final String getChinesischSimpl() {
    	return this.chinesischSimplProperty().get();
    }
    
    public final void setChinesischSimpl(final String chinesischSimpl) {
    	this.chinesischSimplProperty().set(chinesischSimpl);
    }
	
    public final SimpleStringProperty chinesischTradProperty() {
    	return this.chinesischTrad;
    }
    
    public final String getChinesischTrad() {
    	return this.chinesischTradProperty().get();
    }
    
    public final void setChinesischTrad(final String chinesischTrad) {
    	this.chinesischTradProperty().set(chinesischTrad);
    }
	
    public final SimpleStringProperty pinyinProperty() {
    	return this.pinyin;
    }
    
    public final String getPinyin() {
    	return this.pinyinProperty().get();
    }
    
    public final void setPinyin(final String pinyin) {
    	this.pinyinProperty().set(pinyin);
    }
    
    public final SimpleStringProperty bedeutungProperty() {
    	return this.bedeutung;
    }
    
    public final String getBedeutung() {
    	return this.bedeutungProperty().get();
    }
    
    public final void setBedeutung(final String bedeutung) {
    	this.bedeutungProperty().set(bedeutung);
    }
    
    public final SimpleStringProperty vokabelBeschreibungProperty() {
    	return this.vokabelBeschreibung;
    }
    
    public final String getVokabelBeschreibung() {
    	return this.vokabelBeschreibungProperty().get();
    }
    
    public final void setVokabelBeschreibung(final String vokabelBeschreibung) {
    	this.vokabelBeschreibungProperty().set(vokabelBeschreibung);
    }
    
    public final SimpleStringProperty wortartProperty() {
		return this.wortart;
	}

	public final String getWortart() {
		return this.wortartProperty().get();
	}

	public final void setWortart(final String wortart) {
		this.wortartProperty().set(wortart);
	}

}
