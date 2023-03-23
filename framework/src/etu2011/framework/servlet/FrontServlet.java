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
import fileActivity.Executor;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

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
            String rootPath = config.getServletContext().getRealPath(this.getServletInfo()) + "/WEB-INF/classes";
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
        out.println();
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
