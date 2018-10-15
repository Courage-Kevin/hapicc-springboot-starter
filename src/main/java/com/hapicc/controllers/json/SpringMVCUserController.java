package com.hapicc.controllers.json;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hapicc.pojo.HapiccJSONResult;
import com.hapicc.pojo.User;

@Controller
@RequestMapping("/springMVC")
public class SpringMVCUserController {

    @RequestMapping("/getUser")
    @ResponseBody
    public User getUser() {
        User user = new User();
        user.setName("vveicc");
        user.setAge(18);
        user.setBirthday(new Date());
        user.setPassword("vveicc");
        user.setDesc("");
        return user;
    }

    @RequestMapping("/getUserJson")
    @ResponseBody
    public HapiccJSONResult getUserJson() {
        User user = new User();
        user.setName("vveicc");
        user.setAge(18);
        user.setBirthday(new Date());
        user.setPassword("vveicc");
        return HapiccJSONResult.ok(user);
    }
}
