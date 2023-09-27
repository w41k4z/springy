package etu2011.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code Auth} annotation indicates that a method needs authentication to
 * be accessed.
 * This is controlled during the request processing.
 * A {@code RuntimeException} is thrown if the user is not authenticated.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {

    /**
     * Specifies the value or profile name associated with the authentication check.
     * This can be used to perform additional profile-based authentication checks.
     * 
     * @return the value or profile name
     */
    String value() default "";
}