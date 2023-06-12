package etu2011.framework.servlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import etu2011.framework.Mapping;
import etu2011.framework.config.FrontServletConfig;
import etu2011.framework.utils.FrontRequestHandler;
import etu2011.framework.utils.map.UrlPatternKey;
import etu2011.framework.utils.map.UrlRegexHashMap;

import fileActivity.Executor;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@MultipartConfig
public class FrontServlet extends HttpServlet {

    private FrontRequestHandler requestHandler;
    private UrlRegexHashMap<UrlPatternKey, Mapping> mappingUrls;
    private HashMap<Class<?>, Object> singletons;

    /* SETTERS SECTION */
    private void setRequestHandler(FrontRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    private void setMappingUrls(UrlRegexHashMap<UrlPatternKey, Mapping> mappingUrls) {
        this.mappingUrls = mappingUrls;
    }

    private void setSingletons(HashMap<Class<?>, Object> singletons) {
        this.singletons = singletons;
    }

    /* GETTERS SECTION */
    private FrontRequestHandler getRequestHandler() {
        return this.requestHandler;
    }

    private UrlRegexHashMap<UrlPatternKey, Mapping> getMappingUrls() {
        return this.mappingUrls;
    }

    private HashMap<Class<?>, Object> getSingletons() {
        return this.singletons;
    }

    /* SERVLET INIT SECTION */
    @SuppressWarnings("unchecked")
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            this.setRequestHandler(new FrontRequestHandler());
            String rootPath = config.getServletContext().getRealPath(this.getServletInfo())
                    .concat("WEB-INF/classes/");
            File[] files = Executor
                    .getSubFiles(new File(rootPath.concat(FrontServletConfig.MODEL_DIRECTORY)));
            Object[] configurations = FrontServletConfig.getConfigurations(rootPath, files);
            this.setMappingUrls((UrlRegexHashMap<UrlPatternKey, Mapping>) configurations[0]);
            this.setSingletons((HashMap<Class<?>, Object>) configurations[1]);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    /* METHODS SECTION */
    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        this.getRequestHandler().process(req, resp, this.getMappingUrls(), this.getSingletons());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            this.processRequest(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            this.processRequest(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
