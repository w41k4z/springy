package etu2011.framework.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
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
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

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
            UrlRegexHashMap<UrlPatternKey, Mapping> mappingUrl, HashMap<String, Object> singletons)
            throws Exception {
        this.prepareRequest(req, resp, mappingUrl);
        if (this.getMappingTarget() != null) {
            this.checkMethodValidity(req);

            Object target = singletons.get(this.getMappingTarget().getClassName()) == null
                    ? Class.forName(this.getMappingTarget().getClassName()).getConstructor().newInstance()
                    : singletons.get(this.getMappingTarget().getClassName());

            this.initModel(req, target);

            this.prepareMethodParameters(req, target);
            Object result = this.getMappingTarget().getMethod().invoke(target,
                    this.getPreparedParameterValues().toArray(new Object[this
                            .getPreparedParameterValues().size()]));
            if (result instanceof ModelView) {
                String view = FrontServletConfig.VIEW_DIRECTORY.concat(((ModelView) result).getView());
                Map<String, Object> data = ((ModelView) result).getData();
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
        // basic field
        while (allParametersName.hasMoreElements()) {
            parameterName = allParametersName.nextElement();
            try {
                field = target.getClass().getDeclaredField(parameterName);
            } catch (NoSuchFieldException e) {
                continue;
            }
            Object parameter = field.getType().equals(UploadedFile.class)
                    ? new UploadedFile(req.getPart(field.getName()))
                    : parameterName.contains("[]") ? req.getParameterValues(parameterName)
                            : req.getParameter(parameterName);
            JavaClass.setObjectFieldValue(target, parameter, field);
        }
        // extra field (eg: file)
        for (Part each : req.getParts()) {
            try {
                field = target.getClass().getDeclaredField(each.getName());
            } catch (NoSuchFieldException e) {
                continue;
            }
            if (field.getType().equals(UploadedFile.class)) {
                JavaClass.setObjectFieldValue(target, new UploadedFile(each), field);
            }
        }
    }

    private void prepareMethodParameters(HttpServletRequest req, Object target) throws Exception {
        ArrayList<Object> values = new ArrayList<Object>();
        Method method = this.getMappingTarget().getMethod();
        HttpParameter param = new HttpParameter();
        for (Parameter parameter : method.getParameters()) {
            param.setParameter(parameter);
            Object[] paramValues = this.getParameterValues(req, param, target);
            if (paramValues != null) {
                values.add(parameter.getType().isArray() ? paramValues : paramValues[0]);
            } else {
                values.add(paramValues);
            }
        }
        this.setPreparedParameterValues(values);
    }

    private Object[] getParameterValues(HttpServletRequest req, HttpParameter param, Object target) throws Exception {
        Object[] values = null;
        if (param.getParameter().isAnnotationPresent(HttpParam.class)) {
            switch (param.getParameterType()) {
                case REQUEST_PARAMETER:
                    values = this.getRequestParameterValues(req, param);
                    break;
                case PATH_VARIABLE:
                    // The castParameterValues need an array of values
                    // Just wrapping the value in a one length array
                    String[] pathVariable = this.getPathVariableValue(param, target) == null ? null
                            : new String[] { this
                                    .getPathVariableValue(param, target) };
                    values = pathVariable;
                    break;
            }
        } else {
            throw new Exception("ModelController request handler method parameters must be annotated with @HttpParam");
        }
        return this.castParameterValues(values, param);
    }

    private Object[] getRequestParameterValues(HttpServletRequest req, HttpParameter param)
            throws IOException, ServletException {
        // The file object is already casted here
        // as it is handle by the UploadedFile class
        return param.getParameter().getType().equals(UploadedFile.class)
                ? new Object[] { new UploadedFile(req.getPart(param.getParameterName())) }
                : param.getParameter().getType().isArray()
                        ? req.getParameterValues(param.getParameterName().concat("[]"))
                        : new String[] { req.getParameter(param.getParameterName()) };
    }

    private String getPathVariableValue(HttpParameter param, Object target) throws Exception {
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

    private Object[] castParameterValues(Object[] values, HttpParameter param) throws ParseException {
        if (values != null) {
            Object[] castedValue = new Object[values.length];
            String paramType = param.getParameter().getType().getSimpleName();
            if (paramType.equals("Date") || paramType.equals("Date[]")) {
                String dateFormat = DateHelpers.getDatePattern(param.getParameter());
                for (int i = 0; i < values.length; i++) {
                    castedValue[i] = DateHelpers.convertToSqlDate(values[i].toString(), dateFormat);
                }
            } else if (paramType.equals("Integer") || paramType.equals("Integer[]")) {
                for (int i = 0; i < values.length; i++) {
                    castedValue[i++] = Integer.parseInt(values[i].toString());
                }
            } else if (paramType.equals("Double") || paramType.equals("Double[]")) {
                for (int i = 0; i < values.length; i++) {
                    castedValue[i++] = Double.parseDouble(values[i].toString());
                }
            } else if (paramType.equals("Float") || paramType.equals("Float[]")) {
                for (int i = 0; i < values.length; i++) {
                    castedValue[i++] = Float.parseFloat(values[i].toString());
                }
            } else if (paramType.equals("Long") || paramType.equals("Long[]")) {
                for (int i = 0; i < values.length; i++) {
                    castedValue[i++] = Long.parseLong(values[i].toString());
                }
            } else if (paramType.equals("Boolean") || paramType.equals("Boolean[]")) {
                for (int i = 0; i < values.length; i++) {
                    castedValue[i++] = Boolean.parseBoolean(values[i].toString());
                }
            } else {
                castedValue = values;
            }

        }
        return values;
    }
}
