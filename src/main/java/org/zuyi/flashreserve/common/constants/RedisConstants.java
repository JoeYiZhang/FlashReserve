package org.zuyi.flashreserve.common.constants;

import org.springframework.stereotype.Component;

@Component
public final class RedisConstants {

    public static final String LOGIN_KEY = "login:";
    public static final String USER_KEY = "user:";
    public static final String ORDER_KEY = "order:";
    public static final String SECKILL_TICKET_KEY = "seckill:ticket:";
    public static final String ORDERNO_KEY = "orderNo:";
    public static final String LOCK_KEY = "lock:";
    //分钟
    public static final long LOGIN_EXPIRATION_TIME = 3*60;
}
