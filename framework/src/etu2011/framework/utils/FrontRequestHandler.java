package etu2011.framework.utils;

import java.lang.reflect.Field;

import java.util.Enumeration;
import java.util.Map;

import etu2011.framework.Mapping;
import etu2011.framework.annotations.UrlMapping;
import etu2011.framework.config.FrontServletConfig;
import etu2011.framework.renderer.ModelView;
import etu2011.framework.utils.javaObject.JavaClass;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontRequestHandler {

    private Mapping mappingTarget;
    private String context;

    /* SETTERS SECTION */
    private void setMappingTarget(Mapping mappingTarget) {
        this.mappingTarget = mappingTarget;
    }

    private void setContext(String context) {
        this.context = context;
    }

    /* GETTERS SECTION */
    public Mapping getMappingTarget() {
        return this.mappingTarget;
    }

    public String getContext() {
        return this.context;
    }

    /* METHODS SECTION */
    public void process(HttpServletRequest req, HttpServletResponse resp,
            Map<String, Mapping> mappingUrl)
            throws Exception {
        this.prepareRequest(req, resp, mappingUrl);
        if (this.getMappingTarget() != null) {
            this.checkMethodValidity(req);
            Object target = Class.forName(this.getMappingTarget().getClassName()).getConstructor().newInstance();

            this.initModel(req, target);

            Object result = this.getMappingTarget().getMethod().invoke(target);
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
            Map<String, Mapping> mappingUrl) throws Exception {
        String url = req.getRequestURL().toString().split("://")[1];
        String context = url.substring(url.indexOf("/")).replace(req.getContextPath(), "");
        this.setContext(context);
        this.setMappingTarget(mappingUrl.get(context));
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
}
