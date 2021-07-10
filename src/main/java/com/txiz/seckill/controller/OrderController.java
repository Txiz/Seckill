package com.txiz.seckill.controller;


import com.google.common.util.concurrent.RateLimiter;
import com.txiz.seckill.service.OrderService;
import com.txiz.seckill.service.StockService;
import com.txiz.seckill.service.UserService;
import com.txiz.seckill.util.RabbitUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Txiz
 * @since 2021-06-22
 */
@RestController
@RequestMapping("/seckill/order")
@Api(tags = "订单控制器")
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Resource
    private OrderService orderService;

    @Resource
    private StockService stockService;

    @Resource
    private UserService userService;

    RateLimiter rateLimiter = RateLimiter.create(10);

    @GetMapping("/createOrderByOptimisticLock/{sid}")
    @ApiOperation(value = "使用乐观锁进行下单操作")
    public String createOrderByOptimisticLock(@PathVariable Integer sid) {
        try {
            int remain = orderService.createOrderByOptimisticLock(sid);
            LOGGER.info("购买成功，剩余库存为: {}", remain);
            return "购买成功，剩余库存为：" + remain;
        } catch (Exception e) {
            LOGGER.error("购买失败：{}", e.getMessage());
            return "购买失败，库存不足";
        }
    }

    @GetMapping("/createOrderByOptimisticLockWithRateLimiter/{sid}")
    @ApiOperation(value = "使用乐观锁+令牌桶限流进行下单操作")
    public String createOrderByOptimisticLockWithRateLimiter(@PathVariable Integer sid) {
        // 1. 阻塞式限流获取令牌
        // LOGGER.info("等待时间 " + rateLimiter.acquire());
        // 2. 非阻塞式限流获取令牌
        int timeout = 1000;
        if (!rateLimiter.tryAcquire(timeout, TimeUnit.MILLISECONDS)) {
            LOGGER.warn("你被限流了，真不幸，直接返回失败");
            return "你被限流了，真不幸，直接返回失败";
        }
        try {
            int remain = orderService.createOrderByOptimisticLock(sid);
            LOGGER.info("购买成功，剩余库存为: {}", remain);
            return "购买成功，剩余库存为：" + remain;
        } catch (Exception e) {
            LOGGER.error("购买失败：{}", e.getMessage());
            return "购买失败：" + e.getMessage();
        }
    }

    @GetMapping("/createOrderByPessimisticLock/{sid}")
    @ApiOperation(value = "使用悲观锁进行下单操作")
    public String createOrderByPessimisticLock(@PathVariable Integer sid) {
        try {
            int remain = orderService.createOrderByPessimisticLock(sid);
            LOGGER.info("购买成功，剩余库存为: {}", remain);
            return "购买成功，剩余库存为：" + remain;
        } catch (Exception e) {
            LOGGER.error("购买失败：{}", e.getMessage());
            return "购买失败：" + e.getMessage();
        }
    }

    @GetMapping("/getVerifyHash")
    @ApiOperation(value = "获取验证值")
    public String getVerifyHash(@RequestParam Integer sid, @RequestParam Integer uid) {
        try {
            String hash = userService.getVerifyHash(sid, uid);
            return "请求下单验证Hash值为：" + hash;
        } catch (Exception e) {
            LOGGER.error("获取验证Hash失败：{}", e.getMessage());
            return "获取验证Hash失败：" + e.getMessage();
        }
    }

    @GetMapping("/createOrderWithVerifyHash")
    @ApiOperation(value = "使用验证值进行下单操作")
    public String createOrderWithVerifyHash(@RequestParam Integer sid, @RequestParam Integer uid, @RequestParam String verifyHash) {
        try {
            int remain = orderService.createOrderWithVerifyHash(sid, uid, verifyHash);
            LOGGER.info("购买成功，剩余库存为: {}", remain);
            return "购买成功，剩余库存为：" + remain;
        } catch (Exception e) {
            LOGGER.error("购买失败：{}", e.getMessage());
            return "购买失败：" + e.getMessage();
        }
    }

    @GetMapping("/createOrderWithVerifyHashAndLimit")
    @ApiOperation(value = "使用验证值进行下单操作，并限制用户的访问频率")
    public String createOrderWithVerifyHashAndLimit(@RequestParam Integer sid, @RequestParam Integer uid, @RequestParam String verifyHash) {
        try {
            synchronized (this) {
                int count = userService.addUserCount(uid);
                LOGGER.info("用户截至该次的访问次数为: {}", count);
                boolean checkUserCount = userService.checkUserCount(uid);
                if (checkUserCount) {
                    LOGGER.error("购买失败，超过频率限制！");
                    return "购买失败，超过频率限制！";
                }
                int remain = orderService.createOrderWithVerifyHash(sid, uid, verifyHash);
                LOGGER.info("购买成功，剩余库存为: {}", remain);
                return "购买成功，剩余库存为：" + remain;
            }
        } catch (Exception e) {
            LOGGER.error("购买失败：{}", e.getMessage());
            return "购买失败：" + e.getMessage();
        }
    }

    @Resource
    private RedissonClient redissonClient;

    private final String CREATE_ORDER_LOCK = "create_order_lock";

    @GetMapping("/createOrderWithVerifyHashAndLimitByRedisLock")
    @ApiOperation(value = "Redisson分布式锁")
    public String createOrderWithVerifyHashAndLimitByRedisLock(@RequestParam Integer sid, @RequestParam Integer uid, @RequestParam String verifyHash) {
        RLock lock = redissonClient.getLock(CREATE_ORDER_LOCK);
        try {
            lock.lock();
            boolean checkUserCount = userService.checkUserCount(uid);
            if (checkUserCount) {
                LOGGER.error("购买失败，超过频率限制！");
                return "购买失败，超过频率限制！";
            }
            int remain = orderService.createOrderWithVerifyHash(sid, uid, verifyHash);
            int count = userService.addUserCount(uid);
            LOGGER.info("用户截至该次的访问次数为: {}", count);
            LOGGER.info("购买成功，剩余库存为: {}", remain);
            return "购买成功，剩余库存为：" + remain;
        } catch (Exception e) {
            LOGGER.error("购买失败：{}", e.getMessage());
            return "购买失败：" + e.getMessage();
        } finally {
            lock.unlock();
        }
    }

    @Resource
    private RabbitUtil rabbitUtil;

    @GetMapping("/createOrder")
    @ApiOperation(value = "比较完全的秒杀")
    public String createOrder(@RequestParam Integer sid, @RequestParam Integer uid, @RequestParam String verifyHash) {
        // 可以提前进行一些验证，比如接口hash值是否匹配，比如是否有库存。
        boolean checkUserCount = userService.checkUserCount(uid);
        if (checkUserCount) {
            LOGGER.error("购买失败（未加锁），超过频率限制！");
            return "购买失败，超过频率限制！";
        }
        RLock lock = redissonClient.getLock(CREATE_ORDER_LOCK);
        try {
            lock.lock();
            checkUserCount = userService.checkUserCount(uid);
            if (checkUserCount) {
                LOGGER.error("购买失败（已加锁），超过频率限制！");
                return "购买失败，超过频率限制！";
            }
            // 在上架秒杀活动的时候，就进行初始的缓存，保证这里stockCount存在。
            Integer stockCount = stockService.getStockFromRedis(sid);
            if (stockCount == null || stockCount == 0) {
                LOGGER.error("库存不足，秒杀请求失败");
                return "库存不足，秒杀请求失败";
            }
            int count = userService.addUserCount(uid);
            LOGGER.info("用户截至该次的访问次数为: {}", count);
            stockService.setStockToRedis(sid, stockCount - 1);
            // 通知消息队列，生成订单
            rabbitUtil.sendMessage("ORDER_EXCHANGE", "ORDER_KEY", sid);
            return "购买成功！";
        } catch (Exception e) {
            LOGGER.error("购买失败：{}", e.getMessage());
            return "购买失败：" + e.getMessage();
        } finally {
            lock.unlock();
        }
    }
}

