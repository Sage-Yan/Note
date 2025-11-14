package com.ysj.jedis.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * @Classname JedisConnectionFactory
 * @Description
 * @Date 2025/11/14 19:25
 * @Created by YanShijie
 */
public class JedisConnectionFactory {

    private static final JedisPool jedisPool;

    // 静态代码块类加载时执行一次
    static {
        // 配置连接池
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 最大8个连接
        poolConfig.setMaxIdle(8);
        // 最大预备8个连接
        poolConfig.setMaxTotal(8);
        // 最小空闲连接
        poolConfig.setMinIdle(0);
        // 最大等待时间
        poolConfig.setMaxWait(Duration.ofMillis(1000));
        // 创建连接池对象
        jedisPool = new JedisPool(poolConfig, "192.168.147.101", 6379, 3000, "Ysj245913@.");
    }

    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

}
