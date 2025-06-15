package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Gemeinsame HTTP-GET-Logik für die Strategien.
 */
public abstract class AbstractComparisonStrategy implements ComparisonStrategy {
    protected String fetchContent(String urlStr) {
        StringBuilder content = new StringBuilder();
        try {
            @SuppressWarnings("deprecation")
			HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    content.append(line).append('\n');
                }
            }
        } catch (Exception e) {
            System.err.println("Fehler beim Laden von " + urlStr + ": " + e.getMessage());
        }
        return content.toString();
    }
}