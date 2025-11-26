--1.参数列表
--1.1 优惠卷ID
local voucherId = ARGV[1];
--1.2 用户ID
local userId = ARGV[2];
--1.3 订单ID
local orderId = ARGV[3];


--2.数据key
--2.1 库存key
local stockKey = "seckill:stock:" .. voucherId;
--2.2订单key
local orderKey = "seckill:order:" .. voucherId;

--3.脚本业务
--3.1判断库存是否充足
if tonumber(redis.call('get', stockKey)) <= 0 then
    --3.2库存不足
    return 1
end
--3.3判断用户是否重复下单 SISMEMBER orderKey userId
if (redis.call('sismember', orderKey, userId) == 1) then
    --3.4存在说明用户重复下单
    return 2
end
-- 3.5扣减库存 incrby stockKey -1
redis.call('incrby', stockKey, -1);
-- 3.6用户下单(保存用户) sadd orderKey userId
redis.call('sadd', orderKey, userId);
-- 3.7发送消息到队列中 XADD stream.orders(队列名称) * (自动生成) k1 v1 k2 v2 ...
redis.call('xadd', 'stream.orders', '*', 'userId', userId, 'voucherId', voucherId, 'id', orderId)
return 0;