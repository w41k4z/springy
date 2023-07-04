package etu2011.framework.renderer;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code ModelView} class is used to render a view when being handled by
 * the framework.
 */
public class ModelView {
    /* FIELDS SECTIONS */
    private String view;
    private Map<String, Object> data;
    private boolean dataIsJson;
    private Map<String, Object> sessions;

    /* CONSTRUCTORS SECTIONS */

    /**
     * Default controller.
     * 
     * @param view the view inside the application default views directory to
     *             render.
     */
    public ModelView(String view) {
        this.setView(view);
        this.setData(new HashMap<>());
        this.setDataIsJson(false);
        this.setSessions(new HashMap<>());
    }

    /**
     * Controller with data.
     * 
     * @param view    the view inside the application default views directory to
     *                render.
     * @param data    the data to be passed to the view.
     * @param isJson  whether the data will be parsed to JSON or not.
     * @param session the session data to be passed to the request sessions.
     */
    public ModelView(String view, Map<String, Object> data, boolean isJson, Map<String, Object> session) {
        this.setView(view);
        this.setData(data);
        this.setDataIsJson(isJson);
        this.setSessions(session);
    }

    /* SETTERS SECTION */

    /**
     * This method sets the view to be rendered.
     * 
     * @param view the view inside the application default views directory to
     */
    public void setView(String view) {
        this.view = view;
    }

    /**
     * This method sets the data to be passed to the view.
     * 
     * @param data the data to be passed to the view.
     */
    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    /**
     * This method sets whether the data will be parsed to JSON or not.
     * 
     * @param isJson whether the data will be parsed to JSON or not.
     */
    public void setDataIsJson(boolean isJson) {
        this.dataIsJson = isJson;
    }

    /**
     * This method sets the session data to be passed to the request sessions.
     * 
     * @param session the session data to be passed to the request sessions.
     */
    public void setSessions(Map<String, Object> session) {
        this.sessions = session;
    }

    /* GETTERS SECTION */

    /**
     * This method returns the view to be rendered.
     * 
     * @return the view to be rendered.
     */
    public String getView() {
        return this.view;
    }

    /**
     * This method returns the data to be passed to the view.
     * 
     * @return the data to be passed to the view.
     */
    public Map<String, Object> getData() {
        return this.data;
    }

    /**
     * This method returns whether the data will be parsed to JSON or not.
     * 
     * @return whether the data will be parsed to JSON or not.
     */
    public boolean dataIsJson() {
        return this.dataIsJson;
    }

    /**
     * This method returns the session data to be passed to the request sessions.
     * 
     * @return the session data to be passed to the request sessions.
     */
    public Map<String, Object> getSessions() {
        return this.sessions;
    }

    /* METHODS SECTION */

    /**
     * This method adds a data to the data map.
     * 
     * @param key   the key of the data.
     * @param value the value of the data.
     */
    public void addData(String key, Object value) {
        this.data.put(key, value);
    }

    /**
     * This method adds a session to the session map.
     * 
     * @param key   the key of the session.
     * @param value the value of the session.
     */
    public void addSession(String key, Object value) {
        this.sessions.put(key, value);
    }
}
