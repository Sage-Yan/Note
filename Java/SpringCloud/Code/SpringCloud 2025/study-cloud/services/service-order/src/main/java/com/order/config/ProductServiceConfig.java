package com.order.config;

import feign.Logger;
import feign.Retryer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ProductServiceConfig {

    /**
     * RestTemplate是spring提供的线程安全的http客户端，用于发送http请求
     */
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    /**
     * 开启feign日志
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

//    @Bean
    Retryer retryer() {
        return new Retryer.Default();
    }
}
