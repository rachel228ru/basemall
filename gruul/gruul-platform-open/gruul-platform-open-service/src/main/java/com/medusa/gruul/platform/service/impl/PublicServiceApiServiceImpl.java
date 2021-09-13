package com.medusa.gruul.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.medusa.gruul.account.api.feign.RemoteMiniAccountService;
import com.medusa.gruul.account.api.model.WxMpUserDto;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.constant.TimeConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.oss.api.feign.RemoteSysOssService;
import com.medusa.gruul.platform.api.entity.MiniInfo;
import com.medusa.gruul.platform.api.entity.PlatformShopInfo;
import com.medusa.gruul.platform.conf.MeConstant;
import com.medusa.gruul.platform.conf.PlatformRedis;
import com.medusa.gruul.platform.conf.SystemConfig;
import com.medusa.gruul.platform.constant.RedisConstant;
import com.medusa.gruul.platform.model.dto.AuthTenantIdMpDto;
import com.medusa.gruul.platform.model.vo.AuthMpLoginVo;
import com.medusa.gruul.platform.model.vo.PublicShopInfoVo;
import com.medusa.gruul.platform.model.vo.SystemConfigVo;
import com.medusa.gruul.platform.model.vo.WxJdkConfigVo;
import com.medusa.gruul.platform.service.IMiniInfoService;
import com.medusa.gruul.platform.service.IPlatformShopInfoService;
import com.medusa.gruul.platform.service.ISystemConfService;
import com.medusa.gruul.platform.service.PublicServiceApiService;
import lombok.extern.log4j.Log4j2;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMaterialService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.open.api.WxOpenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author whh
 * @description
 * @data: 2020/7/18
 */
@Service
@Log4j2
public class PublicServiceApiServiceImpl implements PublicServiceApiService {

    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private WxOpenService wxOpenService;

    @Autowired
    private ISystemConfService systemConfService;
    @Autowired
    private IMiniInfoService miniInfoService;
    @Autowired
    private IPlatformShopInfoService platformShopInfoService;
    @Autowired
    private RemoteMiniAccountService remoteMiniAccountService;

    @Autowired
    private RemoteSysOssService remoteSysOssService;

    @Override
    public WxJdkConfigVo getWxJdkConfig(String url) {
        if (StrUtil.isEmpty(url)) {
            throw new ServiceException("错误url");
        }
        if (!ReUtil.isMatch(PatternPool.URL_HTTP, url)) {
            throw new ServiceException("url匹配不通过");
        }
        String tenantId = TenantContextHolder.getTenantId();
        if (StrUtil.isEmpty(tenantId)) {
            throw new ServiceException("非法域名格式");
        }
        if (tenantId.length() != CommonConstants.NUMBER_SIX) {
            throw new ServiceException("无效请求参数");
        }
        CompletableFuture<PlatformShopInfo> shopInfoCompletableFuture = CompletableFuture.supplyAsync(() -> platformShopInfoService.getByTenantId(tenantId.toString()));

        MiniInfo mpInfo = miniInfoService.getByMpTenantId(tenantId.toString());
        if (mpInfo == null) {
            throw new ServiceException("商户不存在有效公众号");
        }
        if (mpInfo.getAuthorizerFlag().equals(CommonConstants.NUMBER_ZERO)) {
            throw new ServiceException("商户公众号已取消授权");
        }
        try {
            PlatformShopInfo platformShopInfo = shopInfoCompletableFuture.get();
            if (platformShopInfo.getIsDue().equals(CommonConstants.NUMBER_ONE)) {
                throw new ServiceException("当前店铺已到期无法使用该功能");
            }
            WxMpService wxMpService = wxOpenService.getWxOpenComponentService().getWxMpServiceByAppid(mpInfo.getAppId());
            WxJsapiSignature jsapiSignature = wxMpService.createJsapiSignature(url);
            WxJdkConfigVo configVo = BeanUtil.toBean(jsapiSignature, WxJdkConfigVo.class);
            MiniInfo miniInfo = miniInfoService.getByMiniTenantId(tenantId);
            configVo.setMiniAppId(miniInfo.getAppId());
            return configVo;
        } catch (WxErrorException wxErrorException) {
            wxErrorException.printStackTrace();
            throw new ServiceException("请求异常");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            throw new ServiceException("商户数据异常");
        }
    }


    @Override
    public PublicShopInfoVo getPublicShopInfo(String tenantId) {
        PlatformShopInfo platformShopInfo = platformShopInfoService.getByTenantId(tenantId);
        if (platformShopInfo == null) {
            throw new ServiceException("无效租户id");
        }
        MiniInfo miniInfo = miniInfoService.getByMiniTenantId(tenantId);
        PublicShopInfoVo vo = new PublicShopInfoVo();
        vo.setMiniAppId(miniInfo.getAppId());
        vo.setUserName(miniInfo.getUserName());
        return vo;
    }

    @Override
    public String authH5(AuthTenantIdMpDto dto) {
        if (StrUtil.isEmpty(dto.getRedirectUri()) || dto.getRedirectUri().contains(MeConstant.WENHAO)) {
            throw new ServiceException("回调地址错误");
        }
        if (!ReUtil.isMatch(PatternPool.URL_HTTP, dto.getRedirectUri())) {
            throw new ServiceException("回调域名格式错误");
        }
        SystemConfigVo systemConfigVo = systemConfService.getTypeInfo(CommonConstants.NUMBER_ZERO);
        if (ObjectUtil.isEmpty(systemConfigVo)) {
            throw new ServiceException("系统异常,请联系平台!!!");
        }
        SystemConfig systemConfig = systemConfigVo.getSystemConfig();
        String tenantId = dto.getTenantId();
        if (StrUtil.isEmpty(tenantId)) {
            throw new ServiceException("无效商户请求");
        }

        String shopId = dto.getShopId();
        if (StrUtil.isEmpty(shopId) || !shopId.contains(tenantId)) {
            throw new ServiceException("店铺数据不存在");
        }
        PlatformShopInfo shopInfo = platformShopInfoService.getByTenantId(tenantId);
        if (shopInfo == null) {
            throw new ServiceException("非法商户请求");
        }
        Long bindMpId = shopInfo.getBindMpId();
        Long bindMiniId = shopInfo.getBindMiniId();
        boolean mpFlag = bindMpId == null || bindMpId == 0;
        boolean miniFlag = bindMiniId == null || bindMiniId == 0;
        if (mpFlag || miniFlag) {
            throw new ServiceException("商户未同时授权小程序和公众号");
        }
        MiniInfo mpInfo = miniInfoService.getById(bindMpId);
        MiniInfo miniInfo = miniInfoService.getById(bindMpId);
        if (miniInfo.getAuthorizerFlag().equals(CommonConstants.NUMBER_ZERO) || mpInfo.getAuthorizerFlag().equals(CommonConstants.NUMBER_ZERO)) {
            throw new ServiceException("公众号或小程序未授权");
        }
        String notifyUrl = systemConfig.getMiniDomain().concat("/api/platform/wechat-mp/auth/notify");
        String state = IdUtil.fastSimpleUUID();
        String scope = dto.getScope();
        if (StrUtil.isEmpty(scope)) {
            scope = "snsapi_userinfo";
        }
        dto.setTenantId(tenantId);
        dto.setMpInfoId(mpInfo.getId());
        PlatformRedis platformRedis = new PlatformRedis();
        platformRedis.setNxPx(RedisConstant.MP_NOTIFY.concat(state), JSON.toJSONString(dto), TimeConstants.FIVE_MINUTES);
        return wxOpenService.getWxOpenComponentService().oauth2buildAuthorizationUrl(mpInfo.getAppId(), notifyUrl, scope, state);
    }

    @Override
    public void authNotify(String code, String state, String appid, HttpServletResponse servletResponse) {
        PlatformRedis platformRedis = new PlatformRedis();
        String notifiRedisKey = RedisConstant.MP_NOTIFY.concat(state);
        String dto = platformRedis.get(notifiRedisKey);
        if (dto == null) {
            throw new ServiceException("无效的回调或回调时间已超时");
        }
        AuthTenantIdMpDto authTenantIdMpDto = JSON.parseObject(dto, AuthTenantIdMpDto.class);
        String authCode = IdUtil.fastSimpleUUID();
        String redirectUri = authTenantIdMpDto.getRedirectUri().concat("?authCode=").concat(authCode);
        authTenantIdMpDto.setCode(code);
        platformRedis.setNxPx(RedisConstant.MP_NOTIFY_AUTH_CODE.concat(authCode), JSON.toJSONString(authTenantIdMpDto), TimeConstants.FIVE_MINUTES);
        platformRedis.del(notifiRedisKey);
        try {
            servletResponse.sendRedirect(redirectUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Result<AuthMpLoginVo> authMpLogin(String authCode) {
        PlatformRedis platformRedis = new PlatformRedis();
        String authCodeRedisKey = RedisConstant.MP_NOTIFY_AUTH_CODE.concat(authCode);
        String dto = platformRedis.get(authCodeRedisKey);
        if (StrUtil.isEmpty(dto)) {
            throw new ServiceException("无效的authCode参数");
        }
        AuthTenantIdMpDto authTenantIdMpDto = JSON.parseObject(dto, AuthTenantIdMpDto.class);
        String code = authTenantIdMpDto.getCode();
        if (StrUtil.isEmpty(code)) {
            return Result.failed(401, "用户未授权");
        }
        String tenantId = authTenantIdMpDto.getTenantId();
        //判断是否允许使用h5
        if (!miniInfoService.bindOpenInfo(tenantId)) {
            throw new ServiceException("当前店铺未同时绑定公众号和小程序，无法进入页面");
        }
        MiniInfo mpInfo = miniInfoService.getById(authTenantIdMpDto.getMpInfoId());
        WxMpService wxMpService = wxOpenService.getWxOpenComponentService().getWxMpServiceByAppid(mpInfo.getAppId());
        try {
            WxMpOAuth2AccessToken auth2AccessToken = wxMpService.oauth2getAccessToken(code);
            WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(auth2AccessToken, "");
            if (StrUtil.isEmpty(wxMpUser.getUnionId())) {
                miniInfoService.createPlatform(tenantId);
                throw new ServiceException("未绑定开发平台无法获取关联数据");
            }
            String shopId = authTenantIdMpDto.getShopId();
            WxMpUserDto wxMpUserDto = BeanUtil.toBean(wxMpUser, WxMpUserDto.class);
            wxMpUserDto.setTenantId(tenantId);
            wxMpUserDto.setShopId(shopId);
            Result result = remoteMiniAccountService.mpLogin(wxMpUserDto);
            if (ObjectUtil.isNull(result)) {
                throw new ServiceException("系统异常");
            }
            if (result.getCode() != CommonConstants.SUCCESS) {
                return result;
            }
            AuthMpLoginVo vo = new AuthMpLoginVo();
            vo.setToken((String) result.getData());
            vo.setTenantId(tenantId);
            vo.setShopId(shopId);
            return Result.ok(vo);
        } catch (WxErrorException wxErrorException) {
            wxErrorException.printStackTrace();
            if (wxErrorException.getError().getErrorCode() == MeConstant.MINI_CODE_40029) {
                throw new ServiceException("invalid code ");
            }
            throw new ServiceException("请求异常");
        }
    }

    @Override
    public Result saveTemporaryFiles(String mediaId) {
        Result<String> upload;
        String tenantId = TenantContextHolder.getTenantId();
        if (StrUtil.isEmpty(tenantId)) {
            throw new ServiceException("非法域名格式");
        }
        if (tenantId.length() != CommonConstants.NUMBER_SIX) {
            throw new ServiceException("无效请求参数");
        }
        MiniInfo mpInfo = miniInfoService.getByMpTenantId(tenantId.toString());
        if (mpInfo == null) {
            throw new ServiceException("商户不存在有效公众号");
        }
        WxMpService wxMpService = wxOpenService.getWxOpenComponentService().getWxMpServiceByAppid(mpInfo.getAppId());
        WxMpMaterialService materialService = wxMpService.getMaterialService();
        byte[] bytes = null;
        try {
            File file = materialService.mediaDownload(mediaId);
            String format = file.getName();
            String prefix = format.substring(format.lastIndexOf(".") + 1);
            try {
                bytes = Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
                throw new ServiceException("临时文件转换失败");
            }
            upload = remoteSysOssService.wxUpload(bytes, prefix);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException("临时文件未找到");
        }
        return upload;
    }

}
