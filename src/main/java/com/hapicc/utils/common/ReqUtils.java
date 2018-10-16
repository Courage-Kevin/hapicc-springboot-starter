package com.hapicc.utils.common;

import org.springframework.util.StringUtils;

import java.util.Map;

public class ReqUtils {

    private static final String K_PAGE = "page";
    private static final String K_ROWS = "rows";
    private static final String K_SIDX = "sidx";
    private static final String K_SORD = "sord";

    private static final Integer DEFAULT_PAGE = 1;
    private static final Integer DEFAULT_ROWS = 10;
    private static final String DEFAULT_SIDX = "id";

    public static Integer getPage(Map<String, String> params) {
        return getIntegerParam(params, K_PAGE, DEFAULT_PAGE);
    }

    public static Integer getRows(Map<String, String> params) {
        return getIntegerParam(params, K_ROWS, DEFAULT_ROWS);
    }

    public static String getSidx(Map<String, String> params, boolean toCamel) {
        return params != null && !StringUtils.isEmpty(params.get(K_SIDX)) ? (
                toCamel ? StrUtils.underline2Camel(params.get(K_SIDX))
                        : StrUtils.camel2Underline(params.get(K_SIDX))
        ) : DEFAULT_SIDX;
    }

    public static String getSord(Map<String, String> params) {
        return params != null && "desc".equals(params.get(K_SORD)) ? "desc" : "asc";
    }

    public static String handleQ(final String q) {
        if (q == null || q.isEmpty()) {
            return null;
        }

        String _q = q.replaceAll("'", "''");
        boolean escape = false;

        if (_q.contains("%")) {
            escape = true;
            _q = _q.replaceAll("%", "/%");
        }
        if (_q.contains("_")) {
            escape = true;
            _q = _q.replaceAll("_", "/_");
        }

        String likeStr = " '%" + _q + "%' ";
        if (escape) {
            likeStr += "ESCAPE '/' ";
        }
        return likeStr;
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
}
