package com.hapicc.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hapicc.pojo.HapiccJSONResult;
import com.hapicc.pojo.Resource;

@RestController
public class HelloController {

    @Autowired
    private Resource resource;

    @RequestMapping("/hello")
    public Object hello() {
        return "Hello Spring Boot~~~";
    }

    @RequestMapping("/getResource")
    public HapiccJSONResult getResource() {

        Resource bean = new Resource();
        BeanUtils.copyProperties(resource, bean);

        return HapiccJSONResult.ok(bean);
    }
}
