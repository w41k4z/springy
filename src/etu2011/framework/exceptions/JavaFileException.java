package etu2011.framework.exceptions;

/**
 * The {@code JavaFileException} class is used to throw an exception when the
 * file is not a java file.
 */
public class JavaFileException extends Exception {

    /**
     * The default constructor with default error message.
     */
    public JavaFileException() {
        super("ERROR: This is not a java file");
    }

    /**
     * The constructor with a custom error message.
     * 
     * @param message the custom error message.
     */
    public JavaFileException(String message) {
        super("ERROR: " + message);
    }
}
