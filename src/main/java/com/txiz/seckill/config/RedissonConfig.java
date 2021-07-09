package com.txiz.seckill.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Redisson分布式锁配置类
 *
 * @author Txiz
 * @date 2021 -07-09
 */
@Configuration
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson() throws IOException {
        //1、创建配置
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://121.196.198.1:6379");
        return Redisson.create(config);
    }
}
