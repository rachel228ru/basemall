package com.medusa.gruul.platform.service;

import cn.binarywang.wx.miniapp.bean.code.WxMaCodeAuditStatus;
import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.api.entity.MiniInfo;
import com.medusa.gruul.platform.api.model.dto.LoginDto;
import com.medusa.gruul.platform.api.model.dto.MiniAuthInfoDto;
import com.medusa.gruul.platform.model.dto.MiniInfoUpdateDto;
import com.medusa.gruul.platform.model.dto.PreAuthCodeDto;
import com.medusa.gruul.platform.model.dto.WxaGetwxacode;
import com.medusa.gruul.platform.model.vo.BaseInfoVo;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author whh
 * @since 2019-09-07
 */
public interface IMiniInfoService extends IService<MiniInfo> {


    /**
     * 获取用户所有的小程序信息
     *
     * @param id 用户id
     * @return com.medusa.gruul.platform.api.entity.MiniInfo
     */
    List<MiniInfo> getUserMini(Long id);

    /**
     * 授权通知
     *
     * @param inMessage dto
     */
    void authNotity(WxOpenXmlMessage inMessage);

    /**
     * 获取小程序最后一次提交审核提交记录
     *
     * @param appId appid
     * @return 审核信息
     */
    WxMaCodeAuditStatus getMiniLastAuditCode(String appId);

    /**
     * 通过小程序原始id,获取小程序信息
     *
     * @param toUser 小程序原始id
     * @return com.medusa.gruul.platform.api.entity.MiniInfo
     */
    MiniInfo getByUserName(String toUser);

    /**
     * 发布小程序
     *
     * @param appId appid
     */
    void codeRelease(String appId);


    /**
     * 更新小程序数据
     *
     * @param miniInfoUpdateDto dto
     */
    void updateMini(MiniInfoUpdateDto miniInfoUpdateDto);

    /**
     * 获取使用模板代码的小程序数量
     *
     * @param templateCodeId 模板代码id
     * @return 数量
     */
    Integer getUseTemplateCodeCount(Long templateCodeId);


    /**
     * 获取使用该套餐的小程序
     *
     * @param comboId 套餐id
     * @return miniInfos
     */
    List<MiniInfo> getByCombo(Integer comboId);


    /**
     * 根据租户id获取店铺绑定的公众号信息
     *
     * @param tenantId 租户Id
     * @return miniInfo com.medusa.gruul.platform.api.entity.MiniInfo
     */
    MiniInfo getByMpTenantId(String tenantId);

    /**
     * 根据租户id获取店铺绑定的小程序详情
     *
     * @param tenantId 租户Id
     * @return miniInfo com.medusa.gruul.platform.api.entity.MiniInfo
     */
    MiniInfo getByMiniTenantId(String tenantId);

    /**
     * 提供小程序登录
     *
     * @param tenantId 租户id
     * @param code     授权码
     * @return com.medusa.gruul.platform.api.model.dto.LoginDto
     */
    LoginDto login(String tenantId, String code);

    /**
     * 获取小程序码,返回base64
     *
     * @param wxaGetwxacode com.medusa.gruul.platform.model.dto.WxaGetwxacode
     * @return com.medusa.gruul.common.core.util.Result
     */
    Result<String> wxaGetwxacode(WxaGetwxacode wxaGetwxacode);

    /**
     * 获取用户默认的小程序
     *
     * @param accountInfoId 平台用户id
     * @return com.medusa.gruul.platform.api.entity.MiniInfo
     */
    MiniInfo getByUserDefualtMini(Long accountInfoId);


    /**
     * 获取小程序用户基础信息
     *
     * @param type
     * @return com.medusa.gruul.platform.model.dto.BaseInfoDto
     */
    BaseInfoVo baseInfo(Integer type);

    /**
     * 小程序更新最新版本
     *
     * @param templateDetailMinisId
     */
    void versionUpdate(Long templateDetailMinisId);

    /**
     * 校验当前租户数据是否正确
     *
     * @param tenantId 租户Id
     * @return com.medusa.gruul.platform.api.entity.MiniInfo
     */
    MiniInfo checkCurrentTenantId(String tenantId);

    /**
     * 获取预售码地址
     *
     * @param preAuthCodeDto dto
     * @return 授权地址
     */
    String getPreAuthCode(PreAuthCodeDto preAuthCodeDto);

    /**
     * 扫码授权url统一回调入口
     *
     * @param authCode  授权码
     * @param expiresIn 有效时间
     * @param uuid      uuid
     * @param response  res
     */
    void preAuthCodeNotify(String authCode, Integer expiresIn, String uuid, HttpServletResponse response);

    /**
     * 将指定的店铺下的小程序和公众号进行三方平台绑定
     *
     * @param tenantId 租户id
     */
    void createPlatform(String tenantId);

    /**
     * 移除店铺绑定的小程序和公众号
     *
     * @param tenantId 租户ID
     */
    void removeByShopInfoAll(String tenantId);

    /**
     * 撤销小程序当前审核中的版本
     */
    void revocation();

    /**
     * 更新小程序基本信息
     */
    void baseInfoUpdate();

    /**
     * 根据租户id换取指定小程序信息
     *
     * @param tenantId 租户id
     * @return com.medusa.gruul.common.core.util.Result
     */
    Result<MiniAuthInfoDto> getMiniAuthInfo(String tenantId);

    /**
     * 根据平台店铺id查询当前授权错误信息,每次授权错误消息最多保留5分钟
     *
     * @param platformShopId 平台店铺id
     * @return 错误消息
     */
    Result<String> errorInfo(Long platformShopId);

    /**
     * 更新小程序最新默认配置
     *
     * @param versionId t_platform_shop_template_detail表id
     * @param tenantId  租户id
     */
    void updateMiniNewConfig(Long versionId, String tenantId);

    /**
     * 手动触发为小程序上传域名
     */
    void miniDomaiReq();

    /**
     * 根据appId查询小程序
     *
     * @param appId 小程序appId
     * @return com.medusa.gruul.platform.api.entity.MiniInfo
     */
    MiniInfo getByAppId(String appId);

    /**
     * 查询当前小程序在使用某个小程序版本子表Id的所有小程序
     *
     * @param templateDetailMinisId 店铺模版详情小程序版本子表Id
     * @return com.medusa.gruul.platform.api.entity.MiniInfo
     */
    List<MiniInfo> getByTemplateDetailMinisId(Long templateDetailMinisId);

    /**
     * 获取店铺小程序和公众号是否已经进行绑定
     * @param tenantId 租户id
     * @return   true or false
     */
    Boolean bindOpenInfo(String tenantId);

}
