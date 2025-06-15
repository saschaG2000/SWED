package monitor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UpdateManager {
 
    private final Map<String,String> lastContent = new HashMap<>();// Speichert den letzten Inhalt pro Subscription-ID

 // Funktion prüft, ob sich der Inhalt einer URL geändert hat
    public String checkUpdates(Subscription sub) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(sub.getUrl());						// Erstellt eine URL aus dem Link der Subscription
            conn = (HttpURLConnection) url.openConnection();		// Öffnet eine Verbindung zur URL
            conn.setRequestMethod("GET");							// Setzt die HTTP-Methode auf GET (Seite abrufen)
            conn.setConnectTimeout(5000);							// Maximal 5 Sekunden für Verbindungsaufbau
            conn.setReadTimeout(5000);								// Maximal 5 Sekunden für das Lesen der Antwort

            BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {	// Solange Zeilen vorhanden sind...
                sb.append(line).append('\n');				// ...füge jede Zeile mit Zeilenumbruch zum StringBuilder hinzu
            }
            in.close();

            String content = sb.toString();							// Wandelt alle gesammelten Zeilen in einen String um
            String old = lastContent.get(sub.getSubId());			// Holt den zuletzt gespeicherten Inhalt dieser Subscription
            if (old == null || !old.equals(content)) {				// Wenn noch kein Inhalt gespeichert wurde ODER sich der Inhalt geändert hat..
                lastContent.put(sub.getSubId(), content);				// ...speichere den neuen Inhalt
                return "Änderung erkannt auf " + sub.getUrl();			// ...und gib eine Änderungsmeldung zurück
            }
            return null;

        } catch (Exception e) {
            System.err.println("Fehler beim Prüfen von " + sub.getUrl() + ": " + e.getMessage());
            return null;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}
