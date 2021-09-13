package com.medusa.gruul.platform.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaCodeService;
import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.code.WxMaCodeAuditStatus;
import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.util.Base64;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.constant.TimeConstants;
import com.medusa.gruul.common.core.constant.enums.AuditstatusEnum;
import com.medusa.gruul.common.core.constant.enums.AuthorizerFlagEnum;
import com.medusa.gruul.common.core.constant.enums.RunFlagEnum;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.DateUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.platform.api.entity.*;
import com.medusa.gruul.platform.api.model.dto.LoginDto;
import com.medusa.gruul.platform.api.model.dto.MiniAuthInfoDto;
import com.medusa.gruul.platform.conf.MeConstant;
import com.medusa.gruul.platform.conf.PlatformRedis;
import com.medusa.gruul.platform.conf.WechatOpenProperties;
import com.medusa.gruul.platform.constant.MpPermissionEnum;
import com.medusa.gruul.platform.constant.RedisConstant;
import com.medusa.gruul.platform.mapper.MiniInfoMapper;
import com.medusa.gruul.platform.model.dto.MiniInfoUpdateDto;
import com.medusa.gruul.platform.model.dto.PreAuthCodeDto;
import com.medusa.gruul.platform.model.dto.WxaGetwxacode;
import com.medusa.gruul.platform.model.vo.BaseInfoVo;
import com.medusa.gruul.platform.model.vo.MiniCodeVersionVo;
import com.medusa.gruul.platform.model.vo.SystemConfigVo;
import com.medusa.gruul.platform.service.*;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenComponentService;
import me.chanjar.weixin.open.api.WxOpenMaService;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.WxOpenCreateResult;
import me.chanjar.weixin.open.bean.WxOpenGetResult;
import me.chanjar.weixin.open.bean.auth.WxOpenAuthorizationInfo;
import me.chanjar.weixin.open.bean.auth.WxOpenAuthorizerInfo;
import me.chanjar.weixin.open.bean.ma.WxMaOpenCommitExtInfo;
import me.chanjar.weixin.open.bean.ma.WxOpenMaCategory;
import me.chanjar.weixin.open.bean.ma.WxOpenMaSubmitAudit;
import me.chanjar.weixin.open.bean.message.WxOpenMaSubmitAuditMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import me.chanjar.weixin.open.bean.result.WxOpenMaCategoryListResult;
import me.chanjar.weixin.open.bean.result.WxOpenMaDomainResult;
import me.chanjar.weixin.open.bean.result.WxOpenMaSubmitAuditResult;
import me.chanjar.weixin.open.bean.result.WxOpenMaWebDomainResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * <p>
 * 小程序基本信息(非授权时获取的) 服务实现类
 * </p>
 *
 * @author whh
 * @since 2019-09-07
 */
@Service
public class MiniInfoServiceImpl extends ServiceImpl<MiniInfoMapper, MiniInfo> implements IMiniInfoService {

    @Resource
    private WechatOpenProperties wechatOpenProperties;
    @Autowired
    private WxOpenService wxOpenService;
    @Autowired
    private IAuthTokenService authTokenService;
    @Autowired
    private IMiniInfoService miniInfoService;
    @Autowired
    private IAuditRecordService auditRecordService;
    @Autowired
    private ISystemConfService systemConfService;
    @Autowired
    private IPlatformShopInfoService platformShopInfoService;
    @Autowired
    private IPlatformShopTemplateDetailService platformShopTemplateDetailService;
    @Autowired
    private IPlatformShopTemplateDetailMinisService platformShopTemplateDetailMinisService;
    @Autowired
    private IWechatPlatformService wechatPlatformService;
    @Autowired
    private IPlatformShopMessageService platformShopMessageService;
    @Autowired
    private PlatformPayConfigService platformPayConfigService;

    @Value("#{'${platform.config.miniBusinessDomain}'.split(',')}")
    private List<String> miniBusinessDomain;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //创建缓存，默认24小时过期
    /**
     * 存储缓存(微信体验码)
     */
    private TimedCache<String, String> timedCache = CacheUtil.newTimedCache(TimeConstants.ONE_DAY);

    private static Map<Integer, String> MP_PERMISSION = new HashMap<>(16);

    static {
        for (MpPermissionEnum value : MpPermissionEnum.values()) {
            MP_PERMISSION.put(value.getCode(), value.getDesc());
        }
    }

    /**
     * 获取小程序体验二维码 base64
     *
     * @param miniInfo
     * @return
     */
    private String getTestQrcode(MiniInfo miniInfo) {
        WxOpenMaService wxMaServiceByAppid = wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(miniInfo.getAppId());
        try {
            File testQrcode = wxMaServiceByAppid.getTestQrcode(null, null);
            return ImgUtil.toBase64(ImgUtil.read(testQrcode), "jpg");
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException("体验二维码获取失败" + e.getError().getErrorMsg());
        }
    }

    /**
     * 获取小程序已经设置的所有类目
     *
     * @return
     */
    private List<WxOpenMaCategory> getMiniAlreadySettingCategory(MiniInfo miniInfo) {
        WxOpenMaService wxMaCodeService = wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(miniInfo.getAppId());
        try {
            WxOpenMaCategoryListResult categoryList = wxMaCodeService.getCategoryList();
            return categoryList.getCategoryList();
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException("获取类目失败" + e.getError().getErrorMsg());
        }
    }


    /**
     * 小程序代码提交审核
     *
     * @return str
     */
    private String codeSubmitAudit(MiniInfo miniInfo, PlatformShopTemplateDetail templateDetail,
                                   PlatformShopTemplateDetailMinis detailMinis) {
        try {
            WxOpenMaService wxMaServiceByAppid = wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(miniInfo.getAppId());
            //判断是否存在已提交审核代码
            WxMaCodeAuditStatus lastAuditCode = this.getMiniLastAuditCode(miniInfo.getAppId());
            if (lastAuditCode != null && lastAuditCode.getStatus().equals(CommonConstants.NUMBER_TWO)) {
                logger.debug("小程序存在审核中代码,请撤销审核代码或等待审核通过");
                return CommonConstants.NUMBER_ZERO.toString();
            }
            List<WxOpenMaSubmitAudit> wxOpenMaSubmitAudits = new ArrayList<>(5);
            //获取上传代码页面
            List<String> page = wxMaServiceByAppid.getPageList().getPageList();
            log.debug("上传代码页面--->".concat(page.toString()));
            //获取审核时可填写的类目信息
            List<WxOpenMaCategory> miniAlreadySettingCategory = this.getMiniAlreadySettingCategory(miniInfo);
            for (int i = 0; i < miniAlreadySettingCategory.size(); i++) {
                WxOpenMaCategory wxOpenMaCategory = miniAlreadySettingCategory.get(0);
                WxOpenMaSubmitAudit wxOpenMaSubmitAudit = new WxOpenMaSubmitAudit();
                wxOpenMaSubmitAudit.setPagePath(page.get(0));
                wxOpenMaSubmitAudit.setFirstId(wxOpenMaCategory.getFirstId());
                wxOpenMaSubmitAudit.setFirstClass(wxOpenMaCategory.getFirstClass());
                wxOpenMaSubmitAudit.setSecondId(wxOpenMaCategory.getSecondId());
                wxOpenMaSubmitAudit.setSecondClass(wxOpenMaCategory.getSecondClass());
                wxOpenMaSubmitAudit.setTitle("首页");
                wxOpenMaSubmitAudits.add(wxOpenMaSubmitAudit);
                if (i == CommonConstants.NUMBER_FOUR) {
                    break;
                }
            }
            WxOpenMaSubmitAuditMessage wxOpenMaSubmitAuditMessage = new WxOpenMaSubmitAuditMessage();
            wxOpenMaSubmitAuditMessage.setItemList(wxOpenMaSubmitAudits);
            WxOpenMaSubmitAuditResult wxOpenMaSubmitAuditResult = wxMaServiceByAppid.submitAudit(wxOpenMaSubmitAuditMessage);
            String auditId = wxOpenMaSubmitAuditResult.getAuditId().toString();
            //记录小程序上传审核记录
            auditRecordService.addMiniAuditRecord(miniInfo, auditId, templateDetail, detailMinis);
            return auditId;
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(e.getError().getErrorMsg(), e.getError().getErrorCode());
        }
    }

    /**
     * 发布已通过审核的小程序
     * 发布最后一个审核通过的小程序代码版本
     *
     * @param appId
     * @return
     */
    @Override
    public void codeRelease(String appId) {
        WxMaCodeService codeService = wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(appId).getCodeService();
        try {
            codeService.release();
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(e.getError().getErrorMsg(), SystemCode.FAILURE.getCode());
        }
    }


    @Override
    public void updateMini(MiniInfoUpdateDto miniInfoUpdateDto) {
        MiniInfo miniInfo = this.getById(miniInfoUpdateDto.getMiniId());
        if (miniInfo == null) {
            throw new ServiceException("错误数据", SystemCode.FAILURE.getCode());
        }
        //操作类型 1-续费 4-禁用或开启
        switch (miniInfoUpdateDto.getOptionType()) {
            case 1:
                long expirationTime = DateUtils.localDateTimeCoverTimestamp(miniInfo.getExpirationTime());
                DateTime offsetMonth = DateUtil.offsetMonth(DateUtil.date(expirationTime), miniInfoUpdateDto.getServiceTime() * CommonConstants.NUMBER_TWELVE);
                miniInfo.setExpirationTime(DateUtils.timestampCoverLocalDateTime(offsetMonth.getTime()));
                break;
            case 4:
                miniInfo.setForbidStatus(miniInfoUpdateDto.getStatus());
                break;
            default:
                throw new ServiceException(SystemCode.FAILURE.getMsg(), SystemCode.FAILURE.getCode());
        }
        this.saveOrUpdate(miniInfo);
    }

    @Override
    public Integer getUseTemplateCodeCount(Long templateCodeId) {
        Integer countNumber = this.getBaseMapper().selectCount(new QueryWrapper<MiniInfo>().eq("current_version_id", templateCodeId));
        return countNumber;
    }


    @Override
    public List<MiniInfo> getByCombo(Integer comboId) {
        return this.baseMapper.selectList(new QueryWrapper<MiniInfo>().eq("combo_id", comboId));
    }


    /**
     * 获取最后一次提交的审核记录
     *
     * @param appId appid
     * @return
     */
    @Override
    public WxMaCodeAuditStatus getMiniLastAuditCode(String appId) {
        WxMaCodeService codeService = wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(appId).getCodeService();
        try {
            return codeService.getLatestAuditStatus();
        } catch (WxErrorException e) {
            return null;
        }
    }

    @Override
    public MiniInfo getByUserName(String toUser) {
        return this.baseMapper.selectOne(new QueryWrapper<MiniInfo>().eq("user_name", toUser));
    }


    /**
     * 生成小程序基本信息
     *
     * @param appId appid
     * @return
     */
    private MiniInfo initMiniInfo(String appId) {
        try {
            WxOpenAuthorizerInfo wxOpenAuthorizerInfo = wxOpenService.getWxOpenComponentService().getAuthorizerInfo(appId).getAuthorizerInfo();
            long currentTimeMillis = System.currentTimeMillis();
            MiniInfo miniInfo = miniInfoService.getOne(new QueryWrapper<MiniInfo>().eq("app_id", appId));
            if (miniInfo == null) {
                miniInfo = new MiniInfo();
                DateTime expirationTime = DateUtil.offsetMonth(new Date(currentTimeMillis), CommonConstants.NUMBER_TWELVE);
                miniInfo.setExpirationTime(DateUtils.timestampCoverLocalDateTime(expirationTime.getTime()));
                miniInfo.setRunFlag(RunFlagEnum.NOT_UPLOADING.getStatus());
                miniInfo.setForbidStatus(CommonConstants.NUMBER_ZERO);
                miniInfo.setAppId(appId);
            }
            miniInfo.setUserName(wxOpenAuthorizerInfo.getUserName());
            miniInfo.setServiceTypeInfo(wxOpenAuthorizerInfo.getServiceTypeInfo());
            if (wxOpenAuthorizerInfo.getMiniProgramInfo() != null) {
                miniInfo.setServiceTypeInfo(CommonConstants.NUMBER_THREE);
            }
            miniInfo.setVerifyTypeInfo(wxOpenAuthorizerInfo.getVerifyTypeInfo());
            miniInfo.setAlias(wxOpenAuthorizerInfo.getAlias());
            //生成基本信息
            miniInfo.setMiniName(wxOpenAuthorizerInfo.getNickName());
            miniInfo.setAuthorizerFlag(AuthorizerFlagEnum.AUTHENTICATION.getStatus());
            miniInfo.setHeadImageUrl(wxOpenAuthorizerInfo.getHeadImg());
            miniInfo.setPrincipalName(wxOpenAuthorizerInfo.getPrincipalName());
            miniInfo.setMiniCode(wxOpenAuthorizerInfo.getQrcodeUrl());
            miniInfo.setSignature(wxOpenAuthorizerInfo.getSignature());
            miniInfo.setBusinessInfo(JSON.toJSONString(wxOpenAuthorizerInfo.getBusinessInfo()));
            miniInfo.setPrincipalType(CommonConstants.NUMBER_ONE);
            miniInfoService.saveOrUpdate(miniInfo);
            return miniInfo;
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        throw new ServiceException(SystemCode.FAILURE.getMsg(), SystemCode.FAILURE.getCode());
    }


    @Override
    public MiniInfo getByMpTenantId(String tenantId) {
        return this.baseMapper.selectOne(new QueryWrapper<MiniInfo>().eq("tenant_id", tenantId)
                .in("service_type_info", Arrays.asList(CommonConstants.NUMBER_ONE, CommonConstants.NUMBER_TWO)));
    }

    @Override
    public MiniInfo getByMiniTenantId(String tenantId) {
        return this.baseMapper.selectOne(new QueryWrapper<MiniInfo>().eq("tenant_id", tenantId)
                .eq("service_type_info", CommonConstants.NUMBER_THREE));
    }

    @Override
    public LoginDto login(String tenantId, String code) {
        logger.debug("登录调用---->".concat("tenantId:").concat(tenantId).concat(",code: ").concat(code));
        MiniInfo miniInfo = this.getByMiniTenantId(tenantId);
        WxOpenComponentService wxOpenComponentService = wxOpenService.getWxOpenComponentService();
        try {
            WxMaJscode2SessionResult wxMaJscode2SessionResult = wxOpenComponentService.miniappJscode2Session(miniInfo.getAppId(), code);
            LoginDto dto = new LoginDto();
            dto.setSessionKey(wxMaJscode2SessionResult.getSessionKey());
            dto.setOpenId(wxMaJscode2SessionResult.getOpenid());
            dto.setUnionId(wxMaJscode2SessionResult.getUnionid());
            return dto;
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(e.getError().getErrorMsg(), e.getCause());
        }
    }

    @Override
    public Result<String> wxaGetwxacode(WxaGetwxacode wxaGetwxacode) {
        Integer width = wxaGetwxacode.getWidth();
        if (width == null || width.equals(0)) {
            width = 430;
        }
        MiniInfo miniInfo = checkCurrentTenantId(TenantContextHolder.getTenantId());
        String key = miniInfo.getAppId().concat(":").concat(wxaGetwxacode.getPath()).concat(":").concat(width.toString());
        String base64 = timedCache.get(key);
        if (StrUtil.isNotEmpty(base64)) {
            return Result.ok(base64);
        }
        WxOpenMaService wxOpenMaService = this.wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(miniInfo.getAppId());
        WxMaQrcodeService qrcodeService = wxOpenMaService.getQrcodeService();
        try {
            byte[] qrcodeBytes = qrcodeService.createWxaCodeBytes(wxaGetwxacode.getPath(), width, true, null, false);
            base64 = "data:image/png;base64," + Base64.byteArrayToBase64(qrcodeBytes);
            timedCache.put(key, base64);
            return Result.ok(base64);
        } catch (WxErrorException e) {
            e.printStackTrace();
            String msg = "系统异常";
            if (e.getError().getErrorCode() == MeConstant.MINI_CODE_61007) {
                msg = "未授权小程管理权限,无法获取小程序二维码";
            }
            return Result.failed(e.getError().getErrorCode(), msg);
        }
    }

    @Override
    public MiniInfo getByUserDefualtMini(Long accountInfoId) {
        return this.getBaseMapper().selectOne(new QueryWrapper<MiniInfo>().eq("default_status", CommonConstants.NUMBER_ONE).eq("account_id", accountInfoId));
    }


    @Override
    public BaseInfoVo baseInfo(Integer type) {
        if (type < CommonConstants.NUMBER_ONE || type > CommonConstants.NUMBER_TWO) {
            throw new ServiceException("错误类型");
        }
        String tenantId = TenantContextHolder.getTenantId();
        if (StrUtil.isEmpty(tenantId)) {
            throw new ServiceException("租户id为空");
        }
        MiniInfo miniInfo = null;
        if (type.equals(CommonConstants.NUMBER_ONE)) {
            miniInfo = getByMpTenantId(tenantId);
        }
        if (type.equals(CommonConstants.NUMBER_TWO)) {
            miniInfo = getByMiniTenantId(tenantId);
        }
        if (miniInfo == null) {
            return new BaseInfoVo();
        }
        BaseInfoVo baseInfoVo = new BaseInfoVo();
        baseInfoVo.setMiniName(miniInfo.getMiniName());
        baseInfoVo.setLogo(miniInfo.getHeadImageUrl());
        baseInfoVo.setAppid(miniInfo.getAppId());
        baseInfoVo.setQrcode(miniInfo.getMiniCode());
        baseInfoVo.setSignature(miniInfo.getSignature());
        baseInfoVo.setPrincipalName(miniInfo.getPrincipalName());
        baseInfoVo.setAlias(miniInfo.getAlias());
        baseInfoVo.setServiceTypeInfo(miniInfo.getServiceTypeInfo());
        baseInfoVo.setVerifyTypeInfo(miniInfo.getVerifyTypeInfo());
        if (type.equals(CommonConstants.NUMBER_TWO)) {
            getMiniInfo(miniInfo, baseInfoVo);
        }
        if (type.equals(CommonConstants.NUMBER_ONE)) {
            baseInfoVo.setAuthorizerFlag(CommonConstants.NUMBER_ONE.equals(miniInfo.getAuthorizerFlag()) ? Boolean.TRUE : Boolean.FALSE);
            if (baseInfoVo.getAuthorizerFlag().equals(Boolean.TRUE)) {
                AuthToken authToken = authTokenService.getByAppid(miniInfo.getAppId());
                if (authToken != null && StrUtil.isNotEmpty(authToken.getFuncInfo())) {
                    List<String> authInfos = new LinkedList<>();
                    List<Integer> authcodes = JSONObject.parseArray(authToken.getFuncInfo()).toJavaList(Integer.class);
                    for (Integer authcode : authcodes) {
                        String permission = MP_PERMISSION.get(authcode);
                        if (StrUtil.isNotEmpty(permission)) {
                            authInfos.add(permission);
                        }
                    }
                    baseInfoVo.setAuthInfo(authInfos);
                }
            }

        }
        return baseInfoVo;
    }

    /**
     * 封装小程序信息
     *
     * @param miniInfo   com.medusa.gruul.platform.api.entity.MiniInfo
     * @param baseInfoVo com.medusa.gruul.platform.model.vo.BaseInfoVo
     */
    private void getMiniInfo(MiniInfo miniInfo, BaseInfoVo baseInfoVo) {
        //小程序查询服务类目
        try {
            WxOpenAuthorizerInfo authorizerInfo = wxOpenService.getWxOpenComponentService().getAuthorizerInfo(miniInfo.getAppId()).getAuthorizerInfo();
            List<WxOpenAuthorizerInfo.MiniProgramInfo.Category> categories = authorizerInfo.getMiniProgramInfo().getCategories();
            String serviceClass = categories.stream().map(obj -> obj.getFirst().concat(">").concat(obj.getSecond()).concat("、")).collect(Collectors.joining());
            baseInfoVo.setServiceClass(serviceClass);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        Long currentVersionId = miniInfo.getCurrentVersionId();
        if (currentVersionId != null && currentVersionId > 0) {
            PlatformShopTemplateDetail shopTemplateDetail = platformShopTemplateDetailService.getByFilterById(currentVersionId);
            baseInfoVo.setCurrentVersionNumName(shopTemplateDetail.getVersion());
            AuditRecord record = auditRecordService.getByIdAndAppId(miniInfo.getCurrentAiditId(), miniInfo.getAppId());
            baseInfoVo.setCurrentVersionSendTime(record.getUpdateTime());
        }
        Long aiditId = miniInfo.getAiditId();
        if (aiditId != null && aiditId > 0 && !aiditId.equals(miniInfo.getCurrentAiditId())) {
            AuditRecord currentAuditRecord = auditRecordService.getByIdAndAppId(miniInfo.getAiditId(), miniInfo.getAppId());
            PlatformShopTemplateDetail shopTemplateDetail = platformShopTemplateDetailService.getByFilterById(currentAuditRecord.getVersionId());
            baseInfoVo.setAuditingVersionNumName(shopTemplateDetail.getVersion());
            baseInfoVo.setAuditStatus(currentAuditRecord.getAuditStatus());
            baseInfoVo.setAuditingVersionSummitTime(currentAuditRecord.getCreateTime());
            baseInfoVo.setAuditingVersionEndTime(currentAuditRecord.getUpdateTime());
            baseInfoVo.setAuditingComeToNothingReason(currentAuditRecord.getReason());
            PlatformShopTemplateDetailMinis shopTemplateDetailIdAndCodeTemplateId = platformShopTemplateDetailMinisService.getByShopTemplateDetailIdAndCodeTemplateId(shopTemplateDetail.getId(), currentAuditRecord.getTemplateId());
            //兼容老店铺
            if (shopTemplateDetailIdAndCodeTemplateId != null) {
                baseInfoVo.setAuditingTemplateDetailMinisId(shopTemplateDetailIdAndCodeTemplateId.getId());
            }
        }
        PlatformShopInfo platformShopInfo = platformShopInfoService.getByTenantId(miniInfo.getTenantId());
        PlatformShopTemplateDetail shopTeamplteNewDetail = platformShopTemplateDetailService.getByShopTeamplteNewDetail(platformShopInfo.getShopTemplateId());
        //不等于当前版本，并且也不是审核中的版本，才设置最新的版本号
        if (shopTeamplteNewDetail != null &&
                !shopTeamplteNewDetail.getId().equals(miniInfo.getCurrentVersionId()) && !shopTeamplteNewDetail.getVersion().equals(baseInfoVo.getAuditingVersionNumName())) {
            baseInfoVo.setVersionUpdateNumName(shopTeamplteNewDetail.getVersion());
            baseInfoVo.setVersionUpdateTime(shopTeamplteNewDetail.getCreateTime());
            //获取模板详情下对应的小程序版本
            List<MiniCodeVersionVo> detailMinis = platformShopTemplateDetailMinisService.
                    getByShopTemplateDetailId(shopTeamplteNewDetail.getId()).stream().map(obj -> BeanUtil.toBean(obj, MiniCodeVersionVo.class)).collect(Collectors.toList());
            baseInfoVo.setCodeVersionVos(detailMinis);
        }
        if (miniInfo.getAuthorizerFlag().equals(CommonConstants.NUMBER_ZERO)) {
            baseInfoVo.setRunFlag(Boolean.FALSE);
            baseInfoVo.setAuthorizerFlag(Boolean.FALSE);
        } else {
            baseInfoVo.setAuthorizerFlag(Boolean.TRUE);
            baseInfoVo.setRunFlag(Boolean.TRUE);
            if (miniInfo.getRunFlag().equals(CommonConstants.NUMBER_ZERO)) {
                baseInfoVo.setRunFlag(Boolean.FALSE);
            }
        }
    }


    @Override
    public void versionUpdate(Long templateDetailMinisId) {
        MiniInfo miniInfo = checkCurrentTenantId(TenantContextHolder.getTenantId());
        PlatformShopInfo platformShopInfo = platformShopInfoService.getByTenantId(miniInfo.getTenantId());
        PlatformShopTemplateDetail shopTeamplteNewDetail = platformShopTemplateDetailService.getByShopTeamplteNewDetail(platformShopInfo.getShopTemplateId());
        PlatformShopTemplateDetailMinis detailMinis = platformShopTemplateDetailMinisService.getById(templateDetailMinisId);
        if (detailMinis == null) {
            throw new ServiceException("不存在子版本");
        }
        if (miniInfo.getCurrentVersionId() != null && miniInfo.getCurrentVersionId().equals(shopTeamplteNewDetail.getId())) {
            throw new ServiceException("当前已是最新版本");
        }
        if (!detailMinis.getShopTemplateDetailId().equals(shopTeamplteNewDetail.getId())) {
            throw new ServiceException("子版本对应不一致");
        }
        this.miniReq(miniInfo);
        //上传最新的模板代码
        miniUploadCode(miniInfo.getAppId(), Long.valueOf(detailMinis.getCodeTempleteId()), shopTeamplteNewDetail.getVersion(),
                shopTeamplteNewDetail.getVersionLog(), MapUtil.of("tenantId", miniInfo.getTenantId()));
        //小程序提交审核
        String audit = this.codeSubmitAudit(miniInfo, shopTeamplteNewDetail, detailMinis);
        if (audit.equals(CommonConstants.NUMBER_ZERO.toString())) {
            throw new ServiceException("小程序存在审核中代码,请撤销审核代码或等待审核通过");
        }
        if (MeConstant.FUYI.equals(audit)) {
            throw new ServiceException("未知异常");
        }

    }

    @Override
    public MiniInfo checkCurrentTenantId(String tenantId) {
        if (StrUtil.isEmpty(tenantId)) {
            throw new ServiceException("租户id为空");
        }
        MiniInfo miniInfo = miniInfoService.getByMiniTenantId(tenantId);
        if (miniInfo == null) {
            throw new ServiceException("非法租户");
        }
        return miniInfo;

    }

    @Override
    public String getPreAuthCode(PreAuthCodeDto preAuthCodeDto) {
        try {
            //校验店铺
            platformShopInfoService.check(preAuthCodeDto.getPlatformShopId());
            //设置路径参数,回调方法中根据uuid找到对应的信息
            PlatformRedis platformRedis = new PlatformRedis();
            String redisKey = IdUtil.fastSimpleUUID();
            //设置路径参数,回调方法中根据uuid找到对应的信息
            String redirectUrl = wechatOpenProperties.getRedirectUrl().replace("$UUID", redisKey);
            platformRedis.setNxPx(RedisConstant.AUTH_KEY.concat(redisKey), JSON.toJSONString(preAuthCodeDto), TimeConstants.THIRTY_MINUTES);
            return wxOpenService.getWxOpenComponentService().getPreAuthUrl(redirectUrl, preAuthCodeDto.getAuthType().toString(), null);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException("获取失败" + e.getError().getErrorMsg());
        }
    }

    @Override
    public void preAuthCodeNotify(String authCode, Integer expiresIn, String uuid, HttpServletResponse response) {
        PlatformRedis platformRedis = new PlatformRedis();
        String jsonData = platformRedis.get(RedisConstant.AUTH_KEY.concat(uuid));
        if (StrUtil.isEmpty(jsonData)) {
            throw new ServiceException("授权超时请重新授权");
        }
        String successPage = "";
        PreAuthCodeDto preAuthCodeDto = JSON.parseObject(jsonData, PreAuthCodeDto.class);
        //绑定店铺与小程序之间的关系
        successPage = preAuthCodeDto.getSuccessPage();
        PlatformShopInfo platformShopInfo = platformShopInfoService.getById(preAuthCodeDto.getPlatformShopId());
        try {
            WxOpenAuthorizationInfo authorizationInfo = wxOpenService.getWxOpenComponentService().getQueryAuth(authCode).getAuthorizationInfo();
            //判断appId是否已经存在
            MiniInfo miniInfo = miniInfoService.getOne(new QueryWrapper<MiniInfo>().eq("app_id", authorizationInfo.getAuthorizerAppid()));
            if (miniInfo != null) {
                //判断是否是在已授权的状态下,是则无法使用该小程序进行授权
                if (miniInfo.getAuthorizerFlag().equals(CommonConstants.NUMBER_ONE)) {
                    String type = "公众号";
                    if (preAuthCodeDto.getAuthType().equals(CommonConstants.NUMBER_TWO)) {
                        type = "小程序";
                    }
                    if (platformShopInfo.getTenantId().equals(miniInfo.getTenantId())) {
                        throw new ServiceException("请勿重复授权相同店铺");
                    } else {
                        throw new ServiceException("授权失败,该type已授权至其他店铺".replace("type", type));
                    }
                }
                //判断是否属于原本店铺,如果不是则删除该小程序信息
                if (!miniInfo.getTenantId().equals(platformShopInfo.getTenantId())) {
                    miniInfoService.removeById(miniInfo.getId());
                    platformShopInfo.setBindMiniId(0L);
                    miniInfo = null;
                }
            }
            //判断店铺是否已存在绑定的小程序或者公众号，不相同就删除
            MiniInfo oldMpInfo = miniInfoService.getById(platformShopInfo.getBindMpId());
            MiniInfo oldMiniInfo = miniInfoService.getById(platformShopInfo.getBindMiniId());
            String tenantId = null;
            PlatformShopInfo up = new PlatformShopInfo();
            //判断是否属于原本店铺的小程序或者公众号,不是则删除原本数据
            if (preAuthCodeDto.getAuthType().equals(CommonConstants.NUMBER_TWO) && oldMiniInfo != null && !oldMiniInfo.getAppId().equals(authorizationInfo.getAuthorizerAppid())) {
                miniInfoService.removeById(oldMiniInfo.getId());
                tenantId = oldMiniInfo.getTenantId();
                up.setBindMpId(0L);
            } else if (preAuthCodeDto.getAuthType().equals(CommonConstants.NUMBER_ONE) && oldMpInfo != null && !oldMpInfo.getAppId().equals(authorizationInfo.getAuthorizerAppid())) {
                miniInfoService.removeById(oldMpInfo.getId());
                tenantId = oldMpInfo.getTenantId();
                up.setBindMpId(0L);
            }
            //更新小程序或公众号原本店铺所绑定的表id
            if (StrUtil.isEmpty(tenantId)) {
                platformShopInfoService.update(up, new QueryWrapper<PlatformShopInfo>().eq("tenant_id", tenantId));
            }

            initAuthToken(authorizationInfo);
            miniInfo = initMiniInfo(authorizationInfo.getAuthorizerAppid());
            //判断是否新注册小程序或公众号,不存在租户id则进行绑定
            if (StrUtil.isEmpty(miniInfo.getTenantId())) {
                miniInfo.setTenantId(platformShopInfo.getTenantId());
                this.updateById(miniInfo);
            }
            //判断授权类型
            if (preAuthCodeDto.getAuthType().equals(CommonConstants.NUMBER_TWO)) {
                platformShopInfo.setBindMiniId(miniInfo.getId());
            } else {
                platformShopInfo.setBindMpId(miniInfo.getId());
            }
            platformRedis.setNxPx(RedisConstant.AUTH_MSG.concat(preAuthCodeDto.getPlatformShopId().toString()), JSON.toJSONString(Result.ok()), TimeConstants.FIVE_MINUTES);
            //异步关联公众号和小程序平台
            CompletableFuture.runAsync(() -> createPlatform(platformShopInfo.getTenantId()));
        } catch (ServiceException serviceException) {
            successPage = setAuthErrorMsg(serviceException.getCode(), serviceException.getMsg(), preAuthCodeDto.getPlatformShopId(), successPage);
        } catch (Exception e) {
            e.printStackTrace();
            successPage = setAuthErrorMsg(CommonConstants.FAIL, e.getMessage(), preAuthCodeDto.getPlatformShopId(), successPage);
        } finally {
            try {
                platformShopInfoService.updateById(platformShopInfo);
                response.sendRedirect(successPage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void createPlatform(String tenantId) {
        MiniInfo miniInfo = this.getByMiniTenantId(tenantId);
        MiniInfo mpInfo = this.getByMpTenantId(tenantId);
        //必须同时存在小程序和公众号才进行绑定
        if (miniInfo == null || mpInfo == null) {
            logger.info("店铺: {} 未同时授权公众号和小程序", tenantId);
            return;
        }
        //判断主体是否相同
        if (!miniInfo.getPrincipalName().equals(mpInfo.getPrincipalName())) {
            logger.info("店铺: {}：公众号与小程序主体不一致无法进行平台绑定", tenantId);
            return;
        }
        if (miniInfo.getAuthorizerFlag().equals(CommonConstants.NUMBER_ZERO) && mpInfo.getAuthorizerFlag().equals(CommonConstants.NUMBER_ZERO)) {
            logger.info("店铺: {}：小程序或公众号未授权无法进行平台绑定", tenantId);
            return;
        }
        logger.info("店铺: {}：公众号与小程序进行平台绑定", tenantId);
        WxOpenComponentService wxOpenComponentService = wxOpenService.getWxOpenComponentService();
        //获取小程序和公众号的平台
        WxOpenGetResult miniOpenAccount = null;
        try {
            miniOpenAccount = wxOpenComponentService.getOpenAccount(miniInfo.getAppId(), WxConsts.AppIdType.MINI_TYPE);
        } catch (WxErrorException e) {
            //89000 -> open not exists，该公众号/小程序未绑定微信开放平台帐号
            if (e.getError().getErrorCode() != MeConstant.WX_OPEN_CODE_89002) {
                logger.info("店铺{} :授权异常 WxErrorException ：{}", tenantId, e.getMessage());
                return;
            }
        }
        WxOpenGetResult mpOpenAccount = null;
        try {
            mpOpenAccount = wxOpenComponentService.getOpenAccount(mpInfo.getAppId(), WxConsts.AppIdType.MP_TYPE);
        } catch (WxErrorException e) {
            if (e.getError().getErrorCode() != MeConstant.WX_OPEN_CODE_89002) {
                logger.info("店铺{} :授权异常 WxErrorException ：{}", tenantId, e.getMessage());
                return;
            }
        }
        try {
            if (miniOpenAccount == null && mpOpenAccount == null) {
                logger.info("店铺 {}：创建三方平台，流程->公众号和小程序都是未授权给开发平台,以小程序为基准，创建三方平台，公众号进行绑定", tenantId);
                WxOpenCreateResult openAccount = wxOpenComponentService.createOpenAccount(miniInfo.getAppId(), WxConsts.AppIdType.MINI_TYPE);
                wxOpenComponentService.bindOpenAccount(mpInfo.getAppId(), WxConsts.AppIdType.MP_TYPE, openAccount.getOpenAppid());
            } else if (miniOpenAccount != null && mpOpenAccount != null) {
                if (miniOpenAccount.getOpenAppid().equals(mpOpenAccount.getOpenAppid())) {
                    logger.info("店铺{}：小程序和公众号开发平台一致无需绑定", tenantId);
                    return;
                }
                logger.info("店铺{}：创建三方平台，流程->公众号和小程序都有已绑定的平台，解绑公众号所绑定的平台，然后用小程序的进行绑定", tenantId);
                wxOpenComponentService.unbindOpenAccount(mpInfo.getAppId(), WxConsts.AppIdType.MP_TYPE, mpOpenAccount.getOpenAppid());
                wxOpenComponentService.bindOpenAccount(mpInfo.getAppId(), WxConsts.AppIdType.MP_TYPE, miniOpenAccount.getOpenAppid());
            } else if (miniOpenAccount == null) {
                logger.info("店铺{}：创建三方平台，流程->小程序不存在开放平台，公众号存在", tenantId);
                wxOpenComponentService.bindOpenAccount(miniInfo.getAppId(), WxConsts.AppIdType.MINI_TYPE, mpOpenAccount.getOpenAppid());
            } else {
                logger.info("店铺{}：创建三方平台，流程->公众号不存在开放平台，小程序存在", tenantId);
                wxOpenComponentService.bindOpenAccount(miniInfo.getAppId(), WxConsts.AppIdType.MINI_TYPE, miniOpenAccount.getOpenAppid());
            }
        } catch (WxErrorException wxErrorException) {
            wxErrorException.printStackTrace();
            logger.info("店铺{}：创建三方平台失败： {}", tenantId, wxErrorException.getMessage());
            return;
        }
        logger.info("店铺{}：三方平台绑定成功", tenantId);
    }

    /**
     * 设置授权错误消息
     *
     * @param errorCode              错误码
     * @param errorMsg               错误消息
     * @param platformShopTemplateId 平台店铺id
     * @param successPage            返回的页面
     * @return 最终返回的页面
     */
    private String setAuthErrorMsg(int errorCode, String errorMsg, Long platformShopTemplateId, String successPage) {
        if (successPage.contains(MeConstant.WENHAO)) {
            successPage = successPage.concat("&auth=true");
        } else {
            successPage = successPage.concat("?auth=true");
        }
        PlatformRedis platformRedis = new PlatformRedis();
        platformRedis.setNxPx(RedisConstant.AUTH_MSG.concat(platformShopTemplateId.toString()), JSON.toJSONString(Result.failed(errorCode, errorMsg)), TimeConstants.FIVE_MINUTES);
        return successPage;
    }

    @Override
    public void removeByShopInfoAll(String tenantId) {
        List<MiniInfo> infos = miniInfoService.list(new QueryWrapper<MiniInfo>().eq("tenant_id", tenantId));
        if (CollectionUtil.isEmpty(infos)) {
            return;
        }
        List<String> appIds = infos.stream().map(MiniInfo::getAppId).collect(Collectors.toList());
        authTokenService.remove(new QueryWrapper<AuthToken>().in("authorizer_appid", appIds));
        miniInfoService.remove(new QueryWrapper<MiniInfo>().eq("tenant_id", tenantId));
    }

    @Override
    public void revocation() {
        MiniInfo miniInfo = checkCurrentTenantId(TenantContextHolder.getTenantId());
        PlatformRedis platformRedis = new PlatformRedis();
        String redisKey = RedisConstant.REVOCATION.concat(miniInfo.getAppId());
        String redisValue = platformRedis.get(redisKey);
        if (StrUtil.isNotEmpty(redisValue)) {
            throw new ServiceException("一天只能撤销一次");
        }
        WxOpenMaService wxOpenMaService = wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(miniInfo.getAppId());
        wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(miniInfo.getAppId());

        try {
            wxOpenMaService.getCodeService().undoCodeAudit();
            AuditRecord auditRecord = auditRecordService.getByIdAndAppId(miniInfo.getAiditId(), miniInfo.getAppId());
            auditRecord.setAuditStatus(AuditstatusEnum.REFUSE.getStatus());
            auditRecord.setReason(AuditstatusEnum.ANNUL.getDescription());
            auditRecordService.updateById(auditRecord);
            miniInfo.setAiditId(Long.valueOf(CommonConstants.NUMBER_ZERO));
            miniInfoService.updateById(miniInfo);
            Date date = new Date();
            DateTime endOfDay = DateUtil.endOfDay(date);
            long time = DateUtil.between(date, endOfDay, DateUnit.MS);
            //任意存值表示今天已操作过
            platformRedis.setNxPx(redisKey, MeConstant.TEXT, time);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void baseInfoUpdate() {
        MiniInfo miniInfo = checkCurrentTenantId(TenantContextHolder.getTenantId());
        if (miniInfo.getAuthorizerFlag().equals(AuthorizerFlagEnum.UNAUTHORIZED.getStatus())) {
            throw new ServiceException("请先重新授权小程序");
        }
        initMiniInfo(miniInfo.getAppId());
    }

    @Override
    public Result<MiniAuthInfoDto> getMiniAuthInfo(String tenantId) {
        MiniAuthInfoDto dto = new MiniAuthInfoDto();
        try {
            MiniInfo miniInfo = checkCurrentTenantId(tenantId);
            WxOpenMaService wxMaServiceByAppid = wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(miniInfo.getAppId());
            String accessToken = wxMaServiceByAppid.getAccessToken();
            dto.setAccessToken(accessToken);
            dto.setAppId(miniInfo.getAppId());
        } catch (ServiceException serviceException) {
            return Result.failed(serviceException.getMsg());
        } catch (WxErrorException e) {
            e.printStackTrace();
            return Result.failed(e.getError().getErrorMsg());
        }
        return Result.ok(dto);
    }

    @Override
    public Result<String> errorInfo(Long platformShopTemplateId) {
        PlatformRedis platformRedis = new PlatformRedis();
        String jsonData = platformRedis.get(RedisConstant.AUTH_MSG.concat(platformShopTemplateId.toString()));
        if (StrUtil.isEmpty(jsonData)) {
            throw new ServiceException("无授权数据");
        }
        Result result = JSON.parseObject(jsonData, Result.class);
        if (result.getCode() == MeConstant.MINI_CODE_80082) {
            result.setMsg("小程序不支持微信直播插件,无法上传代码");
        }
        return result;
    }

    @Override
    public void updateMiniNewConfig(Long versionId, String tenantId) {
        PlatformShopInfo platformShopInfo = platformShopInfoService.getByTenantId(tenantId);
        PlatformShopTemplateDetail shopTemplateDetail = platformShopTemplateDetailService.getById(versionId);
        if (platformShopInfo != null) {
            PlatformShopInfo info = new PlatformShopInfo();
            info.setShopTemplateDetailId(versionId);
            info.setId(platformShopInfo.getId());
            platformShopInfoService.updateById(info);
        }
        //更新当前版本订阅消息默认值数据
        try {
            platformShopMessageService.upSubscriptionMsg(shopTemplateDetail.getVersion(), tenantId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void miniDomaiReq() {
        MiniInfo miniInfo = checkCurrentTenantId(TenantContextHolder.getTenantId());
        this.miniReq(miniInfo);
    }

    @Override
    public MiniInfo getByAppId(String appId) {
        return this.getBaseMapper().selectOne(new QueryWrapper<MiniInfo>().eq("app_id", appId));
    }

    @Override
    public List<MiniInfo> getByTemplateDetailMinisId(Long templateDetailMinisId) {
        return this.getBaseMapper().selectList(new QueryWrapper<MiniInfo>().eq("template_detail_minis_id", templateDetailMinisId));
    }

    @Override
    public Boolean bindOpenInfo(String tenantId) {
        MiniInfo mpInfo = getByMpTenantId(tenantId);
        if (mpInfo == null || mpInfo.getAuthorizerFlag().equals(CommonConstants.NUMBER_ZERO)) {
            return Boolean.FALSE;
        }
        MiniInfo miniInfo = getByMiniTenantId(tenantId);
        if (miniInfo == null || mpInfo.getAuthorizerFlag().equals(CommonConstants.NUMBER_ZERO)) {
            return Boolean.FALSE;
        }
        WxOpenComponentService wxOpenComponentService = wxOpenService.getWxOpenComponentService();
        try {
            WxOpenGetResult miniOpenAccount = wxOpenComponentService.getOpenAccount(miniInfo.getAppId(), WxConsts.AppIdType.MINI_TYPE);
            WxOpenGetResult mpOpenAccount = wxOpenComponentService.getOpenAccount(mpInfo.getAppId(), WxConsts.AppIdType.MP_TYPE);
            if (!miniOpenAccount.getOpenAppid().equals(mpOpenAccount.getOpenAppid())) {
                logger.error("店铺：{} 小程序：{} 公众号：{}  未进行平台绑定", tenantId, miniInfo.getAppId(), mpInfo.getAppId());
                return Boolean.FALSE;
            }
        } catch (WxErrorException wxErrorException) {
            wxErrorException.printStackTrace();
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }


    /**
     * 新增的小程序,默认上传最新版本
     * <p>
     * s1.1.5弃用
     *
     * @param miniInfo 小程序信息
     */
    @Deprecated
    private void newMiniUpload(MiniInfo miniInfo) {
        //设置小程序请求域名
        this.miniReq(miniInfo);
        //获取店铺模板使用的模板版本
        PlatformShopInfo platformShopInfo = platformShopInfoService.getByTenantId(miniInfo.getTenantId());
        PlatformShopTemplateDetail templateDetail = platformShopTemplateDetailService.getByShopTeamplteNewDetail(platformShopInfo.getShopTemplateId());
        //上传最新的模板代码
        miniUploadCode(miniInfo.getAppId(), templateDetail.getCodeTempleteId(), templateDetail.getVersion(),
                "", MapUtil.of("tenantId", miniInfo.getTenantId()));
        updateById(miniInfo);
        //小程序提交审核
//        codeSubmitAudit(miniInfo, templateDetail, detailMinis);

    }

    /**
     * @param templateId  模板id
     * @param userVersion 定义版本
     * @param userDesc    版本描述
     */
    private void miniUploadCode(String appId, Long templateId, String userVersion, String userDesc, Map<String, Object> ext) {
        WxOpenMaService wxOpenMaService = wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(appId);
        WxMaOpenCommitExtInfo wxMaOpenCommitExtInfo = WxMaOpenCommitExtInfo.INSTANCE();
        wxMaOpenCommitExtInfo.setExtAppid(appId);
        // 自定义数据
        wxMaOpenCommitExtInfo.setExtMap(ext);
        try {
            wxOpenMaService.codeCommit(templateId, userVersion, userDesc, wxMaOpenCommitExtInfo);
        } catch (WxErrorException e) {
            e.printStackTrace();
            int errorCode = e.getError().getErrorCode();
            switch (errorCode) {
                case 61007:
                    throw new ServiceException("该小程序授权权限不足,请检查是否授权第三方代码管理权限", errorCode);
                case -80082:
                    throw new ServiceException("该小程序没有直播权限，无法上传代码", errorCode);
                default:
                    throw new ServiceException(e.getError().getErrorMsg(), errorCode);
            }
        }

    }

    /**
     * 设置小程序请求域名
     *
     * @param miniInfo com.medusa.gruul.platform.api.entity.MiniInfo
     */
    private void miniReq(MiniInfo miniInfo) {
        WxOpenMaService wxMaServiceByAppid = wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(miniInfo.getAppId());
        try {
            log.debug("设置请求域名");
            SystemConfigVo systemConfigVo = systemConfService.getTypeInfo(0);
            if (null == systemConfigVo || null == systemConfigVo.getSystemConfig() || StrUtil.isEmpty(systemConfigVo.getSystemConfig().getMiniDomain())) {
                throw new ServiceException("服务器配置未填写");
            }
            WxOpenMaDomainResult openMaDomainResult = wxMaServiceByAppid.modifyDomain("get", null, null, null, null);
            String value = systemConfigVo.getSystemConfig().getMiniDomain();
            //请求域名
            List<String> requestDomainList = isAddReq(openMaDomainResult.getRequestdomainList(), value);
            String ossUrl = "https://oss-tencent.bgniao.cn";
            //上传文件域名
            List<String> uploadDomainList = isAddReq(openMaDomainResult.getUploaddomainList(), value);
            uploadDomainList = isAddReq(uploadDomainList, ossUrl);
            //下载域名
            List<String> downloadDomainList = isAddReq(openMaDomainResult.getDownloaddomainList(), value);
            downloadDomainList = isAddReq(downloadDomainList, ossUrl);
            List<String> wsRequestDomainList = isAddReq(openMaDomainResult.getRequestdomainList(), value.replace("https", "wss"));
            WxOpenMaDomainResult set = wxMaServiceByAppid.modifyDomain("set", requestDomainList, wsRequestDomainList, uploadDomainList, downloadDomainList);
            logger.info("请求域名相关修改成功数据为: {}", set.toString());
            //小程序业务域名
            List<String> webviewdomainList = wxMaServiceByAppid.getWebViewDomainInfo().getWebviewdomainList();
            if (CollectionUtil.isNotEmpty(miniBusinessDomain)) {
                for (String miniBusinessDomain : miniBusinessDomain) {
                    webviewdomainList = isAddReq(webviewdomainList, miniBusinessDomain);
                }
            }
            WxOpenMaWebDomainResult webDomainResult = wxMaServiceByAppid.setWebViewDomainInfo("add", webviewdomainList);
            logger.info("小程序业务域名修改成功数据为: {}", webDomainResult.toString());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断是否添加新增请求域名
     *
     * @param domainList 已添加请求域名
     * @param domainStr  需要添加请求域名
     * @return
     */
    private List<String> isAddReq(List<String> domainList, String domainStr) {
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>(domainList.size());
        linkedHashSet.addAll(domainList);
        linkedHashSet.add(domainStr);
        return new ArrayList<String>(linkedHashSet);
    }


    @Override
    public List<MiniInfo> getUserMini(Long id) {
        return this.baseMapper.selectList(new QueryWrapper<MiniInfo>().eq("account_id", id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void authNotity(WxOpenXmlMessage inMessage) {
        try {
            switch (inMessage.getInfoType()) {
                case "authorized":
                    //授权阶段不新增数据,在回调接口进行新增,需要进行业务判断
                    break;
                case "updateauthorized":
                    authorized(inMessage);
                    break;
                case "unauthorized":
                    String authorizerAppid = inMessage.getAuthorizerAppid();
                    MiniInfo miniInfo = this.getByAppid(authorizerAppid);
                    miniInfo.setAuthorizerFlag(AuthorizerFlagEnum.UNAUTHORIZED.getStatus());
                    this.saveOrUpdate(miniInfo);
                    platformShopInfoService.setShopPcNewVersion(miniInfo.getTenantId());
                    break;
                default:
                    throw new ServiceException("错误请求");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 授权流程
     *
     * @param inMessage me.chanjar.weixin.open.bean.message.WxOpenXmlMessage
     * @return com.medusa.gruul.platform.api.entity.MiniInfo
     */
    private MiniInfo authorized(WxOpenXmlMessage inMessage) throws WxErrorException {
        WxOpenAuthorizationInfo authorizationInfo = wxOpenService.getWxOpenComponentService().getQueryAuth(inMessage.getAuthorizationCode()).getAuthorizationInfo();
        initAuthToken(authorizationInfo);
        MiniInfo miniInfo = initMiniInfo(authorizationInfo.getAuthorizerAppid());
        return miniInfo;
    }

    private MiniInfo getByAppid(String authorizerAppid) {
        return this.baseMapper.selectOne(new QueryWrapper<>(new MiniInfo()).eq("app_id", authorizerAppid));
    }

    /**
     * 更新或添加调用凭据和授权信息
     *
     * @param authorizationInfo 授权信息
     * @return
     */
    private void initAuthToken(WxOpenAuthorizationInfo authorizationInfo) {
        AuthToken authToken = authTokenService.getByAppid(authorizationInfo.getAuthorizerAppid());
        if (authToken == null) {
            authToken = new AuthToken();
        }
        authToken.setAuthorizerAccessToken(authorizationInfo.getAuthorizerAccessToken());
        authToken.setAuthorizerAppid(authorizationInfo.getAuthorizerAppid());
        authToken.setAuthorizerRefreshToken(authorizationInfo.getAuthorizerRefreshToken());
        DateTime expiresIn = DateUtil.offsetSecond(DateUtil.date(), authorizationInfo.getExpiresIn());
        authToken.setExpiresIn(DateUtils.timestampCoverLocalDateTime(expiresIn.toTimestamp().getTime()));
        authToken.setFuncInfo(authorizationInfo.getFuncInfo().toString());
        if (!authTokenService.saveOrUpdate(authToken)) {
            throw new ServiceException("数据保存失败");
        }
    }
}
