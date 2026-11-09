package org.zuyi.flashreserve.service;

import com.baomidou.mybatisplus.spring.service.IService;
import org.zuyi.flashreserve.common.DTO.LoginDTO;
import org.zuyi.flashreserve.common.entity.User;
import org.zuyi.flashreserve.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;


public interface UserService extends IService<User> {

    Result login(LoginDTO loginDTO);

    Result logout(HttpServletRequest request);

    Result register(LoginDTO loginDTO);

    Result me();

}
