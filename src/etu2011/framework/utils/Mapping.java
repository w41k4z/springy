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

    /**
     * The default constructor
     * 
     * @param className The class + package name
     * @param method    The mapped method
     */
    public Mapping(String className, Method method) {
        this.className = className;
        this.method = method;
    }

    /* SETTERS SECTION */

    /**
     * Set the class field name
     * 
     * @param className The <package.class.name>
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Set the mapped field method
     * 
     * @param method The mapped method
     */
    public void setMethod(Method method) {
        this.method = method;
    }

    /* GETTERS SECTION */

    /**
     * Returns the class entire name
     * 
     * @return The class name
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * Returns the mapped method
     * 
     * @return The mapped method
     */
    public Method getMethod() {
        return this.method;
    }
}