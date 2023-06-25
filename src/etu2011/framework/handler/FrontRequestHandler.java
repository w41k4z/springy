package etu2011.framework.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import etu2011.framework.annotations.Auth;
import etu2011.framework.annotations.HttpParam;
import etu2011.framework.annotations.ModelController;
import etu2011.framework.annotations.RestAPI;
import etu2011.framework.annotations.Sessions;
import etu2011.framework.annotations.UrlMapping;
import etu2011.framework.config.FrontServletConfig;
import etu2011.framework.renderer.ModelView;
import etu2011.framework.utils.HttpParameter;
import etu2011.framework.utils.Mapping;
import etu2011.framework.utils.UploadedFile;
import etu2011.framework.utils.helpers.DateHelpers;
import etu2011.framework.utils.javaObject.JavaClass;
import etu2011.framework.utils.map.UrlPatternKey;
import etu2011.framework.utils.map.UrlRegexHashMap;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

public class FrontRequestHandler {
    /* FIELDS SECTION */
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

    /***
     * @param req        the request object
     * @param resp       the response object
     * @param mappingUrl the mapping url from the front servlet
     * @param singletons the singletons map from the front servlet
     * @param config     the servlet config
     * @throws Exception
     */
    public void process(HttpServletRequest req, HttpServletResponse resp,
            UrlRegexHashMap<UrlPatternKey, Mapping> mappingUrl, HashMap<Class<?>, Object> singletons,
            ServletConfig config)
            throws Exception {

        this.prepareRequest(req, resp, mappingUrl);
        if (this.getMappingTarget() != null) {
            // checking http method validity
            this.checkMethodValidity(req);

            Object target = null;
            if (singletons.get(Class.forName(this.getMappingTarget().getClassName())) == null) {
                target = Class.forName(this.getMappingTarget().getClassName()).getConstructor().newInstance();
            } else {
                target = singletons.get(Class.forName(this.getMappingTarget().getClassName()));
                this.resetModel(target);
            }
            // setting model fields
            this.initModel(req, target);

            // getting all the parameters needed by the invoked method
            this.prepareMethodParameters(req, target);

            // checking user authentication
            this.checkMethod(req, config);

            // getting method return value
            Method method = this.getMappingTarget().getMethod();
            Object result = method.invoke(target,
                    this.getPreparedParameterValues().toArray(new Object[this
                            .getPreparedParameterValues().size()]));
            if (method.isAnnotationPresent(RestAPI.class)) {
                PrintWriter out = resp.getWriter();
                String jsonFormat = new Gson().toJson(result);
                out.println(jsonFormat);
            } else if (result instanceof ModelView) {
                String view = FrontServletConfig.VIEW_DIRECTORY.concat(((ModelView) result).getView());
                // model view data
                Map<String, Object> data = ((ModelView) result).getData();
                if (((ModelView) result).dataIsJson()) {
                    String jsonFormat = new Gson().toJson(data);
                    req.setAttribute("result", jsonFormat);
                } else {
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                        req.setAttribute(entry.getKey(), entry.getValue());
                    }
                }
                // model sessions
                Map<String, Object> sessions = ((ModelView) result).getSessions();
                for (Map.Entry<String, Object> entry : sessions.entrySet()) {
                    req.getSession().setAttribute(entry.getKey(), entry.getValue());
                }
                RequestDispatcher dispatcher = req.getRequestDispatcher("/" + view); // to make the path absolute
                dispatcher.forward(req, resp);
            } else {
                resp.sendError(404);
                return;
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

    private void checkMethod(HttpServletRequest req, ServletConfig config) {
        Method method = this.getMappingTarget().getMethod();
        if (method.isAnnotationPresent(Auth.class)) {
            String sessionName = config.getInitParameter("sessionName");
            if (req.getSession().getAttribute(sessionName) != null) {
                String profileName = config.getInitParameter("sessionProfile");
                String profile = method.getAnnotation(Auth.class).value();
                if (profile.length() > 0) {
                    if (!req.getSession().getAttribute(profileName).equals(profile)) {
                        throw new RuntimeException("You are not allowed to access this page. '" + profile + "s' only");
                    }
                }
            } else {
                throw new RuntimeException("You are not allowed to access this page.");
            }
        }
    }

    private void resetModel(Object target) throws Exception {
        for (Field field : target.getClass().getDeclaredFields()) {
            JavaClass.setObjectFieldValue(target, null, field);
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
            Object parameter = parameterName.contains("[]") ? req.getParameterValues(parameterName)
                    : req.getParameter(parameterName);
            JavaClass.setObjectFieldValue(target, parameter, field);
        }
        // extra field (eg: file)
        String contentType = req.getHeader("Content-Type");
        if (contentType != null
                && (contentType.contains("multipart/form-data") ||
                        contentType.contains("multipart/mixed"))) {
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
        // sessions
        for (Field each : target.getClass().getDeclaredFields()) {
            if (each.isAnnotationPresent(Sessions.class) && each.getType().equals(Map.class)) {
                Enumeration<String> sessions = req.getSession().getAttributeNames();
                Map<String, Object> sessionsMap = new HashMap<String, Object>();
                while (sessions.hasMoreElements()) {
                    String sessionName = sessions.nextElement();
                    sessionsMap.put(sessionName, req.getSession().getAttribute(sessionName));
                }
                JavaClass.setObjectFieldValue(target, sessionsMap, each);
            }
        }
    }

    private void prepareMethodParameters(HttpServletRequest req, Object target) throws Exception {
        ArrayList<Object> values = new ArrayList<Object>();
        Method method = this.getMappingTarget().getMethod();

        // creating a HttpParameter object up here
        // for memory efficiency
        HttpParameter param = new HttpParameter();
        for (Parameter parameter : method.getParameters()) {
            // wrapping the Parameter inside a HttpParameter
            // for the method getParameterValues()
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

    // fetching the method parameter values from the request
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
        }
        return this.castParameterValues(values, param);
    }

    // foe request parameters like `?id=1`
    private Object[] getRequestParameterValues(HttpServletRequest req, HttpParameter param)
            throws IOException, ServletException {
        // The file object is already casted here
        // as it is handle by the UploadedFile class
        Object[] values = null;
        if (param.getParameter().getType().equals(UploadedFile.class)) {
            String contentType = req.getHeader("Content-Type");
            if (contentType != null
                    && (contentType.contains("multipart/form-data") ||
                            contentType.contains("multipart/mixed"))) {
                values = new Object[] { new UploadedFile(req.getPart(param.getParameterName())) };
            }
        } else {
            values = param.getParameter().getType().isArray()
                    ? req.getParameterValues(param.getParameterName().concat("[]"))
                    : new String[] { req.getParameter(param.getParameterName()) };
        }
        return values;
    }

    // for path variable parameters like `/{id}`
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

    // casting the parameter values to the parameter type
    private Object[] castParameterValues(Object[] values, HttpParameter param)
            throws Exception {
        if (values != null) {
            Object[] castedValue = new Object[values.length];
            String paramType = param.getParameter().getType().getSimpleName();
            switch (paramType) {
                case "Date":
                    String[] dateFormats = DateHelpers.getSupportedDatePatterns(param.getParameter());
                    for (int i = 0; i < values.length; i++) {
                        for (int j = 0; j < dateFormats.length; j++) {
                            try {
                                castedValue[i] = DateHelpers.convertToSqlDate(values[i].toString().trim(),
                                        dateFormats[j]);
                                break;
                            } catch (ParseException e) {
                                if (j == dateFormats.length - 1) {
                                    throw new Exception("The date format is not supported");
                                }
                            }
                        }
                    }
                    break;
                default:
                    for (int i = 0; i < values.length; i++) {
                        castedValue[i] = param.getParameter().getType().getConstructor(String.class)
                                .newInstance(values[i]);
                    }
                    break;
            }
        }
        return values;
    }
}
