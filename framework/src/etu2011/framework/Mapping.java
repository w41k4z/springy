package etu2011.framework;

import java.lang.reflect.Method;

public class Mapping {

    private String className;
    private Method method;

    // constructor
    public Mapping(String className, Method method) {
        this.className = className;
        this.method = method;
    }

    // setter
    public void setClassName(String className) {
        this.className = className;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    // getter
    public String getClassName() {
        return this.className;
    }

    public Method getMethod() {
        return this.method;
    }
}