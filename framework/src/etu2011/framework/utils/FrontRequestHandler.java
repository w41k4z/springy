package etu2011.framework.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Map;

import etu2011.framework.Mapping;
import etu2011.framework.annotations.UrlMapping;
import etu2011.framework.config.FrontServletConfig;
import etu2011.framework.renderer.ModelView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontRequestHandler {

    private Mapping mappingTarget;
    private ArrayList<Object> preparedParameterValues;

    /* SETTERS SECTION */
    private void setMappingTarget(Mapping mappingTarget) {
        this.mappingTarget = mappingTarget;
    }

    private void setPreparedParameterValues(ArrayList<Object> paramsValue) {
        this.preparedParameterValues = paramsValue;
    }

    /* GETTERS SECTION */
    public Mapping getMappingTarget() {
        return this.mappingTarget;
    }

    public ArrayList<Object> getPreparedParameterValues() {
        return this.preparedParameterValues;
    }

    /* METHODS SECTION */
    private void prepareRequest(HttpServletRequest req, HttpServletResponse resp, Map<String, Mapping> mappingUrl) {
        String url = req.getRequestURL().toString().split("://")[1];
        String context = url.substring(url.indexOf("/")).replace(req.getContextPath(), "");
        this.setMappingTarget(mappingUrl.get(context));
    }

    private void checkMethodValidity(HttpServletRequest req) throws NoSuchMethodException {
        String mappingMethodType = this.getMappingTarget().getMethod().getAnnotation(UrlMapping.class).method()
                .toString();
        if (!req.getMethod().toUpperCase(null).equals(mappingMethodType.toUpperCase(null))) {
            throw new NoSuchMethodException("This url is not allowed for " + req.getMethod() + " method");
        }
    }

    private void prepareMethodParameters(HttpServletRequest req) throws IllegalArgumentException {
        ArrayList<Object> values = new ArrayList<Object>();
        Method method = this.getMappingTarget().getMethod();
        HttpParameter param = new HttpParameter();
        for (Parameter parameter : method.getParameters()) {
            param.setParameter(parameter);
            values.add(this.getParameterValue(req, param));
        }
        this.setPreparedParameterValues(values);
    }

    private Object getParameterValue(HttpServletRequest req, HttpParameter param) throws NullPointerException {
        Object value;
        switch (param.getParameterType()) {
            case REQUEST_PARAMETER:
                value = this.getRequestParameterValue(req, param);
            case PATH_VARIABLE:
                value = this.getPathVariableValue(req, param);
            default:
                value = req;
        }
        if (value == null) {
            throw new NullPointerException("Parameter value is null");
        }
        return this.castParameterValue(value, param);
    }

    private Object getRequestParameterValue(HttpServletRequest req, HttpParameter param) {
        return req.getParameter(param.getParameterName());
    }

    private Object getPathVariableValue(HttpServletRequest req, HttpParameter param) {
        return null;
    }

    private Object castParameterValue(Object value, HttpParameter param) {
        switch (param.getParameter().getType().getSimpleName()) {
            case "HttpServletRequest":
                return value;
            case "Integer":
                return Integer.parseInt(value.toString());
            case "int":
                return Integer.parseInt(value.toString());
            case "Double":
                return Double.parseDouble(value.toString());
            case "double":
                return Double.parseDouble(value.toString());
            case "Float":
                return Float.parseFloat(value.toString());
            case "float":
                return Float.parseFloat(value.toString());
            case "Long":
                return Long.parseLong(value.toString());
            case "long":
                return Long.parseLong(value.toString());
            case "Boolean":
                return Boolean.parseBoolean(value.toString());
            case "boolean":
                return Boolean.parseBoolean(value.toString());
            default:
                return value.toString();
        }
    }

    public void process(HttpServletRequest req, HttpServletResponse resp, Map<String, Mapping> mappingUrl)
            throws IOException {
        PrintWriter out = resp.getWriter();
        this.prepareRequest(req, resp, mappingUrl);
        if (this.getMappingTarget() != null) {
            try {
                this.checkMethodValidity(req);
                this.prepareMethodParameters(req);
                Object target = Class.forName(this.getMappingTarget().getClassName()).getConstructor().newInstance();
                Object result = this.getMappingTarget().getMethod().invoke(target, this.getPreparedParameterValues());
                if (result instanceof ModelView modelView) {
                    String view = FrontServletConfig.VIEW_DIRECTORY.concat(modelView.getView());
                    Map<String, Object> data = modelView.getData();
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                        req.setAttribute(entry.getKey(), entry.getValue());
                    }
                    RequestDispatcher dispatcher = req.getRequestDispatcher(view);
                    dispatcher.forward(req, resp);
                }
            } catch (Exception e) {
                out.println(e);
            }
        } else {
            resp.sendError(404);
            return;
        }
    }
}
