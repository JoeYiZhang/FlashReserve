package org.zuyi.flashreserve.common.DTO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class SessionDTO {

    // 所属活动 ID
    @NotNull
    private Long activityId;
    @NotBlank
    // 场次名称
    private String sessionName;

    @NotEmpty
    // 活动开始时间
    private LocalDateTime startTime;
    @NotEmpty
    // 活动结束时间
    private LocalDateTime endTime;
    @NotEmpty
    // 开始售卖时间
    private LocalDateTime saleStartTime;
    @NotEmpty
    // 停止售卖时间
    private LocalDateTime saleEndTime;


}
