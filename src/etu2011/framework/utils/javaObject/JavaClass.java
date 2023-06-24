package etu2011.framework.utils.javaObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

import etu2011.framework.utils.helpers.DateHelpers;
import etu2011.framework.utils.helpers.StringHelpers;

/* 
 * Used for Java Class Object manipulation
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
    public Method[] getMethodByAnnotation(Class<? extends Annotation> annotationClass) {
        ArrayList<Method> methods = new ArrayList<>();
        for (Method method : this.getJavaClass().getMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                methods.add(method);
            }
        }
        return methods.toArray(new Method[methods.size()]);
    }

    public static void setObjectFieldValue(Object object, Object data, Field field) throws Exception {
        Object castedData;
        Method setter = object.getClass().getMethod(StringHelpers.toCamelCase("set", field.getName()),
                field.getType());

        if (data == null || data.toString().trim().length() == 0
                || data.toString().trim().toLowerCase().equals("null")) {
            castedData = null;
        } else {
            switch (field.getType().getSimpleName()) {
                case "Date":
                    String dateFormat = DateHelpers.getDatePattern(field);
                    castedData = DateHelpers.convertToSqlDate(data.toString().trim(), dateFormat);
                    break;

                case "Timestamp":
                    castedData = Timestamp.valueOf(data.toString().trim());
                    break;

                case "Time":
                    castedData = Time.valueOf(data.toString().trim());
                    break;

                default:
                    castedData = field.getType().getConstructor(String.class).newInstance(data.toString());
                    break;
            }
        }
        setter.invoke(object, castedData);
    }
}
