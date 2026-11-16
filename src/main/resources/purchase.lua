local user_id = ARGS[1]
local ticket_id = ARGS[2]
local orderNo = ARGS[3]
local quantity = ARGS[4]

local stock_key = "seckill:stock:" .. ticket_id
local order_key = "orderNo:" .. orderNo
--这里解决的是有没有购买资格的问题
--首先是超卖问题 需要检定库存
if(tonumber(redis.call('get',stock_key)) - quantity <=0) then
    return 1
end

--解决一人一单问题
if(redis.call('sismember',order_key,user_id)==1) then
    return 2
end

--redis库存减一
redis.call('incryby',stock_key,quantity)
--redis加入set
redis.call('sadd',order_key,user_id)
--将消息传到消息队列中
redis.call("xadd",'stream.orders','*','userId',user_id,'ticketId',ticket_id,'id',order_key)
return 0