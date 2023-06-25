package etu2011.framework.utils.helpers;

/**
 * The {@code StringHelpers} class is used to provide some useful methods for
 * the framework.
 */
public class StringHelpers {

    /**
     * @param str1 the first string.
     * @param str2 the second string.
     * @return the camelcase concatenation of the two strings.
     */
    public static String toCamelCase(String str1, String str2) {
        StringBuffer S2 = new StringBuffer(str2);
        S2.setCharAt(0, Character.toUpperCase(S2.charAt(0)));
        return str1.concat(S2.toString());
    }
}
