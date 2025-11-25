package com.ysj.consumer.application.service;

import com.ysj.provider.api.facade.UserFacade;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * @Classname OrderService
 * @Description 服务类
 * @Date 2025/10/24 16:50
 * @Created by YanShijie
 */
@Service
public class OrderApplicationService {

    @DubboReference(version = "1.0")
    private UserFacade userFacade;

    public String getOrder() {
        return userFacade.getUser();
    }
}
