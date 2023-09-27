package etu2011.framework.exceptions;

public class ClassValidationException extends RuntimeException {

    public ClassValidationException() {
        super("ERROR: Please check this class validity");
    }

    /**
     * Constructor with custom message
     * 
     * @param message
     */
    public ClassValidationException(String message) {
        super(message);
    }
}
