package com.txiz.seckill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类
 *
 * @author Txiz
 * @since 2021-06-22
 **/
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // String类型 key序列器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // String类型 value序列器
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // 哈希类型 key序列器
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // 哈希类型 key序列器
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}
