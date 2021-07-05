package com.txiz.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.txiz.seckill.entity.Order;
import com.txiz.seckill.entity.Stock;
import com.txiz.seckill.mapper.OrderMapper;
import com.txiz.seckill.mapper.StockMapper;
import com.txiz.seckill.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Txiz
 * @since 2021-06-22
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Resource
    private StockMapper stockMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int createOrderByOptimisticLock(Integer sid) {
        // 检查库存
        Stock stock = stockMapper.selectById(sid);
        if (stock.getSale().equals(stock.getCount())) {
            throw new RuntimeException("库存不足！");
        }
        // Mybatis Plus 乐观锁更新库存
        LOGGER.info("试图更新库存");
        stock.setSale(stock.getSale() + 1);
        int success = stockMapper.updateById(stock);
        if (success == 0) {
            throw new RuntimeException("版本号不匹配，乐观锁更新失败！");
        }
        return createOrder(stock);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int createOrderByPessimisticLock(Integer sid) {
        // 检查库存，MySQL select for update 语句实现悲观锁
        Stock stock = stockMapper.selectByIdForUpdate(sid);
        if (stock.getSale().equals(stock.getCount())) {
            throw new RuntimeException("库存不足");
        }
        // 不使用乐观锁，更新库存
        Stock newStock = new Stock();
        newStock.setSid(sid);
        newStock.setSale(stock.getSale() + 1);
        int success = stockMapper.updateById(newStock);
        if (success == 0) {
            throw new RuntimeException("库存更新异常！");
        }
        return createOrder(stock);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int createOrderWithVerifyHash(Integer sid, Integer uid, String verifyHash) {
        // 使用sid + uid 作为Redis的key
        String key = "seckill_" + sid + "_" + uid;
        // 使用key从Redis中获取Hash值
        String value = (String) redisTemplate.opsForValue().get(key);
        // 验证value是否存在
        if (value == null) {
            throw new RuntimeException("Hash值不存在，可以能是已经过期！");
        }
        // 验证Hash值
        if (!value.equals(verifyHash)) {
            throw new RuntimeException("Hash值与Redis中不一致！");
        }
        LOGGER.info("验证Hash值成功");
        // 乐观锁更新库存
        return createOrderByOptimisticLock(sid);
        // 悲观锁更新库存
        // return createOrderByPessimisticLock(sid);
    }

    private int createOrder(Stock stock) {
        // 创建订单
        Order order = new Order();
        order.setSid(stock.getSid());
        order.setUid(1);
        order.setName(stock.getName());
        order.setCreateTime(new Date());
        // 保存订单
        if (!save(order)) {
            throw new RuntimeException("订单保存异常！");
        }
        return stock.getCount() - stock.getSale();
    }
}
