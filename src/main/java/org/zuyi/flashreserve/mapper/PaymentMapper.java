package org.zuyi.flashreserve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.zuyi.flashreserve.common.entity.Payment;

@Mapper
public interface PaymentMapper extends BaseMapper<Payment> {
}
