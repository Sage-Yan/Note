package com.ysj.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @Classname RedisDemoApplicationTests
 * @Description Redis测试
 * @Date 2025/11/14 19:47
 * @Created by YanShijie
 */
@SpringBootTest
public class RedisDemoApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void testString() {
        redisTemplate.opsForValue().set("name", "YanShijie");
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println(name);
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    @Test
    void testStringRedisTemplate() {

    }

}
