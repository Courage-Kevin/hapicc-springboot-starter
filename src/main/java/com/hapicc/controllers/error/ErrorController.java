package com.hapicc.controllers.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hapicc.pojo.HapiccJSONResult;

@Controller
@RequestMapping("err")
public class ErrorController {

    @RequestMapping("error")
    public String error() {

        System.out.println(1 / 0);

        return "thymeleaf/error";
    }

    @RequestMapping("ajaxerror")
    public String ajaxerror() {
        return "thymeleaf/ajaxerror";
    }

    @RequestMapping("getAjaxerror")
    @ResponseBody
    public HapiccJSONResult getAjaxerror() {

        System.out.println(1 / 0);

        return HapiccJSONResult.ok();
    }
}
