package etu2011.framework.utils.helpers;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;

import etu2011.framework.annotations.DatePattern;

/**
 * The {@code DateHelpers} class is used to provide helper methods for time,
 * date and datetime
 * operations.
 * 
 * @see etu2011.framework.annotations.DatePattern
 */
public class DateHelpers {

    /**
     * Returns the supported time|date|datetime patterns for a given date field.
     * 
     * @param dateField The date field to get the supported date patterns from.
     * @return The supported date patterns for the given date field or the default
     *         date format if the element is not annotated with @DatePattern.
     */
    public static String[] getSupportedPatterns(AnnotatedElement dateField)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (dateField.isAnnotationPresent(DatePattern.class)) {
            return dateField.getAnnotation(DatePattern.class).value();
        }
        return new String[] {
                getValidSqlDateFormat((Class<?>) dateField.getClass().getMethod("getType").invoke(dateField)) };
    }

    /**
     * Converts a given date to a Date | Time | Timestamp object.
     * 
     * @param type    the type of the date to be converted.
     * @param date    the date to be converted.
     * @param pattern the date pattern of the given date.
     * @return a java.sql.Date object.
     * @throws ParseException            if the given date is not compatible with
     *                                   the given date pattern.
     * @throws InvocationTargetException if the valueOf method throws an exception
     */
    public static java.util.Date format(Class<?> type, String date, String pattern)
            throws ParseException, InvocationTargetException {
        java.text.SimpleDateFormat sourceFormat = new java.text.SimpleDateFormat(pattern);
        java.util.Date utilDate = sourceFormat.parse(date);

        String validPattern = getValidSqlDateFormat(type);
        java.text.SimpleDateFormat targetFormat = new java.text.SimpleDateFormat(validPattern);
        String formattedDateExpression = targetFormat.format(utilDate);
        try {
            Method valueOf = type.getMethod("valueOf", String.class);
            return (java.util.Date) type.cast(valueOf.invoke(type, formattedDateExpression));
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("The given type is not a date type");
        } catch (IllegalAccessException e) {
            /*
             * This exception will never be thrown because
             * an IllegalArgumentException will be thrown if
             * the type is not a date type (Specifically, the method valueOf
             * is only accessible for java.sql.Date, java.sql.Time and
             * java.sql.Timestamp)
             */
            return null;
        }
    }

    /**
     * Returns the valid pattern according to the provided type
     * 
     * @param type The date type
     * @return The pattern for the specified date type
     */
    public static String getValidSqlDateFormat(Class<?> type) {
        type = type.isArray() ? type.getComponentType() : type;
        switch (type.getSimpleName()) {
            case "Time":
                return "HH:mm:ss";
            case "Date":
                return "yyyy-MM-dd";
            case "Timestamp":
                return "yyyy-MM-dd HH:mm:ss";
            default:
                throw new IllegalArgumentException("The `" + type.getSimpleName() + "` type is not a date type");
        }
    }
}
