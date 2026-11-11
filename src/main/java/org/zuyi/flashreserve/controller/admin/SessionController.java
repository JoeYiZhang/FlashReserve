package org.zuyi.flashreserve.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zuyi.flashreserve.common.DTO.SessionDTO;
import org.zuyi.flashreserve.common.entity.Session;
import org.zuyi.flashreserve.common.result.Result;
import org.zuyi.flashreserve.service.SessionService;

@RestController("AdminSessionController")
@Slf4j
@Tag(name = "活动详情")
@RequestMapping("/api/admin/session")
public class SessionController {

    @Autowired
    private SessionService sessionService;
    @PostMapping("/create")
    @Operation(tags = "创建场次")
    public Result create(@Valid @RequestBody SessionDTO sessionDTO){
        Result result = sessionService.create(sessionDTO);
        return result;
    }
}
