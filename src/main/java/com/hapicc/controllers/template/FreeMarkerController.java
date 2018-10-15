package com.hapicc.controllers.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hapicc.pojo.Resource;

@Controller
@RequestMapping("/ftl")
public class FreeMarkerController {

    @Autowired
    private Resource resource;

    @RequestMapping("/index")
    public String index(ModelMap map) {
        map.addAttribute("resource", resource);
        return "freemarker/index";
    }

    @RequestMapping("/about")
    public String about() {
        return "freemarker/about/about";
    }
}
