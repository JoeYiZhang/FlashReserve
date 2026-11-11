package org.zuyi.flashreserve.common.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.zuyi.flashreserve.common.constants.CodeConstants;
import org.zuyi.flashreserve.common.entity.Activity;
import org.zuyi.flashreserve.common.entity.Ticket;
import org.zuyi.flashreserve.common.exception.BussinessException;

@Getter
@AllArgsConstructor
public enum TicketStatus {
    DRAFT(0,"草稿"),
    PUBLISHED(1,"上架"),
    OFFLINE(2,"下架"),
    CANCELLED(3,"取消");

    private final Integer code;
    private final String decreption;

    public TicketStatus fromCode(Integer code) {
        if(code == null){
            throw new IllegalArgumentException("状态码错误");
        }

        for(TicketStatus status : TicketStatus.values()){
            if(status.getCode().equals(code)){
                return status;
            }
        }
        throw new BussinessException(CodeConstants.FAIL,"未知活动状态码" + code);
    }
    public static Boolean canPublish(Ticket ticket){
        if(ticket.getStatus().equals(TicketStatus.PUBLISHED.getCode())||
                ticket.getStatus().equals(TicketStatus.DRAFT.getCode())){
            return true;
        }
        return false;
    }
}
