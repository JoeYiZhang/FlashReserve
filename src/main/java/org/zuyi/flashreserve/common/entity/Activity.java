package org.zuyi.flashreserve.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("activity")
public class Activity {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 活动名称
    private String title;

    // 活动介绍
    private String description;

    // 封面图片 URL
    private String coverUrl;

    // 场馆名称
    private String venueName;

    // 场馆地址
    private String venueAddress;

    // 活动状态：0草稿 1上架 2下架 3取消
    private Integer status;

    // 创建人 ID
    private Long createdBy;

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;
}