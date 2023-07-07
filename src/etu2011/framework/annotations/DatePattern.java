package etu2011.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code DatePattern} annotation helps supporting date patterns.
 * This framework uses
 * {@code java.sql.Date}/{@code java.sql.Time}/{@code java.sql.Timestamp} for
 * date types.
 * Parsing a date from a string is done using
 * {@code java.util.Date.valueOf(String)} (all its subclass has this method)
 * but this method requires the date to be in a specific the format
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface DatePattern {
    /**
     * List of all the supported patterns for the annotated field.
     * 
     * @return the date pattern(s) to be supported
     */
    String[] value();
}
