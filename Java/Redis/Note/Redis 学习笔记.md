# Redis7  学习笔记

作者：Shijie Yan

---

## 1. Redis 介绍

### 1.1 概念

**Redis：** **RE**mote **Di**ctionary **S**erver（远程字典服务器）

基于**内存**的一种**K、V键值对**内存数据库。

### 1.2 功能与作用

1. 分布式缓存
2. 内存存储和持久化（RDB + AOF 混合持久化）
   - **RDB 持久化** 是 Redis 将内存中的数据 **定期快照（Snapshot）** 保存到磁盘上的一种方式。
   - **AOF 持久化** 是 Redis 以 **追加命令日志** 的方式记录每次写操作。
3. 高可用架构搭配
   - 单机
   - 主存
   - 哨兵
   - 集群
4. 缓存穿透、击穿、雪崩
5. 分布式锁
6. 队列
7. 排行榜 + 点赞
8. ......

### 1.3 优势

1. 性能极高
2. 支持数据持久化
3. 支持数据备份

## 2. Redis 安装配置

```cmd
 redis-server [自定义配置文件路径]
 redis-cli # 启动
 auth [密码]
```

......

## 3. 通用命令

1. 查看通用命令

```shell
help @generic # 查看所用通用命令
help [命令] # 查看指定命令用法
```

2. 通用命令

```shell
1. KEYS pattern # 查看匹配的key（不建议在生产环境使用）
2. DEL key [key ...] # 删除一个或多个key
3. EXISTS key [key ...] # 判断key是否存在
4. EXPIRE key seconds [NX|XX|GT|LT] # 给一个key #设置有效期
	NX（Not Exist,）仅在 key 没有设置过期时间时，才设置新的过期时间。
	XX（Exist）：仅在 key 已经设置过期时间时，才设置新的过期时间。
	GT（Greater Than）：仅在新的过期时间（seconds）大于当前过期时间时，才设置新的过期时间。
	LT（Less Than）：仅在新的过期时间（seconds）小于当前过期时间时，才设置新的过期时间。
5. TTL key #查看一个key的剩余有效期
```

## 4. Redis 数据类型

> **官网介绍：**https://redis.ac.cn/docs/latest/develop/data-types/
>
> **String、Hash、List、Set、Sorted Set、Vertor Set、Stream、Bitmap、Bitfield、Geospatial、JSON、Probabilistic、Time series**

### 4.1 String

1. **介绍**
   - String 字符串类型，其value是字符串，但是根据格式不同又可以分为`string`、`int`（可以做自增、自减）、`float`（可以做自增、自减）3类。
   - 不论是那种格式，底层都以字节数组进行存储，只不过编码方式不同。
   - 字符串类型最大不能超过512M。
2. **常用命令**

|    命令     |                           介绍                            |
| :---------: | :-------------------------------------------------------: |
|     SET     |        添加或修改一个已经存在的String类型的键值对         |
|     GET     |                根据key获取String类型value                 |
|    MSET     |               批量添加多个String类型键值对                |
|    MGET     |           根据多个key获取多个String类型的value            |
|    INCR     |                    让一个整型key自增1                     |
|   INCRBY    |                让一个整型key自增并指定步长                |
| INCRBYFLOAT |               让一个浮点数key自增并指定步长               |
|    SETNX    | 添加一个String类型键值对，前提是这个key不存在，否则不执行 |
|    SETEX    |          添加一个String类型键值对，并指定有效期           |

### 4.2 Hash

1. **介绍**
   - Hash 类型也叫散列，其value是一个无序字典类似Java中的HashMap

2. **常用命令**

|                  命令                   |                             介绍                             |
| :-------------------------------------: | :----------------------------------------------------------: |
| HSET key field value [field value ...]  |              添加或者修改hash类型key的field字段              |
|             HGET key field              |                 获取一个hash类型的field的值                  |
| HMSET key field value [field value ...] |               批量添加多个hash类型key的field值               |
|       HMGET key field [field ...]       |                 批量添加多个hash类型field值                  |
|               HGETALL key               |          获取一个hash类型的key中的所有field和value           |
|                HKEYS key                |              获取一个hash类型的key中的所有field              |
|                HVALS key                |               获取一个hash类型key中的所有value               |
|       HINCRBY key field increment       |            让一个hash类型key的字段自增并指定步长             |
|         HSETNX key field value          | 添加一个hash类型的key的field值，前提是这个field不存在，否则不执行 |

### 4.3 List

1. **介绍**
   - Redis中的List的类型与Java中的LinkedList类似，可以看做一个双向链表结构。既可以支持正向检索，也可以支持反向检索。
   - 特征也与LinkedList类似，有序、元素可以重复、插入和删除快、查询速度一般。
   - 常用来存储一个有序数据，例如：朋友圈点赞列表，评论列表等。
2. **常用命令**

|              命令               |                             介绍                             |
| :-----------------------------: | :----------------------------------------------------------: |
| LPUSH key element [element ...] |                 向列表左端插入一个或多个元素                 |
|        LPOP key [count]         |          移除并返回列表左端第一个元素，没有返回nil           |
| RPUSH key element [element ...] |                 向列表右端插入一个或多个元素                 |
|        RPOP key [count]         |          移除并返回列表右端第一个元素，没有返回nil           |
|      LRANGE key start stop      |                    返回角标范围内所有元素                    |
|          BLPOP与BRPOP           | 与LPOP与RPOP类似，只不过没有元素世等待指定时间，而不是直接返回nil |

3. **扩展**
   - 利用List模拟栈、队列、阻塞队列。

### 4.4 Set

1. **介绍**
   - Redis中的Set结构与Java中的HashSet类似，可以看做一个value为null的HashMap。因为也是一个Hash表，因此具备与HashSet类似的特征。
   - 无序、元素不可重复、查找快、支持交集、并集、差集等功能。
2. **常用命令**

|             命令             |            介绍             |
| :--------------------------: | :-------------------------: |
| SADD key member [member ...] |  向set中添加一个或多个元素  |
| SREM key member [member ...] |     移除set中的指定元素     |
|          SCARD key           |     返回set中元素的个数     |
|     SISMEMBER key member     | 判断一个元素是否存在于set中 |
|         SMEMBERS key         |     获取set中的所有元素     |
|     SINTER key [key ...]     |            交集             |
|     SDIFF key [key ...]      |            差集             |
|     SUNION key [key ...]     |            并集             |

### 4.5 SortedSet

1. **介绍**
   - Redis的SortedSet是一个可排序的set集合，与Java中的TreeSet有些类似，但底层数据结构差别很大。
   - SortedSet中的每一个元素都带一个score属性，可以基于score属性对元素进行排序，底层的实现是一个跳表（SkipList）加hash表。
   - 可排序、元素不重复、查询速度快。
   - 因为SortedSet的可排序特性，经常被用来实现排行榜这样的功能。

2. **常用命令**

|                             命令                             |                    介绍                    |
| :----------------------------------------------------------: | :----------------------------------------: |
| ZADD key [NX\|XX] [GT\|LT] [CH] [INCR] score member [score member ...] |   添加或编辑一个或多个元素到sortedset中    |
|                 ZREM key member [member ...]                 |                删除指定元素                |
|                      ZSCORE key member                       |           获取指定元素的score值            |
|                       ZRANK key member                       |             获取指定元素的排名             |
|                          ZCARD key                           |                获取元素个数                |
|                      ZCOUNT key min max                      |      统计score在指定范围内的元素个数       |
|                 ZINCRBY key increment member                 |      让指定元素自增，increment为步长       |
| ZRANGE key start stop [BYSCORE\|BYLEX] [REV] [LIMIT offset count] [WITHSCORES] |  按照score排序好，获取指定排名范围的元素   |
| ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT offset count]  | 按照score排序好，获取指定score范围内的元素 |
|                    ZDIFF、ZINTER、ZUNION                     |              差集、交集、并集              |

（注意：所有的排名默认都是升序，如果要降序则在命令Z后面添加REV即可）

## 5. Redis Java客户端

### 5.1 三种 Redis 客户端

1. Jedis：以Redis命令作为方法名称，学习成本低，简单实用。但是Jedis实例是线程不安全的，多线程环境下需要基于连接池来使用。
2. Lettuce：Lettuce是基于Natty实现的，支持同步，异步和响应式编程方式，并且是线程安全的。支持Redis和哨兵模式、集群模式、管道模式。
3. Redisson：是一个基于Redis实现分布式、可伸缩的Java数据结构集合。包含诸如Map、Queue、Lock、Semaphore、AtomicLong等强大功能。

### 5.2 Jedis

#### 5.2.1 基本使用

1. 引入依赖

```xml
<!--jedis-->
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>7.0.0</version>
</dependency>
```

2. 建立连接

```java
@BeforeEach
void setUp() {
    // 1. 建立连接
    jedis = new Jedis("192.168.147.101", 6379);
    // 2. 设置密码
    jedis.auth("Ysj245913@.");
    // 3. 选择库
    jedis.select(0);
}
```

3. 测试string

```java
@Test
void testString() {
    // 存入数据
    String result = jedis.set("name", "YanShijie");
    System.out.println("result" + result);
    //  获取数据
    String name = jedis.get("name");
    System.out.println("name" + name);
}
```

4. 释放资源

```java
@AfterEach
void tearDown() {
    if (jedis != null) {
        jedis.close();
    }
}
```

#### 5.2.2 Jedis连接池

> Jedis本身是线程不安全的，并且频繁的创建和销毁连接会有性能损耗，因此我们推荐大家使用Jedis连接池替代Jedis的直连方式。

```java
public class JedisConnectionFactory {

    private static final JedisPool jedisPool;

    // 静态代码块类加载时执行一次
    static {
        // 配置连接池
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 最大8个连接
        poolConfig.setMaxIdle(8);
        // 最大预备8个连接
        poolConfig.setMaxTotal(8);
        // 最小空闲连接
        poolConfig.setMinIdle(0);
        // 最大等待时间
        poolConfig.setMaxWait(Duration.ofMillis(1000));
        // 创建连接池对象
        jedisPool = new JedisPool(poolConfig, "192.168.147.101", 6379, 3000, "Ysj245913@.");
    }

    public static Jedis getJedis() {
        return jedisPool.getResource();
    }
}
```

### 5.3 SpringDataRedis

#### 5.3.1 基本介绍

1. 提供了对不同Redis客户端的整合（Lettuce 和 Jedis）。
2. 提供了RedisTemplate统一API来操作Redis。
3. 支持Redis的发布订阅模型、Redis哨兵、Redis集群、基于Lettuce的响应式编程、基于JDK、JSON、字符串、Spring对象的数据序列化与反序列化。
4. 支持Redis的JDKCollection实现。

#### 5.3.2 基本使用

> RedisTemplate工具类，封装了各种对Redis的操作。并且将不同数据类型的操作API封装到不同类型中。

1. 引入依赖

```xml
<!--redis-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
    <version>3.5.7</version>
</dependency>
<!--连接池依赖-->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
    <version>2.12.1</version>
</dependency>
```

2. 测试String

```java
@SpringBootTest
public class RedisDemoApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void testString() {
        redisTemplate.opsForValue().set("name", "YanShijie");
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println(name);
    }
}
```

#### 5.3.3 序列化方式

> RedisTemplate可以接收任意Object作为值写入Redis，只不过写入之前会把Object序列化为字节形式，默认采用JDK序列化。
>
> 缺点：可读性差、内存占用大、出现乱码

1. 问题一
   - 默认序列化方式会发生编码问题导致数据不一致。

2. 解决方式

> 设置序列化方式（JSON序列化器）

```java
package com.ysj.redis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @Classname RedisConfig
 * @Description Redis配置类
 * @Date 2025/11/14 20:03
 * @Created by YanShijie
 */
@Configuration
public class RedisConfig {
    
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 1. 创建RedisTemplate对象
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 设置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // c创建序列化工具
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        // 设置key序列化工具
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        // 设置value的序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        // 返回
        return redisTemplate;
    }
}
```

3. 问题二
   - 为了在反序列化时知道对象的类型，Json序列化器会将类的class类型写入json结果中，存入Redis，会带来额外的内存开销。

4. 解决方式

> 为了节省内存空间，我们并不会使用JSON序列化器来处理value，而是统一使用String序列化器，要求只能存储String类型的key和value。当需要存储Java对象时，手动完成对象的序列化和反序列化。
>
> Spring默认提供了一个StringRedisTemplat类他的key和value序列化方式默认就是String方式。省区了自定义RedisTempalte的过程

```java
StringRedisTemplate... // 使用方式同RedisTemplate相同不过要进行手动序列化
```



## 6. 企业实战

### 6.1 短信登录



​	

### 6.2 查询缓存





### 6.3 优惠卷秒杀





### 6.4 分布式锁





### 6.5 秒杀优化





### 6.6 Redis消息队列







