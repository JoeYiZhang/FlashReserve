package org.zuyi.flashreserve.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("ticket")
public class Ticket {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 所属场次 ID
    private Long sessionId;

    // 票档名称
    private String tierName;

    // 票价
    private BigDecimal price;

    // 总库存
    private Integer totalStock;

    // 可用库存
    private Integer availableStock;

    // 每个用户最大购买数量
    private Integer purchaseLimit;

    // 票档状态：0禁用 1可售
    private Integer status;

    // 乐观锁版本号
    private Integer version;

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;
}