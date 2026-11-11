package org.zuyi.flashreserve.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zuyi.flashreserve.common.DTO.TicketDTO;
import org.zuyi.flashreserve.common.result.Result;
import org.zuyi.flashreserve.service.TicketService;

@RestController("AdminTicketController")
@Slf4j
@Tag(name = "票单")
@RequestMapping("api/admin/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;
    /**
     * 创建票档
     */
    @Operation(tags = "创建票档")
    @PostMapping
    public Result createTicket(@Valid @RequestBody TicketDTO ticketDTO) {
        Result result = ticketService.create(ticketDTO);
        return result;
    }
}
