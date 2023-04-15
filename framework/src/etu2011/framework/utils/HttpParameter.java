package etu2011.framework.utils;

import java.lang.reflect.Parameter;

import etu2011.framework.annotations.HttpParam;
import etu2011.framework.enumerations.HttpParameters;
import jakarta.servlet.http.HttpServletRequest;

public class HttpParameter {

    private Parameter parameter;
    private HttpParameters parameterType;
    private String parameterName;

    /* CONSTRUCTOR SECTION */
    public HttpParameter(Parameter parameter) throws IllegalArgumentException {
        this.setParameter(parameter);
    }

    /* SETTER SECTION */
    public void setParameter(Parameter parameter) throws IllegalArgumentException {
        if (!isHttpParameter(parameter)) {
            throw new IllegalArgumentException(
                    "This is not a valid http parameter\n.VALID: Annotated with @HttpParam or of type HttpServletRequest");
        }
        this.parameter = parameter;
        if (parameter.isAnnotationPresent(HttpParam.class)) {
            switch (parameter.getAnnotation(HttpParam.class).type()) {
                case REQUEST_PARAMETER:
                    this.parameterType = HttpParameters.REQUEST_PARAMETER;
                    break;
                case PATH_VARIABLE:
                    this.parameterType = HttpParameters.PATH_VARIABLE;
                    break;
                default:
                    break;
            }
        }
        this.parameterName = parameter.getName();
        if (this.getParameter().isAnnotationPresent(HttpParam.class)) {
            this.parameterName = this.getParameter().getAnnotation(HttpParam.class).name().length() == 0
                    ? this.parameterName
                    : this.getParameter().getAnnotation(HttpParam.class).name();
        }
    }

    /* GETTER SECTION */
    public Parameter getParameter() {
        return this.parameter;
    }

    public HttpParameters getParameterType() {
        return this.parameterType;
    }

    public String getParameterName() {
        return this.parameterName;
    }

    /* METHODS SECTION */
    public static boolean isHttpParameter(Parameter parameter) {
        return parameter.getType().equals(HttpServletRequest.class)
                || parameter.isAnnotationPresent(HttpParam.class);
    }
}
