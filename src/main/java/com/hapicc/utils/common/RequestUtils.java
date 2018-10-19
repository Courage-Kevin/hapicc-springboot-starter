package com.hapicc.utils.common;

import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.regex.Pattern;

public class RequestUtils {

    public static final char ESCAPE = '/';

    private static final String K_PAGE = "page";
    private static final String K_ROWS = "rows";
    private static final String K_SIDX = "sidx";
    private static final String K_SORD = "sord";
    private static final String K_NEED_TOTAL = "needTotal";
    private static final String K_Q = "q";

    private static final Integer DEFAULT_PAGE = 1;
    private static final Integer DEFAULT_ROWS = 10;
    private static final String DEFAULT_SIDX = "id";

    public static Integer getPage(Map<String, String> params) {
        return getPage(params, false);
    }

    public static Integer getPage(Map<String, String> params, boolean zeroBased) {
        Integer page = getIntegerParam(params, K_PAGE, DEFAULT_PAGE);
        page = page > 0 ? page : DEFAULT_PAGE;
        return zeroBased ? page - 1 : page;
    }

    public static Integer getRows(Map<String, String> params) {
        return getIntegerParam(params, K_ROWS, DEFAULT_ROWS);
    }

    public static String getSidx(Map<String, String> params, boolean toCamel) {
        return params != null && !StringUtils.isEmpty(params.get(K_SIDX)) ? (
                toCamel ? underline2Camel(params.get(K_SIDX))
                        : camel2Underline(params.get(K_SIDX))
        ) : DEFAULT_SIDX;
    }

    public static String getSord(Map<String, String> params) {
        return params != null && "desc".equals(params.get(K_SORD)) ? "desc" : "asc";
    }

    public static boolean needTotal(Map<String, String> params) {
        return params != null && "true".equals(params.get(K_NEED_TOTAL));
    }

    public static String getQ(Map<String, String> params) {
        return getQ(params, false);
    }

    public static String getQ(Map<String, String> params, boolean escape) {
        if (params == null) {
            return null;
        }

        String q = params.get(K_Q);
        if (StringUtils.isEmpty(q)) {
            return null;
        }

        q = q.replaceAll("'", "''");

        if (escape) {
            if (q.contains("%")) {
                q = q.replaceAll("%", ESCAPE + "%");
            }
            if (q.contains("_")) {
                q = q.replaceAll("_", ESCAPE + "_");
            }
        }

        return "%" + q + "%";
    }

    public static Integer getIntegerParam(Map<String, String> params, String name, Integer defaultValue) {
        if (params != null && !StringUtils.isEmpty(params.get(name))) {
            try {
                return Integer.valueOf(params.get(name));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

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
        String result = Pattern.compile("([A-Z])").matcher(camelFmtStr).replaceAll("_$1").toLowerCase();
        result = result.replaceAll("_+", "_");
        return result.startsWith("_") ? result.substring(1) : result;
    }
}
