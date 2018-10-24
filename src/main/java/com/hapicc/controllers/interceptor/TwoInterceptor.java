package com.hapicc.controllers.interceptor;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.hapicc.pojo.HapiccJSONResult;
import com.hapicc.utils.json.JacksonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TwoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // do something before handle...
        if (true) {
            renderErrorResponse(response, HapiccJSONResult.errorMsg("被 two 拦截。。。"));
        }

        log.info("被 two 拦截。。。");

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // TODO do something after handle but before render...
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO do something after render...
    }

    public void renderErrorResponse(HttpServletResponse response, HapiccJSONResult result) throws Exception {
        try (OutputStream os = response.getOutputStream()) {
            response.setContentType("text/json");
            response.setCharacterEncoding("UTF-8");
            os.write(JacksonUtils.obj2Json(result).getBytes());
            os.flush();
        }
    }
}
