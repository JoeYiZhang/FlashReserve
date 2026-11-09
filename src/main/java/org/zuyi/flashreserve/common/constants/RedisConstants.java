package org.zuyi.flashreserve.common.constants;

import org.springframework.stereotype.Component;

@Component
public final class RedisConstants {

    public static final String LOGIN_KEY = "login:";

    //分钟
    public static final long LOGIN_EXPIRATION_TIME = 3*60;
}
