package com.txiz.seckill.service.impl;

import com.txiz.seckill.entity.Order;
import com.txiz.seckill.mapper.OrderMapper;
import com.txiz.seckill.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Txiz
 * @since 2021-06-22
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
