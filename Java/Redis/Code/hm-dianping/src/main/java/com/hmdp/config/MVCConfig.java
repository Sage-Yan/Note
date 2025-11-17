package com.hmdp.config;

import com.hmdp.utils.LoginInterceptor;
import com.hmdp.utils.RefreshInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Classname MVCConfig
 * @Description MVC配置
 * @Date 2025/11/15 11:49
 * @Created by YanShijie
 */
@Configuration
@RequiredArgsConstructor
public class MVCConfig implements WebMvcConfigurer {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 默认按照添加顺序执行,但是可以通过order设置优先级，小的先执行。
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/user/login", "/user/code", "/shop/**", "/blog/hot", "/shop-type/**", "/upload/**", "/voucher/**"
                )
                .order(1);

        registry.addInterceptor(new RefreshInterceptor(stringRedisTemplate))
                .addPathPatterns("/**")
                .order(0);
    }
}
