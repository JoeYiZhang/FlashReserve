package org.zuyi.flashreserve.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.zuyi.flashreserve.common.DTO.LoginDTO;
import org.zuyi.flashreserve.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.zuyi.flashreserve.common.utils.AuthHeaderUtils;
import org.zuyi.flashreserve.service.UserService;

@RestController
@Tag(name = "用户端接口")
@Slf4j
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/me")
    public Result me(){
        Result result = userService.me();
        return result;
    }
    /**
     *
     * @param loginDTO
     * @return
     */
    @PostMapping("/login")
    @Operation(tags = "用户登陆")
    public Result login(@RequestBody LoginDTO loginDTO) {
        log.info("用户登陆:{}",loginDTO);
        Result result = userService.login(loginDTO);
        return result;
    }

    @PostMapping("/logout")
    @Operation(tags = "用户登出")
    public Result logout(HttpServletRequest request) {
        log.info("用户退出:");
        String token = AuthHeaderUtils.getToken(request);
        log.info("token:",token);
        Result result = userService.logout(request);
        return result;
    }

    @PostMapping("/register")
    public Result register(@RequestBody LoginDTO loginDTO) {

        log.info("注册用户:");
        Result result = userService.register(loginDTO);
        log.info("注册返回结果: {}", result);
        return result;
    }

}
