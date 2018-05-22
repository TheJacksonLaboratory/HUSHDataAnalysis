package org.jax.utils;

public class StringUtils {

    public static String stripEndQuotes(String s) {
        if (s.startsWith("\"")) {
            s = s.substring(1, s.length());
        }
        if (s.endsWith("\"")) {
            s = s.substring(0, s.length() - 1 );
        }
        return s;
    }
}
