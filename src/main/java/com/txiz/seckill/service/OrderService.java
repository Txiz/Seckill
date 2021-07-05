package com.txiz.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.txiz.seckill.entity.Order;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Txiz
 * @since 2021 -06-22
 */
public interface OrderService extends IService<Order> {


    /**
     * Create order by optimistic lock int.
     *
     * @param sid the sid
     * @return the int
     */
    int createOrderByOptimisticLock(Integer sid);


    /**
     * Create order by pessimistic lock int.
     *
     * @param sid the sid
     * @return the int
     */
    int createOrderByPessimisticLock(Integer sid);

    /**
     * Create order with verify hash int.
     *
     * @param sid        the sid
     * @param uid        the uid
     * @param verifyHash the sid
     * @return the int
     */
    int createOrderWithVerifyHash(Integer sid, Integer uid, String verifyHash);
}
