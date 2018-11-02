package com.hapicc.common.utils;

import org.slf4j.Logger;
import org.slf4j.event.Level;

public class LogUtils {

    public static void log(Logger log, Object logLevel, String format, Object... args) {
        if (logLevel != null) {
            String level = String.valueOf(logLevel);
            if (Level.DEBUG.name().equals(level)) {
                log.debug(format, args);
            } else if (Level.TRACE.name().equals(level)) {
                log.trace(format, args);
            }
        } else {
            log.info(format, args);
        }
    }
}
