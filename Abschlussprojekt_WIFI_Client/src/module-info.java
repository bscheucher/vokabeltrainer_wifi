module Abschlussprojekt_WIFI_Client {
	requires transitive javafx.graphics;
	requires javafx.controls;
	
	requires java.net.http;
	requires javafx.base;
	requires java.sql;
	requires Abschlussprojekt_WIFI_Klassen;
	
	exports client;
	opens client to javafx.base;
}