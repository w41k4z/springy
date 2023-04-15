package models;

import etu2011.framework.annotations.UrlMapping;
import etu2011.framework.renderer.ModelView;

import jakarta.servlet.http.HttpServletRequest;

public class Test {

    @UrlMapping("/test")
    public ModelView helloWorld() {
        ModelView modelView = new ModelView("test.jsp");
        modelView.add("testVariable", "Coucou enao, milay ilay Java");
        modelView.add("testVariable2", new String[] { "Tableau OK" });
        return modelView;
    }

    @UrlMapping("/test2")
    public ModelView huhu(HttpServletRequest req) {
        ModelView modelView = new ModelView("test.jsp");
        modelView.add("testVariable", req.getParameter("test"));
        modelView.add("testVariable2", new String[] { "Tableau OK" });
        return modelView;
    }
}