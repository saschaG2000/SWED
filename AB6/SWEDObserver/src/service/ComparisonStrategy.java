package service;


/**
 * Strategy-Interface für Website-Vergleichsstrategien.
 */
public interface ComparisonStrategy {
    /**
     * Lädt die URL und prüft, ob sich im Vergleich zum letzten Aufruf etwas geändert hat.
     * @param url die zu überprüfende URL
     * @return true, wenn sich die Seite (nach der gewählten Strategie) verändert hat
     */
    boolean hasChanged(String url);
}