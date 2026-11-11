package org.zuyi.flashreserve.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class SessionVO {
    private Long id;
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
    //票
    private List<TicketVO>  ticketVOS = new ArrayList<>();
}
