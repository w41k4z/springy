package etu2011.framework.utils.map;

/**
 * The {@code UrlPatternKey} class is used to represent the URLRegexHashMap key.
 * 
 * @see etu2011.framework.utils.map.UrlRegexHashMap
 */
public class UrlPatternKey {
    /* FIELDS SECTION */
    private String url; // ex: /path/{x}/to/{y}
    private String pattern;

    /* CONSTRUCTOR SECTION */

    /**
     * The default constructor is private to prevent the creation of an object
     */
    private UrlPatternKey() {
    }

    public UrlPatternKey(String url) {
        this.setUrl(url);
        this.setPattern(url);
    }

    /* SETTERS SECTION */
    private void setUrl(String url) {
        this.url = url;
    }

    private void setPattern(String key) {
        this.pattern = this.createPattern(key);
    }

    /* GETTERS SECTION */
    public String getUrl() {
        return this.url;
    }

    public String getPattern() {
        return this.pattern;
    }

    /* METHODS SECTION */

    /**
     * This method creates a pattern from the given URL.
     * 
     * @param url The URL to create the pattern from.
     * @return The pattern created from the given URL.
     */
    public String createPattern(String url) {
        return url.replaceAll("\\{.*?\\}", "(.*?)").replaceAll("/", "\\\\/");
    }

    /**
     * This method creates a {@code UrlPatternKey} object from the given URL.
     * 
     * @param url The URL to create the {@code UrlPatternKey} object from.
     * @return The {@code UrlPatternKey} object created from the given URL.
     */
    public static UrlPatternKey URL(String url) {
        UrlPatternKey urlOnly = new UrlPatternKey();
        urlOnly.setUrl(url);
        return urlOnly;
    }

    /* OVERRIDES SECTION */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UrlPatternKey) {
            return this.getPattern().equals(((UrlPatternKey) obj).getPattern());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getPattern().hashCode();
    }

    @Override
    public String toString() {
        return this.getUrl();
    }
}
