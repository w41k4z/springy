package etu2011.framework.utils.helpers;

import java.lang.reflect.AnnotatedElement;
import java.text.ParseException;

import etu2011.framework.annotations.DatePattern;

/**
 * The {@code DateHelpers} class is used to provide helper methods for date
 * operations.
 * 
 * @see etu2011.framework.annotations.DatePattern
 */
public class DateHelpers {

    /**
     * Returns the supported date patterns for a given date field.
     * 
     * @param dateField the date field to get the supported date patterns from.
     * @return the supported date patterns for a given date field or the default
     *         date format if the element is not annotated with @DatePattern.
     */
    public static String[] getSupportedDatePatterns(AnnotatedElement dateField) {
        if (dateField.isAnnotationPresent(DatePattern.class)) {
            return dateField.getAnnotation(DatePattern.class).value();
        }
        return new String[] { "yyyy-MM-dd" };
    }

    /**
     * Converts a given date to a java.sql.Date object.
     * 
     * @param date              the date to be converted.
     * @param originDatePattern the date pattern of the given date.
     * @return a java.sql.Date object.
     * @throws ParseException if the given date is not compatible with the given
     *                        date pattern.
     */
    public static java.sql.Date convertToSqlDate(String date, String originDatePattern)
            throws ParseException {
        java.text.SimpleDateFormat sourceFormat = new java.text.SimpleDateFormat(originDatePattern);
        java.util.Date utilDate = sourceFormat.parse(date);
        java.text.SimpleDateFormat targetFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = targetFormat.format(utilDate);
        return java.sql.Date.valueOf(formattedDate);
    }
}
