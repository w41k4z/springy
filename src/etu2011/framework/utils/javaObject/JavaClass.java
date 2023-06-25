package etu2011.framework.utils.javaObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Time;
import java.sql.Timestamp;
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
    public JavaClass() {
    }

    public JavaClass(Class<?> javaClass) {
        this.setJavaClass(javaClass);
    }

    /* SETTER SECTION */
    public void setJavaClass(Class<?> javaClass) {
        this.javaClass = javaClass;
    }

    /* GETTER SECTION */
    public Class<?> getJavaClass() {
        return this.javaClass;
    }

    /* METHODS SECTION */

    /**
     * @param annotationClass the annotation class.
     * @return all the methods annotatedwith the given annotation class.
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
     * @param object the object to set the field value from.
     * @param data   the parameter value for the field setter.
     * @param field  the field to set the value for.
     * @throws Exception
     */
    public static void setObjectFieldValue(Object object, Object data, Field field) throws Exception {
        Object castedData = null;
        Method setter = object.getClass().getMethod(StringHelpers.toCamelCase("set", field.getName()),
                field.getType());

        if (data == null || data.toString().trim().length() == 0
                || data.toString().trim().toLowerCase().equals("null")) {
            castedData = null;
        } else {
            switch (field.getType().getSimpleName()) {
                case "Date":
                    String[] dateFormats = DateHelpers.getSupportedDatePatterns(field);
                    for (int i = 0; i < dateFormats.length; i++) {
                        try {
                            castedData = DateHelpers.convertToSqlDate(data.toString().trim(), dateFormats[i]);
                            break;
                        } catch (ParseException e) {
                            if (i == dateFormats.length - 1) {
                                throw new Exception("The date format is not supported");
                            }
                        }
                    }
                    break;

                case "Timestamp":
                    castedData = Timestamp.valueOf(data.toString().trim());
                    break;

                case "Time":
                    castedData = Time.valueOf(data.toString().trim());
                    break;

                default:
                    try {
                        // Basic type : INTEGER, STRING, BOOLEAN, DOUBLE, FLOAT, LONG, SHORT, BYTE
                        castedData = field.getType().getConstructor(String.class).newInstance(data.toString());
                    } catch (Exception e) {
                        // Object type
                        castedData = data;
                    }
                    break;
            }
        }
        setter.invoke(object, castedData);
    }
}
