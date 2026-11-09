package org.zuyi.flashreserve.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
//@TableName(value = "user")
public class User {

    //id
    @TableId
    private Long id;

    //用户名
    private String userName;

    //昵称
    private String nickName;

    //密码
    private String password;

    //创建时间 yyyy-mm-dd
    private LocalDateTime createTime;

    //更新时间 yyyy-mm-dd
    private LocalDateTime updateTime;

    //性别
    private String sex;

    //账号状态
    private Integer status;

    //手机号
    private String phone;


}
