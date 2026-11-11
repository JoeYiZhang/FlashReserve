package org.zuyi.flashreserve.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("order")
public class Order {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 业务订单号
    private String orderNo;

    // 下单用户 ID
    private Long userId;

    // 订单状态：0待支付 1已支付
    // 2已取消 3退款中 4已退款
    private Integer status;

    // 订单总金额
    private BigDecimal totalAmount;

    // 订单支付截止时间
    private LocalDateTime expireTime;

    // 支付成功时间
    private LocalDateTime paidTime;

    // 订单取消时间
    private LocalDateTime cancelTime;

    // 订单取消原因
    private String cancelReason;

    // 请求幂等号（如已执行前面添加字段的 ALTER TABLE）
    private String requestId;

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;
}