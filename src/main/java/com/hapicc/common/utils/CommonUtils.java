package com.hapicc.common.utils;

import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class CommonUtils {

    private static final ThreadLocal<SimpleDateFormat> dateFormatThreadLocal = ThreadLocal.withInitial(() -> {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat;
    });

    @SuppressWarnings("unchecked")
    public static void preProcessMap(Map map) {
        map.forEach((k, v) -> {
            if (v instanceof Date) {
                map.put(k, dateFormatThreadLocal.get().format(v));
            } else if (v.getClass().getCanonicalName().equals("org.codehaus.groovy.runtime.GStringImpl")) {
                map.put(k, v.toString());
            } else if (v instanceof Map) {
                preProcessMap((Map) v);
            }
        });
    }

    public static boolean isMDCValueNotNull(String str) {
        return !StringUtils.isBlank(str) && !str.equalsIgnoreCase("null");
    }
}
