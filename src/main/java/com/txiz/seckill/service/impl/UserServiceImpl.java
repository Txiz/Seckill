package com.txiz.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.txiz.seckill.entity.User;
import com.txiz.seckill.mapper.UserMapper;
import com.txiz.seckill.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Txiz
 * @since 2021-06-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public String getVerifyHash(Integer sid, Integer uid) {
        // 使用sid + uid 作为Redis的key
        String key = "seckill_" + sid + "_" + uid;
        // 调用加密工具生成加密串，这里就假装加密了
        String value = "already encrypt " + sid + " " + uid;
        // 保存到Redis里面，这里就保存3分钟
        long time = 3L;
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);
        LOGGER.info("Redis写入：{} {}", key, value);
        return value;
    }

    @Override
    public int addUserCount(Integer uid) {
        // 使用uid 作为Redis的key
        String key = "limit_" + uid;
        // 使用key从Redis中获取缓存
        Integer value = (Integer) redisTemplate.opsForValue().get(key);
        int limit = 0;
        // 如果key不存在，就写入key，值为0；反之，就值加1，重新存入key
        // 统计频率就统计1分钟内的
        long time = 1L;
        if (value != null) {
            limit = value + 1;
        }
        redisTemplate.opsForValue().set(key, limit, time, TimeUnit.MINUTES);
        LOGGER.info("Redis写入：{} {}", key, limit);
        return limit;
    }

    @Override
    public boolean checkUserCount(Integer uid) {
        // 使用uid 作为Redis的key
        String key = "limit_" + uid;
        // 使用key从Redis中获取缓存
        Integer value = (Integer) redisTemplate.opsForValue().get(key);
        // 如果key不存在，证明无记录
        if (value == null) {
            LOGGER.error("该用户疑似没有访问记录");
            return true;
        }
        // 设置允许值，假设1分钟只允许10次
        int ALLOW_COUNT = 10;
        return value > ALLOW_COUNT;
    }
}
