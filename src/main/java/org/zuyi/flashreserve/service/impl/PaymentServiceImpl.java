package org.zuyi.flashreserve.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.zuyi.flashreserve.common.entity.Payment;
import org.zuyi.flashreserve.mapper.PaymentMapper;
import org.zuyi.flashreserve.service.PaymentService;

@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {
}
