package etu2011.framework.renderer;

import java.util.HashMap;
import java.util.Map;

public class ModelView {

    private String view;
    private Map<String, Object> data;

    // constructors
    public ModelView(String view) {
        this.setView(view);
        this.setData(new HashMap<>());
    }

    public ModelView(String view, Map<String, Object> data) {
        this.setView(view);
        this.setData(data);
    }

    // setters
    public void setView(String view) {
        this.view = view;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    // getters
    public String getView() {
        return this.view;
    }

    public Map<String, Object> getData() {
        return this.data;
    }

    // methods
    public void add(String key, Object value) {
        this.data.put(key, value);
    }
}
