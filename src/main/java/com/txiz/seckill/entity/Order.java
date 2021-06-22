package com.txiz.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

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
@TableName("tb_order")
@ApiModel(value = "Order对象", description = "订单表")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单表主键ID")
    @TableId(value = "oid", type = IdType.AUTO)
    private Integer oid;

    @ApiModelProperty(value = "库存ID")
    private Integer sid;

    @ApiModelProperty(value = "库存名称")
    private String name;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
