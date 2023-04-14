package etu2011.framework.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

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
import java.util.Map;

public class FrontServlet extends HttpServlet {

    private Map<String, Mapping> mappingUrls;
    private String viewsDirectory;

    /* SETTERS SECTION */
    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        this.mappingUrls = mappingUrls;
    }

    private void setViewsDirectory(String viewsDirectory) {
        this.viewsDirectory = viewsDirectory;
    }

    /* GETTERS SECTION */
    private Map<String, Mapping> getMappingUrls() {
        return this.mappingUrls;
    }

    private String getViewsDirectory() {
        return this.viewsDirectory;
    }

    /* SERVLET INIT SECTION */
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

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            this.setMappingUrls(new HashMap<String, Mapping>());
            this.setViewsDirectory(config.getInitParameter("views-directory"));
            String rootPath = config.getServletContext().getRealPath(this.getServletInfo()) + "WEB-INF/classes/";
            File[] files = Executor.getSubFiles(new File(rootPath.concat("models/")));
            this.findAllMappedMethod(rootPath, files);
        } catch (ServletException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* REQUESTS HANDLER */
    private void processingRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        String url = req.getRequestURL().toString().split("://")[1];
        String context = url.substring(url.indexOf("/")).replace(req.getContextPath(), "");
        Mapping mapping = this.getMappingUrls().get(context);
        if (mapping != null) {
            try {
                Object target = Class.forName(mapping.getClassName()).getConstructor().newInstance();
                Method method = target.getClass().getDeclaredMethod(mapping.getMethod());
                Object result = method.invoke(target);
                if (result instanceof ModelView modelView) {
                    String view = this.getViewsDirectory().concat(modelView.getView());
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processingRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processingRequest(req, resp);
    }
}
