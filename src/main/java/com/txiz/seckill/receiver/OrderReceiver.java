package com.txiz.seckill.receiver;

import com.txiz.seckill.service.OrderService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 数据库订单生成并修改库存
 *
 * @author Txiz
 * @date 2021-07-10
 **/
@Component
public class OrderReceiver {

    @Resource
    private OrderService orderService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ORDER", durable = "true"),
            exchange = @Exchange(value = "ORDER_EXCHANGE"),
            key = {"ORDER_KEY"}
    ))
    public void send(Integer sid) {
        orderService.createOrderByOptimisticLock(sid);
    }
}

