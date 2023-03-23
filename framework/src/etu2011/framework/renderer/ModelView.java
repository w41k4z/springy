package etu2011.framework.renderer;

public class ModelView {

    private String view;

    // constructor
    public ModelView(String view) {
        this.setView(view);
    }

    // setter
    public void setView(String view) {
        this.view = view;
    }

    // getter
    public String getView() {
        return this.view;
    }
}
