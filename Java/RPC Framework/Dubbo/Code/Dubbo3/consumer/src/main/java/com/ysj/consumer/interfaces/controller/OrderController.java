package com.ysj.consumer.interfaces.controller;

import com.ysj.consumer.application.service.OrderApplicationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname OrderController
 * @Description 控制类
 * @Date 2025/10/24 16:49
 * @Created by YanShijie
 */
@RestController
public class OrderController {

    @Resource
    private OrderApplicationService orderApplicationService;

    @GetMapping("/order")
    public String getOrder() {
        return orderApplicationService.getOrder();
    }

}
