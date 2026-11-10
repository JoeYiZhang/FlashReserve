package org.zuyi.flashreserve.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("activity_session")
public class ActivitySession {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 所属活动 ID
    private Long activityId;

    // 场次名称
    private String sessionName;

    // 活动开始时间
    private LocalDateTime startTime;

    // 活动结束时间
    private LocalDateTime endTime;

    // 开始售卖时间
    private LocalDateTime saleStartTime;

    // 停止售卖时间
    private LocalDateTime saleEndTime;

    // 状态：0未开放 1开放 2暂停 3取消
    private Integer status;

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;
}