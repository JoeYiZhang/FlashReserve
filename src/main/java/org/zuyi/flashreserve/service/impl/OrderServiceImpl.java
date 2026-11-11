package org.zuyi.flashreserve.service.impl;

import com.baomidou.mybatisplus.spring.service.IService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.zuyi.flashreserve.common.entity.Order;
import org.zuyi.flashreserve.mapper.OrderMapper;
import org.zuyi.flashreserve.service.OrderService;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
