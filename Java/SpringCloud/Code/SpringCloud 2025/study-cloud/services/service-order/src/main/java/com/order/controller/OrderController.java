package com.order.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.order.Order;
import com.order.properties.OrderProperties;
import com.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@RefreshScope // 激活配置自动刷新功能
@RestController
@RequestMapping("/order")
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderProperties orderProperties;

//    // 获取Nacos中的配置
//    @Value("${order.timeout}")
//    String orderTimeout;
//    @Value("${order.auto-confirm}")
//    String orderAutoConfirm;


    @GetMapping("/getConfig")
    public String getConfig() {
        return "orderTimeout: " + orderProperties.getTimeout() +
                ", orderAutoConfirm: " + orderProperties.getAutoConfirm() +
                ", dbUrl: " + orderProperties.getDbUrl();
    }

    /**
     * 创建订单
     */
    @GetMapping("/create")
    public Order createOrder(@RequestParam("userId") Long userId, @RequestParam("productId") Long productId) {
        return orderService.createOrder(productId, userId);
    }

    /**
     * 创建订单-秒杀
     */
    @GetMapping("/create/kill")
    // 自定义流控埋点
    @SentinelResource(value = "kill-order", fallback =  "killOrderFallback")
    public Order createKillOrder(@RequestParam("userId") Long userId, @RequestParam("productId") Long productId) {
        return orderService.createOrder(productId, userId);
    }

    public Order killOrderFallback(Long userId, Long productId, Throwable e) {
        log.info("fallback.....................");
        Order order = new Order() {{
            setId(-1L);
            setAddress("商品已下架");
        }};

        return order;
    }

    /**
     * 写数据
     */
    @GetMapping("/write")
    public String writeData() {
        return "write data success";
    }
    /**
     * 读数据
     */
    @GetMapping("/read")
    public String readData() {
        log.info("read success......");
        return "read data success";
    }

}
