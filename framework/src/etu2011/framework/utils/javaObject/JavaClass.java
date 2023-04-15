package etu2011.framework.utils.javaObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;

/* 
 * Used for Java Class Object manipulation
*/

public class JavaClass {

    private Class<?> javaClass;

    // constructors
    public JavaClass() {
    }

    public JavaClass(Class<?> javaClass) {
        this.setJavaClass(javaClass);
    }

    // setter
    public void setJavaClass(Class<?> javaClass) {
        this.javaClass = javaClass;
    }

    // getter
    public Class<?> getJavaClass() {
        return this.javaClass;
    }

    public Method[] getMethodByAnnotation(Class<? extends Annotation> annotationClass) {
        ArrayList<Method> methods = new ArrayList<>();
        for (Method method : this.getJavaClass().getMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                methods.add(method);
            }
        }
        return methods.toArray(new Method[methods.size()]);
    }
}
