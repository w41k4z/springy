package etu2011.framework.servlet;

import java.io.File;
import java.io.IOException;

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

    /* SETTERS SECTION */
    private void setRequestHandler(FrontRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    private void setMappingUrls(UrlRegexHashMap<UrlPatternKey, Mapping> mappingUrls) {
        this.mappingUrls = mappingUrls;
    }

    /* GETTERS SECTION */
    private FrontRequestHandler getRequestHandler() {
        return this.requestHandler;
    }

    private UrlRegexHashMap<UrlPatternKey, Mapping> getMappingUrls() {
        return this.mappingUrls;
    }

    /* SERVLET INIT SECTION */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            this.setRequestHandler(new FrontRequestHandler());
            String rootPath = config.getServletContext().getRealPath(this.getServletInfo())
                    .concat("WEB-INF/classes/");
            File[] files = Executor
                    .getSubFiles(new File(rootPath.concat(FrontServletConfig.MODEL_DIRECTORY)));
            this.setMappingUrls(FrontServletConfig.getAllMappedMethod(rootPath, files));
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    /* METHODS SECTION */
    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        this.getRequestHandler().process(req, resp, this.getMappingUrls());
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
