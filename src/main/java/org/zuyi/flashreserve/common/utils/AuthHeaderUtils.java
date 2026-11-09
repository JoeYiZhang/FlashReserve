package org.zuyi.flashreserve.common.utils;


import jakarta.servlet.http.HttpServletRequest;

public final class AuthHeaderUtils {
    public  static final String getToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header == null || !header.startsWith("Bearer ")){
            return null;
        }
        String token = header.substring(7).trim();
        // 32 字节随机数据经过 Base64URL 编码为 43 个字符
        if (!token.matches("[A-Za-z0-9_-]{43}")) {
            return null;
        }

        return token;
    }
}
