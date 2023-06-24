package etu2011.framework.renderer;

import java.util.HashMap;
import java.util.Map;

public class ModelView {
    /* FIELDS SECTIONS */
    private String view;
    private Map<String, Object> data;
    private boolean dataIsJson;
    private Map<String, String> sessions;

    /* CONSTRUCTORS SECTIONS */
    public ModelView(String view) {
        this.setView(view);
        this.setData(new HashMap<>());
        this.setDataIsJson(false);
        this.setSessions(new HashMap<>());
    }

    public ModelView(String view, Map<String, Object> data, boolean isJson, Map<String, String> session) {
        this.setView(view);
        this.setData(data);
        this.setDataIsJson(isJson);
        this.setSessions(session);
    }

    /* SETTERS SECTION */
    public void setView(String view) {
        this.view = view;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void setDataIsJson(boolean isJson) {
        this.dataIsJson = isJson;
    }

    public void setSessions(Map<String, String> session) {
        this.sessions = session;
    }

    /* GETTERS SECTION */
    public String getView() {
        return this.view;
    }

    public Map<String, Object> getData() {
        return this.data;
    }

    public boolean dataIsJson() {
        return this.dataIsJson;
    }

    public Map<String, String> getSessions() {
        return this.sessions;
    }

    /* METHODS SECTION */
    public void addData(String key, Object value) {
        this.data.put(key, value);
    }

    public void addSession(String key, Object value) {
        this.sessions.put(key, value.toString());
    }
}
