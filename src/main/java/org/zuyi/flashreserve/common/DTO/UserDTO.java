package org.zuyi.flashreserve.common.DTO;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    //昵称
    private String nickName;

    //创建时间 yyyy-mm-dd
    private LocalDateTime createTime;

    //性别
    private String sex;

    //手机号
    private String phone;


}
