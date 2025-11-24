package com.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

@SpringBootTest
public class LoadBalancerTest {
    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Test
    void Test() {
        ServiceInstance choose = loadBalancerClient.choose("server-product");
        System.out.println("loadBalancer choose: " + choose.getHost() + ":" + choose.getPort());
        ServiceInstance choose1 = loadBalancerClient.choose("server-product");
        System.out.println("loadBalancer choose: " + choose1.getHost() + ":" + choose1.getPort());
        ServiceInstance choose2 = loadBalancerClient.choose("server-product");
        System.out.println("loadBalancer choose: " + choose2.getHost() + ":" + choose2.getPort());
    }
}
