package com.hapicc.utils.common;

import java.util.regex.Pattern;

public class StrUtils {

    public static String underline2Camel(String underlineFmtStr) {
        if (underlineFmtStr == null) {
            return null;
        }
        String[] items = underlineFmtStr.split("_");
        StringBuilder sb = new StringBuilder();
        boolean handleFirst = false;
        for (String item : items) {
            if (!item.isEmpty()) {
                if (handleFirst) {
                    sb.append(item.substring(0, 1).toUpperCase()).append(item.substring(1));
                } else {
                    sb.append(item.substring(0, 1).toLowerCase()).append(item.substring(1));
                    handleFirst = true;
                }
            }
        }
        return sb.toString();
    }

    public static String camel2Underline(String camelFmtStr) {
        if (camelFmtStr == null) {
            return null;
        }
        String result = camelFmtStr.replaceAll("_", "");
        result = Pattern.compile("([A-Z])").matcher(result).replaceAll("_$1").toLowerCase();
        return result.startsWith("_") ? result.substring(1) : result;
    }
}
