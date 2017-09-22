package com.BobScript.Support;

public class TypeSupport {
    public static boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }

    public static boolean tryInt(String s) {
        for (int i = 0; i < s.length(); i++)
            if (!isDigit(s.charAt(i)))
                return false;

        return true;
    }

    public static boolean tryDouble(String s) {
        boolean point = false;
        for (int i = 0; i < s.length(); i++) {
            if (!isDigit(s.charAt(i)))
                if (s.charAt(i) != '.')
                    return false;
                else if (i == s.length() - 1)
                    return false;
                else if (point)
                    return false;
                else {
                    point = true;
                }
        }

        return true;
    }

    public static boolean tryConstString(String s) {
        return s.length() > 1 && s.charAt(0) == '\'' && s.charAt(s.length() - 1) == '\'';
    }

    public static boolean tryBoolean(String s) {
        return s.equals("true") || s.equals("false");
    }

    public static boolean tryNull(String s) { return s.equals("null"); }

    public static String toConstString(String s) {
        return s.substring(1, s.length() - 1);
    }

    public static boolean isArithmeticOperation(String s) {
        return s.equals("+") || s.equals("*") || s.equals("-") || s.equals("/") ||
                s.equals("<") || s.equals(">") || s.equals("==");
    }
}
