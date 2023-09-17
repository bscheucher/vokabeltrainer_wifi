
package server;

/*Dieser Code richtet einen einfachen HTTP-Server mithilfe der in Java integrierten Klasse com.sun.net.httpserver.HttpServer ein. 
 * Der Server überwacht die „localhost“-Adresse und den Port 8084 und antwortet auf Anfragen mit der in der 
 * VokabeltrainerHandler-Klasse definierten Logik (die im Codeausschnitt nicht bereitgestellt wird). Der Server kann durch Drücken 
 * der Eingabetaste in der Konsole gestoppt werden, in der der Server ausgeführt wird.*/

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

public class VokabeltrainerServer {

	public static void main(String[] args) {
		try {
			// Eine Instanz von InetAddress für die Adresse „localhost“ wird erstellt.
			InetAddress inet = InetAddress.getByName("localhost");

			// Eine InetSocketAddress wird unter Verwendung der zuvor generierten
			// InetAddress und Port 8084 erstellt.
			InetSocketAddress addr = new InetSocketAddress(inet, 8084);

			/*
			 * Eine Instanz von HttpServer wird mit der Methode HttpServer.create()
			 * erstellt, wobei die Adresse und 0 als zweites Argument übergeben werden (was
			 * die maximale Anzahl eingehender Verbindungen in der Warteschlange angibt; 0
			 * bedeutet unbegrenzt).
			 */
			HttpServer server = HttpServer.create(addr, 0);

			// Für den Root-Path („/“) wird ein Kontext erstellt und die Handlerklasse
			// VokabeltrainerHandler zugeordnet.
			server.createContext("/", new VokabeltrainerHandler());

			/*
			 * Der Executor für den Server wird mithilfe von Executors.newCachedThreadPool()
			 * auf einen zwischengespeicherten Thread-Pool festgelegt. Dadurch kann der
			 * Server mehrere eingehende Anforderungen gleichzeitig mithilfe eines Pools
			 * wiederverwendbarer Threads verarbeiten.
			 */
			server.setExecutor(Executors.newCachedThreadPool());

			// Der Server wird mit der Methode server.start() gestartet.
			server.start();

			/*
			 * In der Konsole wird eine Meldung ausgegebeb, die angibt, dass der Server
			 * läuft, und das Programm wartet auf Benutzereingaben (Drücken der
			 * Eingabetaste) mithilfe von System.in.read().
			 */
			System.out.println("VokabeltrainerServer laeuft - zum Beenden Eingabetaste druecken");
			try {
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}

			/*
			 * Nach dem Drücken der Eingabetaste wird in der Konsole die Meldung ausgegeben,
			 * die darauf hinweist, dass der Server gestoppt wird. Der Server wird mit
			 * server.stop(0) gestoppt und der Thread-Pool des Executors wird mit
			 * ((ExecutorService)server.getExecutor()).shutdown() heruntergefahren.
			 */
			System.out.println("VokabeltrainerServer wird gestoppt");
			server.stop(0);
			((ExecutorService) server.getExecutor()).shutdown();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
