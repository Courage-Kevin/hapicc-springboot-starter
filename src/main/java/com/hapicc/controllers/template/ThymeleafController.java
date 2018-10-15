package com.hapicc.controllers.template;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hapicc.pojo.Resource;
import com.hapicc.pojo.User;

@Controller
@RequestMapping("/th")
public class ThymeleafController {

    @Autowired
    private Resource resource;

    @RequestMapping("/index")
    public String index(ModelMap map) {
        map.addAttribute("resource", resource);
        return "thymeleaf/index";
    }

    @RequestMapping("/about")
    public String about() {
        return "thymeleaf/about";
    }

    // 演示 Thymeleaf 常用标签的使用方法
    @RequestMapping("/label")
    public String label(ModelMap map) {

        User user = new User();
        user.setName("manager");
        user.setAge(24);
        user.setBirthday(new Date());
        user.setPassword("vveicc");
        user.setDesc("<font color='red'><b>Hello world!</b></font>");

        map.addAttribute("user", user);

        User user1 = new User();
        user1.setName("nick");
        user1.setAge(21);
        user1.setBirthday(new Date());
        user1.setPassword("nick");

        User user2 = new User();
        user2.setName("xiaoge");
        user2.setAge(16);
        user2.setBirthday(new Date());
        user2.setPassword("123456");

        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user1);
        userList.add(user2);

        map.addAttribute("userList", userList);

        return "thymeleaf/label";
    }

    @PostMapping("postform")
    public String postform(User user) {
        System.out.println("The user with name: " + user.getName() + ", age: " + user.getAge());
        return "redirect:/th/label";
    }
}
