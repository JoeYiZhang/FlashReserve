package org.zuyi.flashreserve.common.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.zuyi.flashreserve.common.constants.CodeConstants;
import org.zuyi.flashreserve.common.constants.MessageConstants;
import org.zuyi.flashreserve.common.entity.Activity;
import org.zuyi.flashreserve.common.entity.Session;
import org.zuyi.flashreserve.common.exception.BussinessException;

@Getter
@AllArgsConstructor
public enum SessionStatus {
    DRAFT(0,"草稿"),
    PUBLISHED(1,"上架"),
    OFFLINE(2,"下架"),
    CANCELLED(3,"取消");
    private final Integer code;
    private final String description;

    public static SessionStatus fromCode(Integer code) {
        if(code == null){
            throw new BussinessException(CodeConstants.SESSION_ERR, MessageConstants.SESSION_STATUS_ERR);
        }

        for(SessionStatus status : SessionStatus.values()){
            if(status.code.equals(code)){
                return status;
            }
        }
        throw new BussinessException(CodeConstants.SESSION_ERR, "未知的状态码" + code);
    }
    public  static Boolean canPublish(Session session){
        if(session.getStatus().equals(SessionStatus.PUBLISHED.getCode())||
                session.getStatus().equals(SessionStatus.DRAFT.getCode())){
            return true;
        }
        return false;
    }
}
