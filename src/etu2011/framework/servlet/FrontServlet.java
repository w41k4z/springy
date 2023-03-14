package etu2011.framework.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import etu2011.framework.Mapping;
import etu2011.framework.annotation.Url;
import etu2011.framework.exception.JavaFileException;
import etu2011.framework.java.JavaClass;
import etu2011.framework.java.JavaFile;
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
    public void init() throws ServletException {
        this.scanProject();
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
        for (Map.Entry<String, Mapping> entry : this.getMappingUrls().entrySet()) {
            out.println("For : \"" + entry.getKey());
            out.println("\tClass:" + entry.getValue().getClassName());
            out.println("\tMethod:" + entry.getValue().getMethod());
            out.println("\n\n\n");
        }
    }

    private void scanProject() {
        File root = new File("src");
        this.findAllMappedMethod(root);
    }

    private void findAllMappedMethod(File root) {
        JavaFile javaFile = new JavaFile();
        JavaClass javaClass = new JavaClass();
        for (File subfile : root.listFiles()) {
            try {
                javaFile.setJavaFile(subfile);
                javaClass.setJavaClass(javaFile.getClassObject());
                Mapping mapping = null;

                for (Method method : javaClass.getMethodByAnnotation(Url.class)) {
                    mapping = new Mapping(javaClass.getClass().getName(), method.getName());
                    this.getMappingUrls().put(method.getAnnotation(Url.class).value(), mapping);
                }
            } catch (JavaFileException e) {
                continue;
            }
        }
    }
}
