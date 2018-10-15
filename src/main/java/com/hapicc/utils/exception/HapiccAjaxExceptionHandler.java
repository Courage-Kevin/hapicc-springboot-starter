package com.hapicc.utils.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hapicc.pojo.HapiccJSONResult;

/**
 * Ajax 形式的全局异常处理类
 */
@Order(10)
//@ControllerAdvice
@RestControllerAdvice // @RestControllerAdvice = @ControllerAdvice + @ResponseBody
public class HapiccAjaxExceptionHandler {

    @ExceptionHandler(value = Exception.class)
//	@ResponseBody
    public HapiccJSONResult errorHandler(HttpServletRequest request, Exception e) throws Exception {

        e.printStackTrace();

        return HapiccJSONResult.errorException(e.getMessage());
    }
}
