package etu2011.framework.config;

import java.io.File;

import java.lang.reflect.Method;
import java.util.HashMap;

import etu2011.framework.annotations.ModelController;
import etu2011.framework.annotations.Scope;
import etu2011.framework.annotations.UrlMapping;
import etu2011.framework.enumerations.Scopes;
import etu2011.framework.exceptions.JavaFileException;
import etu2011.framework.utils.Mapping;
import etu2011.framework.utils.javaObject.JavaClass;
import etu2011.framework.utils.javaObject.JavaFile;
import etu2011.framework.utils.map.UrlPatternKey;
import etu2011.framework.utils.map.UrlRegexHashMap;

/**
 * The {@code FrontServletConfig} class is an extended class as configuration
 * for the main servlet.
 * It is used by the {@code FrontServlet} class.
 * 
 * @see etu2011.framework.FrontServlet
 */
public class FrontServletConfig {
    /* FIELDS SECTION */
    public static final String VIEW_DIRECTORY = "views/";
    public static final String CONTROLLER_DIRECTORY = "controllers/";

    /* METHOD SECTION */

    /**
     * This method returns the configurations needed by the main servlet.
     * 
     * @param rootPath the path to the controllers directory.
     * @see etu2011.framework.utils.javaObject.JavaFile#getClassObject(String).
     * 
     * @param fileTree the list of files in the controllers directory.
     *
     * @return an array of objects containing the mapped methods as [0] and the
     *         singletons as [1].
     * @throws Exception
     */
    public static Object[] getConfigurations(String rootPath, File[] fileTree)
            throws Exception {
        /*
         * Declaring variables up here
         * and injecting their dependencies in the loop using setters
         * for better memory performance
         */

        JavaFile javaFile = new JavaFile();
        JavaClass javaClass = new JavaClass();
        Mapping mapping = null;
        UrlRegexHashMap<UrlPatternKey, Mapping> mappedMethod = new UrlRegexHashMap<UrlPatternKey, Mapping>();
        HashMap<Class<?>, Object> singletons = new HashMap<>();

        for (File file : fileTree) {
            try {
                javaFile.setJavaFile(file);
                javaClass.setJavaClass(javaFile.getClassObject(rootPath));

                /*
                 * all classes in the controller directory that has not the @ModelController
                 * will be ignored
                 */
                if (javaClass
                        .getJavaClass().isAnnotationPresent(ModelController.class)) {
                    for (Method method : javaClass.getMethodByAnnotation(UrlMapping.class)) {
                        if (javaClass.getJavaClass().isAnnotationPresent(Scope.class)) {
                            mapping = new Mapping(javaClass.getJavaClass().getName(), method);
                            UrlPatternKey urlPatternKey = new UrlPatternKey(
                                    javaClass.getJavaClass().getAnnotation(ModelController.class).route()
                                            .concat(method.getAnnotation(UrlMapping.class).url()));
                            mappedMethod.put(urlPatternKey, mapping);
                            if (javaClass.getJavaClass().getAnnotation(Scope.class).value().equals(Scopes.SINGLETON)) {
                                singletons.put(javaClass.getJavaClass(),
                                        javaClass.getJavaClass().getConstructor().newInstance());
                            }
                        } else {
                            throw new Exception("The class " + javaClass.getJavaClass().getName()
                                    + " has no scope annotation.");
                        }
                    }

                }
            } catch (JavaFileException e) {
                continue;
            }
        }

        return new Object[] { mappedMethod, singletons };
    }
}