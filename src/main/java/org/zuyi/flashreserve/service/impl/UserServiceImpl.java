package org.zuyi.flashreserve.service.impl;

import cn.hutool.core.util.RandomUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomUtils;
import org.zuyi.flashreserve.VO.LoginVO;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.zuyi.flashreserve.common.DTO.LoginDTO;
import org.zuyi.flashreserve.common.constants.CodeConstants;
import org.zuyi.flashreserve.common.constants.MessageConstants;
import org.zuyi.flashreserve.common.constants.RedisConstants;
import org.zuyi.flashreserve.common.entity.User;
import org.zuyi.flashreserve.common.exception.BussinessException;
import org.zuyi.flashreserve.common.result.Result;
import org.zuyi.flashreserve.common.utils.AuthHeaderUtils;
import org.zuyi.flashreserve.common.utils.TokenUtils;
import org.zuyi.flashreserve.common.utils.UserContent;

import lombok.RequiredArgsConstructor;
import org.zuyi.flashreserve.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zuyi.flashreserve.service.UserService;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

    private final StringRedisTemplate stringRedisTemplate;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Override
    public Result login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        if(username == null || password == null){
           throw new BussinessException(CodeConstants.LOGIN_BLANK,MessageConstants.LOGIN_BLANK);
        }
        User user = query().eq("user_name", username).one();
        if(user == null){
            throw new  BussinessException(CodeConstants.FAIL,MessageConstants.LOGIN_FAIL);
        }

        if(!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())){
            throw new  BussinessException(CodeConstants.FAIL,MessageConstants.LOGIN_FAIL);
        }
        //检查账号状态
        if(user.getStatus().equals(0)){
            throw new  BussinessException(CodeConstants.FAIL,MessageConstants.USER_STATUS_ERR);
        }

        //存入redis中
        Long userId = user.getId();
        String token = TokenUtils.generateToken();
        String hashToken = TokenUtils.hashToken(token);
        System.out.println(token);
        stringRedisTemplate.opsForValue().set(RedisConstants.LOGIN_KEY + hashToken,
                Long.toString(userId),
                RedisConstants.LOGIN_EXPIRATION_TIME,
                TimeUnit.MINUTES);
        LoginVO loginVO = new LoginVO();
        loginVO.setUsername(username);
        loginVO.setToken(token);
        UserContent.setUserId(userId);
        return Result.success(loginVO);
    }

    @Override
    public Result logout(HttpServletRequest request) {
        String token = AuthHeaderUtils.getToken(request);
        String hashToken = TokenUtils.hashToken(token);
        Boolean success = stringRedisTemplate.delete(RedisConstants.LOGIN_KEY + hashToken);
        if(BooleanUtils.isFalse(success)){
            log.info("token:",token);
            throw new  BussinessException(CodeConstants.FAIL,"token有问题");
        }
        UserContent.removeUser();
        return Result.success();
    }

    @Override
    public Result register(LoginDTO loginDTO) {
        String password = loginDTO.getPassword();
        String username = loginDTO.getUsername();

        User user = query().eq("user_name", username).one();
        if(user != null){
            throw new  BussinessException(CodeConstants.LOGIN_DUP_REGESTER,MessageConstants.DUP_USER);
        }

        user = new User();
        //TODO加密算法
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setUserName(username);
        user.setNickName(RandomUtil.randomString(10));

        boolean save = save(user);
        if(BooleanUtils.isFalse(save)){
            throw new  BussinessException(CodeConstants.REGESTER_ERR,MessageConstants.REGISTER_ERR);
        }
        return Result.success();
    }

    @Override
    public Result me() {
        long userId = UserContent.getUserId();
        return Result.success(userId);
    }


}
