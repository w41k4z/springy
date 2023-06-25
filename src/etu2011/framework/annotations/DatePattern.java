package etu2011.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code DatePattern} annotation helps supporting date patterns.
 * This framework uses {@code java.sql.Date} for date types.
 * Parsing a date from a string is done using
 * {@code java.sql.Date.valueOf(String)}
 * but this method requires the date to be in the format {@code yyyy-[m]m-[d]d}
 * (This is also the {@code java.sql.Date} format).
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DatePattern {
    /**
     * @return the date pattern(s) to be supported
     */
    String[] value();
}
