package org.zuyi.flashreserve.common.DTO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TicketDTO {
    // 所属场次 ID
    @NotNull
    private Long sessionId;
    @NotBlank
    // 票档名称
    private String tierName;
    @DecimalMin(value = "0.01")
    // 票价
    private BigDecimal price;
    @Positive
    // 总库存
    private Integer totalStock;
    @Positive
    // 每个用户最大购买数量
    private Integer purchaseLimit;

}
