package org.zuyi.flashreserve.controller.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("AdminOrderItemController")
@Slf4j
@Tag(name = "订单具体信息")
@RequestMapping("/api/admin/orderitem")
public class OrderItemController {
}
