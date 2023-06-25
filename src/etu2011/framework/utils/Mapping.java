package etu2011.framework.utils;

import java.lang.reflect.Method;

/**
 * The {@code Mapping} class is used to represent the FrontServlet
 * UrlRegexHashMap value.
 */
public class Mapping {
    /* FIELDS SECTION */
    private String className;
    private Method method;

    /* CONSTRUCTOR SECTION */
    public Mapping(String className, Method method) {
        this.className = className;
        this.method = method;
    }

    /* SETTERS SECTION */
    public void setClassName(String className) {
        this.className = className;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    /* GETTERS SECTION */
    public String getClassName() {
        return this.className;
    }

    public Method getMethod() {
        return this.method;
    }
}