package org.zuyi.flashreserve.common.status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.zuyi.flashreserve.common.constants.CodeConstants;
import org.zuyi.flashreserve.common.entity.Activity;
import org.zuyi.flashreserve.common.entity.Order;
import org.zuyi.flashreserve.common.entity.OrderItem;
import org.zuyi.flashreserve.common.exception.BussinessException;
import org.zuyi.flashreserve.service.OrderService;

@Getter
@AllArgsConstructor
public enum  OrderStatus {
    PAID(0,"待支付"),
    ALREADY_PAID(1,"已支付"),
    CANCELLED(3,"取消");

    private final Integer code;
    private final String decreption;

    public OrderStatus fromCode(Integer code) {
        if(code == null){
            throw new IllegalArgumentException("状态码错误");
        }

        for(OrderStatus status : OrderStatus.values()){
            if(status.getCode().equals(code)){
                return status;
            }
        }
        throw new BussinessException(CodeConstants.FAIL,"未知活动状态码" + code);
    }

}
