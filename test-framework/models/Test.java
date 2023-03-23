package models;

import etu2011.framework.annotation.Url;
import etu2011.framework.renderer.ModelView;

public class Test {

    @Url("/test-springy/test")
    public ModelView helloWorld() {
        return new ModelView("/views/test.jsp");
    }
}