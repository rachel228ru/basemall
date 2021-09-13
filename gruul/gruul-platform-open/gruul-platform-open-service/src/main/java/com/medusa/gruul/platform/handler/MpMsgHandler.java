package com.medusa.gruul.platform.handler;

import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import me.chanjar.weixin.mp.builder.outxml.TextBuilder;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 内部公众号使用的处理器
 *
 * @author whh
 * @description
 * @data: 2020/9/27
 */
@Component
public class MpMsgHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService mpService,
                                    WxSessionManager sessionManager) {
        String[] obj = {"客服账号"};
        if (StringUtils.startsWithAny(wxMessage.getContent(), obj)) {
            return new TextBuilder().fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser()).content(wxMessage.getFromUser()).build();
        } else {
            return null;
        }

    }
}
