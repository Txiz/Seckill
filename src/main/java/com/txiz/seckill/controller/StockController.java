package com.txiz.seckill.controller;


import com.txiz.seckill.service.OrderService;
import com.txiz.seckill.service.StockService;
import com.txiz.seckill.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Txiz
 * @since 2021-06-22
 */
@RestController
@RequestMapping("/seckill/stock")
@Api(tags = "库存控制器")
public class StockController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);

    @Resource
    private OrderService orderService;

    @Resource
    private StockService stockService;

    @Resource
    private UserService userService;

    @GetMapping("/getStockFromMysql/{sid}")
    @ApiOperation(value = "从MySQL中获取库存")
    public String getStockFromMysql(@PathVariable Integer sid) {
        Integer count;
        try {
            count = stockService.getStockFromMysql(sid);
        } catch (Exception e) {
            LOGGER.error("查询库存失败: {}", e.getMessage());
            return "查询库存失败";
        }
        LOGGER.info("商品: {} 剩余库存为: {}", sid, count);
        return String.format("商品: %d 剩余库存为: %d", sid, count);
    }

    @GetMapping("/getStockFromRedis/{sid}")
    @ApiOperation(value = "从Redis中获取库存")
    public String getStockFromRedis(@PathVariable Integer sid) {
        Integer count;
        try {
            count = stockService.getStockFromRedis(sid);
            if (count == null) {
                count = stockService.getStockFromMysql(sid);
                LOGGER.info("缓存未命中，查询数据库");
                stockService.setStockToRedis(sid, count);
                LOGGER.info("写入缓存");
            }
        } catch (Exception e) {
            LOGGER.error("查询库存失败: {}", e.getMessage());
            return "查询库存失败";
        }
        LOGGER.info("商品: {} 剩余库存为: {}", sid, count);
        return String.format("商品: %d 剩余库存为: %d", sid, count);
    }

    @GetMapping("/setStockToRedis/{sid}")
    @ApiOperation(value = "向Redis中写入库存")
    public String setStockToRedis(@PathVariable Integer sid) {
        stockService.setStockToRedis(sid, stockService.getStockFromMysql(sid));
        return "缓存设置成功";
    }
}

