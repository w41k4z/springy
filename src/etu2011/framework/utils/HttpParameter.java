package etu2011.framework.utils;

import java.lang.reflect.Parameter;

import etu2011.framework.annotations.HttpParam;
import etu2011.framework.enumerations.HttpParameters;

/**
 * The {@code HttpParameter} class is used to represent an http parameter.
 * 
 * @see etu2011.framework.annotations.HttpParam
 */
public class HttpParameter {
    /* FIELDS SECTION */
    private Parameter parameter;
    private HttpParameters parameterType = null;
    private String parameterName;

    /* CONSTRUCTOR SECTION */

    /**
     * The default constructor
     */
    public HttpParameter() {
    }

    /**
     * Constructor with the target Parameter object
     * 
     * @param parameter The parameter object
     */
    public HttpParameter(Parameter parameter) {
        this.setParameter(parameter);
    }

    /* SETTER SECTION */

    /**
     * Set the parameter object field, its type and its name
     * 
     * @param parameter the parameter to set.
     */
    public void setParameter(Parameter parameter) {
        if (!isHttpParameter(parameter)) {
            throw new IllegalArgumentException(
                    "This is not a valid http parameter\n.VALID: Annotated with @HttpParam");
        }
        this.parameter = parameter;
        this.parameterType = parameter.getAnnotation(HttpParam.class).type();
        this.parameterName = this.getParameter().getAnnotation(HttpParam.class).name().trim().length() == 0
                ? parameter.getName()
                : this.getParameter().getAnnotation(HttpParam.class).name();
    }

    /* GETTER SECTION */

    /**
     * Returns the parameter object
     * 
     * @return The parameter object
     */
    public Parameter getParameter() {
        return this.parameter;
    }

    /**
     * Returns the parameter type
     * 
     * @return The parameter type
     */
    public HttpParameters getParameterType() {
        return this.parameterType;
    }

    /**
     * Returns the parameter name
     * 
     * @return The parameter name
     */
    public String getParameterName() {
        return this.parameterName;
    }

    /* METHODS SECTION */

    /**
     * Check if the target parameter is a Http parameter
     * 
     * @param parameter The parameter object
     * @return True if the parameter target is a Http parameter, otherwise false
     */
    public static boolean isHttpParameter(Parameter parameter) {
        return parameter.isAnnotationPresent(HttpParam.class);
    }
}
