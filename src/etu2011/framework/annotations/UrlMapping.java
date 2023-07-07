package etu2011.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import etu2011.framework.enumerations.HttpMethods;

/**
 * The {@code UrlMapping} annotation is used to map a method to a specific URL.
 * Only methods that are annotated with {@code UrlMapping} will be able to
 * handle requests.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UrlMapping {
    /**
     * The URL mapped to this method.
     * 
     * @return the URL of the method
     */
    String url();

    /**
     * The HTTP method type accepted by the annotated method.
     * 
     * @return the HTTP method type of the method
     * 
     * @see etu2011.framework.enumerations.HttpMethods
     */
    HttpMethods method() default HttpMethods.GET;
}
