package com.powersteeringsoftware.libs.util;

import com.powersteeringsoftware.libs.logger.PSLogger;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Random;

public class StrUtil {
    private static final char[] PATTERN = "qazwsxedcrfvtgbyhnujmik,ol.p;/[']".toCharArray();
    private static Random rand = new Random();

    private static String getRandomString(int len) {
        StringBuffer sb = new StringBuffer(len);
        while (sb.length() < len) {
            sb.append(PATTERN[rand.nextInt(PATTERN.length)]);
        }
        return sb.toString();
    }

    public static boolean equals(String s1, String s2) {
        if (s1 == null) s1 = "";
        if (s2 == null) s2 = "";
        return s1.equals(s2);
    }


    public static String escapeQuotes(String str) {
        if (str == null) return null;
        return StringEscapeUtils.escapeJavaScript(str);
    }

    public static String quoteUnicode(CharSequence charSeq) {
        StringBuffer strBuffer = new StringBuffer("");
        for (int i = 0; i < charSeq.length(); i++) {
            strBuffer.append(CharUtils.unicodeEscaped(charSeq.charAt(i)));
        }
        return strBuffer.toString();
    }


    public static String quoteReqExp(CharSequence aRegexFragment) {
        final StringBuilder result = new StringBuilder();

        StringCharacterIterator iterator = new StringCharacterIterator(String.valueOf(aRegexFragment));
        char character = iterator.current();
        while (character != CharacterIterator.DONE) {
            /*
             * All literals need to have backslashes doubled.
             */
            if (character == '.') {
                result.append("\\.");
            } else if (character == '\\') {
                result.append("\\\\");
            } else if (character == '?') {
                result.append("\\?");
            } else if (character == '*') {
                result.append("\\*");
            } else if (character == '+') {
                result.append("\\+");
            } else if (character == '&') {
                result.append("\\&");
            } else if (character == ':') {
                result.append("\\:");
            } else if (character == '{') {
                result.append("\\{");
            } else if (character == '}') {
                result.append("\\}");
            } else if (character == '[') {
                result.append("\\[");
            } else if (character == ']') {
                result.append("\\]");
            } else if (character == '(') {
                result.append("\\(");
            } else if (character == ')') {
                result.append("\\)");
            } else if (character == '^') {
                result.append("\\^");
            } else if (character == '$') {
                result.append("\\$");
            } else {
                //the char is not a special one
                //add it to the result as is
                result.append(character);
            }
            character = iterator.next();
        }
        return result.toString();
    }


    /**
     * debug method
     *
     * @param line
     * @return
     */
    public static StringBuffer stringToUnicode(String line) {
        StringBuffer ostr = new StringBuffer();
        for (char c : line.toCharArray()) {
            ostr.append("\\u");
            String hex = Integer.toHexString(c & 0xFFFF);
            for (int j = 0; j < 4 - hex.length(); j++)
                ostr.append("0");
            ostr.append(hex.toLowerCase());
        }
        return ostr;
    }

    public static String stringToURL(String str) {
        return str.
                replace("(", "%28").
                replace(")", "%29").
                replace(" ", "%20").
                replace(",", "%2C");
    }

    public static String urlToString(String str) {
        return urlToString(str, false);
    }

    public static String urlToString(String str, boolean fullReplace) {
        str = str.
                replace("%28", "(").
                replace("%29", ")").
                replace("%20", " ").
                replace("%2C", ",");
        if (!fullReplace) {
            return str;
        }
        return str.
                replace("%3A", ":").
                replace("%2F", "/");
    }

    public static String replaceUrlSlashes(String url) {
        return url.replaceAll("\\\\+", "/").replace("/./", "/").replaceAll("([^:])/+", "$1/");
    }

    public static String bytesToString(byte[] bytes) {
        if (bytes.length == 0) return "";
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes) {
            sb.append(b).append(",");
        }
        return sb.substring(0, sb.length() - 1);

    }

    public static String replaceSpaces(String txt) {
        return txt.replaceAll("\\xa0", " ");
    }

    public static String replaceNumberSpaces(String txt) {
        return txt.replaceAll("(\\d)\\s(\\d)", "$1\u00a0$2");
    }

    public static String trim(String txt) {
        return replaceSpaces(txt).trim();
    }

    public static String replaceASCII(String txt) {
        return txt.replaceAll("[^\\p{ASCII}]", "?");
    }

    public static String htmlToString(String str) {
        return str.
                replace("&amp;", "&").
                replace("&lt;", "<").
                replace("&gt;", ">").
                replace("&nbsp;", " ").
                replace("&#92;", "\\");
    }

    public static String stringToHtml(String str) {
        return str.
                replace("&", "&amp;").
                replace("<", "&lt;").
                replace(">", "&gt;").
                replace(" ", "&nbsp;").
                replace("\\", "&#92;");
    }

    public static String[] splitHtml(String str) {
        return str.split("<br/>");
    }

    public static String toUTF8(String str, String encoding) {
        try {
            byte[] utf8Bytes = str.getBytes(encoding);
            return new String(utf8Bytes, "UTF8");
        } catch (UnsupportedEncodingException e) {
            PSLogger.warn(e.getMessage());
            return str;
        }
    }

    public static String toUTF8(String str) {
        try {
            return new String(str.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            PSLogger.warn(e.getMessage());
            return str;
        }
    }


    public static String fromUTF8(String str, String encoding) {
        try {
            byte[] utf8Bytes = str.getBytes("utf-8");
            return new String(utf8Bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            PSLogger.warn(e.getMessage());
            return str;
        }
    }

    public static String fromUTF8(String str) {
        try {
            return new String(str.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            PSLogger.warn(e.getMessage());
            return str;
        }
    }

}
