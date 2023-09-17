package client;

/*Diese Klasse stellt Methoden zum Ausführen verschiedener HTTP-Operationen 
 * (GET, POST, PUT, DELETE) für Ressourcen bereit, die von der REST-API 
 * exponiert werden.*/

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;

import klassen.Meldung;
import klassen.XMLHandler;

public class ServiceFunctions {
	private static final String url = "http://localhost:8084/";

	/*
	 * Diese Methode führt eine HTTP-GET-Anfrage aus, um Daten von der angegebenen
	 * URL abzurufen. Sie erstellt einen URL basierend auf den bereitgestellten
	 * Parametern „item“ und „id“, sendet die GET-Anfrage und verarbeitet die
	 * Antwort entsprechend.
	 */
	public static String get(String item, String id) throws VokabeltrainerException {
		try {
			String urls = url + item;
			if (id != null && id.length() > 0)
				urls += "/" + id;
			URI uri = new URI(urls);
			HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<byte[]> response = client.send(request, BodyHandlers.ofByteArray());
			int retc = response.statusCode();
			String line = new String(response.body());
			if (retc == 200)
				return line;
			else
				throw new VokabeltrainerException(new Meldung(line).getText());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			throw new VokabeltrainerException(e.toString());
		}
	}

	/*
	 * Es gibt zwei überladene Post-Methoden. Beide Methoden führen eine
	 * HTTP-POST-Anfrage aus, um Daten an den angegebenen URL zu senden. Sie
	 * erstellen den URL basierend auf den bereitgestellten Parametern item, id und
	 * id2 (für die zweite Post-Methode), senden die bereitgestellten Detaildaten
	 * als Requestbody und verarbeiten die Antwort.
	 */
	public static void post(String item, String id, String detail) throws VokabeltrainerException {
		try {
			URI uri = new URI(url + item + "/" + id);
			detail = XMLHandler.XML_PROLOG_UTF8 + detail;
			HttpRequest request = HttpRequest.newBuilder(uri).header("Content-Type", "text/plain; charset=UTF-8")
					.POST(BodyPublishers.ofString(detail)).build();
			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<byte[]> response = client.send(request, BodyHandlers.ofByteArray());
			int retc = response.statusCode();
			if (retc != 201)
				throw new VokabeltrainerException(new Meldung(new String(response.body())).getText());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			throw new VokabeltrainerException(e.toString());
		}
	}

	public static void post(String item, String id1, String id2, String detail) throws VokabeltrainerException {
		try {
			URI uri = new URI(url + item + "/" + id1 + "/" + id2); // Modified URI creation
			detail = XMLHandler.XML_PROLOG_UTF8 + detail;
			HttpRequest request = HttpRequest.newBuilder(uri).header("Content-Type", "text/plain; charset=UTF-8")
					.POST(BodyPublishers.ofString(detail)).build();
			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<byte[]> response = client.send(request, BodyHandlers.ofByteArray());
			int retc = response.statusCode();
			if (retc != 201)
				throw new VokabeltrainerException(new Meldung(new String(response.body())).getText());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			throw new VokabeltrainerException(e.toString());
		}
	}

	/*
	 * Die Put-Methode führt eine HTTP-PUT-Anfrage aus, um Daten unter der
	 * angegebenen URL zu aktualisieren. Ähnlich wie die Post-Methoden erstellt sie
	 * den URL und sendet die bereitgestellten Detaildaten im Requestbody.
	 */
	public static void put(String item, String id, String detail) throws VokabeltrainerException {
		try {
			URI uri = new URI(url + item + "/" + id);
			detail = XMLHandler.XML_PROLOG_UTF8 + detail;
			HttpRequest request = HttpRequest.newBuilder(uri).header("Content-Type", "text/plain; charset=UTF-8")
					.PUT(BodyPublishers.ofString(detail)).build();
			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<byte[]> response = client.send(request, BodyHandlers.ofByteArray());
			int retc = response.statusCode();
			if (retc != 201)
				throw new VokabeltrainerException(new Meldung(new String(response.body())).getText());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			throw new VokabeltrainerException(e.toString());
		}
	}

	/*
	 * Es gibt zwei überladene Delete-Methoden. Sie führen HTTP-DELETE-Anfragen aus,
	 * um Daten von der angegebenen URL zu entfernen. Der URL wird basierend auf den
	 * bereitgestellten Parametern item, id bzw. id1 und id2 (für die zweite Delete-Methode)
	 * erstellt.
	 */
	public static void delete(String item, String id) throws VokabeltrainerException {
		try {
			String urls = url + item;
			if (id != null && id.length() > 0)
				urls += "/" + id;
			URI uri = new URI(urls);
			HttpRequest request = HttpRequest.newBuilder(uri).DELETE().build();
			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<byte[]> response = client.send(request, BodyHandlers.ofByteArray());
			int retc = response.statusCode();
			if (retc != 204)
				throw new VokabeltrainerException(new Meldung(new String(response.body())).getText());
		} catch (IOException | URISyntaxException | InterruptedException e) {
			throw new VokabeltrainerException(e.toString());
		}
	}

	public static void delete(String item, String id1, String id2) throws VokabeltrainerException {
		try {
			String urls = url + item;
			if (id1 != null && id1.length() > 0) {
				urls += "/" + id1;
				if (id2 != null && id2.length() > 0) {
					urls += "/" + id2;
				}
			}
			URI uri = new URI(urls);
			HttpRequest request = HttpRequest.newBuilder(uri).DELETE().build();
			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<byte[]> response = client.send(request, BodyHandlers.ofByteArray());
			int retc = response.statusCode();
			if (retc != 204) {
				throw new VokabeltrainerException(new Meldung(new String(response.body())).getText());
			}
		} catch (IOException | URISyntaxException | InterruptedException e) {
			throw new VokabeltrainerException(e.toString());
		}
	}

}
