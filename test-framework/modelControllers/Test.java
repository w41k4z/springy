package modelControllers;

import java.sql.Date;

import etu2011.framework.annotations.DatePattern;
import etu2011.framework.annotations.ModelController;
import etu2011.framework.annotations.UrlMapping;
import etu2011.framework.enumerations.HttpMethods;
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
    @UrlMapping(url = "/tsara", method = HttpMethods.POST)
    public ModelView helloWorld() {
        ModelView modelView = new ModelView("test.jsp");
        modelView.add("testVariable", null);
        modelView.add("testVariable2", null);
        return modelView;
    }

    @UrlMapping(url = "/")
    public ModelView test() {
        ModelView modelView = new ModelView("test.jsp");
        modelView.add("testVariable", this.getDate());
        modelView.add("testVariable2",
                new String[] { this.getDate().toString() + " : This is the value of field date" });
        return modelView;
    }
}