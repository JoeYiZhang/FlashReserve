package org.zuyi.flashreserve.controller.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("UserOrderItemController")
@Slf4j
@RequestMapping("/api/user/orderitem")
@Tag(name = "订单接口")
public class OrderItemController {
}
