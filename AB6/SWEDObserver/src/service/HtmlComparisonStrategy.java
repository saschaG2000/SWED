package service;


/**
 * 2) Identical HTML content:
 *    Vergleicht den kompletten HTML-String.
 */
public class HtmlComparisonStrategy extends AbstractComparisonStrategy {
    private String lastHtml = null;

    @Override
    public boolean hasChanged(String url) {
        String content = fetchContent(url);
        if (lastHtml == null || !content.equals(lastHtml)) {
            lastHtml = content;
            return true;
        }
        return false;
    }
}