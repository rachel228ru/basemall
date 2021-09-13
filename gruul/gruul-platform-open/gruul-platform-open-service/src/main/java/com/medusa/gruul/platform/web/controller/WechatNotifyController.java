package com.medusa.gruul.platform.web.controller;

import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.platform.conf.MeConstant;
import com.medusa.gruul.platform.service.IMiniInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.impl.WxOpenMessageRouter;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author whh
 */
@Api(hidden = true)
@RestController
@RequestMapping("/notify")
public class WechatNotifyController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public WxOpenService wxOpenService;
    @Autowired
    public WxOpenMessageRouter wxOpenMessageRouter;
    @Autowired
    private IMiniInfoService miniInfoService;

    /**
     * 当公众号/小程序对第三方平台进行授权、取消授权、更新授权后，
     * 微信服务器会向第三方平台方的授权事件接收 URL（创建时由第三方平台时填写）以 POST 的方式推送相关通知。
     */
    @RequestMapping("/receive_ticket")
    @EscapeLogin
    @ApiOperation(value = "授权变更通知推送", hidden = true)
    public Object receiveTicket(@RequestBody(required = false) String requestBody,
                                @RequestParam("timestamp") String timestamp, @RequestParam("nonce") String nonce,
                                @RequestParam("signature") String signature,
                                @RequestParam(name = "encrypt_type", required = false) String encType,
                                @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        this.logger.info(
                "\n接收微信请求：[signature=[{}], encType=[{}], msgSignature=[{}],"
                        + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                signature, encType, msgSignature, timestamp, nonce, requestBody);

        if (!StringUtils.equalsIgnoreCase(MeConstant.AES, encType)
                || !wxOpenService.getWxOpenComponentService().checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        // aes加密的消息
        WxOpenXmlMessage inMessage = WxOpenXmlMessage.fromEncryptedXml(requestBody,
                wxOpenService.getWxOpenConfigStorage(), timestamp, nonce, msgSignature);
        this.logger.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
        //新增(全网发布需要)
        if (StringUtils.equalsAnyIgnoreCase(inMessage.getAppId(), MeConstant.WXD101A85AA106F53E, MeConstant.WX570BC396A51B8FF8)) {
            return "success";
        }
        if (StringUtils.equalsIgnoreCase(inMessage.getInfoType(), MeConstant.COMPONENT_VERIFY_TICKET)) {
            wxOpenService.getWxOpenConfigStorage().setComponentVerifyTicket(inMessage.getComponentVerifyTicket());
            return "success";
        }
        //取消、更新授权
        if (StringUtils.equalsAnyIgnoreCase(inMessage.getInfoType(), MeConstant.AUTHORIZED, MeConstant.UNAUTHORIZED, MeConstant.UPDATEAUTHORIZED)) {
            miniInfoService.authNotity(inMessage);
            return "success";
        }
        return "success";
    }

    @RequestMapping("{appId}/callback")
    @EscapeLogin
    @ApiOperation(value = "消息与事件接收URL", hidden = true)
    public Object callback(@RequestBody(required = false) String requestBody,
                           @PathVariable("appId") String appId,
                           @RequestParam("signature") String signature,
                           @RequestParam("timestamp") String timestamp,
                           @RequestParam("nonce") String nonce,
                           @RequestParam("openid") String openid,
                           @RequestParam("encrypt_type") String encType,
                           @RequestParam("msg_signature") String msgSignature) {
        this.logger.info(
                "\n接收微信请求：[appId=[{}], openid=[{}], signature=[{}], encType=[{}], msgSignature=[{}],"
                        + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                appId, openid, signature, encType, msgSignature, timestamp, nonce, requestBody);
        if (!StringUtils.equalsIgnoreCase(MeConstant.AES, encType)
                || !wxOpenService.getWxOpenComponentService().checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        String out = "";
        // aes加密的消息
        WxMpXmlMessage inMessage = WxOpenXmlMessage.fromEncryptedMpXml(requestBody,
                wxOpenService.getWxOpenConfigStorage(), timestamp, nonce, msgSignature);
        this.logger.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
        // 全网发布测试用例
        if (StringUtils.equalsAnyIgnoreCase(appId, MeConstant.WXD101A85AA106F53E, MeConstant.WX570BC396A51B8FF8)) {
            this.logger.debug("进入测试用例");
            try {
                if (StringUtils.equals(inMessage.getMsgType(), MeConstant.TEXT)) {
                    if (StringUtils.equals(inMessage.getContent(), MeConstant.TESTCOMPONENT_MSG_TYPE_TEXT)) {
                        out = WxOpenXmlMessage.wxMpOutXmlMessageToEncryptedXml(
                                WxMpXmlOutMessage.TEXT().content("TESTCOMPONENT_MSG_TYPE_TEXT_callback")
                                        .fromUser(inMessage.getToUser()).toUser(inMessage.getFromUser()).build(),
                                wxOpenService.getWxOpenConfigStorage());
                    } else if (StringUtils.startsWith(inMessage.getContent(), MeConstant.QUERY_AUTH_CODE)) {
                        String msg = inMessage.getContent().replace("QUERY_AUTH_CODE:", "") + "_from_api";
                        WxMpKefuMessage kefuMessage =
                                WxMpKefuMessage.TEXT().content(msg).toUser(inMessage.getFromUser()).build();
                        wxOpenService.getWxOpenComponentService().getWxMpServiceByAppid(appId).getKefuService()
                                .sendKefuMessage(kefuMessage);
                    }
                } else if (StringUtils.equals(inMessage.getMsgType(), MeConstant.EVENT)) {
                    WxMpKefuMessage kefuMessage = WxMpKefuMessage.TEXT().content(inMessage.getEvent() + "from_callback")
                            .toUser(inMessage.getFromUser()).build();
                    wxOpenService.getWxOpenComponentService().getWxMpServiceByAppid(appId).getKefuService()
                            .sendKefuMessage(kefuMessage);
                }
            } catch (WxErrorException e) {
                logger.error("callback", e);
            }
        } else {
            this.logger.debug("进入路由");
            WxMpXmlOutMessage outMessage = wxOpenMessageRouter.route(inMessage, appId);
            if (outMessage != null) {
                out = WxOpenXmlMessage.wxMpOutXmlMessageToEncryptedXml(outMessage,
                        wxOpenService.getWxOpenConfigStorage());
            }
        }
        return out;
    }


}