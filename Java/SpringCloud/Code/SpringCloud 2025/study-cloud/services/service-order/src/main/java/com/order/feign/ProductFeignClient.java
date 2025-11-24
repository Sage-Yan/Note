package com.order.feign;

import com.order.feign.failback.ProductFeignClientFailBack;
import com.product.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "server-product", fallback = ProductFeignClientFailBack.class)
public interface ProductFeignClient {

    /**
     * 在Openfegin是发送get请求
     */
    @GetMapping("/product/getProduct/{id}")
    Product getProductById(@PathVariable("id") Long id);

}
