package com.study.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GateWayMainApplicaiton {
    public static void main(String[] args) {
        SpringApplication.run(GateWayMainApplicaiton.class);
    }
}
