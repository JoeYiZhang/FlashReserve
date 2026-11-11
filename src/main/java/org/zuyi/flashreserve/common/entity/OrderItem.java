package org.zuyi.flashreserve.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("order_item")
public class OrderItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 所属订单 ID
    private Long orderId;

    // 票档 ID
    private Long tierId;

    // 票档名称快照
    private String tierName;

    // 购买数量
    private Integer quantity;

    // 下单时的单价
    private BigDecimal unitPrice;

    // 明细总金额
    private BigDecimal totalPrice;

    // 创建时间
    private LocalDateTime createTime;
}