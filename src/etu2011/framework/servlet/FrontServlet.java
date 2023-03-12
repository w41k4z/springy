package etu2011.framework.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import java.lang.reflect.Method;

import etu2011.framework.Mapping;
import etu2011.framework.annotation.method.GetMapping;
import etu2011.framework.annotation.method.PostMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;

public class FrontServlet extends HttpServlet {

    private HashMap<String, Mapping> mappingUrls;

    @Override
    public void init() throws ServletException {
        mappingUrls = new HashMap<String, Mapping>();
        mappingUrls.put("/hello", new Mapping("etu2011.framework.servlet.HelloServlet", "hello"));
        mappingUrls.put("/hello2", new Mapping("etu2011.framework.servlet.HelloServlet", "hello2"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processingRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processingRequest(req, resp);
    }

    private void processingRequest(HttpServletRequest req, HttpServletResponse resp) {

    }

    private Method getGetMappingMethod(String urlMapping) {
        for (Method method : this.getClass().getMethods()) {
            if (method.isAnnotationPresent(GetMapping.class)) {
                if (method.getAnnotation(GetMapping.class).url().equals(urlMapping))
                    return method;
            }
        }
        return null;
    }

    private Method getPostMappingMethod(String urlMapping) {
        for (Method method : this.getClass().getMethods()) {
            if (method.isAnnotationPresent(PostMapping.class)) {
                if (method.getAnnotation(PostMapping.class).url().equals(urlMapping))
                    return method;
            }
        }
        return null;
    }
}
