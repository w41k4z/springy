package etu2011.framework.utils;

public class Singleton {

    /* FIELD SECTION */
    private static Singleton instance = null;

    /* CONSTRUCTOR SECTION */
    protected Singleton() {
        // Exists only to defeat instantiation.
    }

    /* METHOD SECTION */
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
