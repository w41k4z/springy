package etu2011.framework.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import etu2011.framework.Mapping;
import etu2011.framework.annotations.UrlMapping;
import etu2011.framework.config.FrontServletConfig;
import etu2011.framework.exceptions.JavaFileException;
import etu2011.framework.renderer.ModelView;
import etu2011.framework.utils.javaObject.JavaClass;
import etu2011.framework.utils.javaObject.JavaFile;
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

    /* SETTERS SECTION */
    private void setMappingUrls(Map<String, Mapping> mappingUrls) {
        this.mappingUrls = mappingUrls;
    }

    /* GETTERS SECTION */
    private Map<String, Mapping> getMappingUrls() {
        return this.mappingUrls;
    }

    /* SERVLET INIT SECTION */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            this.setMappingUrls(new HashMap<String, Mapping>());
            String rootPath = config.getServletContext().getRealPath(this.getServletInfo()).concat("WEB-INF/classes/");
            File[] files = Executor
                    .getSubFiles(new File(rootPath.concat(FrontServletConfig.MODEL_DIRECTORY)));
            this.setMappingUrls(FrontServletConfig.getAllMappedMethod(rootPath, files));
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
}
