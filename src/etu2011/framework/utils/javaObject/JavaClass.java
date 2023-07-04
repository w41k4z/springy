package etu2011.framework.utils.javaObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;

import etu2011.framework.utils.helpers.DateHelpers;
import etu2011.framework.utils.helpers.StringHelpers;

/**
 * The {@code JavaClass} class is used to provide some useful methods for java
 * class manipulation.
 */
public class JavaClass {
    /* FIELD SECTION */
    private Class<?> javaClass;

    /* CONSTRUCTOR SECTION */

    /**
     * Default constructor.
     */
    public JavaClass() {
    }

    /**
     * Constructor with the class type parameter.
     * 
     * @param javaClass the java class to be set.
     */
    public JavaClass(Class<?> javaClass) {
        this.setJavaClass(javaClass);
    }

    /* SETTER SECTION */

    /**
     * Set the Class object
     * 
     * @param javaClass
     */
    public void setJavaClass(Class<?> javaClass) {
        this.javaClass = javaClass;
    }

    /* GETTER SECTION */

    /**
     * Returns the Class object
     * 
     * @return the Class object
     */
    public Class<?> getJavaClass() {
        return this.javaClass;
    }

    /* METHODS SECTION */

    /**
     * Returns all the methods annotated with the given annotation class.
     * 
     * @param annotationClass the annotation class.
     * @return all the methods annotated with the given annotation class.
     */
    public Method[] getMethodByAnnotation(Class<? extends Annotation> annotationClass) {
        ArrayList<Method> methods = new ArrayList<>();
        for (Method method : this.getJavaClass().getMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                methods.add(method);
            }
        }
        return methods.toArray(new Method[methods.size()]);
    }

    /**
     * Sets the field value for the given target object using its setter
     * 
     * @param object the object to set the field value from.
     * @param data   the parameter value for the field setter.
     * @param field  the field to set the value for.
     * 
     * @throws NoSuchMethodException     if the field has no setter (following the
     *                                   java naming convention)
     * @throws InvocationTargetException if the setter throws an exception
     * @throws IllegalAccessException    if the setter is not public
     */
    public static void setObjectFieldValue(Object object, Object data, Field field)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object castedData = null;
        Method setter = object.getClass().getMethod(StringHelpers.toCamelCase("set", field.getName()),
                field.getType());

        if (data == null || data.toString().trim().length() == 0
                || data.toString().trim().toLowerCase().equals("null")) {
            castedData = null;
        } else {
            if (java.util.Date.class.isAssignableFrom(field.getType())) {
                String[] dateFormats = DateHelpers.getSupportedPatterns(field);
                for (int i = 0; i < dateFormats.length; i++) {
                    try {
                        castedData = DateHelpers.format(field.getType(), data.toString().trim(),
                                dateFormats[i]);
                        break;
                    } catch (ParseException e) {
                        if (i == dateFormats.length - 1) {
                            throw new IllegalArgumentException("The date format is not supported");
                        }
                    }
                }
            } else {
                try {
                    // Basic type : INTEGER, STRING, BOOLEAN, DOUBLE, FLOAT, LONG, SHORT, BYTE
                    castedData = field.getType().getConstructor(String.class).newInstance(data.toString());
                } catch (Exception e) {
                    // Object type
                    castedData = data;
                }
            }
        }
        setter.invoke(object, castedData);
    }
}
