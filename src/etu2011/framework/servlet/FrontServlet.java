package etu2011.framework.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import java.lang.reflect.Method;

import annotation.method.GetMapping;
import annotation.method.PostMapping;
import etu2011.framework.Mapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;

public class FrontServlet extends HttpServlet {

    private HashMap<String, Mapping> mappingUrls;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println(req.getContextPath());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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
