package com.hapicc.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.hapicc.pojo.HapiccJSONResult;

/**
 * 兼容 Web 与 Ajax 形式的通用全局异常处理类
 */
@Order(0)
@ControllerAdvice
public class HapiccExceptionHandler {

    public static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler(Exception.class)
    public Object errorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {

        e.printStackTrace();

        if (isAjax(request)) {
            return HapiccJSONResult.errorException(e.getMessage());
        } else {
            ModelAndView mav = new ModelAndView();
            mav.addObject("url", request.getRequestURL());
            mav.addObject("exception", e);
            mav.setViewName(DEFAULT_ERROR_VIEW);
            return mav;
        }
    }

    public static boolean isAjax(HttpServletRequest request) {
        return request.getHeader("X-Requested-With") != null
                && request.getHeader("X-Requested-With").equals("XMLHttpRequest");
    }
}
