package org.zuyi.flashreserve.controller.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("UserActivityController")
@Tag(name = "活动端接口")
@Slf4j
@RequestMapping("/api/user/activity")
public class ActivityController {
}
