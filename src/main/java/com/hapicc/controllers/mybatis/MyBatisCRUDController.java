package com.hapicc.controllers.mybatis;

import java.util.Date;
import java.util.Map;

import com.hapicc.utils.json.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hapicc.pojo.HapiccJSONResult;
import com.hapicc.pojo.JqGridResult;
import com.hapicc.pojo.SysUser;
import com.hapicc.services.mybatis.UserService;

@Slf4j
@RestController
@RequestMapping("mybatis")
public class MyBatisCRUDController {

    @Autowired
    private Sid sid;

    @Autowired
    private UserService userService;

    @PostMapping("user")
    public HapiccJSONResult save(@RequestBody SysUser user) {
        try {
            if (userService.save(user) == 1) {
                return HapiccJSONResult.ok(user);
            } else {
                return HapiccJSONResult.build(400, "Failed to create!", null);
            }
        } catch (Exception e) {
            log.warn("Error occurred when save user: " + JacksonUtils.obj2Json(user), e);
            return HapiccJSONResult.errorException("Error occurred when save user!");
        }
    }

    @RequestMapping(value = "user/{userId}", method = { RequestMethod.PUT })
    public HapiccJSONResult update(@PathVariable String userId, @RequestBody SysUser user) {

        if (userService.get(userId) == null) {
            return HapiccJSONResult.build(404, "The user not found!", null);
        }

        if (userService.update(userId, user) == 1) {
            return HapiccJSONResult.ok(userService.get(userId));
        } else {
            return HapiccJSONResult.build(400, "Failed to update!", null);
        }
    }

    @RequestMapping(value = "user/{userId}", method = { RequestMethod.DELETE })
    public HapiccJSONResult delete(@PathVariable String userId) {

        if (userService.get(userId) == null) {
            return HapiccJSONResult.build(404, "The user not found!", null);
        }

        if (userService.delete(userId) == 1) {
            return HapiccJSONResult.ok();
        } else {
            return HapiccJSONResult.build(400, "Failed to delete!", null);
        }
    }

    @RequestMapping(value = "user/{userId}", method = { RequestMethod.GET })
    public HapiccJSONResult get(@PathVariable String userId) {

        SysUser user = userService.get(userId);

        if (user != null) {
            return HapiccJSONResult.ok(user);
        } else {
            return HapiccJSONResult.build(404, "The user not found!", null);
        }
    }

    @RequestMapping(value = "user", method = { RequestMethod.GET })
    public HapiccJSONResult list(@RequestParam Map<String, String> params) {
        try {
            JqGridResult ret = userService.list(params);
            return HapiccJSONResult.ok(ret);
        } catch (Exception e) {
            log.warn("Error occurred when list user with params: " + JacksonUtils.obj2Json(params), e);
            return HapiccJSONResult.build(400, "Invalid parameters!", null);
        }
    }

    @GetMapping("user/{userId}/simple")
    public HapiccJSONResult getUserSimpleInfo(@PathVariable String userId) {

        SysUser user = userService.getUserSimpleInfo(userId);

        if (user != null) {
            return HapiccJSONResult.ok(user);
        } else {
            return HapiccJSONResult.build(404, "The user not found!", null);
        }
    }

    @RequestMapping("user/testtrx")
    public HapiccJSONResult saveUserTestTransaction() {

        Date now = new Date();

        SysUser user = new SysUser();
        user.setId(sid.next());
        user.setName("hapicccccc");
        user.setLoginName("cc");
        user.setPassword(sid.nextShort());
        user.setDateCreated(now);
        user.setLastUpdated(now);

        try {
            userService.saveUserTestTransaction(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return HapiccJSONResult.ok(userService.get(user.getId()));
    }
}
