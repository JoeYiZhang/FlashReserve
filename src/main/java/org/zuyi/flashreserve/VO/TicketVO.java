package org.zuyi.flashreserve.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class TicketVO {
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

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;
}
