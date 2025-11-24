package com.order.service.impl;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.order.Order;
import com.order.feign.ProductFeignClient;
import com.order.service.OrderService;
import com.product.Product;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    LoadBalancerClient loadBalancerClient;
    @Autowired
    ProductFeignClient productFeignClient;

    @Override
    @SentinelResource(value = "createOrder", blockHandler = "createOrderFallbackHandler")
    public Order createOrder(Long productId, Long userId) {
//        Product product = getProductBalancedAnnotation(productId);
        Product product = productFeignClient.getProductById(productId);

        Order order = new Order();
        order.setId(1L);
        // 总金额
        order.setTotalAmount(product.getPrice().multiply(new BigDecimal(product.getNum())));
        order.setUserId(userId);
        order.setNickName("小明");
        order.setAddress("成都");
        // 商品列表
        order.setProducts(List.of(product));
        return order;
    }

    /**
     * sentinel 兜底回调
     * @param productId
     * @param userId
     * @param exception
     * @return
     */
    public Order createOrderFallbackHandler(Long productId, Long userId, BlockException exception) {
        log.error("createOrder fallbackHandler, productId: {}, userId: {}", productId, userId, exception);
        return new Order(){{
            setId(0L);
            setTotalAmount(BigDecimal.ZERO);
            setNickName("未知用户");
            setAddress("异常信息：" + exception.getClass());
        }};
    }

    /**
     * 远程调用获取商品信息
     */
    private Product getProduct(Long productId) {
        // 获取到商品服务所在的ip+端口
        List<ServiceInstance> instances = discoveryClient.getInstances("server-product");
        ServiceInstance serviceInstance = instances.get(0);
        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/product/getProduct/" + productId;
        // 发送远程请求
        log.info("发送远程请求：{}", url);
        Product product = restTemplate.getForObject(url, Product.class);

        return product;
    }

    /**
     * 远程调用获取商品信息 - @LoadBalanced注解实现负载均衡
     */
    private Product getProductBalancedAnnotation(Long productId) {
        String url = "http://server-product/product/getProduct/" + productId;
        // 发送远程请求
        log.info("发送远程请求：{}", url);
        Product product = restTemplate.getForObject(url, Product.class);

        return product;
    }

    /**
     * 远程调用获取商品信息 - 负载均衡
     */
    private Product getProductBalanced(Long productId) {
        // 获取到商品服务所在的ip+端口
        ServiceInstance choose = loadBalancerClient.choose("server-product");
        String url = "http://" + choose.getHost() + ":" + choose.getPort() + "/product/getProduct/" + productId;
        // 发送远程请求
        log.info("发送远程请求：{}", url);
        Product product = restTemplate.getForObject(url, Product.class);

        return product;
    }
}
