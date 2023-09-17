package server;

/* Diese Klasse implementiert Datenbankoperationen mit Java und JDBC für die Vokabeltrainer-App
 * und verarbeitet Datenbankinteraktionen im Zusammenhang mit mehreren Tabellen. 
 * Der Code umfasst Methoden zum Erstellen von Tabellen, zum Einfügen, Lesen, Aktualisieren und Löschen 
 * von Vokabeln, Vokabellisten und semantischen Feldern sowie zum Abrufen semantischer Felder und Listen 
 * für einen bestimmten Vokabeleintrag.*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import klassen.Liste;
import klassen.SemantischesFeld;
import klassen.Vokabel;

public class Datenbank {

	// Datenbank.URL und Datenabankverbindung werden etabliert
	private static final String DB_LOCATION = "C:\\Java\\DB_Vokabeltrainer";
	private static final String CONNECTION = "jdbc:derby:" + DB_LOCATION + ";create=true";

	// Für die Felder der Tabelle "Vokabel" werden String-Variablen initialisiert
	private static final String VOKABEL_TABLE = "Vokabel";
	private static final String VOKABEL_ID = "VokabelID";
	private static final String VOKABEL_CHINESISCH_SIMPL = "ChinesischSimpl";
	private static final String VOKABEL_CHINESISCH_TRAD = "ChinesischTrad";
	private static final String VOKABEL_PINYIN = "Pinyin";
	private static final String VOKABEL_BEDEUTUNG = "Bedeutung";
	private static final String VOKABEL_BESCHREIBUNG = "VokabelBeschreibung";
	private static final String VOKABEL_WORTART = "Wortart";

	// Für die Felder der Tabelle "SemantischesFeld" werden String-Variablen
	// initialisiert
	private static final String SEMFELD_TABLE = "SemantischesFeld";
	private static final String SEMFELD_ID = "SemFeldID";
	private static final String SEMFELD_BEZEICHNUNG = "SemFeldBezeichnung";
	private static final String SEMFELD_BESCHREIBUNG = "SemFeldBeschreibung";

	//// Für die Felder der Tabelle "Liste" werden String-Variablen initialisiert
	private static final String LISTE_TABLE = "Liste";
	private static final String LISTE_ID = "ListeID";
	private static final String LISTE_BEZEICHNUNG = "ListeBezeichnung";
	private static final String LISTE_BESCHREIBUNG = "ListeBeschreibung";

	// Für die Felder des Junction-Table "VokabelSemFeld" werden String-Variablen
	// initialisiert
	private static final String VOKABEL_SEMFELD_TABLE = "VokabelSemFeld";
	private static final String VOKABEL_SEMFELD_ID = "VokabelSemfeldID";
	private static final String VOKABEL_SEMFELD_VOKABEL_ID = "VokabelID";
	private static final String VOKABEL_SEMFELD_SEMFELD_ID = "SemFeldID";

	// Für die Felder des Junction-Table "VokabelListe" werden String-Variablen
	// initialisiert
	private static final String VOKABEL_LISTE_TABLE = "VokabelListe";
	private static final String VOKABEL_LISTE_ID = "VokabelListeID";
	private static final String VOKABEL_LISTE_VOKABEL_ID = "VokabelID";
	private static final String VOKABEL_LISTE_LISTE_ID = "ListeID";

	/*
	 * Die Methode createTableVokabel() erstellt eine Tabelle mit dem Namen Vokabel,
	 * sofern diese noch nicht vorhanden ist. Diese Tabelle enthält Vokabeleinträge
	 * mit verschiedenen Attributen.
	 */
	public static void createTableVokabel() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.createStatement();
			rs = conn.getMetaData().getTables(null, null, VOKABEL_TABLE.toUpperCase(), new String[] { "TABLE" });
			if (rs.next()) {
				return;
			}

			String ct = "CREATE TABLE " + VOKABEL_TABLE + " (" + VOKABEL_ID
					+ " INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
					+ VOKABEL_CHINESISCH_SIMPL + " VARCHAR(200), " + VOKABEL_CHINESISCH_TRAD + " VARCHAR(200), "
					+ VOKABEL_PINYIN + " VARCHAR(200), " + VOKABEL_BEDEUTUNG + " VARCHAR(200), " + VOKABEL_BESCHREIBUNG
					+ " VARCHAR(200), " + VOKABEL_WORTART + " VARCHAR(20), " + "PRIMARY KEY(" + VOKABEL_ID + "))";

			stmt.executeUpdate(ct);
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	/*
	 * Die Methode readVokabeln() ruft alle Vokabeleinträge aus der Vokabel-Tabelle
	 * ab und erstellt Vokabel-Objekte aus den abgerufenen Daten.
	 */
	public static ArrayList<Vokabel> readVokabeln() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<Vokabel> vokabeln = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + VOKABEL_TABLE);

			while (rs.next()) {
				int vokabelID = rs.getInt("VokabelID");
				String chinesischSimpl = rs.getString("ChinesischSimpl");
				String chinesischTrad = rs.getString("ChinesischTrad");
				String pinyin = rs.getString("Pinyin");
				String bedeutung = rs.getString("Bedeutung");
				String vokabelBeschreibung = rs.getString("VokabelBeschreibung");
				String vokabelWortart = rs.getString("Wortart");

				vokabeln.add(new Vokabel(vokabelID, chinesischSimpl, chinesischTrad, pinyin, bedeutung,
						vokabelBeschreibung, vokabelWortart));
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
		return vokabeln;
	}

	/*
	 * Die Methode insertVokabel(Vokabel vokabel) fügt mithilfe des bereitgestellten
	 * Vokabel-Objekts einen neuen Vokabulareintrag in die Vokabel-Tabelle ein.
	 */
	public static void insertVokabel(Vokabel vokabel) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DriverManager.getConnection(CONNECTION);
			String insert = "INSERT INTO " + VOKABEL_TABLE + " (" + VOKABEL_CHINESISCH_SIMPL + ", "
					+ VOKABEL_CHINESISCH_TRAD + ", " + VOKABEL_PINYIN + ", " + VOKABEL_BEDEUTUNG + ", "
					+ VOKABEL_BESCHREIBUNG + ", " + VOKABEL_WORTART + ") VALUES (?, ?, ?, ?, ?, ?)";
			stmt = conn.prepareStatement(insert);
			stmt.setString(1, vokabel.getChinesischSimpl());
			stmt.setString(2, vokabel.getChinesischTrad());
			stmt.setString(3, vokabel.getPinyin());
			stmt.setString(4, vokabel.getBedeutung());
			stmt.setString(5, vokabel.getVokabelBeschreibung());
			stmt.setString(6, vokabel.getWortart());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	/*
	 * Die Methode updateVokabel(Vokabel vokabel) aktualisiert einen vorhandenen
	 * Vokabel-Eintrag in der Vokabel-Tabelle mithilfe des bereitgestellten
	 * Vokabel-Objekts.
	 */
	public static void updateVokabel(Vokabel vokabel) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		String update = "UPDATE " + VOKABEL_TABLE + " SET " + VOKABEL_CHINESISCH_SIMPL + " = ?, "
				+ VOKABEL_CHINESISCH_TRAD + " = ?, " + VOKABEL_PINYIN + " = ?, " + VOKABEL_BEDEUTUNG + " = ?, "
				+ VOKABEL_BESCHREIBUNG + " = ?, " + VOKABEL_WORTART + " = ? WHERE " + VOKABEL_ID + " = ?";
		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.prepareStatement(update);
			stmt.setInt(7, vokabel.getVokabelID());
			stmt.setString(1, vokabel.getChinesischSimpl());
			stmt.setString(2, vokabel.getChinesischTrad());
			stmt.setString(3, vokabel.getPinyin());
			stmt.setString(4, vokabel.getBedeutung());
			stmt.setString(5, vokabel.getVokabelBeschreibung());
			stmt.setString(6, vokabel.getWortart());

			stmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	/*
	 * Die Methode getAssociatedListen(int vokabelID) ruft zugehörige Listen für
	 * einen bestimmten Vokabulareintrag mithilfe von JOIN-Operationen mit den
	 * jeweiligen Verknüpfungstabellen ab.
	 */
	public static ArrayList<Liste> getAssociatedListen(int vokabelID) throws SQLException {
		ArrayList<Liste> associatedListen = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(CONNECTION);
			String query = "SELECT * FROM " + LISTE_TABLE + " INNER JOIN " + VOKABEL_LISTE_TABLE + " ON " + LISTE_TABLE
					+ "." + LISTE_ID + " = " + VOKABEL_LISTE_TABLE + "." + VOKABEL_LISTE_LISTE_ID + " WHERE "
					+ VOKABEL_LISTE_TABLE + "." + VOKABEL_LISTE_VOKABEL_ID + " = ?";

			stmt = conn.prepareStatement(query);
			stmt.setInt(1, vokabelID);
			rs = stmt.executeQuery();

			while (rs.next()) {
				int listeID = rs.getInt(LISTE_ID);
				String bezeichnung = rs.getString(LISTE_BEZEICHNUNG);
				String beschreibung = rs.getString(LISTE_BESCHREIBUNG);

				Liste liste = new Liste(listeID, bezeichnung, beschreibung);
				associatedListen.add(liste);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				throw e;
			}
		}
		return associatedListen;
	}

	/*
	 * Die Methode getAssociatedSemFelder(int vokabelID) ruft zugehörige semantische
	 * Felder für einen bestimmten Vokabulareintrag mithilfe von JOIN-Operationen
	 * mit den jeweiligen Verknüpfungstabellen ab.
	 */
	public static ArrayList<SemantischesFeld> getAssociatedSemFelder(int vokabelID) throws SQLException {
		ArrayList<SemantischesFeld> associatedSemFelder = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(CONNECTION);
			String query = "SELECT * FROM " + SEMFELD_TABLE + " INNER JOIN " + VOKABEL_SEMFELD_TABLE + " ON "
					+ SEMFELD_TABLE + "." + SEMFELD_ID + " = " + VOKABEL_SEMFELD_TABLE + "."
					+ VOKABEL_SEMFELD_SEMFELD_ID + " WHERE " + VOKABEL_SEMFELD_TABLE + "." + VOKABEL_SEMFELD_VOKABEL_ID
					+ " = ?";
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, vokabelID);
			rs = stmt.executeQuery();

			while (rs.next()) {
				int semFeldID = rs.getInt(SEMFELD_ID);
				String bezeichnung = rs.getString(SEMFELD_BEZEICHNUNG);
				String beschreibung = rs.getString(SEMFELD_BESCHREIBUNG);

				SemantischesFeld semFeld = new SemantischesFeld(semFeldID, bezeichnung, beschreibung);
				associatedSemFelder.add(semFeld);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				throw e;
			}
		}
		return associatedSemFelder;
	}

	/*
	 * Die Methode deleteVokabel(int vokabelId) löscht einen Vokabeleintrag aus der
	 * Vokabel-Tabelle basierend auf der bereitgestellten Vokabel-ID.
	 */
	public static void deleteVokabel(int vokabelId) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DriverManager.getConnection(CONNECTION);
			String delete = "DELETE FROM " + VOKABEL_TABLE + " WHERE " + VOKABEL_ID + " = ?";
			stmt = conn.prepareStatement(delete);
			stmt.setInt(1, vokabelId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public static void createTableSemFeld() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.createStatement();
			rs = conn.getMetaData().getTables(null, null, SEMFELD_TABLE.toUpperCase(), new String[] { "TABLE" });
			if (rs.next()) {
				return;
			}

			String ct = "CREATE TABLE " + SEMFELD_TABLE + " (" + SEMFELD_ID
					+ " INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " + SEMFELD_BEZEICHNUNG
					+ " VARCHAR(200), " + SEMFELD_BESCHREIBUNG + " VARCHAR(200), " + "PRIMARY KEY(" + SEMFELD_ID + "))";

			stmt.executeUpdate(ct);
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}

	}

	public static ArrayList<SemantischesFeld> readSemFelder() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<SemantischesFeld> semFelder = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + SEMFELD_TABLE);

			while (rs.next()) {
				int semFeldID = rs.getInt("SemFeldID");
				String semFeldBezeichnung = rs.getString("SemFeldBezeichnung");
				String semFeldBeschreibung = rs.getString("SemFeldBeschreibung");

				semFelder.add(new SemantischesFeld(semFeldID, semFeldBezeichnung, semFeldBeschreibung));
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
		return semFelder;
	}

	public static void insertSemFeld(SemantischesFeld semFeld) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DriverManager.getConnection(CONNECTION);
			String insert = "INSERT INTO " + SEMFELD_TABLE + " (" + SEMFELD_BEZEICHNUNG + ", " + SEMFELD_BESCHREIBUNG
					+ ") VALUES (?, ?)";
			pstmt = conn.prepareStatement(insert);
			pstmt.setString(1, semFeld.getSemFeldBezeichnung());
			pstmt.setString(2, semFeld.getSemFeldBeschreibung());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public static void updateSemFeld(SemantischesFeld semFeld) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DriverManager.getConnection(CONNECTION);
			String update = "UPDATE " + SEMFELD_TABLE + " SET " + SEMFELD_BEZEICHNUNG + " = ?, " + SEMFELD_BESCHREIBUNG
					+ " = ? WHERE " + SEMFELD_ID + " = ?";
			pstmt = conn.prepareStatement(update);
			pstmt.setString(1, semFeld.getSemFeldBezeichnung());
			pstmt.setString(2, semFeld.getSemFeldBeschreibung());
			pstmt.setInt(3, semFeld.getSemFeldID());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public static void deleteSemFeld(int semFeldId) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DriverManager.getConnection(CONNECTION);
			String delete = "DELETE FROM " + SEMFELD_TABLE + " WHERE " + SEMFELD_ID + " = ?";
			pstmt = conn.prepareStatement(delete);
			pstmt.setInt(1, semFeldId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	/*
	 * Die Methode createTableSemFeld() erstellt eine SemantischeFeld-Tabelle, falls
	 * diese noch nicht vorhanden ist. Die Tabelle enthält Attribute für ein
	 * semantisches Feld, einschließlich SemFeldID, SemFeldBezeichnung und
	 * SemFeldBeschreibung.
	 */
	public static void createTableListe() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.createStatement();
			rs = conn.getMetaData().getTables(null, null, LISTE_TABLE.toUpperCase(), new String[] { "TABLE" });
			if (rs.next()) {
				return;
			}

			String ct = "CREATE TABLE " + LISTE_TABLE + " (" + LISTE_ID
					+ " INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " + LISTE_BEZEICHNUNG
					+ " VARCHAR(200), " + LISTE_BESCHREIBUNG + " VARCHAR(200), " + "PRIMARY KEY(" + LISTE_ID + "))";
			// createTableVokabelListe();
			stmt.executeUpdate(ct);
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}

	}

	/*
	 * Die Methode readSemFelder() ruft alle semantischen Felder aus der Tabelle
	 * „SemantischesFeld“ ab und erstellt aus den abgerufenen Daten
	 * SemantischesFeld-Objekte.
	 */
	public static ArrayList<Liste> readListen() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<Liste> listen = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + LISTE_TABLE);

			while (rs.next()) {
				int listeID = rs.getInt("ListeID");
				String listeBezeichnung = rs.getString("ListeBezeichnung");
				String listeBeschreibung = rs.getString("ListeBeschreibung");

				listen.add(new Liste(listeID, listeBezeichnung, listeBeschreibung));
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
		return listen;
	}

	/*
	 * Die Methode insertSemFeld() fügt mithilfe des bereitgestellten
	 * SemantischesFeld-Objekts einen neuen semantischen Feldeintrag in die Tabelle
	 * SemantischesFeld ein.
	 */
	public static void insertListe(Liste liste) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DriverManager.getConnection(CONNECTION);
			String insert = "INSERT INTO " + LISTE_TABLE + " (" + LISTE_BEZEICHNUNG + ", " + LISTE_BESCHREIBUNG
					+ ") VALUES (?, ?)";
			pstmt = conn.prepareStatement(insert);
			pstmt.setString(1, liste.getListeBezeichnung());
			pstmt.setString(2, liste.getListeBeschreibung());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	/*
	 * Die Methode updateSemFeld(SemantischesFeld semFeld) aktualisiert einen
	 * vorhandenen Eintrag in der SemantischesFeld-Tabelle mithilfe des
	 * bereitgestellten SemantischesFeld-Objekts.
	 */
	public static void updateListe(Liste liste) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DriverManager.getConnection(CONNECTION);
			String update = "UPDATE " + LISTE_TABLE + " SET " + LISTE_BEZEICHNUNG + " = ?, " + LISTE_BESCHREIBUNG
					+ " = ? WHERE " + LISTE_ID + " = ?";
			pstmt = conn.prepareStatement(update);
			pstmt.setString(1, liste.getListeBezeichnung());
			pstmt.setString(2, liste.getListeBeschreibung());
			pstmt.setInt(3, liste.getListeID());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	/*
	 * Die Methode deleteSemFeld(int semFeldId) löscht einen Eintrag aus der Tabelle
	 * SemantischesFeld basierend auf der bereitgestellten semantischen Feld-ID.
	 */
	public static void deleteListe(int listeId) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DriverManager.getConnection(CONNECTION);
			String delete = "DELETE FROM " + LISTE_TABLE + " WHERE " + LISTE_ID + " = ?";
			pstmt = conn.prepareStatement(delete);
			pstmt.setInt(1, listeId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	/*
	 * Erstellt die VokabelSemFeld-Verbindungstabelle, falls sie noch nicht
	 * vorhanden ist. Die Tabelle enthält die Spalten VokabelSemFeldID, VokabelID
	 * und SemFeldID, die auf die jeweiligen IDs in den Tabellen Vokabel und
	 * SemantischesFeld verweisen. Fremdschlüssel werden eingerichtet, um die
	 * referenzielle Integrität zu gewährleisten.
	 */
	public static void createTableVokabelSemFeld() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.createStatement();
			rs = conn.getMetaData().getTables(null, null, VOKABEL_SEMFELD_TABLE.toUpperCase(),
					new String[] { "TABLE" });
			if (rs.next()) {
				return;
			}

			String ct = "CREATE TABLE " + VOKABEL_SEMFELD_TABLE + " (" + VOKABEL_SEMFELD_ID
					+ " INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
					+ VOKABEL_SEMFELD_VOKABEL_ID + " INTEGER, " + VOKABEL_SEMFELD_SEMFELD_ID + " INTEGER, "
					+ "FOREIGN KEY (" + VOKABEL_SEMFELD_VOKABEL_ID + ") REFERENCES " + VOKABEL_TABLE + "(" + VOKABEL_ID
					+ "), " + "FOREIGN KEY (" + VOKABEL_SEMFELD_SEMFELD_ID + ") REFERENCES " + SEMFELD_TABLE + "("
					+ SEMFELD_ID + "), " + "PRIMARY KEY(" + VOKABEL_SEMFELD_ID + "))";

			stmt.executeUpdate(ct);
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	/*
	 * Ruft Vokabeleinträge ab, die einem bestimmten semantischen Feld zugeordnet
	 * sind. Verwendet eine SQL-Abfrage mit JOINs zwischen VokabelSemFeld- und
	 * Vokabel-Tabellen, um relevante Einträge aus der Tabelle Vokabel abzurufen.
	 */
	public static ArrayList<Vokabel> readVokabelnSemFeld(int semFeldID) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Vokabel> vokabelSemFeld = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(CONNECTION);
			String query = "SELECT * FROM " + VOKABEL_SEMFELD_TABLE + " INNER JOIN " + VOKABEL_TABLE + " ON "
					+ VOKABEL_TABLE + "." + VOKABEL_ID + " = " + VOKABEL_SEMFELD_TABLE + "."
					+ VOKABEL_SEMFELD_VOKABEL_ID + " WHERE " + VOKABEL_SEMFELD_TABLE + "." + VOKABEL_SEMFELD_SEMFELD_ID
					+ " = ?";
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, semFeldID);
			rs = stmt.executeQuery();

			// Die erhaltenen Daten werden verarbeitet und die ArrayList wird gefüllt.
			while (rs.next()) {
				int vokabelId = rs.getInt(VOKABEL_ID);
				String vokabelChinesischSimpl = rs.getString(VOKABEL_CHINESISCH_SIMPL);
				String vokabelChinesischTrad = rs.getString(VOKABEL_CHINESISCH_TRAD);
				String vokabelPinyin = rs.getString(VOKABEL_PINYIN);
				String vokabelBedeutung = rs.getString(VOKABEL_BEDEUTUNG);
				String vokabelBeschreibung = rs.getString(VOKABEL_BESCHREIBUNG);
				String vokabelWortart = rs.getString(VOKABEL_WORTART);

				// Erzeugt ein Vokabel-Objekt unf fügt es zur ArrayList
				vokabelSemFeld.add(new Vokabel(vokabelId, vokabelChinesischSimpl, vokabelChinesischTrad, vokabelPinyin,
						vokabelBedeutung, vokabelBeschreibung, vokabelWortart));
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}

		// Die ArrayList mit Vokabel-Objekten wird zurückgegeben
		return vokabelSemFeld;
	}

	/*
	 * Fügt einem semantischen Feld einen Vokabel-Eintrag hinzu, indem ein
	 * entsprechender Datensatz in die VokabelSemFeld-Tabelle eingefügt wird.
	 * Beinhaltet einen Check, der doppelte Einträge verhindert.
	 */
	public static void addVokabelToSemFeld(int vokabelId, int semFeldId) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			conn = DriverManager.getConnection(CONNECTION);

			// Überprüfen, ob vokabelId and listeId als Kombination bereits in der Tabelle
			// existieren
			String checkQuery = "SELECT COUNT(*) FROM " + VOKABEL_SEMFELD_TABLE + " WHERE " + VOKABEL_SEMFELD_VOKABEL_ID
					+ " = ? AND " + VOKABEL_SEMFELD_SEMFELD_ID + " = ?";

			stmt = conn.prepareStatement(checkQuery);
			stmt.setInt(1, vokabelId);
			stmt.setInt(2, semFeldId);
			resultSet = stmt.executeQuery();

			if (resultSet.next()) {
				int count = resultSet.getInt(1);
				if (count > 0) {
					// Kombination existiert bereits, wird nicht erneut eingefügt
					return;
				}
			}

			// Einfügen der neuen noch nicht vorhandenen Kombination aus vokabelId und
			// semFeldId
			String insertQuery = "INSERT INTO " + VOKABEL_SEMFELD_TABLE + " (" + VOKABEL_SEMFELD_VOKABEL_ID + ", "
					+ VOKABEL_SEMFELD_SEMFELD_ID + ") VALUES (?, ?)";

			stmt = conn.prepareStatement(insertQuery);
			stmt.setInt(1, vokabelId);
			stmt.setInt(2, semFeldId);
			stmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	/*
	 * Löscht einen bestimmten Vokabulareintrag aus einem semantischen Feld, indem
	 * der entsprechende Datensatz aus der VokabelSemFeld-Tabelle entfernt wird.
	 */
	public static void deleteVokabelFromSemFeld(int vokabelId, int semFeldId) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DriverManager.getConnection(CONNECTION);

			String deleteQuery = "DELETE FROM " + VOKABEL_SEMFELD_TABLE + " WHERE " + VOKABEL_SEMFELD_VOKABEL_ID
					+ " = ? AND " + VOKABEL_SEMFELD_SEMFELD_ID + " = ?";

			pstmt = conn.prepareStatement(deleteQuery);
			pstmt.setInt(1, vokabelId);
			pstmt.setInt(2, semFeldId);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	/*
	 * Erstellt die VokabelListe-Verbindungstabelle, falls sie noch nicht vorhanden
	 * ist. Die Tabelle enthält die Spalten VokabelListeID, VokabelID und ListeID,
	 * die auf die jeweiligen IDs in den Tabellen Vokabel und Liste verweisen.
	 * Fremdschlüssel werden eingerichtet, um die referenzielle Integrität zu
	 * gewährleisten.
	 */
	public static void createTableVokabelListe() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(CONNECTION);
			stmt = conn.createStatement();
			rs = conn.getMetaData().getTables(null, null, VOKABEL_LISTE_TABLE.toUpperCase(), new String[] { "TABLE" });
			if (rs.next()) {
				return;
			}

			String ct = "CREATE TABLE " + VOKABEL_LISTE_TABLE + " (" + VOKABEL_LISTE_ID
					+ " INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
					+ VOKABEL_LISTE_VOKABEL_ID + " INTEGER, " + VOKABEL_LISTE_LISTE_ID + " INTEGER, " + "FOREIGN KEY ("
					+ VOKABEL_LISTE_VOKABEL_ID + ") REFERENCES " + VOKABEL_TABLE + "(" + VOKABEL_ID + "), "
					+ "FOREIGN KEY (" + VOKABEL_LISTE_LISTE_ID + ") REFERENCES " + LISTE_TABLE + "(" + LISTE_ID + "), "
					+ "PRIMARY KEY(" + VOKABEL_LISTE_ID + "))";

			stmt.executeUpdate(ct);
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	/*
	 * Ruft Vokabeleinträge ab, die einer bestimmten Liste zugeordnet sind.
	 * Verwendet eine SQL-Abfrage mit JOINs zwischen VokablListe- und
	 * Vokabel-Tabellen, um relevante Einträge aus der Tabelle Vokabel abzurufen.
	 */
	public static ArrayList<Vokabel> readVokabelnListe(int listeID) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Vokabel> vokabelList = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(CONNECTION);
			String query = "SELECT * FROM " + VOKABEL_LISTE_TABLE + " INNER JOIN " + VOKABEL_TABLE + " ON "
					+ VOKABEL_TABLE + "." + VOKABEL_ID + " = " + VOKABEL_LISTE_TABLE + "." + VOKABEL_LISTE_VOKABEL_ID
					+ " WHERE " + VOKABEL_LISTE_TABLE + "." + VOKABEL_LISTE_LISTE_ID + " = ?";
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, listeID);
			rs = stmt.executeQuery();

			// Die erhaltenen Daten werden verarbeitet und die ArrayList wird gefüllt.
			while (rs.next()) {
				int vokabelId = rs.getInt(VOKABEL_ID);
				String vokabelChinesischSimpl = rs.getString(VOKABEL_CHINESISCH_SIMPL);
				String vokabelChinesischTrad = rs.getString(VOKABEL_CHINESISCH_TRAD);
				String vokabelPinyin = rs.getString(VOKABEL_PINYIN);
				String vokabelBedeutung = rs.getString(VOKABEL_BEDEUTUNG);
				String vokabelBeschreibung = rs.getString(VOKABEL_BESCHREIBUNG);
				String vokabelWortart = rs.getString(VOKABEL_WORTART);

				// Erzeugt ein Vokabel-Objekt unf fügt es zur ArrayList
				vokabelList.add(new Vokabel(vokabelId, vokabelChinesischSimpl, vokabelChinesischTrad, vokabelPinyin,
						vokabelBedeutung, vokabelBeschreibung, vokabelWortart));
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}

		// Die ArrayList mit Vokabel-Objekten wird zurückgegeben
		return vokabelList;
	}

	/*
	 * Fügt einer Liste einen Vokabel-Eintrag hinzu, indem ein entsprechender
	 * Datensatz in die Liste-Tabelle eingefügt wird. Beinhaltet einen Check, der
	 * doppelte Einträge verhindert.
	 */
	public static void addVokabelToListe(int vokabelId, int listeId) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			conn = DriverManager.getConnection(CONNECTION);

			// Überprüfen, ob vokabelId and listeId als Kombination bereits in der Tabelle
			// existieren
			String checkQuery = "SELECT COUNT(*) FROM " + VOKABEL_LISTE_TABLE + " WHERE " + VOKABEL_LISTE_VOKABEL_ID
					+ " = ? AND " + VOKABEL_LISTE_LISTE_ID + " = ?";

			stmt = conn.prepareStatement(checkQuery);
			stmt.setInt(1, vokabelId);
			stmt.setInt(2, listeId);
			resultSet = stmt.executeQuery();

			if (resultSet.next()) {
				int count = resultSet.getInt(1);
				if (count > 0) {
					// Kombination existiert bereits, wird nicht erneut eingefügt
					return;
				}
			}

			// Einfügen der neuen noch nicht vorhandenen Kombination aus vokabelId und
			// listeId
			String insertQuery = "INSERT INTO " + VOKABEL_LISTE_TABLE + " (" + VOKABEL_LISTE_VOKABEL_ID + ", "
					+ VOKABEL_LISTE_LISTE_ID + ") VALUES (?, ?)";

			stmt = conn.prepareStatement(insertQuery);
			stmt.setInt(1, vokabelId);
			stmt.setInt(2, listeId);
			stmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	/*
	 * Löscht einen bestimmten Vokabulareintrag aus einer Liste, indem der
	 * entsprechende Datensatz aus der Liste-Tabelle entfernt wird.
	 */
	public static void deleteVokabelFromList(int vokabelId, int listeId) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DriverManager.getConnection(CONNECTION);

			String deleteQuery = "DELETE FROM " + VOKABEL_LISTE_TABLE + " WHERE " + VOKABEL_LISTE_VOKABEL_ID
					+ " = ? AND " + VOKABEL_LISTE_LISTE_ID + " = ?";

			pstmt = conn.prepareStatement(deleteQuery);
			pstmt.setInt(1, vokabelId);
			pstmt.setInt(2, listeId);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
}
