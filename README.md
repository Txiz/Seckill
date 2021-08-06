# 小型秒杀系统

### 涉及技术

Springboot 2.5、MybatisPlus、Knife4j、Redisson、Guaua、MySQL、Redis 

### 项目描述

针对实现下单扣减库存这一流程。使用乐观锁、悲观锁、分布式锁等不同的方式防止超卖；使用令牌桶算法进行限流；使用MD5算法加密抢购接口；使用Redis限制单用户的抢购频率；使用延时双删、canal实现缓存数据库双写一致性；使用消息队列RabbitMQ实现删除重试机制以及下单的异步处理。
