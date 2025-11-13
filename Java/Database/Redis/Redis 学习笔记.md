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

| 命令        | 介绍                                                      |
| ----------- | --------------------------------------------------------- |
| SET         | 添加或修改一个已经存在的String类型的键值对                |
| GET         | 根据key获取String类型value                                |
| MSET        | 批量添加多个String类型键值对                              |
| MGET        | 根据多个key获取多个String类型的value                      |
| INCR        | 让一个整型key自增1                                        |
| INCRBY      | 让一个整型key自增并指定步长                               |
| INCRBYFLOAT | 让一个浮点数key自增并指定步长                             |
| SETNX       | 添加一个String类型键值对，前提是这个key不存在，否则不执行 |
| SETEX       | 添加一个String类型键值对，并指定有效期                    |

### 4.2 Hash

1. 介绍

   - Hash 类型也叫散列，其value是一个无序字典类似Java中的HashMap

   
