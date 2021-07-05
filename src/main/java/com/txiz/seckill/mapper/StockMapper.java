package com.txiz.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.txiz.seckill.entity.Stock;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Txiz
 * @since 2021 -06-22
 */
public interface StockMapper extends BaseMapper<Stock> {

    /**
     * Select by id for update stock.
     *
     * @param sid the sid
     * @return the stock
     */
    Stock selectByIdForUpdate(Integer sid);
}
