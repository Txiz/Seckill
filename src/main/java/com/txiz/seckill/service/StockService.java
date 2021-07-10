package com.txiz.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.txiz.seckill.entity.Stock;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Txiz
 * @since 2021 -06-22
 */
public interface StockService extends IService<Stock> {

    /**
     * Gets stock from mysql.
     *
     * @param sid the sid
     * @return the stock from mysql
     */
    Integer getStockFromMysql(Integer sid);

    /**
     * Gets stock from redis.
     *
     * @param sid the sid
     * @return the stock from redis
     */
    Integer getStockFromRedis(Integer sid);

    /**
     * Sets stock to redis.
     *
     * @param sid   the sid
     * @param count the count
     */
    void setStockToRedis(Integer sid, Integer count);
}
