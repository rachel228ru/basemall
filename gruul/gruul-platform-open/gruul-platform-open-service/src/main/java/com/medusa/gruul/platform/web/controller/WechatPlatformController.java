package com.medusa.gruul.platform.web.controller;


import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.binarywang.wx.miniapp.bean.template.WxMaPubTemplateTitleListResult;
import cn.hutool.core.util.StrUtil;
import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.platform.model.vo.WxOpenMaCodeTemplateVo;
import com.medusa.gruul.platform.service.IWechatPlatformService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.open.bean.result.WxOpenMaCategoryListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 微信第三方相关接口(部分)
 * </p>
 *
 * @author whh
 * @since 2020-02-02
 */
@Api(tags = "微信第三方相关接口(部分),仅限后端自行调用", hidden = true)
@RestController
@RequestMapping("/wechat")
public class WechatPlatformController {

    @Autowired
    private IWechatPlatformService wechatPlatformService;


    @ApiOperation(value = "从开放平台获取小程序模板库")
    @EscapeLogin
    @GetMapping("/gettemplatelist")
    public Result<List<WxOpenMaCodeTemplateVo>> getTemplateList() {
        List<WxOpenMaCodeTemplateVo> templateList = wechatPlatformService.getTemplateList(null);
        return Result.ok(templateList);
    }

    @GetMapping("/service-class")
    @EscapeLogin
    @ApiOperation(value = "获取指定appId的服务类目")
    public Result getAppIdServiceClass(@RequestParam String appId) {
        WxOpenMaCategoryListResult appIdServiceClass = wechatPlatformService.getAppIdServiceClass(appId);
        return Result.ok(appIdServiceClass);
    }


    @GetMapping("/getpubtemplatetitles")
    @EscapeLogin
    @ApiOperation(value = "获取模板标题列表")
    public Result getpubtemplatetitles(@RequestParam(name = "appId") String appId, @RequestParam String serviceIds, @RequestParam Integer page,
                                       @RequestParam Integer size) {
        WxMaPubTemplateTitleListResult getpubtemplatetitles = wechatPlatformService.getpubtemplatetitles(appId, serviceIds, page, size);
        return Result.ok(getpubtemplatetitles);
    }


    @GetMapping("/getpubtemplatekeywords")
    @EscapeLogin
    @ApiOperation(value = "获取模板标题列表")
    public Result getpubtemplatekeywords(@RequestParam(name = "appId") String appId, @RequestParam String tid) {
        List<WxMaSubscribeService.PubTemplateKeyword> getpubtemplatekeywords = wechatPlatformService.getpubtemplatekeywords(appId, tid);
        return Result.ok(getpubtemplatekeywords);
    }

    @GetMapping("/add/class")
    @EscapeLogin
    @ApiOperation(value = "添加指定类目", hidden = true, tags = "测试用")
    public Result addClass(@RequestParam(name = "appId") String appId) {
        wechatPlatformService.addClass(appId);
        return Result.ok();
    }


    @GetMapping("/clear/subscribe/msg")
    @EscapeLogin
    @ApiOperation(value = "清空指定小程序订阅模板消息")
    public Result clearSubscribeMsg() {
        String tenantId = TenantContextHolder.getTenantId();
        if (StrUtil.isEmpty(tenantId)) {
            throw new ServiceException("租户id为空");
        }
        wechatPlatformService.clearSubscribeMsg(tenantId);
        return Result.ok();
    }

    @GetMapping("/add/subscribe/msg")
    @EscapeLogin
    @ApiOperation(value = "为指定小程序更新指定版本订阅消息")
    public Result addSubscribeMsg(@RequestParam(name = "version") String version) {
        String tenantId = TenantContextHolder.getTenantId();
        if (StrUtil.isEmpty(tenantId)) {
            throw new ServiceException("租户id为空");
        }
        wechatPlatformService.addSubscribeMsg(tenantId, version);
        return Result.ok();
    }


}
