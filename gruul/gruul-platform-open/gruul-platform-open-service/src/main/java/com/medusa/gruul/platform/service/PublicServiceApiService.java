package com.medusa.gruul.platform.service;

import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.model.dto.AuthTenantIdMpDto;
import com.medusa.gruul.platform.model.vo.AuthMpLoginVo;
import com.medusa.gruul.platform.model.vo.PublicShopInfoVo;
import com.medusa.gruul.platform.model.vo.WxJdkConfigVo;

import javax.servlet.http.HttpServletResponse;

/**
 * @author whh
 */
public interface PublicServiceApiService {

    /**
     * 获取微信jdk配置信息
     *
     * @param url
     * @return com.medusa.gruul.platform.model.vo.WxJdkConfigVo
     */
    WxJdkConfigVo getWxJdkConfig(String url);

    /**
     * h5公众号根据授权code换取可与小程序共用的token
     *
     * @param code code
     * @return com.medusa.gruul.platform.model.vo.AuthMpLoginVo
     */
    Result<AuthMpLoginVo> authMpLogin(String code);

    /**
     * 根据租户id换取店铺小程序的appId
     *
     * @param tenantId 租户id
     * @return appId
     */
    PublicShopInfoVo getPublicShopInfo(String tenantId);

    /**
     * 获取公众号用户授权url
     *
     * @param dto com.medusa.gruul.platform.model.dto.AuthTenantIdMpDto
     * @return 跳转地址
     */
    String authH5(AuthTenantIdMpDto dto);

    /**
     * authH5 统一授权回调地址
     *
     * @param code            授权的code
     * @param state           生成的state
     * @param appid           公众号appId
     * @param servletResponse
     */
    void authNotify(String code, String state, String appid, HttpServletResponse servletResponse);

    /**
     * h5临时文件上传到oss
     *
     * @param mediaId 媒体文件id
     * @return 上传文件存储信息
     */
    Result saveTemporaryFiles(String mediaId);


}
