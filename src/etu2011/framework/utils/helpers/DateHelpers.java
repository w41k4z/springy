package etu2011.framework.utils.helpers;

import java.lang.reflect.AnnotatedElement;
import java.text.ParseException;

import etu2011.framework.annotations.DatePattern;

public class DateHelpers {

    public static String getDatePattern(AnnotatedElement dateField) {
        if (dateField.isAnnotationPresent(DatePattern.class)) {
            return dateField.getAnnotation(DatePattern.class).value();
        }
        return "yyyy-MM-dd";
    }

    public static java.sql.Date convertToSqlDate(String date, String originDatePattern)
            throws ParseException {
        java.text.SimpleDateFormat sourceFormat = new java.text.SimpleDateFormat(originDatePattern);
        java.util.Date utilDate = sourceFormat.parse(date);
        java.text.SimpleDateFormat targetFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = targetFormat.format(utilDate);
        return java.sql.Date.valueOf(formattedDate);
    }
}
