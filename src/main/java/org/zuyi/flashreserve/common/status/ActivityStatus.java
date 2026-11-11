package org.zuyi.flashreserve.common.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.zuyi.flashreserve.common.constants.CodeConstants;
import org.zuyi.flashreserve.common.entity.Activity;
import org.zuyi.flashreserve.common.exception.BussinessException;

@Getter
@AllArgsConstructor
public enum ActivityStatus {
    DRAFT(0,"草稿"),
    PUBLISHED(1,"上架"),
    OFFLINE(2,"下架"),
    CANCELLED(3,"取消");

    private final Integer code;
    private final String decreption;

    public ActivityStatus fromCode(Integer code) {
        if(code == null){
            throw new IllegalArgumentException("状态码错误");
        }

        for(ActivityStatus status : ActivityStatus.values()){
            if(status.getCode().equals(code)){
                return status;
            }
        }
        throw new BussinessException(CodeConstants.FAIL,"未知活动状态码" + code);
    }
    public static Boolean canPublish(Activity activity){
        if(activity.getStatus().equals(ActivityStatus.PUBLISHED.getCode())||
        activity.getStatus().equals(ActivityStatus.DRAFT.getCode())){
            return true;
        }
        return false;
    }

}
