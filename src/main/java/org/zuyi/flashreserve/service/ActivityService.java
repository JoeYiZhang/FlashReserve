package org.zuyi.flashreserve.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.spring.service.IService;
import jakarta.validation.Valid;
import org.zuyi.flashreserve.common.DTO.ActivityDTO;
import org.zuyi.flashreserve.common.entity.Activity;
import org.zuyi.flashreserve.common.result.Result;

public interface ActivityService extends IService<Activity> {
    Result create(@Valid ActivityDTO activityDTO);

    Result publish(Long activityId);

    Result selectById(Long activityId);
}
