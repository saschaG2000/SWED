package service;

/**
 * 3) Identical text content:
 *    Extrahiert den Text (ohne Tags) und vergleicht ihn.
 */
public class TextComparisonStrategy extends AbstractComparisonStrategy {
    private String lastText = null;

    @Override
    public boolean hasChanged(String url) {
        String html = fetchContent(url);
        // Sehr einfache Text-Extraktion: alles zwischen '<' und '>' entfernen
        String text = html.replaceAll("<[^>]+>", "");
        if (lastText == null || !text.equals(lastText)) {
            lastText = text;
            return true;
        }
        return false;
    }
}