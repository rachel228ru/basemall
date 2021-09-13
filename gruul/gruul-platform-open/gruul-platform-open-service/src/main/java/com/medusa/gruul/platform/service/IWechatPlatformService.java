package com.medusa.gruul.platform.service;

import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.binarywang.wx.miniapp.bean.template.WxMaPubTemplateTitleListResult;
import com.medusa.gruul.platform.model.vo.WxOpenMaCodeTemplateVo;
import me.chanjar.weixin.open.bean.result.WxOpenMaCategoryListResult;

import java.util.List;

/**
 * <p>
 * 微信第三方相关 服务类
 * </p>
 *
 * @author whh
 * @since 2020-02-02
 */
public interface IWechatPlatformService {

    /**
     * 获取指定appId服务类目
     *
     * @param appId appId
     * @return cn.binarywang.wx.miniapp.api.WxMaSubscribeService
     */
    WxOpenMaCategoryListResult getAppIdServiceClass(String appId);

    /**
     * 获取模板标题列表
     *
     * @param appId      appId
     * @param serviceIds 服务类目id 逗号分隔
     * @param page       页数
     * @param size       条数
     * @return 获取模板标题下的关键词库
     */
    WxMaPubTemplateTitleListResult getpubtemplatetitles(String appId, String serviceIds, Integer page, Integer size);

    /**
     * 获取模板标题下的关键词库
     *
     * @param appId appId
     * @param tid   模板标题 id，可通过接口获取
     * @return
     */
    List<WxMaSubscribeService.PubTemplateKeyword> getpubtemplatekeywords(String appId, String tid);

    /**
     * 清空小程序模板消息
     *
     * @param tenantId 租户id
     */
    void clearSubscribeMsg(String tenantId);

    /**
     * 新增小程序指定版本模板消息
     *
     * @param tenantId 租户id
     * @param version  版本
     */
    void addSubscribeMsg(String tenantId, String version);

    /**
     * 添加指定类型
     *
     * @param appId 小程序appId
     */
    void addClass(String appId);


    /**
     * 获取小程序模板
     *
     * @param templateId 模板id
     * @return com.medusa.gruul.platform.model.vo.WxOpenMaCodeTemplateVo
     */
    List<WxOpenMaCodeTemplateVo> getTemplateList(Long templateId);

}
