package org.zuyi.flashreserve.service;

import com.baomidou.mybatisplus.spring.service.IService;
import org.zuyi.flashreserve.common.entity.Order;
import org.zuyi.flashreserve.common.result.Result;


public interface OrderService extends IService<Order> {
    Result purchase(Long id,Integer quantity);

}
