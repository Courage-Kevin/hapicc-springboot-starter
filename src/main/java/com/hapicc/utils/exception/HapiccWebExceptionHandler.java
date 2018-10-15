package com.hapicc.utils.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Web 形式的全局异常处理类
 */
@Order(100)
@ControllerAdvice
public class HapiccWebExceptionHandler {

	public static final String DEFAULT_ERROR_VIEW = "error";

	@ExceptionHandler(value = Exception.class)
	public Object errorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {

		e.printStackTrace();

		ModelAndView mav = new ModelAndView();
		mav.addObject("url", request.getRequestURL());
		mav.addObject("exception", e);
		mav.setViewName(DEFAULT_ERROR_VIEW);
		return mav;
	}
}
