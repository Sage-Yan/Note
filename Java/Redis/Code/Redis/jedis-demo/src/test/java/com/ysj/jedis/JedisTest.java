package com.ysj.jedis;

import com.ysj.jedis.util.JedisConnectionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

/**
 * @Classname JedisTest
 * @Description Jedis连接测试
 * @Date 2025/11/14 19:14
 * @Created by YanShijie
 */

public class JedisTest {

    private Jedis jedis;

    @BeforeEach
    void setUp() {
        // 1. 建立连接
        // jedis = new Jedis("192.168.147.101", 6379);
        jedis = JedisConnectionFactory.getJedis();
        // 2. 设置密码
        // jedis.auth("Ysj245913@.");
        // 3. 选择库
        jedis.select(0);
    }

    @Test
    void testString() {
        // 存入数据
        String result = jedis.set("name", "YanShijie");
        System.out.println("result" + result);
        //  获取数据
        String name = jedis.get("name");
        System.out.println("name" + name);
    }

    @AfterEach
    void tearDown() {
        if (jedis != null) {
            jedis.close();
        }
    }
}
