package etu2011.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code ModelController} annotation tells the framework that
 * the class is a controller.
 * It is called {@code ModelController} because it is a controller that can wrap
 * an http request parameters within itself.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModelController {
    /**
     * @return the route of the controller
     */
    String route() default "";
}