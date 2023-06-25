package etu2011.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import etu2011.framework.enumerations.HttpParameters;

/**
 * The {@code HttpParam} annotation indicates that an argument of a method is
 * to be bound to a HTTP parameter.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpParam {
    /**
     * @return the name of the HTTP parameter
     */
    String name() default "";

    /**
     * @return the type of the HTTP parameter
     * 
     * @see etu2011.framework.enumerations.HttpParameters
     */
    HttpParameters type();
}
