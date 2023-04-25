package modelControllers;

import java.sql.Date;

import etu2011.framework.annotations.DatePattern;
import etu2011.framework.annotations.HttpParam;
import etu2011.framework.annotations.ModelController;
import etu2011.framework.annotations.UrlMapping;
import etu2011.framework.enumerations.HttpMethods;
import etu2011.framework.enumerations.HttpParameters;
import etu2011.framework.renderer.ModelView;

@ModelController(route = "/test")
public class Test {

    @DatePattern("dd/MM/yy") // aside the default yy-mm-dd format, this format will be accepted
    private Date date;

    /* CONSTRUCTOR SECTION */
    public Test() throws Exception {
        super();
    }

    /* SETTERS SECTION */
    public void setDate(Date date) {
        this.date = date;
    }

    /* GETTERS SECTION */
    public Date getDate() {
        return this.date;
    }

    /* METHODS SECTION */
    @UrlMapping(url = "/{x}/tsara/{y}", method = HttpMethods.POST)
    public ModelView helloWorld(@HttpParam(type = HttpParameters.PATH_VARIABLE) String x,
            @HttpParam(name = "y", type = HttpParameters.PATH_VARIABLE) int yy,
            @HttpParam(type = HttpParameters.REQUEST_PARAMETER) String zzz) {
        ModelView modelView = new ModelView("test.jsp");
        modelView.add("testVariable", "x = " + x + ", y = " + yy + ", z = " + zzz);
        modelView.add("testVariable2", new String[] { zzz });
        return modelView;
    }

    @UrlMapping(url = "/")
    public ModelView test(@HttpParam(type = HttpParameters.REQUEST_PARAMETER) int test) {
        ModelView modelView = new ModelView("test.jsp");
        modelView.add("testVariable", test);
        modelView.add("testVariable2",
                new String[] { this.getDate().toString() + " : This is the value of field date" });
        return modelView;
    }
}