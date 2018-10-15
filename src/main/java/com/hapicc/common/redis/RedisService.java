package com.hapicc.common.redis;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    public void withRedis(Consumer<Jedis> consumer) {
        try (Jedis jedis = jedisPool.getResource()) {
            consumer.accept(jedis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Boolean setObj(final String key, final Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean setObj(final String key, final Object value, final Long seconds) {
        try {
            redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Object getObj(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * Delete all keys matching the given pattern.
     * 
     * @param pattern must not be null.
     * @return The number of keys that were removed.
     */
    public Long delPattern(final String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            return redisTemplate.delete(keys);
        }
        return 0L;
    }
}
