package service;

/**
 * 1) Identical content size:
 *    Vergleicht die Länge des HTML-Strings.
 */
public class SizeComparisonStrategy extends AbstractComparisonStrategy {
    private long lastSize = -1;

    @Override
    public boolean hasChanged(String url) {
        String content = fetchContent(url);
        long size = content.length();
        if (lastSize == -1 || size != lastSize) {
            lastSize = size;
            return true;
        }
        return false;
    }
}