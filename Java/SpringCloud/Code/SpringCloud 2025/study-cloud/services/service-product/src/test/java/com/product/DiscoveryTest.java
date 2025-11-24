package com.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.DiscoveryClient;

@SpringBootTest
public class DiscoveryTest {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Test
    public void discoveryTest() {
        for (String service : discoveryClient.getServices()) {
            System.out.println("service: " + service);

            // 获取ip+port
            discoveryClient.getInstances(service).forEach(instance -> {
                System.out.println("host: " + instance.getHost() + ", port: " + instance.getPort());
            });
        }

    }
}
