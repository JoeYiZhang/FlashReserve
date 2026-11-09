package org.zuyi.flashreserve.common.utils;
import org.springframework.stereotype.Component;

@Component
public final class UserContent {
    static final ThreadLocal<Long> UserHolder = new ThreadLocal<>();
    private UserContent(){

    };


    public static void setUserId(long userId){
        UserHolder.set(userId);
    }

    public static long getUserId(){
        return UserHolder.get();
    }


    public static void removeUser(){
        UserHolder.remove();
    }
}
