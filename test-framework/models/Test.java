package models;

import etu2011.framework.annotations.HttpParam;
import etu2011.framework.annotations.UrlMapping;
import etu2011.framework.enumerations.HttpMethods;
import etu2011.framework.renderer.ModelView;

import jakarta.servlet.http.HttpServletRequest;

public class Test {

    @UrlMapping(url = "/test")
    public ModelView helloWorld() {
        ModelView modelView = new ModelView("test.jsp");
        modelView.add("testVariable", "Coucou enao, milay ilay Java");
        modelView.add("testVariable2", new String[] { "Tableau OK" });
        return modelView;
    }

    @UrlMapping(url = "/test2", method = HttpMethods.POST)
    public ModelView huhu(@HttpParam int test) {
        ModelView modelView = new ModelView("test.jsp");
        modelView.add("testVariable", test);
        modelView.add("testVariable2", new String[] { "Tableau OK" });
        return modelView;
    }
}