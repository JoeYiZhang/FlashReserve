package org.zuyi.flashreserve.service;

import com.baomidou.mybatisplus.spring.service.IService;
import org.zuyi.flashreserve.common.DTO.TicketDTO;
import org.zuyi.flashreserve.common.entity.Ticket;
import org.zuyi.flashreserve.common.result.Result;

public interface TicketService extends IService<Ticket> {
    Result create(TicketDTO ticketDTO);

}
