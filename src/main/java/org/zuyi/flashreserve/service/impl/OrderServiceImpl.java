package org.zuyi.flashreserve.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.spring.service.IService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import io.lettuce.core.RedisClient;
import jakarta.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.AopProxyFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zuyi.flashreserve.common.constants.CodeConstants;
import org.zuyi.flashreserve.common.constants.MessageConstants;
import org.zuyi.flashreserve.common.constants.RedisConstants;
import org.zuyi.flashreserve.common.entity.Order;
import org.zuyi.flashreserve.common.entity.OrderItem;
import org.zuyi.flashreserve.common.entity.OrderTmp;
import org.zuyi.flashreserve.common.entity.Ticket;
import org.zuyi.flashreserve.common.exception.BussinessException;
import org.zuyi.flashreserve.common.result.Result;
import org.zuyi.flashreserve.common.status.OrderStatus;
import org.zuyi.flashreserve.common.utils.RedisIdWorker;
import org.zuyi.flashreserve.common.utils.UserContent;
import org.zuyi.flashreserve.mapper.OrderMapper;
import org.zuyi.flashreserve.service.OrderItemService;
import org.zuyi.flashreserve.service.OrderService;
import org.zuyi.flashreserve.service.TicketService;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {


    private String queueName = "stream.orders";
    private OrderServiceImpl proxy;
    private static  DefaultRedisScript<Long> SECKILL_SCRIPT;
    static{
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("purchase.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private RedissonClient redissonClient;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private class orderHandler implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    List<MapRecord<String, Object, Object>> records = stringRedisTemplate.opsForStream().read(
                            Consumer.from("g1", "c1"),
                            StreamReadOptions.empty().count(1).block(Duration.ofSeconds(3)),
                            StreamOffset.create(queueName, ReadOffset.lastConsumed())
                    );
                    if (records.isEmpty() || records == null) {
                        continue;
                    }

                    MapRecord<String, Object, Object> record = records.get(0);
                    Map<Object, Object> map = record.getValue();
                    OrderTmp orderTmp = BeanUtil.fillBeanWithMap(map, new OrderTmp(), true);
                    handleCreateOrder(orderTmp);
                    stringRedisTemplate.opsForStream().acknowledge(queueName, "g1", record.getId());
                } catch (Exception e) {
                    log.info("提交订单出现异常");
                    try {
                        handlePendingList();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                }

            }
        }

        public void handlePendingList() throws InterruptedException {

            while (true) {
                try {
                    List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
                            Consumer.from("g1", "c1"),
                            StreamReadOptions.empty().count(1).block(Duration.ofSeconds(2)),
                            StreamOffset.create(queueName, ReadOffset.from("0"))
                    );

                    if (list == null || list.isEmpty()) {
                        continue;
                    }

                    MapRecord<String, Object, Object> record = list.get(0);
                    Map<Object, Object> map = record.getValue();
                    OrderTmp orderTmp = BeanUtil.fillBeanWithMap(map, new OrderTmp(), true);
                    handleCreateOrder(orderTmp);
                    stringRedisTemplate.opsForStream().acknowledge(queueName, "g1", record.getId());
                } catch (Exception e) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

        }
    }

        public void handleCreateOrder(OrderTmp orderTmp) {
            long userId = UserContent.getUserId();
            String lock_key = RedisConstants.LOCK_KEY + userId;
            RLock lock = redissonClient.getLock(lock_key);
            try {
                boolean success = lock.tryLock();
                if (Boolean.TRUE.equals(success)) {
                    proxy.createOrderAndOrderItem(orderTmp);
                } else {
                    log.info("不允许重复下单");
                    return;
                }
            } finally {
                lock.unlock();
            }

        }

        /**
         * 购买
         * @return
         */
        @Override
        public Result purchase(Long id, Integer quantity) {
            //这里的id是ticket的id
            //v2： 去sql中去查还有多少库存 如果库存够 那么就减一 然后生成订单 写在数据库中
            //v3:  现在redis中存储库存信息 通过redis判断是否还有库存 然后创建订单消息 存在数据库中
            //v4： 添加锁 通过判断信息后 写入mysql订单信息
            //v5： 使用分布式锁 写入mysql订单信息
            //v6： 使用阻塞队列 先通过lua脚本完成数据查询并确认购买资格 然后将消息添加到阻塞队列中 从阻塞队列中去获得消息
            //v7： 使用消息队列 通过lua脚本完成数据查询并确认购买资格 然后将消息添加到消息队列中 然后再从pel队列中去确认
            //获取用户id 以及票档id
            long userId = UserContent.getUserId();
            long orderNo = RedisIdWorker.nextId(RedisConstants.ORDER_KEY);
            //TODO 需要处理一人买多单才到上限的问题
            Long r = stringRedisTemplate.execute(SECKILL_SCRIPT, Collections.emptyList(), userId, id, orderNo, quantity);
            if (r.intValue() == 1) {
                throw new BussinessException(CodeConstants.SECKILL_ERR, MessageConstants.STOCK_ERR);
            } else if (r.intValue() == 2) {
                throw new BussinessException(CodeConstants.SECKILL_ERR, MessageConstants.SECKILL_LIMIT);
            }
            proxy = (OrderServiceImpl) AopContext.currentProxy();
            return Result.success(orderNo);
        }


        @Transactional(rollbackFor = Exception.class)
        public Result createOrderAndOrderItem(OrderTmp orderTmp) {
            Order order = new Order();
            OrderItem orderItem = new OrderItem();
            order.setOrderNo(orderTmp.getOrderNo());
            orderItem.setQuantity(orderTmp.getQuantity());
            orderItem.setTierId(orderTmp.getTicketId());
            //检查幂等
            //定义幂等key orderNo: + orderNo setifabsent
            String orderNo = order.getOrderNo();
            long userId = UserContent.getUserId();
            String idStr = String.valueOf(userId);
            Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(RedisConstants.ORDERNO_KEY + orderNo, idStr);
            if (Boolean.FALSE.equals(success)) {
                throw new BussinessException(CodeConstants.SECKILL_ERR, MessageConstants.ORDER_DUP);
            }
            //TODO 增加双层保险 mysql增加幂等标志
            //创建新的订单
            Ticket ticket = ticketService.lambdaQuery().eq(Ticket::getId, orderItem.getTierId()).one();
            BigDecimal price = ticket.getPrice();
            String request = UUID.randomUUID().toString().replaceAll("-", "");
            LocalDateTime now = LocalDateTime.now();
            order.setCreateTime(now);
            order.setUpdateTime(now);
            order.setRequestId(request);
            order.setTotalAmount(price.multiply(BigDecimal.valueOf(orderItem.getQuantity())));
            order.setStatus(OrderStatus.PAID.getCode());
            success = save(order);
            if (Boolean.FALSE.equals(success)) {
                throw new BussinessException(CodeConstants.ORDER_ERR, MessageConstants.ORDER_SAVE_ERR);
            }
            orderItem.setOrderId(order.getId());
            orderItem.setCreateTime(now);
            orderItem.setTierName(ticket.getTierName());
            orderItem.setTotalPrice(price.multiply(BigDecimal.valueOf(orderItem.getQuantity())));
            orderItem.setUnitPrice(price);
            success = orderItemService.save(orderItem);
            if (Boolean.FALSE.equals(success)) {
                throw new BussinessException(CodeConstants.ORDERITEM_ERR, MessageConstants.ORDERITEM_SAVE_ERR);
            }
            return Result.success(orderNo);
        }



}
