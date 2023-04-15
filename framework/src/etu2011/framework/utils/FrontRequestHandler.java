package etu2011.framework.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Map;

import etu2011.framework.Mapping;
import etu2011.framework.config.FrontServletConfig;
import etu2011.framework.renderer.ModelView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontRequestHandler {

    private Mapping mappingTarget;
    private ArrayList<Object> preparedMethodParameters;

    /* SETTERS SECTION */
    private void setMappingTarget(Mapping mappingTarget) {
        this.mappingTarget = mappingTarget;
    }

    private void setPreparedMethodParameters(ArrayList<Object> params) {
        this.preparedMethodParameters = params;
    }

    /* GETTERS SECTION */
    public Mapping getMappingTarget() {
        return this.mappingTarget;
    }

    public ArrayList<Object> getPreparedMethod() {
        return this.preparedMethodParameters;
    }

    /* METHODS SECTION */
    private void prepareRequest(HttpServletRequest req, HttpServletResponse resp, Map<String, Mapping> mappingUrl) {
        String url = req.getRequestURL().toString().split("://")[1];
        String context = url.substring(url.indexOf("/")).replace(req.getContextPath(), "");
        this.setMappingTarget(mappingUrl.get(context));
    }

    private void prepareMethodParameters(HttpServletRequest req) {
        Method method = this.getMappingTarget().getMethod();
        boolean hasRequestParameter = false;
        for (Parameter parameter : method.getParameters()) {
            if (parameter.getType().equals(HttpServletRequest.class)) {
                hasRequestParameter = true;
                break;
            }
        }
    }

    private Object getParameterValue(HttpServletRequest req, HttpParameter param) {
        switch (param.getParameterType()) {
            case REQUEST_PARAMETER:
                return this.getRequestParameterValue(req, param);
            case PATH_VARIABLE:
                return null;
            default:
                return req;
        }
    }

    private Object getRequestParameterValue(HttpServletRequest req, HttpParameter param) {
        return req.getParameter(param.getParameterName());
    }

    private Object castParameterValue(Object value, HttpParameter param) {
        switch (param.getParameter().getType().getSimpleName()) {
            case "int":
                return Integer.parseInt(value.toString());
            case "double":
                return null;
            default:
                return value;
        }
    }

    private void process(HttpServletRequest req, HttpServletResponse resp, Map<String, Mapping> mappingUrl)
            throws IOException {
        PrintWriter out = resp.getWriter();

        this.prepareRequest(req, resp, mappingUrl);

        if (this.getMappingTarget() != null) {
            try {
                Object target = Class.forName(this.getMappingTarget().getClassName()).getConstructor().newInstance();
                Method method = this.getMappingTarget().getMethod();
                boolean hasRequestParameter = false;
                for (Parameter parameter : method.getParameters()) {
                    if (parameter.getType().equals(HttpServletRequest.class)) {
                        hasRequestParameter = true;
                        break;
                    }
                }
                Object result = hasRequestParameter ? method.invoke(target, req) : method.invoke(target);
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
