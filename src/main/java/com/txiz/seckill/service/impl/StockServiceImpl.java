package com.txiz.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.txiz.seckill.entity.Stock;
import com.txiz.seckill.mapper.StockMapper;
import com.txiz.seckill.service.StockService;
import org.springframework.data.redis.core.StringRedisTemplate;
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
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {

    @Resource
    private StockMapper stockMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Integer getStockFromMysql(Integer sid) {
        Stock stock = stockMapper.selectById(sid);
        return stock.getCount() - stock.getSale();
    }

    @Override
    public Integer getStockFromRedis(Integer sid) {
        String key = "stock_count_" + sid;
        String value = stringRedisTemplate.opsForValue().get(key);
        if (value != null) {
            return Integer.parseInt(value);
        }
        return null;
    }

    @Override
    public void setStockToRedis(Integer sid, Integer count) {
        String key = "stock_count_" + sid;
        String value = String.valueOf(count);
        stringRedisTemplate.opsForValue().set(key, value, 60L, TimeUnit.MINUTES);
    }
}
