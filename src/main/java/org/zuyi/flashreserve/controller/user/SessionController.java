package org.zuyi.flashreserve.controller.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("UserSessionController")
@Slf4j
@Tag(name = "活动详情")
@RequestMapping("/api/user/session")
public class SessionController {
}
