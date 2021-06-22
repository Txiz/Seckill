package com.txiz.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author Txiz
 * @since 2021-06-22
 */
@Data
@EqualsAndHashCode()
@TableName("tb_stock")
@ApiModel(value = "Stock对象", description = "库存表")
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "库存表主键ID")
    @TableId(value = "sid", type = IdType.AUTO)
    private Integer sid;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "库存")
    private Integer count;

    @ApiModelProperty(value = "已售")
    private Integer sale;

    @ApiModelProperty(value = "版本号")
    private Integer version;
}
