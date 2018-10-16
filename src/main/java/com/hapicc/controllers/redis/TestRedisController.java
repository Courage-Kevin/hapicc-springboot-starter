package com.hapicc.controllers.redis;

import com.hapicc.common.redis.RedisService;
import com.hapicc.pojo.HapiccJSONResult;
import com.hapicc.pojo.User;
import com.hapicc.utils.common.JsonUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("redis")
public class TestRedisController {

    @Autowired
    private Sid sid;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private RedisService redisService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("testString")
    public HapiccJSONResult testString() {

        String key = "test:string";

        Long expire = 7200L;
        String value = "Hello Redis~~~";

        if (stringRedisTemplate.hasKey(key)) {
            value = stringRedisTemplate.opsForValue().get(key);
            expire = stringRedisTemplate.getExpire(key);
        } else {
            stringRedisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("value", value);
        result.put("expire", expire);
        return HapiccJSONResult.ok(result);
    }

    @RequestMapping("testJedisPool")
    public HapiccJSONResult testJedisPool() {

        String key = "test:jedis:pool";

        long expire = 3600L;
        String value = "Hello JedisPool~~~";

        try (Jedis jedis = jedisPool.getResource()) {
            if (jedis.exists(key)) {
                value = jedis.get(key);
                expire = jedis.ttl(key);
            } else {
                jedis.set(key, value);
                jedis.expire(key, (int) expire);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("value", value);
        result.put("expire", expire);
        return HapiccJSONResult.ok(result);
    }

    @RequestMapping("testJedis")
    public HapiccJSONResult testJedis() {

        String key = "test:jedis";

        int expire = 3600;
        String value = "Hello Jedis~~~";

        Map<String, Object> result = new HashMap<>();

        redisService.withRedis(jedis -> {
            if (jedis.exists(key)) {
                result.put("value", jedis.get(key));
                result.put("expire", jedis.ttl(key));
            } else {
                jedis.set(key, value);
                jedis.expire(key, expire);
                result.put("value", value);
                result.put("expire", expire);
            }
        });

        return HapiccJSONResult.ok(result);
    }

    @RequestMapping("testJson")
    public HapiccJSONResult testJson() {

        String jsonKey = "test:json";
        String listJsonKey = "test:json:list";
        String setKey = "test:set:json";
        String listKey = "test:list:json";

        User user1 = createUser("User001");
        String userJson1 = JsonUtils.obj2Json(user1);

        User user2 = createUser("User002");
        String userJson2 = JsonUtils.obj2Json(user2);

        User user3 = createUser("User003");
        String userJson3 = JsonUtils.obj2Json(user3);

        stringRedisTemplate.opsForValue().set(jsonKey, userJson1, 5, TimeUnit.SECONDS);
        User redisUser = JsonUtils.json2Obj(stringRedisTemplate.opsForValue().get(jsonKey), User.class);

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        stringRedisTemplate.opsForValue().set(listJsonKey, JsonUtils.obj2Json(userList), 5, TimeUnit.SECONDS);
        List<User> redisUserList = JsonUtils.json2List(stringRedisTemplate.opsForValue().get(listJsonKey), User.class);

        stringRedisTemplate.opsForSet().add(setKey, userJson1, userJson2, userJson3);
        stringRedisTemplate.expire(setKey, 5, TimeUnit.SECONDS);
        Long setSize = stringRedisTemplate.opsForSet().size(setKey);
        User setPopUser = JsonUtils.json2Obj(stringRedisTemplate.opsForSet().pop(setKey), User.class);

        stringRedisTemplate.opsForList().leftPushAll(listKey, userJson1, userJson2, userJson3);
        stringRedisTemplate.expire(listKey, 5, TimeUnit.SECONDS);
        Long listSize = stringRedisTemplate.opsForList().size(listKey);
        User listLeftPopUser = JsonUtils.json2Obj(stringRedisTemplate.opsForList().leftPop(listKey), User.class);

        Map<String, Object> result = new HashMap<>();
        result.put("redisUser", redisUser);
        result.put("redisUserList", redisUserList);
        result.put("setSize", setSize);
        result.put("setPopUser", setPopUser);
        result.put("listSize", listSize);
        result.put("listLeftPopUser", listLeftPopUser);
        return HapiccJSONResult.ok(result);
    }

    @RequestMapping("testObj")
    public HapiccJSONResult testObject() {

        String objKey = "test:object";
        String listObjKey = "test:object:list";

        User user1 = createUser("User001");
        User user2 = createUser("User002");
        User user3 = createUser("User003");

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        redisService.setObj(objKey, user1, 5L);
        User redisUser = (User) redisService.getObj(objKey);

        redisService.setObj(listObjKey, userList, 5L);
        @SuppressWarnings("unchecked")
        List<User> redisUserList = (List<User>) redisService.getObj(listObjKey);

        Map<String, Object> result = new HashMap<>();
        result.put("redisUser", redisUser);
        result.put("redisUserList", redisUserList);
        return HapiccJSONResult.ok(result);
    }

    private User createUser(String name) {
        User user = new User();
        user.setName(name);
        user.setAge(18);
        user.setBirthday(new Date());
        user.setPassword(sid.next());
        return user;
    }
}
