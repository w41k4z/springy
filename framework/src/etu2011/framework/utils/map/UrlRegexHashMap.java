package etu2011.framework.utils.map;

import java.util.HashMap;
import java.util.Map;

public class UrlRegexHashMap<SK, V> extends HashMap<UrlPatternKey, Object> {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object url) {
        for (Map.Entry<UrlPatternKey, Object> entry : this.entrySet()) {
            if (url.toString().matches(entry.getKey().getPattern())) {
                return (V) entry.getValue();
            }
        }
        return null;
    }
}
