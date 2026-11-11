package org.zuyi.flashreserve.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zuyi.flashreserve.common.DTO.ActivityDTO;
import org.zuyi.flashreserve.common.result.Result;
import org.zuyi.flashreserve.service.ActivityService;


@RestController("AdminActivityController")
@Tag(name = "活动端接口")
@Slf4j
@RequestMapping("/api/admin/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;
    @PostMapping("/create")
    @Operation(tags = "创建新活动")
    public Result create(@Valid @RequestBody ActivityDTO activityDTO) {
        log.info("创建活动:{}", activityDTO);
        Result result = activityService.create(activityDTO);
        return result;
    }

    /**
     * 发布活动
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    @Operation(tags = "发布活动")
    public Result publish(@PathVariable Long id){
        log.info("发布活动：{}",id);
        Result result =  activityService.publish(id);
        return result;
    }

    @GetMapping("/{id}")
    @Operation(tags = "查看活动")
    public Result selectById(@PathVariable Long id){
        Result result = activityService.selectById(id);
        return result;
    }
}
