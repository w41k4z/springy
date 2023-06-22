package etu2011.framework.renderer;

import java.util.HashMap;
import java.util.Map;

public class ModelView {

    private String view;
    private Map<String, Object> data;
    private Map<String, String> sessions;

    // constructors
    public ModelView(String view) {
        this.setView(view);
        this.setData(new HashMap<>());
        this.setSessions(new HashMap<>());
    }

    public ModelView(String view, Map<String, Object> data, Map<String, String> session) {
        this.setView(view);
        this.setData(data);
        this.setSessions(session);
    }

    // setters
    public void setView(String view) {
        this.view = view;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void setSessions(Map<String, String> session) {
        this.sessions = session;
    }

    // getters
    public String getView() {
        return this.view;
    }

    public Map<String, Object> getData() {
        return this.data;
    }

    public Map<String, String> getSessions() {
        return this.sessions;
    }

    // methods
    public void addData(String key, Object value) {
        this.data.put(key, value);
    }

    public void addSession(String key, Object value) {
        this.sessions.put(key, value.toString());
    }
}
