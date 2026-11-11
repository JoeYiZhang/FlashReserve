package org.zuyi.flashreserve.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.zuyi.flashreserve.common.entity.OrderItem;
import org.zuyi.flashreserve.mapper.OrderItemMapper;
import org.zuyi.flashreserve.service.OrderItemService;
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {
}
