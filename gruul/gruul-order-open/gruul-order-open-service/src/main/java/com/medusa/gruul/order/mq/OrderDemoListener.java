package com.medusa.gruul.order.mq;


import cn.hutool.json.JSONObject;
import com.medusa.gruul.order.api.constant.OrderQueueNameConstant;
import com.medusa.gruul.order.api.model.OrderVo;
import com.medusa.gruul.order.config.CounterHandler;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;


/**
 * 订阅订单消息demo
 *
 * @author alan
 * @date 2019/10/6 13:49
 */
@Slf4j
@Component
public class OrderDemoListener {

    @Resource
    private CounterHandler counterHandler;

    /**
     * 使用方法：
     * 1.创建自己队列 assemble.order.payed  模块名.业务名.动作
     * 2.通过路由键 gruul.order.payed 到交换机 gruul.order.direct 绑定自己创建的队列
     * 3.收到消息之后手动确认，不然消息会重复发送
     *
     * @param orderVo
     * @param properties
     * @param channel
     * @return void
     * @author alan
     * @date 2019/12/1 11:07
     */
    @RabbitListener(queues = OrderQueueNameConstant.ORDER_PAYED)
    public void payed(OrderVo orderVo, MessageProperties properties, Channel channel) throws IOException {
        log.info("payed receive message:" + orderVo.toString());
        JSONObject object = new JSONObject(2);
        object.set("orderId", orderVo.getId());
        object.set("mp3", "http://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/api/20200228113926mix(1).mp3");
        counterHandler.sendMessageToUser(orderVo.getShopId(), object.toString());
        //手动确认
        channel.basicAck(properties.getDeliveryTag(), true);
    }


    @RabbitListener(queues = OrderQueueNameConstant.ORDER_COMPLETED)
    public void completed(OrderVo orderVo, MessageProperties properties, Channel channel) throws IOException {
        log.info("completed receive message:" + orderVo.toString());
        //手动确认
        channel.basicAck(properties.getDeliveryTag(), true);
    }

}

