package com.hapicc.controllers.interceptor;

import com.hapicc.constants.LogConstants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String x_request_id = request.getHeader(LogConstants.X_REQUEST_ID);
        if (StringUtils.isEmpty(x_request_id)) {
            x_request_id = UUID.randomUUID().toString().replaceAll("-", "");
        }
        MDC.put(LogConstants.X_REQUEST_ID, x_request_id);

        String queryString = request.getQueryString() == null ? "" : "?" + request.getQueryString();
        log.info("=== request url: {} {}{}", request.getMethod(), request.getRequestURI(), queryString);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        MDC.remove(LogConstants.X_REQUEST_ID);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
