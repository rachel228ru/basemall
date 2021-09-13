package com.medusa.gruul.common.rabbitmq.interfaces;

import com.rabbitmq.client.ConnectionFactory;

/**
 * @author wangpeng
 * @data 2018-07-08下午1:41:10
 * @description TODO
 * @version V1.0
 */
public interface ReConnectionListener {
    /**
     * 重连
     *
     * @param connectionFactory
     */
    void resetConnectionFactory(ConnectionFactory connectionFactory);
}
