package etu2011.framework.utils.helpers;

public class StringHelpers {

    public static String toCamelCase(String str1, String str2) {
        StringBuffer S2 = new StringBuffer(str2);
        S2.setCharAt(0, Character.toUpperCase(S2.charAt(0)));
        return str1.concat(S2.toString());
    }
}
