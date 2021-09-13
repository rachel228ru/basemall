package com.medusa.gruul.platform.web.controller;

import cn.hutool.core.util.StrUtil;
import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.conf.MeConstant;
import com.medusa.gruul.platform.model.dto.AuthTenantIdMpDto;
import com.medusa.gruul.platform.model.vo.AuthMpLoginVo;
import com.medusa.gruul.platform.model.vo.WxJdkConfigVo;
import com.medusa.gruul.platform.service.PublicServiceApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Huihuang Wu
 * @description
 * @data: 2020/9/19
 */
@Api(tags = "公众号相关接口")
@RequestMapping("/wechat-mp")
@RestController
@Slf4j
public class MpController {

    @Autowired
    private PublicServiceApiService publicServiceApiService;

    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private WxMpMessageRouter messageRouter;



    //Todo 该处为h5 后端未删减

    @GetMapping("/wx/jdk-config")
    @EscapeLogin
    @ApiOperation(value = "获取公众号微信jdk配置信息")
    public Result<WxJdkConfigVo> wxJdkConfig(@RequestParam(name = "url") String url) {
        WxJdkConfigVo vo = publicServiceApiService.getWxJdkConfig(url);
        return Result.ok(vo);
    }


    @PostMapping("/auth/tenantId/mp")
    @EscapeLogin
    @ApiOperation(value = "获取某个租户id下的公众号授权跳转地址")
    public Result<String> authTenantIdMp(@RequestBody AuthTenantIdMpDto dto) {
        String authUrl = publicServiceApiService.authH5(dto);
        return Result.ok(authUrl);
    }

    @GetMapping("/auth/notify")
    @EscapeLogin
    @ApiOperation(value = "/auth/tenantId/mp ==> 微信授权回调地址", hidden = true)
    public void authNotify(@RequestParam(name = "code", required = false) String code,
                           @RequestParam(name = "state") String state,
                           @RequestParam(name = "appid", required = false) String appid, HttpServletResponse servletResponse) {
        publicServiceApiService.authNotify(code, state, appid, servletResponse);
    }


    @GetMapping("/wx/mp/account/info")
    @EscapeLogin
    @ApiOperation(value = "根据/auth/tenantId/mp接口中设置的回调地址中拿到authCode换取h5可用token,仅一次有效")
    public Result<AuthMpLoginVo> authMpLogin(@RequestParam(name = "authCode") String authCode) {
        return publicServiceApiService.authMpLogin(authCode);
    }


    @ApiOperation(value = "内部公众号回调地址")
    @EscapeLogin
    @RequestMapping(value = "portal/{appId}", produces = "application/xml; charset=UTF-8")
    public String post(
            @RequestBody(required = false) String requestBody,
            @PathVariable(value = "appId") String appid,
            @RequestParam("signature") String signature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam(value = "echostr", required = false) String echostr,
            @RequestParam(value = "openid", required = false) String openId,
            @RequestParam(name = "encrypt_type", required = false) String encType,
            @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        if (StrUtil.isNotEmpty(echostr)) {
            return echostr;
        }
        if (StrUtil.isEmpty(requestBody)) {
            return requestBody;
        }
        log.info("\n接收微信请求：[openid=[{}], [signature=[{}], encType=[{}], msgSignature=[{}],"
                        + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                openId, signature, encType, msgSignature, timestamp, nonce, requestBody);

        if (!this.wxMpService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }
        String out = null;
        if (encType == null) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = this.messageRouter.route(inMessage);
            if (outMessage == null) {
                return "";
            }
            out = outMessage.toXml();
        } else if (MeConstant.AES.equalsIgnoreCase(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxMpService.getWxMpConfigStorage(),
                    timestamp, nonce, msgSignature);
            log.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
            WxMpXmlOutMessage outMessage = this.messageRouter.route(inMessage);
            if (outMessage == null) {
                return "";
            }
            out = outMessage.toEncryptedXml(wxMpService.getWxMpConfigStorage());
        }

        log.debug("\n组装回复信息：{}", out);
        return out;
    }

    @GetMapping("/wx/upload")
    @EscapeLogin
    @ApiOperation(value = "h5临时文件上传到oss")
    public Result wxUpload(@RequestParam(name = "mediaId")String mediaId) {
        return Result.ok(publicServiceApiService.saveTemporaryFiles(mediaId));
    }

}
