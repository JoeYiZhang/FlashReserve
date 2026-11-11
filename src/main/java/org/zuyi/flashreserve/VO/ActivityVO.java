package org.zuyi.flashreserve.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.zuyi.flashreserve.common.entity.Session;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ActivityVO {
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

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;
    //场次
    private List<SessionVO> sessionVOS = new ArrayList<>();
}
