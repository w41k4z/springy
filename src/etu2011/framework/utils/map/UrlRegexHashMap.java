package etu2011.framework.utils.map;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code UrlRegexHashMap} class is used to provide a HashMap supporting
 * regex.
 * It is need to enable the {@code PATH_VARIABLE} feature.
 */
public class UrlRegexHashMap<SK, V> extends HashMap<UrlPatternKey, Object> {

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
