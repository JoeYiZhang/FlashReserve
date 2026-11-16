package org.zuyi.flashreserve.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zuyi.flashreserve.common.result.Result;
import org.zuyi.flashreserve.service.OrderService;

@RestController("UserOrderController")
@Slf4j
@Tag(name = "订单")
@RequestMapping("/api/user/order")
public class OrderController {

   @Autowired
   private OrderService orderService;

    @Operation(tags = "购买")
    @PostMapping("/seckill/{id}")
    public Result purchase(@PathVariable Long id,@PathParam("quantity") Integer quantity){
        Result result = orderService.purchase(id,quantity);
        return result;
    }
}
