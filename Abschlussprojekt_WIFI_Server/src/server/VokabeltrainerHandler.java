package server;

/*Die Klasse implementiert die HttpHandler-Schnittstelle und verarbeitet HTTP-Requests 
 * und -Responses für die mit der Vokabeltrainer-App verbundenen Serveranwendung.
 * Der Code leitet eingehende Requests basierend auf den in der Request-URI angegebenen Pfaden 
 * weiter. Unterschiedliche Pfade entsprechen unterschiedlichen Operationen, im wesenlichen sind 
 * das die HTTP-Methoden GET, POST, PUT und DELETE. Die Pfade werden aufgesplttet und analysiert, 
 * um die auszuführenden Operationen zu ermitteln.*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import klassen.AssociatedListen;
import klassen.AssociatedSemFelder;
import klassen.Liste;
import klassen.Listen;
import klassen.Meldung;
import klassen.SemantischeFelder;
import klassen.SemantischesFeld;
import klassen.Vokabel;
import klassen.Vokabeln;
import klassen.VokabelnListe;
import klassen.VokabelnSemFeld;
import klassen.XMLHandler;

public class VokabeltrainerHandler implements HttpHandler {

	//Die Datenbanktabellen werden beim Laden der Klasse statisch initialisiert.
	static {
		try {

			Datenbank.createTableVokabel();
			Datenbank.createTableListe();
			Datenbank.createTableSemFeld();
			Datenbank.createTableVokabelListe();
			Datenbank.createTableVokabelSemFeld();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	/*Die Handle-Methode ist der Einstiegspunkt für die Verarbeitung eingehender HTTP-Anfragen. 
	 * Sie bestimmt die HTTP-Methode (GET, POST, PUT, DELETE) und verarbeitet die Anfrage basierend 
	 * auf den angegebenen Pfaden.*/
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String requestMethod = exchange.getRequestMethod();
		URI uri = exchange.getRequestURI();
		System.out.println(uri + " " + requestMethod);
		String path = uri.getPath();
		if (path.startsWith("/"))
			path = path.substring(1);
		String[] paths = path.split("/");
		if (requestMethod.equalsIgnoreCase("GET"))
			get(exchange, paths);
		else if (requestMethod.equalsIgnoreCase("POST"))
			post(exchange, paths);
		else if (requestMethod.equalsIgnoreCase("PUT"))
			put(exchange, paths);
		else if (requestMethod.equalsIgnoreCase("DELETE"))
			delete(exchange, paths);
		else
			setResponse(exchange, 400, new Meldung("Falscher HTTP Befehl " + requestMethod).toXML());
	}
	
	/*Die Methoden get, post, put und delete sind für die Verarbeitung spizifischer HTTP-Methoden verantwortlich und analysieren 
	 * die eingehenden Request-Paths und verarbeiten die entsprechenden Datenbankoperationen.
	 * Die Methoden interagieren mit der Datenbank-Klasse (i.e. Datenbankinteraktionsklasse), um CRUD-Operationen 
	 * (Erstellen, Lesen, Aktualisieren, Löschen) für Vokabeln, Listen, semantische Felder usw. durchzuführen. 
	 * Abhängig von der Anfrage generieren die Methoden XML-Responses, die abgerufene oder geänderte Daten enthalten.*/

	/*Die GET-Methode wird implementiert für das Lesen von Vokabeln, Listen, semantischen Feldern, Vokabeln in Listen, 
	 * Vokabeln in semantischen Feldern, einem Vokabel zugeordnete Listen und einem Vokabel zugeordnete semantische Felder.*/
	private void get(HttpExchange exchange, String[] paths) {
		int retc = 200;
		String response = "";
		if (paths.length == 1 && paths[0].equals("vokabeln")) {
			try {
				ArrayList<Vokabel> alVokabeln = Datenbank.readVokabeln();
				Vokabeln v = new Vokabeln(alVokabeln);
				response = v.toXML();
			} catch (SQLException e) {
				retc = 500;
				response = new Meldung(e.toString()).toXML();
			}
		}

		else if (paths.length == 1 && paths[0].equals("listen")) {
			try {
				ArrayList<Liste> alListen = Datenbank.readListen();
				Listen l = new Listen(alListen);
				response = l.toXML();
			} catch (SQLException e) {
				retc = 500;
				response = new Meldung(e.toString()).toXML();
			}
		}

		else if (paths.length == 1 && paths[0].equals("semantischefelder")) {
			try {
				ArrayList<SemantischesFeld> alSemFelder = Datenbank.readSemFelder();
				SemantischeFelder s = new SemantischeFelder(alSemFelder);
				response = s.toXML();
			} catch (SQLException e) {
				retc = 500;
				response = new Meldung(e.toString()).toXML();
			}
		}

		else if (paths.length == 2 && paths[0].equals("vokabelnliste")) {
			try {
				ArrayList<Vokabel> alVokabelnListe = Datenbank.readVokabelnListe(Integer.parseInt(paths[1]));
				VokabelnListe vl = new VokabelnListe(alVokabelnListe);
				response = vl.toXML();
			} catch (SQLException e) {
				retc = 500;
				response = new Meldung(e.toString()).toXML();
			}
		}

		else if (paths.length == 2 && paths[0].equals("vokabelnsemfeld")) {
			try {
				ArrayList<Vokabel> alVokabelnSemFeld = Datenbank.readVokabelnSemFeld(Integer.parseInt(paths[1]));
				VokabelnSemFeld vs = new VokabelnSemFeld(alVokabelnSemFeld);
				response = vs.toXML();
			} catch (SQLException e) {
				retc = 500;
				response = new Meldung(e.toString()).toXML();
			}
		} else if (paths.length == 2 && paths[0].equals("associatedlisten")) {
			try {
				ArrayList<Liste> alAssociatedListen = Datenbank.getAssociatedListen(Integer.parseInt(paths[1]));
				AssociatedListen al = new AssociatedListen(alAssociatedListen);
				response = al.toXML();
			} catch (SQLException e) {
				retc = 500;
				response = new Meldung(e.toString()).toXML();
			}
		} else if (paths.length == 2 && paths[0].equals("associatedsemfelder")) {
			try {
				ArrayList<SemantischesFeld> alAssociatedSemFelder = Datenbank
						.getAssociatedSemFelder(Integer.parseInt(paths[1]));
				AssociatedSemFelder as = new AssociatedSemFelder(alAssociatedSemFelder);
				response = as.toXML();
			} catch (SQLException e) {
				retc = 500;
				response = new Meldung(e.toString()).toXML();
			}
		}
		setResponse(exchange, retc, response);
	}
	
	/*Die POST-Methode wird implementiert für das in die Datenbank Schreiben von Vokabeln, 
	 * Listen, semantischen Feldern sowie das Zuordnen von Vokabeln zu Listen und semantischen Feldern.*/
	private void post(HttpExchange exchange, String[] paths) {
		int retc = 201;
		String response = "";
		InputStream is = exchange.getRequestBody();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		if (paths.length == 2 && paths[0].equals("vokabel")) {
			try {
				String line = br.readLine();
				System.out.println("\t" + line);
				Vokabel v = new Vokabel(line);
				Datenbank.insertVokabel(v);
			} catch (IOException | SQLException e) {
				retc = 500;
				response = new Meldung(e.toString()).toXML();
			}
		} else if (paths.length == 2 && paths[0].equals("liste")) {
			try {
				String line = br.readLine();
				System.out.println("\t" + line);
				Liste l = new Liste(line);
				Datenbank.insertListe(l);
			} catch (IOException | SQLException e) {
				retc = 500;
				response = new Meldung(e.toString()).toXML();
			}
		} else if (paths.length == 2 && paths[0].equals("semantischesfeld")) {
			try {
				String line = br.readLine();
				System.out.println("\t" + line);
				SemantischesFeld s = new SemantischesFeld(line);
				Datenbank.insertSemFeld(s);
			} catch (IOException | SQLException e) {
				retc = 500;
				response = new Meldung(e.toString()).toXML();
			}
		}

		else if (paths.length == 3 && paths[0].equals("vokabelnliste")) {
			try {
				String line = br.readLine();
				System.out.println("\t" + line);
				VokabelnListe vl = new VokabelnListe(line);
				Datenbank.addVokabelToListe(Integer.parseInt(paths[1]), Integer.parseInt(paths[2]));
			} catch (IOException | SQLException e) {
				retc = 500;
				response = new Meldung(e.toString()).toXML();
			}
		}

		else if (paths.length == 3 && paths[0].equals("vokabelnsemfeld")) {
			try {
				String line = br.readLine();
				System.out.println("\t" + line);
				VokabelnSemFeld vs = new VokabelnSemFeld(line);
				Datenbank.addVokabelToSemFeld(Integer.parseInt(paths[1]), Integer.parseInt(paths[2]));
			} catch (IOException | SQLException e) {
				retc = 500;
				response = new Meldung(e.toString()).toXML();
			}
		}

		else {
			retc = 400;
			response = new Meldung("Falsche URL").toXML();
		}
		setResponse(exchange, retc, response);
	}

	/*Die PUT-Methode wird implementiert für das Aktualisieren von Vokabeln, 
	 * Listen und semantischen Feldern.*/
	private void put(HttpExchange exchange, String[] paths) {
		int retc = 201;
		String response = "";
		InputStream is = exchange.getRequestBody();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		if (paths.length == 2 && paths[0].equals("vokabel")) {
			try {
				String line = br.readLine();
				System.out.println("\t" + line);
				Vokabel v = new Vokabel(line);
				v.setVokabelID(Integer.parseInt(paths[1]));
				Datenbank.updateVokabel(v);
			} catch (IOException | SQLException e) {
				retc = 500;
				response = new Meldung(e.toString()).toXML();
			}
		} else if (paths.length == 2 && paths[0].equals("liste")) {
			try {
				String line = br.readLine();
				System.out.println("\t" + line);
				Liste l = new Liste(line);
				l.setListeID(Integer.parseInt(paths[1]));
				Datenbank.updateListe(l);
			} catch (IOException | SQLException e) {
				retc = 500;
				response = new Meldung(e.toString()).toXML();
			}
		} else if (paths.length == 2 && paths[0].equals("semantischesfeld")) {
			try {
				String line = br.readLine();
				System.out.println("\t" + line);
				SemantischesFeld s = new SemantischesFeld(line);
				s.setSemFeldID(Integer.parseInt(paths[1]));
				Datenbank.updateSemFeld(s);
			} catch (IOException | SQLException e) {
				retc = 500;
				response = new Meldung(e.toString()).toXML();
			}
		}

		else {
			retc = 400;
			response = new Meldung("Falsche URL").toXML();
		}
		setResponse(exchange, retc, response);
	}

	/*Die DELETE-Methode wird implementiert für das Löschen von Vokabeln, Listen und semantischen Feldern sowie
	 * das Entfernen von zugeordneten Vokabeln aus Listen und semantischen Feldern*/
	private void delete(HttpExchange exchange, String[] paths) {
		int retc = 204;
		String response = "";

		if (paths.length > 0) {
			if (paths[0].equals("vokabel")) {
				if (paths.length == 2) {
					try {
						Datenbank.deleteVokabel(Integer.parseInt(paths[1]));
					} catch (SQLException e) {
						response = new Meldung(e.toString()).toXML();
						retc = 500;
					}
				} else {
					retc = 400;
					response = new Meldung("Falsche URL").toXML();
				}
				
			} else if (paths[0].equals("liste")) {
				if (paths.length == 2) {
					try {
						Datenbank.deleteListe(Integer.parseInt(paths[1]));
					} catch (SQLException e) {
						response = new Meldung(e.toString()).toXML();
						retc = 500;
					}
				} else {
					retc = 400;
					response = new Meldung("Falsche URL").toXML();
				}
				
			} else if (paths[0].equals("semantischesfeld")) {
				if (paths.length == 2) {
					try {
						Datenbank.deleteSemFeld(Integer.parseInt(paths[1]));
					} catch (SQLException e) {
						response = new Meldung(e.toString()).toXML();
						retc = 500;
					}
				} else {
					retc = 400;
					response = new Meldung("Falsche URL").toXML();
				}

			} else if (paths[0].equals("vokabelnliste")) {
				if (paths.length == 3) {
					try {
						Datenbank.deleteVokabelFromList(Integer.parseInt(paths[1]), Integer.parseInt(paths[2]));
					} catch (SQLException e) {
						response = new Meldung(e.toString()).toXML();
						retc = 500;
					}
				} else {
					retc = 400;
					response = new Meldung("Falsche URL").toXML();
				}

			} else if (paths[0].equals("vokabelnsemfeld")) {
				if (paths.length == 3) {
					try {
						Datenbank.deleteVokabelFromSemFeld(Integer.parseInt(paths[1]), Integer.parseInt(paths[2]));
					} catch (SQLException e) {
						response = new Meldung(e.toString()).toXML();
						retc = 500;
					}
				} else {
					retc = 400;
					response = new Meldung("Falsche URL").toXML();
				}
			} else {
				retc = 400;
				response = new Meldung("Falsche URL").toXML();
			}
		} else {
			retc = 400;
			response = new Meldung("Falsche URL").toXML();
		}

		setResponse(exchange, retc, response);
	}

	/*Die setResponse-Methode wird verwendet, um Responses zu generieren und an den Client zu senden. 
	 * Antwortheader, die den Content-Type enthalten, werden festgelegt. Response-Inhalte werden mithilfe 
	 * der UTF-8-Kodierung in Bytes umgewandelt und der Response an den Client gesendet.*/
	private void setResponse(HttpExchange exchange, int rc, String r) {
		if (r.length() > 0)
			r = XMLHandler.XML_PROLOG_UTF8 + r;
		System.out.println("   returncode = " + rc + "\n   responsebody = '" + r + "'");
		exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
		byte[] bytes = r.getBytes(StandardCharsets.UTF_8);
		try {
			exchange.sendResponseHeaders(rc, rc != 204 ? bytes.length : -1);
			OutputStream os = exchange.getResponseBody();
			if (rc != 204)
				os.write(bytes);
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
