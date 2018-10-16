package com.hapicc.controllers.json;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hapicc.pojo.HapiccJSONResult;
import com.hapicc.pojo.User;

//@Controller
@RestController // @RestController = @Controller + @ResponseBody
@RequestMapping("/springBoot")
public class SpringBootUserController {

//	@ResponseBody
    @RequestMapping("/getUser")
    public User getUser() {
        User user = new User();
        user.setName("vveicc");
        user.setAge(18);
        user.setBirthday(new Date());
        user.setPassword("vveicc");
        user.setDesc("");
        return user;
    }

//	@ResponseBody
    @RequestMapping("/getUserJson")
    public HapiccJSONResult getUserJson() {
        User user = new User();
        user.setName("vveicc");
        user.setAge(18);
        user.setBirthday(new Date());
        user.setPassword("vveicc");
        return HapiccJSONResult.ok(user);
    }
}
