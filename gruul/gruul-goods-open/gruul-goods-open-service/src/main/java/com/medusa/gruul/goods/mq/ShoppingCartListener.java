package com.medusa.gruul.goods.mq;

import com.medusa.gruul.goods.api.constant.ShoppingCartMyQueneName;
import com.medusa.gruul.goods.service.api.IApiShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 购物车消息监听
 * </p>
 *
 * @author lcysike
 * @since 2019-11-19
 */

@Slf4j
@Service
public class ShoppingCartListener {

    @Autowired
    private IApiShoppingCartService apiShoppingCartService;

    @RabbitListener(queues = ShoppingCartMyQueneName.GENERATE_SHOPPING_CART)
    public void receive(ShoppingCartMessage shoppingCartMessage) {
        log.info("receive message:" + shoppingCartMessage.toString());
        //更新购物车数据库表数据
        apiShoppingCartService.updateShoppingCartDatabase(shoppingCartMessage);
    }

}
