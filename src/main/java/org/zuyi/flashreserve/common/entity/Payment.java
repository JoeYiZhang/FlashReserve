package org.zuyi.flashreserve.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("payment")
public class Payment {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 支付流水号
    private String paymentNo;

    // 关联订单 ID
    private Long orderId;

    // 支付金额
    private BigDecimal amount;

    // 支付方式：0模拟支付
    private Integer paymentMethod;

    // 支付状态：0待支付 1成功 2失败 3关闭
    private Integer status;

    // 支付成功时间
    private LocalDateTime paidTime;

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;
}