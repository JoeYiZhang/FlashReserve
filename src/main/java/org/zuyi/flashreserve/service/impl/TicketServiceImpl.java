package org.zuyi.flashreserve.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zuyi.flashreserve.common.DTO.TicketDTO;
import org.zuyi.flashreserve.common.constants.CodeConstants;
import org.zuyi.flashreserve.common.constants.MessageConstants;
import org.zuyi.flashreserve.common.entity.Session;
import org.zuyi.flashreserve.common.entity.Ticket;
import org.zuyi.flashreserve.common.exception.BussinessException;
import org.zuyi.flashreserve.common.result.Result;
import org.zuyi.flashreserve.common.status.TicketStatus;
import org.zuyi.flashreserve.mapper.TicketMapper;
import org.zuyi.flashreserve.service.SessionService;
import org.zuyi.flashreserve.service.TicketService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class TicketServiceImpl extends ServiceImpl<TicketMapper, Ticket> implements TicketService {
    @Autowired
    private SessionService sessionService;
    @Override
    public Result create(TicketDTO ticketDTO) {
        Session session = sessionService.lambdaQuery().eq(Session::getId, ticketDTO.getSessionId()).one();
        if(session==null){
            throw new BussinessException(CodeConstants.TICKET_ERR, MessageConstants.SESSION_BLANK);
        }

        Ticket ticket = lambdaQuery()
                .eq(Ticket::getSessionId, ticketDTO.getSessionId())
                .eq(Ticket::getTierName, ticketDTO.getTierName())
                .one();
        if(ticket!=null){
            throw  new BussinessException(CodeConstants.TICKET_ERR, MessageConstants.TICKET_DUP);
        }
        if(ticketDTO.getPurchaseLimit()<=0){
            throw new BussinessException(CodeConstants.TICKET_ERR, MessageConstants.TICKET_PURCHASE_LIMIT_ERR);
        }
        if(ticketDTO.getTotalStock()<=0){
            throw new BussinessException(CodeConstants.TICKET_ERR,MessageConstants.TICKET_STOCK_ERR);
        }
        if(ticketDTO.getPrice().longValue()<=0){
            throw new BussinessException(CodeConstants.TICKET_ERR,MessageConstants.TICKET_PRICE_ERR);
        }
        LocalDateTime now = LocalDateTime.now();
        BeanUtils.copyProperties(ticketDTO,ticket);
        ticket.setStatus(TicketStatus.DRAFT.getCode());
        ticket.setCreateTime(now);
        ticket.setUpdateTime(now);
        boolean success = save(ticket);
        if(BooleanUtils.isFalse(success)){
            throw new BussinessException(CodeConstants.TICKET_ERR,MessageConstants.TICKET_SAVE_ERR);
        }
        return Result.success(ticket);

    }
}
