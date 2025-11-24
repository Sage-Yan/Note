package com.order.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


/**
 * 使用@ConfigurationProperties注解来批量读取配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "order")
public class OrderProperties {

    String timeout;
    String autoConfirm;
    String dbUrl;
}
