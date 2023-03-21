package etu2011.framework;

public class Mapping {

    private String className;
    private String method;

    // constructor
    public Mapping(String className, String method) {
        this.className = className;
        this.method = method;
    }

    // setter
    public void setClassName(String className) {
        this.className = className;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    // getter
    public String getClassName() {
        return this.className;
    }

    public String getMethod() {
        return this.method;
    }
}