package etu2011.framework.utils.map;

public class UrlPatternKey {
    /* FIELDS SECTION */
    private String url; // ex: /path/{x}/to/{y}
    private String pattern;

    /* CONSTRUCTOR SECTION */
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
    public String createPattern(String url) {
        return url.replaceAll("\\{.*?\\}", "(.*?)").replaceAll("/", "\\\\/");
    }

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
