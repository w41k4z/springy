package models;

import etu2011.framework.annotations.HttpParam;
import etu2011.framework.annotations.ModelController;
import etu2011.framework.annotations.UrlMapping;
import etu2011.framework.enumerations.HttpMethods;
import etu2011.framework.enumerations.HttpParameters;
import etu2011.framework.renderer.ModelView;

import jakarta.servlet.http.HttpServletRequest;

@ModelController(route = "/test")
public class Test {

    @UrlMapping(url = "/{x}/tsara/{y}", method = HttpMethods.POST)
    public ModelView helloWorld(@HttpParam(type = HttpParameters.PATH_VARIABLE) String x,
            @HttpParam(name = "y", type = HttpParameters.PATH_VARIABLE) int yy,
            @HttpParam(type = HttpParameters.REQUEST_PARAMETER) String zzz,
            HttpServletRequest request) {
        ModelView modelView = new ModelView("test.jsp");
        modelView.add("testVariable", "x = " + x + ", y = " + yy + ", z = " + zzz);
        modelView.add("testVariable2", new String[] { request.getParameter("mmm") });
        return modelView;
    }

    @UrlMapping(url = "/")
    public ModelView huhu(@HttpParam(type = HttpParameters.REQUEST_PARAMETER) int test, HttpServletRequest request) {
        ModelView modelView = new ModelView("test.jsp");
        modelView.add("testVariable", test);
        modelView.add("testVariable2", new String[] { request.getParameter("test") });
        return modelView;
    }
}