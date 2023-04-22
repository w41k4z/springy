package modelControllers;

import etu2011.framework.annotations.HttpParam;
import etu2011.framework.annotations.Controller;
import etu2011.framework.annotations.UrlMapping;
import etu2011.framework.enumerations.HttpMethods;
import etu2011.framework.enumerations.HttpParameters;
import etu2011.framework.renderer.ModelView;
import etu2011.framework.servlet.ModelController;

import orm.annotation.Table;
import orm.annotation.Column;

@Controller(route = "/test")
@Table(columnCount = 1)
public class Test extends ModelController<Test> {

    @Column
    private String value;

    /* CONSTRUCTOR SECTION */
    public Test() throws Exception {
        super();
    }

    /* SETTERS SECTION */
    public void setValue(String test) {
        this.value = test;
    }

    /* GETTERS SECTION */
    public String getValue() {
        return this.value;
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
    public ModelView huhu(@HttpParam(type = HttpParameters.REQUEST_PARAMETER) int test) {
        ModelView modelView = new ModelView("test.jsp");
        modelView.add("testVariable", test);
        modelView.add("testVariable2", new String[] { this.getValue() + " : This is the value of field value" });
        return modelView;
    }
}