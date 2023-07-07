package etu2011.framework.config;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import etu2011.framework.annotations.ModelController;
import etu2011.framework.annotations.Scope;
import etu2011.framework.annotations.UrlMapping;
import etu2011.framework.enumerations.Scopes;
import etu2011.framework.exceptions.ClassValidationException;
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
 * @see etu2011.framework.servlet.FrontServlet
 */
public class FrontServletConfig {
    /* FIELDS SECTION */
    public static final String VIEW_DIRECTORY = "views/";
    public static final String CONTROLLER_DIRECTORY = "controllers/";

    /* METHOD SECTION */

    /**
     * This method returns the configurations needed by the main servlet.
     * 
     * @param rootPath The path to the classes directory. This path is
     *                 relative to the webapp directory and is used by the
     *                 {@code etu2011.framework.utils.javaObject.JavaFile#getClassObject()}
     *                 method.
     * @see etu2011.framework.utils.javaObject.JavaFile#getClassObject(String)
     * 
     * @param fileTree the list of files in the classes directory.
     *
     * @return an array of objects containing the mapped methods as [0] and the
     *         singletons as [1].
     * @throws SecurityException         if a security manager exists and it denies
     *                                   the caller's access to the default empty
     *                                   constructor based on the security manager's
     *                                   policy
     * @throws NoSuchMethodException     if a controller does not have a
     *                                   public constructor with no parameters.
     * @throws InvocationTargetException if the underlying constructor throws an
     *                                   exception
     * @throws IllegalArgumentException  if the number of actual and formal
     *                                   parameters differ,
     *                                   or if an unwrapping conversion for
     *                                   primitive arguments fails
     *                                   or if, after possible unwrapping a
     *                                   parameter value cannot be converted
     *                                   to the corresponding formal parameter type
     *                                   by a method invocation conversion
     * @throws InstantiationException    if a controller class represents an
     *                                   abstract class
     */
    public static Object[] getConfigurations(String rootPath, File[] fileTree)
            throws InvocationTargetException, InstantiationException, IllegalArgumentException,
            NoSuchMethodException, SecurityException {
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
                            throw new ClassValidationException("The class " + javaClass.getJavaClass().getName()
                                    + " has no scope annotation.");
                        }
                    }

                }
            } catch (JavaFileException e) {
                // Just ignoring the file if it is not a java file
                continue;
            } catch (IllegalAccessException e) {
                /*
                 * Impossible to happen because the constructor we are using is public
                 * It is just thrown by the basic newInstance() method from the Constructor
                 * class
                 */
            }
        }

        return new Object[] { mappedMethod, singletons };
    }
}