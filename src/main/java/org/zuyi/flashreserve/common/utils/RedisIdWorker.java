package org.zuyi.flashreserve.common.utils;


import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
@Component
public final class RedisIdWorker {

    @Resource
    private static  StringRedisTemplate stringRedisTemplate;
    public  static final Long startTime = 1767225600L;
    public static  Long nextId(String prefix){
        Long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        Long timeStamp = now - startTime;
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        Long increment = stringRedisTemplate.opsForValue().increment("INC:" + prefix + date);
        return timeStamp <<32 | increment;
    }
}
//@Autowired
//private StringRedisTemplate stringRedisTemplate;
//
//final Long  BEGIN_TIME = 1767225600L;
//public Long nextId(String keyPrefix){
//    //1、需要获取现在的时间戳
//    long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
//    long timeStamp = now - BEGIN_TIME;
//
//    //2、需要根据string的自增
//    String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy:MM:DD"));
//    Long increment = stringRedisTemplate.opsForValue().increment("INC:" + keyPrefix + ":" + date);
//    return timeStamp << 32 |  increment;
//}
