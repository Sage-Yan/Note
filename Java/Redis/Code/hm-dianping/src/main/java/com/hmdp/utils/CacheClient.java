package com.hmdp.utils;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.hmdp.utils.RedisConstants.*;

/**
 * @Classname CacheClient
 * @Description 缓存工具类
 * @Date 2025/11/16 15:20
 * @Created by YanShijie
 */
@Component
@RequiredArgsConstructor
public class CacheClient<T> {

    private final StringRedisTemplate stringRedisTemplate;

    // 线程池
    public static final ExecutorService CACHE_REBUILDER_EXECUTOR = Executors.newFixedThreadPool(10);

    // 设置普通缓存
    public void set(String key, T value, Long time, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), time, timeUnit);
    }

    // 设置逻辑过时缓存
    public void setWithLogicExpire(String key, T value, Long time, TimeUnit timeUnit) {
        // 设置逻辑过期
        RedisData<T> redisData = new RedisData<>();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(timeUnit.toSeconds(time)));
        // 写入redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    /**
     * 缓存穿透
     * @param keyPrefix key前缀
     * @param id 查询id
     * @param type 反序列化类型
     * @param dbFallBack 缓存重建逻辑
     * @param time 时间
     * @param timeUnit 时间单位
     * @return 返回结果
     * @param <R> id类型
     */
    public <R> T queryWithPassThrough (String keyPrefix, R id, Class<T> type, Function<R, T> dbFallBack, Long time, TimeUnit timeUnit) {
        String key = keyPrefix + id;
        // 从redis查询缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        // 判断是否存在
        if (StrUtil.isNotBlank(json)) {
            // 存在直接返回
            return JSONUtil.toBean(json, type);
        }
        // 缓存击穿
        if(json != null) {
            return null;
        }
        // 不存在 更具id查询数据库
        T r = dbFallBack.apply(id);
        // 不存在 处理缓存穿透
        if(r == null) {
            // 将空值写入redis
            stringRedisTemplate.opsForValue().set(key,"", CACHE_NULL_TTL, TimeUnit.MINUTES);
            // 返回null
            return null;
        }
        // 存在写入Redis
        this.set(key, r, time, timeUnit);

        return r;
    }


    /**
     * 逻辑过时解决缓存击穿
     * @param keyPrefix key前缀
     * @param id 查询id
     * @param type 反序列化类型
     * @param dbFallBack 查询逻辑
     * @param time 时间
     * @param timeUnit 时间单位
     * @return 数据
     * @param <R> ID类型
     */
    public <R> T queryWithLogicalExpire(String keyPrefix, R id, Class<T> type, Function<R, T> dbFallBack, Long time, TimeUnit timeUnit) {
        // 缓存key
        String key = keyPrefix + id;
        // 互斥锁key
        String lockKey = LOCK_SHOP_KEY + id;
        // 1. 尝试从redis查询缓存
        String shopJson = stringRedisTemplate.opsForValue().get(key);
        // 2. 判断是否命中
        if (StrUtil.isBlank(shopJson)) {
            // 3. 未命中
            return null;
        }
        // 4. 命中需要先把json反序列化对象
        RedisData<T> redisData = JSONUtil.toBean(shopJson, new TypeReference<RedisData<T>>() {}, false);
        LocalDateTime expireTime = redisData.getExpireTime();
        T data = redisData.getData();
        // 5. 判断是否过期
        if (expireTime.isAfter(LocalDateTime.now())) {
            // 5.1 未过期直接返回数据
            return data;
        }
        // 5.2 过期进行缓存重建
        // 6. 缓存重建
        // 6.1 获取互斥锁
        boolean isLock = tryLock(lockKey);
        // 6.2 判断是否获取锁成功
        if (isLock) {
            // 6.3 成功，开启独立线程 实现缓存重建
            CACHE_REBUILDER_EXECUTOR.submit(() -> {
                // 重建缓存
                try {
                    // 查询数据库
                    T res = dbFallBack.apply(id);
                    // 写入redis
                    this.setWithLogicExpire(key, res, time, timeUnit);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    // 释放锁
                    unlock(lockKey);
                }
            });
        }
        // 6.4 返回数据
        return data;
    }

    /**
     * 上锁 setnx
     * @param key key
     * @return 是否成功
     */
    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    /**
     * 释放锁
     * @param key key
     */
    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }
}
