package etu2011.framework.config;

import java.io.File;

import java.lang.reflect.Method;

import etu2011.framework.Mapping;
import etu2011.framework.annotations.ModelController;
import etu2011.framework.annotations.UrlMapping;
import etu2011.framework.exceptions.JavaFileException;
import etu2011.framework.utils.javaObject.JavaClass;
import etu2011.framework.utils.javaObject.JavaFile;
import etu2011.framework.utils.map.UrlPatternKey;
import etu2011.framework.utils.map.UrlRegexHashMap;

public class FrontServletConfig {

    public static final String VIEW_DIRECTORY = "views/";
    public static final String MODEL_DIRECTORY = "models/";

    /* METHOD SECTION */
    public static UrlRegexHashMap<UrlPatternKey, Mapping> getAllMappedMethod(String rootPath, File[] fileTree)
            throws Exception {
        // Declaring variables up here
        // and injecting their dependencies in the loop using setters
        // for better performance

        JavaFile javaFile = new JavaFile();
        JavaClass javaClass = new JavaClass();
        Mapping mapping = null;
        UrlRegexHashMap<UrlPatternKey, Mapping> mappedMethod = new UrlRegexHashMap<UrlPatternKey, Mapping>();

        for (File file : fileTree) {
            try {
                javaFile.setJavaFile(file);
                javaClass.setJavaClass(javaFile.getClassObject(rootPath));

                // all classes in the model directory must be annotated with @ModelController
                if (!javaClass.getJavaClass().isAnnotationPresent(ModelController.class)) {
                    throw new Exception("The class " + javaClass.getJavaClass().getName()
                            + " is not annotated with @ModelController");
                }

                for (Method method : javaClass.getMethodByAnnotation(UrlMapping.class)) {
                    mapping = new Mapping(javaClass.getJavaClass().getName(), method);
                    UrlPatternKey urlPatternKey = new UrlPatternKey(
                            javaClass.getJavaClass().getAnnotation(ModelController.class).route()
                                    .concat(method.getAnnotation(UrlMapping.class).url()));
                    mappedMethod.put(urlPatternKey, mapping);
                }
            } catch (JavaFileException e) {
                continue;
            }
        }

        return mappedMethod;
    }
}