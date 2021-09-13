package com.medusa.gruul.order;

import com.medusa.gruul.common.core.monitor.EnableMonitorHeartbeat;
import com.medusa.gruul.common.swagger.annotation.EnableGruulSwagger2;
import com.medusa.gruul.order.config.CounterHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@EnableWebSocket
@EnableGruulSwagger2
@SpringCloudApplication
@EnableFeignClients(basePackages = "com.medusa.gruul.*.api.feign")
@MapperScan("com.medusa.gruul.order.mapper")
@EnableDiscoveryClient
public class OrderApplication implements WebSocketConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }


    @Bean
    public CounterHandler counterHandler() {
        return new CounterHandler();
    }

    /**
     * 注册WebSocket处理类
     *
     * @param webSocketHandlerRegistry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        //支持websocket 的 connection，指定counterHandler处理路径为/ws 的长连接请求
        webSocketHandlerRegistry
                .addHandler(counterHandler(), "/broadcast").setAllowedOrigins("*")
                //添加WebSocket握手请求的拦截器.
                .addInterceptors(new CounterHandler.CountHandshakeInterceptor());

        //不支持websocket的 connection,采用sockjs
        webSocketHandlerRegistry.addHandler(counterHandler(), "/sockjs/broadcast").setAllowedOrigins("*")
                //添加WebSocket握手请求的拦截器.
                .addInterceptors(new CounterHandler.CountHandshakeInterceptor()).withSockJS();
    }
}