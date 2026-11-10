package org.zuyi.flashreserve.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.zuyi.flashreserve.common.constants.RedisConstants;
import org.zuyi.flashreserve.common.exception.AuthorizeException;
import org.zuyi.flashreserve.common.utils.AuthHeaderUtils;
import org.zuyi.flashreserve.common.utils.TokenUtils;
import org.zuyi.flashreserve.common.utils.UserContent;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class LoginInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("Authorization");
        UserContent.removeUser();

        String token = AuthHeaderUtils.getToken(request);
        if(token == null){
            throw new AuthorizeException("未携带token");
        }

        String hashToken = TokenUtils.hashToken(token);
        String idStr = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_KEY + hashToken);
        if(idStr == null){
            throw new AuthorizeException("未找到该用户");
        }

        stringRedisTemplate.expire(RedisConstants.LOGIN_KEY + token, 30, TimeUnit.MINUTES);
        try {
            Long userId = Long.valueOf(idStr);
            UserContent.setUserId(userId);
        } catch (Exception e) {
            throw new AuthorizeException(e.getMessage());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
       UserContent.removeUser();
    }
}
