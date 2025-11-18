package com.hmdp;

import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.hmdp.utils.CacheClient;
import com.hmdp.utils.RedisConstants;
import com.hmdp.utils.RedisIdWorker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@SpringBootTest
class HmDianPingApplicationTests {

    @Autowired
    RedisIdWorker redisIdWorker;

    @Autowired
    CacheClient cacheClient;

    @Autowired
    IShopService shopService;

    private ExecutorService es = Executors.newFixedThreadPool(500);

    Long id2 = 1L;

    @Test
    void testSetCache() {
        cacheClient.setWithLogicExpire(RedisConstants.CACHE_SHOP_KEY, 1L, 2L, TimeUnit.MINUTES);
    }

    @Test
    void testIdWorker() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(300);

        Runnable task = () -> {
            for (int i = 0; i < 100; i++) {
                long id = redisIdWorker.nextId("order");
                System.out.println("id = " + id);
            }
            countDownLatch.countDown();
        };
        long start = System.currentTimeMillis();
        for (int i = 0; i < 300; i++) {
            es.submit(task);
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println("运行时间: " + (end - start));
    }

}
