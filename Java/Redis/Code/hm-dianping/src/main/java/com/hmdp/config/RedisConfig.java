package com.hmdp.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname RedisConfig
 * @Description Redis相关配置类
 * @Date 2025/11/19 15:26
 * @Created by YanShijie
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedissonClient redissonClient() {
        // 配置类
        Config config = new Config();
        // 添加redis地址 密码
        config.useSingleServer().setAddress("redis://192.168.147.100:6379").setPassword("Ysj245913@.");
        // 创建客户端
        return Redisson.create(config);
    }
}
