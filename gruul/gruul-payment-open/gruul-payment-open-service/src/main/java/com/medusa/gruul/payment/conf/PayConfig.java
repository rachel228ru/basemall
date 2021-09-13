package com.medusa.gruul.payment.conf;

import com.medusa.gruul.payment.api.enums.PayChannelEnum;
import com.medusa.gruul.payment.api.enums.WxPayEnum;
import com.medusa.gruul.payment.handler.WxPayMiniHandler;
import com.medusa.gruul.payment.router.PayMessageRouter;
import com.medusa.gruul.payment.service.impl.IpsPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author whh
 */
@Component
public class PayConfig {

    @Autowired
    private WxPayMiniHandler wxPayMiniHandler;

    @Autowired
    private IpsPayServiceImpl ipsPayService;


    @Bean
    public PayMessageRouter payMessageRouter() {
        final PayMessageRouter wxOpenMessageRouter = new PayMessageRouter();
        //微信jsapi请求
        wxOpenMessageRouter.rule().async(false).payChannel(PayChannelEnum.WX.getType()).tradeType(WxPayEnum.JSAPI_MINI.getType()).handler(this.wxPayMiniHandler).end();
        wxOpenMessageRouter.rule().async(false).payChannel(PayChannelEnum.WX.getType()).tradeType(WxPayEnum.JSAPI_MP.getType()).handler(this.wxPayMiniHandler).end();

        /**
         * 微信h5
         */
        wxOpenMessageRouter.rule().async(false).payChannel(PayChannelEnum.WX.getType()).tradeType(WxPayEnum.H5.getType()).handler(this.wxPayMiniHandler).end();

        /**
         * ips
         */
        wxOpenMessageRouter.rule().async(false).payChannel(PayChannelEnum.IPS.getType()).tradeType(WxPayEnum.IPS_JS_API.getType()).handler(this.ipsPayService).end();

        return wxOpenMessageRouter;
    }


}
