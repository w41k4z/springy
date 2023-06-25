package etu2011.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import etu2011.framework.enumerations.Scopes;

/*
 * The {@code Scope} annotation specifies the scope of the controller.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {
    /**
     * @return the scope of the controller
     * 
     * @see etu2011.framework.enumerations.Scopes
     */
    Scopes value();
}
