package etu2011.framework.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import etu2011.framework.Mapping;
import etu2011.framework.annotation.Url;
import etu2011.framework.exception.JavaFileException;
import etu2011.framework.javaObject.JavaClass;
import etu2011.framework.javaObject.JavaFile;
import etu2011.framework.renderer.ModelView;

import fileActivity.Executor;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;

public class FrontServlet extends HttpServlet {

    private HashMap<String, Mapping> mappingUrls;

    // setter
    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        this.mappingUrls = mappingUrls;
    }

    // getter
    public HashMap<String, Mapping> getMappingUrls() {
        return this.mappingUrls;
    }

    // methods
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            this.setMappingUrls(new HashMap<String, Mapping>());
            String rootPath = config.getServletContext().getRealPath(this.getServletInfo()) + "WEB-INF/classes/";
            File root = new File(rootPath);
            File[] fileTree = this.scanProject(root);
            this.findAllMappedMethod(rootPath, fileTree);
        } catch (ServletException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processingRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processingRequest(req, resp);
    }

    private void processingRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        String url = req.getRequestURL().toString().split("://")[1];
        String context = url.substring(url.indexOf("/")).replace(req.getContextPath(), "");
        Mapping mapping = this.getMappingUrls().get(context);
        out.print(context);
        if (mapping != null) {
            try {
                Object target = Class.forName(mapping.getClassName()).getConstructor().newInstance();
                Method method = target.getClass().getDeclaredMethod(mapping.getMethod());
                Object result = method.invoke(target);
                if (result instanceof ModelView modelView) {
                    String view = modelView.getView();
                    RequestDispatcher dispatcher = req.getRequestDispatcher(view);
                    dispatcher.forward(req, resp);
                }
            } catch (NoSuchMethodException e) {
                out.println(e);
            } catch (SecurityException e) {
                out.println(e);
            } catch (ClassNotFoundException e) {
                out.println(e);
            } catch (IllegalAccessException e) {
                out.println(e);
            } catch (InvocationTargetException e) {
                out.println(e);
            } catch (InstantiationException e) {
                out.println(e);
            } catch (IllegalArgumentException e) {
                out.println(e);
            } catch (ServletException e) {
                out.println(e);
            }
        } /*
           * else {
           * resp.sendError(404);
           * return;
           * }
           */
    }

    private File[] scanProject(File root) throws Exception {
        return Executor.getFileTree(root);
    }

    private void findAllMappedMethod(String rootPath, File[] fileTree) {
        JavaFile javaFile = new JavaFile();
        JavaClass javaClass = new JavaClass();
        Mapping mapping = null;
        for (File file : fileTree) {
            try {
                javaFile.setJavaFile(file);
                javaClass.setJavaClass(javaFile.getClassObject(rootPath));

                for (Method method : javaClass.getMethodByAnnotation(Url.class)) {
                    mapping = new Mapping(javaClass.getJavaClass().getName(), method.getName());
                    this.getMappingUrls().put(method.getAnnotation(Url.class).value(), mapping);
                }
            } catch (JavaFileException e) {
                continue;
            }
        }
    }
}
