package com.medusa.gruul.payment.router;

import com.medusa.gruul.payment.api.model.dto.PayRequestDto;
import com.medusa.gruul.payment.api.model.dto.PayResultDto;
import com.medusa.gruul.payment.handler.PayMessageHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author whh
 */
public class PayMessageRouterRule {

    /**
     * 是否继续进行路由
     */
    private boolean reEnter = false;
    /**
     * 是否异步 默认同步
     */
    private boolean async = true;


    /**
     * 支付渠道
     */
    private Integer payChannel;

    /**
     * 交易类型
     */
    private Integer tradeType;

    private final PayMessageRouter routerBuilder;

    private List<PayMessageHandler> handlers = new ArrayList<>(3);

    public PayMessageRouterRule(PayMessageRouter payMessageRouter) {
        this.routerBuilder = payMessageRouter;
    }

    /**
     * 设置是否异步执行，默认是true
     */
    public PayMessageRouterRule async(boolean async) {
        this.async = async;
        return this;
    }

    /**
     * 如果payChannel等于某值
     */
    public PayMessageRouterRule payChannel(Integer payChannel) {
        this.payChannel = payChannel;
        return this;
    }

    /**
     * 如果event等于某值
     */
    public PayMessageRouterRule tradeType(Integer tradeType) {
        this.tradeType = tradeType;
        return this;
    }


    /**
     * 规则结束，代表如果一个消息匹配该规则，那么它将不再会进入其他规则
     */
    public PayMessageRouter end() {
        this.routerBuilder.getRules().add(this);
        return this.routerBuilder;
    }

    /**
     * 规则结束，但是消息还会进入其他规则
     */
    public PayMessageRouter next() {
        this.reEnter = true;
        return end();
    }

    /**
     * 设置支付请求处理器
     */
    public PayMessageRouterRule handler(PayMessageHandler handler) {
        this.handlers.add(handler);
        return this;
    }

    public void setReEnter(boolean reEnter) {
        this.reEnter = reEnter;
    }

    public boolean isReEnter() {
        return this.reEnter;
    }

    public boolean isAsync() {
        return this.async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public boolean test(PayRequestDto payRequestDto) {
        return
                (this.payChannel == null || this.payChannel.equals(payRequestDto.getPayChannel()))
                        &&
                        (this.tradeType == null || this.tradeType.equals(payRequestDto.getTradeType()));

    }

    public PayResultDto service(PayRequestDto payRequestDto) {
        // 交给handler处理
        PayResultDto res = null;
        for (PayMessageHandler handler : this.handlers) {
            res = handler.handle(payRequestDto);
        }
        return res;
    }
}
