package etu2011.framework.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

import etu2011.framework.Mapping;
import etu2011.framework.annotations.HttpParam;
import etu2011.framework.annotations.ModelController;
import etu2011.framework.annotations.UrlMapping;
import etu2011.framework.config.FrontServletConfig;
import etu2011.framework.renderer.ModelView;
import etu2011.framework.utils.javaObject.JavaClass;
import etu2011.framework.utils.map.UrlPatternKey;
import etu2011.framework.utils.map.UrlRegexHashMap;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontRequestHandler {

    private Mapping mappingTarget;
    private String context;
    private ArrayList<Object> preparedParameterValues;

    /* SETTERS SECTION */
    private void setMappingTarget(Mapping mappingTarget) {
        this.mappingTarget = mappingTarget;
    }

    private void setContext(String context) {
        this.context = context;
    }

    private void setPreparedParameterValues(ArrayList<Object> paramsValue) {
        this.preparedParameterValues = paramsValue;
    }

    /* GETTERS SECTION */
    public Mapping getMappingTarget() {
        return this.mappingTarget;
    }

    public String getContext() {
        return this.context;
    }

    public ArrayList<Object> getPreparedParameterValues() {
        return this.preparedParameterValues;
    }

    /* METHODS SECTION */
    public void process(HttpServletRequest req, HttpServletResponse resp,
            UrlRegexHashMap<UrlPatternKey, Mapping> mappingUrl)
            throws Exception {
        this.prepareRequest(req, resp, mappingUrl);
        if (this.getMappingTarget() != null) {
            this.checkMethodValidity(req);
            Object target = Class.forName(this.getMappingTarget().getClassName()).getConstructor().newInstance();

            this.initModel(req, target);

            this.prepareMethodParameters(req, target);
            Object result = this.getMappingTarget().getMethod().invoke(target,
                    this.getPreparedParameterValues().toArray(new Object[this
                            .getPreparedParameterValues().size()]));
            if (result instanceof ModelView modelView) {
                String view = FrontServletConfig.VIEW_DIRECTORY.concat(modelView.getView());
                Map<String, Object> data = modelView.getData();
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    req.setAttribute(entry.getKey(), entry.getValue());
                }
                RequestDispatcher dispatcher = req.getRequestDispatcher("/" + view); // to make the path relative
                dispatcher.forward(req, resp);
            }
        } else {
            resp.sendError(404);
            return;
        }
    }

    private void prepareRequest(HttpServletRequest req, HttpServletResponse resp,
            UrlRegexHashMap<UrlPatternKey, Mapping> mappingUrl) throws Exception {
        String url = req.getRequestURL().toString().split("://")[1];
        String context = url.substring(url.indexOf("/")).replace(req.getContextPath(), "");
        this.setContext(context);
        this.setMappingTarget(mappingUrl.get(UrlPatternKey.URL(context)));
    }

    private void checkMethodValidity(HttpServletRequest req) throws Exception {
        String mappingMethodType = this.getMappingTarget().getMethod().getAnnotation(UrlMapping.class).method()
                .toString();
        if (!req.getMethod().toUpperCase().equals(mappingMethodType.toUpperCase())) {
            throw new Exception("This url is not allowed for " + req.getMethod() + " method");
        }
    }

    private void initModel(HttpServletRequest req, Object target) throws Exception {
        Enumeration<String> allParametersName = req.getParameterNames();
        String parameterName;
        Field field;
        while (allParametersName.hasMoreElements()) {
            parameterName = allParametersName.nextElement();
            try {
                field = target.getClass().getDeclaredField(parameterName);
            } catch (NoSuchFieldException e) {
                continue;
            }
            JavaClass.setObjectFieldValue(target, req.getParameter(parameterName), field);
        }
    }

    private void prepareMethodParameters(HttpServletRequest req, Object target) throws Exception {
        ArrayList<Object> values = new ArrayList<Object>();
        Method method = this.getMappingTarget().getMethod();
        HttpParameter param = new HttpParameter();
        for (Parameter parameter : method.getParameters()) {
            param.setParameter(parameter);
            values.add(this.getParameterValue(req, param, target));
        }
        this.setPreparedParameterValues(values);
    }

    private Object getParameterValue(HttpServletRequest req, HttpParameter param, Object target) throws Exception {
        Object value = null;
        if (param.getParameter().isAnnotationPresent(HttpParam.class)) {
            switch (param.getParameterType()) {
                case REQUEST_PARAMETER:
                    value = this.getRequestParameterValue(req, param);
                    break;
                case PATH_VARIABLE:
                    value = this.getPathVariableValue(param, target);
                    break;
            }
        } else {
            throw new Exception("ModelController request handler method parameters must be annotated with @HttpParam");
        }
        if (value == null) {
            throw new Exception("Parameter " + param.getParameterName() + " value is null");
        }
        return this.castParameterValue(value, param);
    }

    private Object getRequestParameterValue(HttpServletRequest req, HttpParameter param) {
        return req.getParameter(param.getParameterName());
    }

    private Object getPathVariableValue(HttpParameter param, Object target) throws Exception {
        String url = target.getClass()
                .getAnnotation(ModelController.class).route()
                .concat(this.getMappingTarget().getMethod().getAnnotation(UrlMapping.class).url());
        String[] urlParts = url.split("/");
        String[] contexParts = this.getContext().split("/");
        for (int i = 0; i < contexParts.length; i++) {
            if (urlParts[i].equals("{" + param.getParameterName() + "}")) {
                return contexParts[i];
            }
        }
        return null;
    }

    private Object castParameterValue(Object value, HttpParameter param) throws ParseException {
        switch (param.getParameter().getType().getSimpleName()) {
            case "Date":
                String dateFormat = DateHelpers.getDatePattern(param.getParameter());
                return DateHelpers.convertToSqlDate(value.toString(), dateFormat);
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
}
