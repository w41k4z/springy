package etu2011.framework.config;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import etu2011.framework.Mapping;
import etu2011.framework.annotations.ModelController;
import etu2011.framework.annotations.UrlMapping;
import etu2011.framework.exceptions.JavaFileException;
import etu2011.framework.utils.javaObject.JavaClass;
import etu2011.framework.utils.javaObject.JavaFile;

public class FrontServletConfig {

    public static final String VIEW_DIRECTORY = "views/";
    public static final String MODEL_DIRECTORY = "models/";

    /* METHOD SECTION */
    public static Map<String, Mapping> getAllMappedMethod(String rootPath, File[] fileTree) {
        // Declaring variables up here
        // and injecting their dependencies in the loop using setters
        // for better performance
        JavaFile javaFile = new JavaFile();
        JavaClass javaClass = new JavaClass();
        Mapping mapping = null;
        Map<String, Mapping> mappedMethod = new HashMap<String, Mapping>();

        for (File file : fileTree) {
            try {
                javaFile.setJavaFile(file);
                javaClass.setJavaClass(javaFile.getClassObject(rootPath));

                if (javaClass.getJavaClass().isAnnotationPresent(ModelController.class) == false) {
                    for (Method method : javaClass.getMethodByAnnotation(UrlMapping.class)) {
                        mapping = new Mapping(javaClass.getJavaClass().getName(), method);
                        mappedMethod.put(method.getAnnotation(UrlMapping.class).url(), mapping);
                    }
                }

            } catch (JavaFileException e) {
                continue;
            }
        }

        return mappedMethod;
    }
}
