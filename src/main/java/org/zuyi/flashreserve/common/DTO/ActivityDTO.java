package org.zuyi.flashreserve.common.DTO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityDTO {
    // 活动名称
    @NotBlank
    private String title;

    // 活动介绍
    @NotBlank
    private String description;

    // 封面图片 URL
    @NotBlank
    private String coverUrl;

    // 场馆名称
    @NotBlank
    private String venueName;

    // 场馆地址
    @NotBlank
    private String venueAddress;



}
