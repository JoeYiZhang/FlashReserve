package org.zuyi.flashreserve.common.entity;

import lombok.Data;

@Data
public class OrderTmp {
    private String orderNo;
    private Long ticketId;
    private Integer quantity;
}
