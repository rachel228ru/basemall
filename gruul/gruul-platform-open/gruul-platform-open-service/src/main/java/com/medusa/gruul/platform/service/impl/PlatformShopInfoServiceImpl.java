package com.medusa.gruul.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.constant.enums.TemplateCodeEnum;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.*;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.common.dto.CurShopInfoDto;
import com.medusa.gruul.common.dto.CurUserDto;
import com.medusa.gruul.platform.api.entity.*;
import com.medusa.gruul.platform.api.enums.QueueEnum;
import com.medusa.gruul.platform.api.model.dto.ShopConfigDto;
import com.medusa.gruul.platform.api.model.dto.ShopInfoDto;
import com.medusa.gruul.platform.api.model.dto.ShopPackageFunctionDto;
import com.medusa.gruul.platform.api.model.vo.PayInfoVo;
import com.medusa.gruul.platform.conf.MeConstant;
import com.medusa.gruul.platform.conf.PlatformRedis;
import com.medusa.gruul.platform.constant.AgentNoticeEnum;
import com.medusa.gruul.platform.constant.DefaultUniqueIdentificationEnum;
import com.medusa.gruul.platform.constant.RedisConstant;
import com.medusa.gruul.platform.mapper.PlatformShopInfoMapper;
import com.medusa.gruul.platform.model.dto.ConsoleUpdateDto;
import com.medusa.gruul.platform.model.dto.PayInfoUpdataDto;
import com.medusa.gruul.platform.model.dto.ShopInfoAdminCreateDto;
import com.medusa.gruul.platform.model.dto.ShopInfoConsoleCreateDto;
import com.medusa.gruul.platform.model.dto.agent.AgentCreateShopDto;
import com.medusa.gruul.platform.model.vo.*;
import com.medusa.gruul.platform.service.*;
import com.medusa.gruul.platform.stp.StpAgentUtil;
import com.medusa.gruul.shops.api.entity.ShopsPartner;
import com.medusa.gruul.shops.api.feign.RemoteShopsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.groovy.util.Maps;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * <p>
 * 店铺信息表 服务实现类
 * </p>
 *
 * @author whh
 * @since 2020-03-06
 */
@Service
@Slf4j
public class PlatformShopInfoServiceImpl extends ServiceImpl<PlatformShopInfoMapper, PlatformShopInfo> implements IPlatformShopInfoService {

    @Autowired
    private IAccountInfoService accountInfoService;
    @Autowired
    private IDefaultValueService defaultValueService;

    @Autowired
    private IPlatformShopTemplateInfoService platformShopTemplateInfoService;
    @Autowired
    private IPlatformShopDeployService platformShopDeployService;
    @Autowired
    private IPlatformShopTemplateDetailService platformShopTemplateDetailService;
    @Autowired
    private RemoteShopsService remoteShopsService;
    @Autowired
    private IMiniInfoService miniInfoService;
    @Autowired
    private PlatformPayConfigService platformPayConfigService;
    @Autowired
    private ISysShopPackageService sysShopPackageService;
    @Autowired
    private ISysShopPackageOrderService sysShopPackageOrderService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private IPlatformShopMessageService platformShopMessageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adminCreate(ShopInfoAdminCreateDto dto) {
        //校验商家是否存在
        AccountInfo accountInfo = accountInfoService.getById(dto.getAccountId());
        if (accountInfo == null) {
            throw new ServiceException("错误账号");
        }
        PlatformShopTemplateInfo platformShopTemplateInfo = platformShopTemplateInfoService.getById(dto.getShopTemplateId());
        if (platformShopTemplateInfo == null) {
            throw new ServiceException("店铺模板不存在");
        }
        SysShopPackage sysShopPackage = sysShopPackageService.getById(dto.getPackageId());
        if (sysShopPackage == null) {
            throw new ServiceException("模板数据不存在");
        }
        if (!sysShopPackage.getTemplateId().equals(dto.getShopTemplateId())) {
            throw new ServiceException("模板与套餐不一致");
        }
        PlatformShopInfo info = new PlatformShopInfo();
        synchronized (PlatformShopInfoServiceImpl.class) {
            List<PlatformShopInfo> shopInfos = this.getBaseMapper().selectList(new QueryWrapper<PlatformShopInfo>().eq("shop_name", dto.getShopName()));
            if (CollectionUtil.isNotEmpty(shopInfos)) {
                throw new ServiceException("店铺名称已存在");
            }
            info.setShopName(dto.getShopName());
            info.setStatus(CommonConstants.NUMBER_TWO);
            info.setShopTemplateId(dto.getShopTemplateId());
            info.setAccountId(dto.getAccountId());
            info.setAgreeOn(CommonConstants.NUMBER_ONE);
            info.setCreateJoin(CommonConstants.NUMBER_ZERO);
            info.setIsPrivatizationDeployment(dto.getIsPrivatizationDeployment());
            info.setTenantId(generateTenantId());
            info.setLogoUrl(dto.getLogoUrl());
            info.setPayType(CommonConstants.NUMBER_ONE);
            info.setPackageId(dto.getPackageId());
            info.setMiniBottomLog("https://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/gruul/20200407/logo.png");
            setShopTemplateDetailId(info);
            this.save(info);
            initShopCache(info);
        }
        PlatformShopDeploy shopDeploy = new PlatformShopDeploy();
        BeanUtils.copyProperties(dto, shopDeploy);
        shopDeploy.setShopId(info.getId());
        platformShopDeployService.save(shopDeploy);
        //生成当前店铺的套餐订单
        sysShopPackageOrderService.adminCreateShopBuy(CommonConstants.NUMBER_THREE, info, sysShopPackage, dto.getOrderPirce(), dto.getGivePackageTime());

        generateInitDefault(info);

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long consoleCreate(ShopInfoConsoleCreateDto dto) {
        CurUserDto httpCurUser = CurUserUtil.getHttpCurUser();
        if (httpCurUser == null) {
            throw new ServiceException("非法请求");
        }
        //校验商家是否存在
        AccountInfo accountInfo = accountInfoService.getById(httpCurUser.getUserId());
        if (accountInfo == null) {
            throw new ServiceException("错误账号");
        }
        PlatformShopTemplateInfo platformShopTemplateInfo = platformShopTemplateInfoService.getById(dto.getShopTemplateId());
        if (platformShopTemplateInfo == null) {
            throw new ServiceException("店铺数据不存在");
        }
        PlatformShopInfo info = new PlatformShopInfo();
        synchronized (PlatformShopInfoServiceImpl.class) {
            List<PlatformShopInfo> shopInfos = this.getBaseMapper().selectList(new QueryWrapper<PlatformShopInfo>().eq("shop_name", dto.getShopName()));
            if (CollectionUtil.isNotEmpty(shopInfos)) {
                throw new ServiceException("店铺名称已存在");
            }
            info.setLogoUrl(dto.getLogoUrl());
            info.setCreateJoin(CommonConstants.NUMBER_ONE);
            info.setShopName(dto.getShopName());
            info.setStatus(CommonConstants.NUMBER_TWO);
            info.setShopTemplateId(dto.getShopTemplateId());
            info.setAccountId(accountInfo.getId());
            info.setAgreeOn(CommonConstants.NUMBER_ONE);
            info.setIsPrivatizationDeployment(CommonConstants.NUMBER_ZERO);
            info.setTenantId(generateTenantId());
            info.setMiniBottomLog("https://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/gruul/20200407/logo.png");
            setShopTemplateDetailId(info);
            info.setPayType(CommonConstants.NUMBER_ONE);
            this.save(info);
            initShopCache(info);
        }
        PlatformShopDeploy shopDeploy = new PlatformShopDeploy();
        shopDeploy.setShopId(info.getId());
        platformShopDeployService.save(shopDeploy);
        generatePackage(platformShopTemplateInfo, info);
        generateInitDefault(info);
        return info.getId();
    }

    /**
     * 生成指定模板套餐
     *
     * @param platformShopTemplateInfo com.medusa.gruul.platform.api.entity.PlatformShopTemplateInfo
     * @param info                     com.medusa.gruul.platform.api.entity.PlatformShopInfo
     */
    private void generatePackage(PlatformShopTemplateInfo platformShopTemplateInfo, PlatformShopInfo info) {
        boolean uniqueIdentificationIsExit = defaultValueService.getByUniqueIdentificationIsExit(platformShopTemplateInfo.getCode());
        if (!uniqueIdentificationIsExit) {
            return;
        }
        List<SysShopPackage> sysShopPackages = sysShopPackageService.getByTemplateLastPackage(platformShopTemplateInfo.getId());
        if (CollectionUtil.isEmpty(sysShopPackages)) {
            log.debug("套餐名: {},id: {},  未找到最新的模板套餐,无法生成赠送套餐", platformShopTemplateInfo.getName(), platformShopTemplateInfo.getId());
            return;
        }
        //商户自己创建创建店铺赠送7天企业版套餐
        SysShopPackage sysShopPackage = sysShopPackages.get(1);
        //生成当前店铺的套餐订单
        sysShopPackageOrderService.adminCreateShopBuy(CommonConstants.NUMBER_TWO, info, sysShopPackage, BigDecimal.valueOf(0.0), 7);
    }


    /**
     * 生成店铺模板所需基本默认值
     *
     * @param info com.medusa.gruul.platform.api.entity.PlatformShopInfo
     */
    private void generateInitDefault(PlatformShopInfo info) {
        TenantContextHolder.setTenantId(info.getTenantId());
        PlatformShopTemplateInfo platformShopTemplateInfo = platformShopTemplateInfoService.getById(info.getShopTemplateId());
        TemplateCodeEnum templateCodeEnum = TemplateCodeEnum.valueOf(platformShopTemplateInfo.getCode());
        //社区拼团和商超生成相同的默认值
        if (templateCodeEnum.isMall() || templateCodeEnum.isSqlp()) {
            List<DefaultUniqueIdentificationEnum> defaultUniqueIdentificationEnums = Arrays.asList(
                    DefaultUniqueIdentificationEnum.PLATFROM_SHOP_MSG,
                    DefaultUniqueIdentificationEnum.MEMBER_CART,
                    DefaultUniqueIdentificationEnum.GOODS_DEFUALT,
                    DefaultUniqueIdentificationEnum.MINI_SHOP_MSG,
                    DefaultUniqueIdentificationEnum.SHOP_GUIDE_PAGE_DEFUALT,
                    DefaultUniqueIdentificationEnum.ACCOUNT_CENTRE_DEFAULT,
                    DefaultUniqueIdentificationEnum.SHOP_TEMPLATE_DEFAULT,
                    DefaultUniqueIdentificationEnum.ORDER_CENTRE_DEFAULT,
                    DefaultUniqueIdentificationEnum.FINANCE_CENTRE_DEFAULT,
                    DefaultUniqueIdentificationEnum.ACCOUNT_DEFAULT
            );
            //通知生成默认店铺
            AccountInfo accountInfo = accountInfoService.getById(info.getAccountId());
            Result<ShopsPartner> shopsPartnerResult = remoteShopsService.save(info.getTenantId(), accountInfo.getPassword(), accountInfo.getPhone(), info.getAccountId());
            if (shopsPartnerResult == null || shopsPartnerResult.getCode() != CommonConstants.SUCCESS) {
                throw new ServiceException("店铺数据生成异常");
            }
            //通知生成默认值
            defaultValueService.informCreateDefaultValue(info, shopsPartnerResult.getData().getShopId(), defaultUniqueIdentificationEnums);
            PlatformShopTemplateDetail shopTemplateDetail = platformShopTemplateDetailService.getById(info.getShopTemplateDetailId());
            //生成默认指定版本消息配置
            platformShopMessageService.generateInitMsg(shopTemplateDetail.getVersion());
            //生成支付配置
            platformPayConfigService.init(info.getTenantId());
        }


    }

    /**
     * 设置最新的平台模板版本
     *
     * @param info com.medusa.gruul.platform.api.entity.PlatformShopInfo
     */
    private void setShopTemplateDetailId(PlatformShopInfo info) {
        PlatformShopTemplateDetail shopTeamplteNewDetail = platformShopTemplateDetailService.getByShopTeamplteNewDetail(info.getShopTemplateId());
        if (shopTeamplteNewDetail == null) {
            throw new ServiceException("该模板未添加模板版本");
        }
        info.setShopTemplateDetailId(shopTeamplteNewDetail.getId());
        platformShopTemplateDetailService.updateById(shopTeamplteNewDetail);
    }

    private String generateTenantId() {
        PlatformShopInfo info = this.baseMapper.getLastTenantId();
        if (info == null) {
            return "100001";
        }
        Long tenantId = Long.valueOf(info.getTenantId());
        tenantId++;
        return tenantId.toString();
    }



    @Override
    public PageUtils<List<ShopViewListVo>> consoleList(Page page) {
        CurUserDto httpCurUser = CurUserUtil.getHttpCurUser();
        if (httpCurUser == null) {
            throw new ServiceException("非法请求");
        }
        //校验商家是否存在
        AccountInfo accountInfo = accountInfoService.getById(httpCurUser.getUserId());
        if (accountInfo == null) {
            throw new ServiceException("错误账号");
        }

        IPage<PlatformShopInfo> iPage = this.getBaseMapper().selectPage(page,
                new QueryWrapper<PlatformShopInfo>()
                        .eq("account_id", accountInfo.getId()));

        return getViewVo(iPage);
    }


    /**
     * 数据封装
     *
     * @param iPage 查询出的分页对象数据
     * @return 返回前端需要的展示数据
     */
    private PageUtils getViewVo(IPage<PlatformShopInfo> iPage) {
        List<PlatformShopInfo> records = iPage.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return new PageUtils(null, (int) iPage.getTotal(), (int) iPage.getSize(), (int) iPage.getCurrent());
        }
        List<ShopViewListVo> vos = new ArrayList<>(records.size());
        List<Long> accountIds = new ArrayList<>(records.size());
        List<Long> teamplateInfoIds = new ArrayList<>(records.size());
        List<Long> packageOrderId = new ArrayList<>();
        List<Long> shopTemplateDetailIds = new ArrayList<>();
        records.forEach(obj -> {
            accountIds.add(obj.getAccountId());
            teamplateInfoIds.add(obj.getShopTemplateId());
            packageOrderId.add(obj.getPackageOrderId());
            shopTemplateDetailIds.add(obj.getShopTemplateDetailId());
        });
        Map<Long, AccountInfo> accountInfoMap = accountInfoService.listByIds(accountIds).stream().collect(Collectors.toMap(AccountInfo::getId, v -> v, (k1, k2) -> k1));
        Map<Long, PlatformShopTemplateInfo> templateInfoMap = platformShopTemplateInfoService.listByIds(teamplateInfoIds).stream().collect(Collectors.toMap(PlatformShopTemplateInfo::getId, v -> v, (k1, k2) -> k1));
        Map<Long, SysShopPackageOrder> packageOrderMap = sysShopPackageOrderService.listByIds(packageOrderId).stream().collect(Collectors.toMap(SysShopPackageOrder::getId, v -> v));
        Map<Long, PlatformShopTemplateDetail> platformShopTemplateDetailMap = platformShopTemplateDetailService.listByIds(shopTemplateDetailIds).stream().collect(Collectors.toMap(PlatformShopTemplateDetail::getId, v -> v));

        for (PlatformShopInfo record : records) {
            ShopViewListVo vo = new ShopViewListVo();
            BeanUtils.copyProperties(record, vo);
            PlatformShopTemplateInfo shopTemplateInfo = templateInfoMap.get(record.getShopTemplateId());
            if (shopTemplateInfo != null) {
                vo.setTemplateName(shopTemplateInfo.getName());
            }
            AccountInfo accountInfo = accountInfoMap.get(record.getAccountId());
            vo.setUserPhone(accountInfo.getPhone());
            vo.setAccountId(accountInfo.getId());
            SysShopPackageOrder sysShopPackageOrder = packageOrderMap.get(record.getPackageOrderId());
            if (sysShopPackageOrder != null) {
                if (StrUtil.isNotEmpty(sysShopPackageOrder.getPackageData())) {
                    SysShopPackage sysShopPackage = JSONObject.parseObject(sysShopPackageOrder.getPackageData(), SysShopPackage.class);
                    vo.setLevel(sysShopPackage.getLevel());
                    vo.setPackageName(sysShopPackage.getName());
                }
                vo.setOrderSource(sysShopPackageOrder.getOrderSource());
            }
            PlatformShopTemplateDetail templateDetail = platformShopTemplateDetailMap.get(record.getShopTemplateDetailId());
            if (templateDetail != null) {
                vo.setVersionName(templateDetail.getVersion());
            }
            vos.add(vo);
        }
        return new PageUtils(vos, (int) iPage.getTotal(), (int) iPage.getSize(), (int) iPage.getCurrent());
    }

    @Override
    public void consoleDel(Long shopId) {
        PlatformShopInfo platformShopInfo = check(shopId);
        removeShopInfo(platformShopInfo);
    }

    /**
     * 移除所有跟店铺相关联数据
     *
     * @param platformShopInfo 店铺信息
     */
    private void removeShopInfo(PlatformShopInfo platformShopInfo) {
        this.removeById(platformShopInfo.getId());
        miniInfoService.removeByShopInfoAll(platformShopInfo.getTenantId());
    }

    @Override
    public void consoleUpdate(ConsoleUpdateDto dto, Long shopId) {
        check(shopId);
        PlatformShopInfo up = new PlatformShopInfo();
        BeanUtils.copyProperties(dto, up);
        up.setId(shopId);
        this.updateById(up);
        PlatformShopInfo info = this.getById(shopId);
        initShopCache(info);
    }

    /**
     * @param shopId 校验请求数据是否正常
     * @return
     */
    @Override
    public PlatformShopInfo check(Long shopId) {
        CurUserDto httpCurUser = CurUserUtil.getHttpCurUser();
        if (httpCurUser == null) {
            throw new ServiceException("无效token");
        }
        //校验商家是否存在
        AccountInfo accountInfo = accountInfoService.getById(httpCurUser.getUserId());
        if (accountInfo == null) {
            throw new ServiceException("错误账号");
        }
        PlatformShopInfo shopInfo = this.getById(shopId);
        if (shopInfo == null) {
            throw new ServiceException("不存在该店铺");
        }
        if (!shopInfo.getAccountId().equals(accountInfo.getId())) {
            throw new ServiceException("非法操作");
        }
        return shopInfo;
    }

    @Override
    public void closeOrOpen(Long shopId) {
        PlatformShopInfo platformShopInfo = check(shopId);
        if (platformShopInfo.getStatus() < CommonConstants.NUMBER_TWO && platformShopInfo.getStatus() > CommonConstants.NUMBER_THREE) {
            throw new ServiceException("店铺当前状态无法打烊或者营业");
        }
        if (platformShopInfo.getStatus().equals(CommonConstants.NUMBER_TWO)) {
            platformShopInfo.setStatus(CommonConstants.NUMBER_THREE);
        } else {
            platformShopInfo.setStatus(CommonConstants.NUMBER_TWO);
        }
        this.updateById(platformShopInfo);
        PlatformShopInfo info = this.getById(shopId);
        initShopCache(info);
    }

    @Override
    public LoginShopInfoVo joinShop(Long shopId) {
        CurUserDto httpCurUser = CurUserUtil.getHttpCurUser();
        if (httpCurUser == null) {
            throw new ServiceException("无效token");
        }
        //校验商家是否存在
        AccountInfo accountInfo = accountInfoService.getById(httpCurUser.getUserId());
        if (accountInfo == null) {
            throw new ServiceException("错误账号");
        }
        if (accountInfo.getAccountType().equals(CommonConstants.NUMBER_TWO)) {
            throw new ServiceException("店铺名下账号无法进行该操作");
        }
        PlatformShopInfo shopInfo = check(shopId);
        if (shopInfo.getStatus().equals(CommonConstants.NUMBER_FOUR)) {
            throw new ServiceException("店铺当前被禁用无法进入后台");
        }
        LoginShopInfoVo loginShopInfoVo = getLoginShopInfoVo(shopInfo);
        accountInfoService.getShopAccountRoleInfo(shopInfo.getAccountId(), loginShopInfoVo);
        AccountInfo info = new AccountInfo();
        info.setLastLoginShopId(shopId);
        info.setId(shopInfo.getAccountId());
        info.setLastLoginTime(LocalDateTime.now());
        accountInfoService.updateById(info);
        return loginShopInfoVo;
    }

    @Override
    public LoginShopInfoVo getLoginShopInfoVo(PlatformShopInfo shopInfo) {
        LoginShopInfoVo infoVo = new LoginShopInfoVo();
        infoVo.setMiniId(shopInfo.getBindMiniId());
        infoVo.setPlatformShopId(shopInfo.getId());
        infoVo.setMpId(shopInfo.getBindMpId());
        infoVo.setTenantId(shopInfo.getTenantId());
        infoVo.setLogoUrl(shopInfo.getLogoUrl());
        infoVo.setShopName(shopInfo.getShopName());
        //todo 暂时默认查询城市合伙人的商店id
        ShopsPartner shopsPartner = remoteShopsService.getByPlatformIdAndTenantId(shopInfo.getAccountId(), shopInfo.getTenantId());
        if (shopsPartner != null) {
            infoVo.setShopId(shopsPartner.getShopId());
        }
        //获取指定版本不过滤删除标识
        PlatformShopTemplateDetail templateDetail = platformShopTemplateDetailService.getByFilterById(shopInfo.getShopTemplateDetailId());
        //获取店铺使用的基础课的pc端后台地址
        infoVo.setBackUrl(templateDetail.getPcTerminaUrl());
        //用户当前地址是502或404的唬则临时使用最新的后台地址
        int responseCode = HttpUtil.createGet(infoVo.getBackUrl()).execute().getStatus();
        if (responseCode == MeConstant.STATUS_502 || responseCode == MeConstant.STATUS_404) {
            PlatformShopTemplateDetail newDetail = platformShopTemplateDetailService.getByShopTeamplteNewDetail(templateDetail.getShopTemplateId());
            infoVo.setBackUrl(newDetail.getPcTerminaUrl());
        }
        return infoVo;
    }

    @Override
    public PlatformShopInfo getByTenantId(String tenantId) {
        return this.baseMapper.selectOne(new QueryWrapper<PlatformShopInfo>().eq("tenant_id", tenantId));
    }



    @Override
    public AccountInfoVo adminJoinShop(Long shopId) {
        PlatformShopInfo platformShopInfo = this.getById(shopId);
        if (platformShopInfo == null) {
            throw new ServiceException("不存在该店铺");
        }
        LoginShopInfoVo loginShopInfoVo = getLoginShopInfoVo(platformShopInfo);
        accountInfoService.getShopAccountRoleInfo(platformShopInfo.getAccountId(), loginShopInfoVo);
        AccountInfo accountInfo = accountInfoService.getById(platformShopInfo.getAccountId());
        AccountInfoVo loginInfoVo = accountInfoService.getLoginInfoVo(accountInfo);
        loginInfoVo.setShopInfoVo(loginShopInfoVo);
        AccountInfo info = new AccountInfo();
        info.setLastLoginShopId(shopId);
        info.setId(platformShopInfo.getAccountId());
        accountInfoService.updateById(info);

        return loginInfoVo;
    }

    @Override
    public PlatformShopInfo checkCurrentTenantId(String tenantId) {
        PlatformShopInfo shopInfo = this.getByTenantId(tenantId);
        if (shopInfo == null) {
            throw new ServiceException("非法租户");
        }
        return shopInfo;
    }

    @Override
    public PayInfoVo payInfo(Integer type, String code, String tenantId) {
        PlatformShopInfo platformShopInfo = checkCurrentTenantId(tenantId);

        PlatformPayConfig platformPayConfig = platformPayConfigService.getByTenantId(platformShopInfo.getTenantId());
        PayInfoVo payInfoVo = new PayInfoVo();
        BeanUtil.copyProperties(platformPayConfig, payInfoVo);
        payInfoVo.setCertificatesPath(platformPayConfig.getCertificatePath());
        payInfoVo.setPayType(platformShopInfo.getPayType());
        payInfoVo.setCertificatesState(StrUtil.isNotEmpty(platformPayConfig.getCertificatePath()));
        // type 1-默认加密  2-明文数据需带上code  3-内部调用  todo s1.0.7之后是否需要废弃前两个类型待定 1,2
        if (CommonConstants.NUMBER_ONE.equals(type)) {
            payInfoVo.setCertificatesPath("");
            payInfoVo.setIpsMerCode(encryptStart(payInfoVo.getIpsMerCode()));
            payInfoVo.setIpsAccCode(encryptStart(payInfoVo.getIpsAccCode()));
            payInfoVo.setIpsCertificatePsw(encryptStart(payInfoVo.getIpsCertificatePsw()));
            payInfoVo.setIpsRsaPublicKey(encryptStart(payInfoVo.getIpsRsaPublicKey()));
            payInfoVo.setIpsRsaPrivateKey(encryptStart(payInfoVo.getIpsRsaPrivateKey()));
            payInfoVo.setIpsAes(encryptStart(payInfoVo.getIpsAes()));
            payInfoVo.setIpsSha(encryptStart(payInfoVo.getIpsSha()));
            payInfoVo.setSxfOrgId(encryptStart(payInfoVo.getSxfOrgId()));
            payInfoVo.setSxfAccCode(encryptStart(payInfoVo.getSxfAccCode()));
            payInfoVo.setSxfCertificatePsw(encryptStart(payInfoVo.getSxfCertificatePsw()));
            payInfoVo.setSxfPublic(encryptStart(payInfoVo.getSxfPublic()));
            payInfoVo.setSxfPrivateKey(encryptStart(payInfoVo.getSxfPrivateKey()));
            payInfoVo.setMchId(encryptStart(payInfoVo.getMchId()));
            payInfoVo.setMchKey(encryptStart(payInfoVo.getMchKey()));
            return payInfoVo;
        }
        if (type.equals(CommonConstants.NUMBER_TWO)) {
            payInfoVo.setCertificatesPath("");
            //校验state数据是否存在
            String jsonData = new PlatformRedis().get(code.concat(":inside"));
            if (StrUtil.isEmpty(jsonData)) {
                throw new ServiceException("校验事变");
            }
            Result result = JSONObject.parseObject(jsonData, Result.class);
            if (result.getCode() != CommonConstants.SUCCESS) {
                throw new ServiceException("扫码异常:" + result.getMsg());
            }
        }
        return payInfoVo;
    }

    /**
     * 加密中间字符串为***
     *
     * @param str 字符串
     * @return
     */
    private String encryptStart(String str) {
        if (StrUtil.isEmpty(str)) {
            return "";
        }
        String encryptStart = StrUtil.sub(str, 0, 3).concat("****").concat(StrUtil.sub(str, str.length() - 3, str.length()));
        return encryptStart;
    }

    @Override
    public String uploadCertificate(MultipartFile file, Long shopId) {
        if (file == null) {
            throw new ServiceException("文件为空", SystemCode.DATA_NOT_EXIST_CODE);
        }
        if (StrUtil.isEmpty(file.getOriginalFilename())) {
            throw new ServiceException("文件格式不正确,请上传微信支付API证书apiclient_cert.p12", SystemCode.PARAM_TYPE_ERROR.getCode());
        }
        int index = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".");
        String postfix = file.getOriginalFilename().substring(index);
        if (!MeConstant.P12.equals(postfix)) {
            throw new ServiceException("文件格式不正确,请上传微信支付API证书apiclient_cert.p12", SystemCode.PARAM_TYPE_ERROR.getCode());
        }
        PlatformShopInfo platformShopInfo = null;
        if (shopId != null && shopId > 0) {
            platformShopInfo = this.getById(shopId);
            if (platformShopInfo == null) {
                throw new ServiceException("无效数据请求");
            }
        } else {
            platformShopInfo = checkCurrentTenantId(TenantContextHolder.getTenantId());
        }
        String dir = "/data/pay-certificate/".concat(platformShopInfo.getTenantId()).concat("/apiclient_cert.p12");
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(dir));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("文件上传失败", SystemCode.FAILURE.getCode());
        }
        return dir;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payInfoUpdata(PayInfoUpdataDto payInfoUpdataDto) {
        PlatformShopInfo platformShopInfo = checkCurrentTenantId(payInfoUpdataDto.getTenantId());
        //修改当前支付渠道
        platformShopInfo.setPayType(payInfoUpdataDto.getPayType());
        this.updateById(platformShopInfo);
        //修改微信支付配置
        PlatformPayConfig platformPayConfig = platformPayConfigService.getByTenantId(payInfoUpdataDto.getTenantId());
        if (platformPayConfig == null) {
            throw new ServiceException("数据异常,请联系平台");
        }
        platformPayConfig.setMchId(payInfoUpdataDto.getMchId());
        platformPayConfig.setMchKey(payInfoUpdataDto.getMchKey());
        platformPayConfig.setCertificatePath(payInfoUpdataDto.getCertificatesPath());
        //修改其他类型配置
        switch (payInfoUpdataDto.getPayType()) {
            case 2:
                payInfoUpdataDto.ipsValidate();
                platformPayConfig.setIpsMerCode(payInfoUpdataDto.getIpsMerCode());
                platformPayConfig.setIpsAccCode(payInfoUpdataDto.getIpsAccCode());
                platformPayConfig.setIpsCertificatePsw(payInfoUpdataDto.getIpsCertificatePsw());
                platformPayConfig.setIpsRsaPublicKey(payInfoUpdataDto.getIpsRsaPublicKey());
                platformPayConfig.setIpsRsaPrivateKey(payInfoUpdataDto.getIpsRsaPrivateKey());
                platformPayConfig.setIpsAes(payInfoUpdataDto.getIpsAes());
                platformPayConfig.setIpsSha(payInfoUpdataDto.getIpsSha());
                platformPayConfigService.updateById(platformPayConfig);
                break;
            case 3:
                payInfoUpdataDto.sxfValidate();
                platformPayConfig.setSxfOrgId(payInfoUpdataDto.getSxfOrgId());
                platformPayConfig.setSxfAccCode(payInfoUpdataDto.getSxfAccCode());
                platformPayConfig.setSxfCertificatePsw(payInfoUpdataDto.getSxfCertificatePsw());
                platformPayConfig.setSxfPublic(payInfoUpdataDto.getSxfPublic());
                platformPayConfig.setSxfPrivateKey(payInfoUpdataDto.getSxfPrivateKey());
                platformPayConfigService.updateById(platformPayConfig);
                break;
            case 4:
                payInfoUpdataDto.sftValidate();
                platformPayConfig.setSftChannelId(payInfoUpdataDto.getSftChannelId());
                platformPayConfig.setSftMd5(payInfoUpdataDto.getSftMd5());
                platformPayConfig.setSftSubMerchantNo(payInfoUpdataDto.getSftSubMerchantNo());
                platformPayConfig.setSftTerminalId(payInfoUpdataDto.getSftTerminalId());
                platformPayConfigService.updateById(platformPayConfig);
                break;
            default:
                platformPayConfigService.updateById(platformPayConfig);
                break;
        }
    }

    @Override
    public List<PlatformShopInfo> getByAccountIdShops(Long accountId) {
        return this.getBaseMapper().selectList(new QueryWrapper<PlatformShopInfo>().eq("account_id", accountId));
    }

    @Override
    public ShopInfoVo shopInfo() {
        PlatformShopInfo platformShopInfo = checkCurrentTenantId(TenantContextHolder.getTenantId());
        ShopInfoVo shopInfoVo = BeanUtil.toBean(platformShopInfo, ShopInfoVo.class);
        shopInfoVo.setPlatformShopId(platformShopInfo.getId());
        String businessHours = platformShopInfo.getBusinessHours();
        if (StrUtil.isNotEmpty(businessHours) && shopInfoVo.getStatus().equals(CommonConstants.NUMBER_TWO)) {
            JSONArray timeArr = JSON.parseArray(businessHours);
            DateTime start = DateUtil.parseTimeToday(DateUtil.formatTime(DateUtil.parse(timeArr.getString(0))));
            DateTime end = DateUtil.parseTimeToday(DateUtil.formatTime(DateUtil.parse(timeArr.getString(1))));
            if (!DateUtil.isIn(new Date(), start, end)) {
                shopInfoVo.setStatus(CommonConstants.NUMBER_FIVE);
            }
        }
        PlatformShopTemplateInfo templateInfo = platformShopTemplateInfoService.getById(platformShopInfo.getShopTemplateId());
        if (templateInfo != null) {
            shopInfoVo.setTemplateName(templateInfo.getName());
            shopInfoVo.setTemplateCode(TemplateCodeEnum.valueOf(templateInfo.getCode()));
            String teamplateVersion = "";
            if (platformShopInfo.getShopTemplateDetailId() != null || platformShopInfo.getShopTemplateDetailId() > 0) {
                PlatformShopTemplateDetail templateDetail = platformShopTemplateDetailService.getById(platformShopInfo.getShopTemplateDetailId());
                if (templateDetail != null) {
                    teamplateVersion = templateDetail.getVersion();
                }
            }
            if (StrUtil.isEmpty(teamplateVersion)) {
                PlatformShopTemplateDetail platformShopTemplateDetail = platformShopTemplateDetailService.getByShopTeamplteNewDetail(templateInfo.getId());
                teamplateVersion = platformShopTemplateDetail.getVersion();
            }
            shopInfoVo.setTeamplateVersion(teamplateVersion);
        }
        if (platformShopInfo.getPackageId() != null) {
            SysShopPackageOrder sysShopPackageOrder = sysShopPackageOrderService.getById(platformShopInfo.getPackageOrderId());
            if (sysShopPackageOrder != null) {
                SysShopPackage sysShopPackage = JSON.parseObject(sysShopPackageOrder.getPackageData(), SysShopPackage.class);
                shopInfoVo.setLevel(sysShopPackage.getLevel());
                shopInfoVo.setPackageName(sysShopPackage.getName());
                shopInfoVo.setOrderSource(sysShopPackageOrder.getOrderSource());
            }
        }
        return shopInfoVo;
    }

    @Override
    public List<PlatformShopInfo> getByPlatformShopTemplateDetailIdAndStatus(Long id, Integer status) {
        return this.getBaseMapper().selectList(new QueryWrapper<PlatformShopInfo>().eq("shop_template_detail_id", id).eq("status", status));
    }

    @Override
    public List<PlatformShopInfo> getByPlatformShopTemplateDetailId(Long shopTemplateDetailId) {
        return this.getBaseMapper().selectList(new QueryWrapper<PlatformShopInfo>().eq("shop_template_detail_id", shopTemplateDetailId));

    }

    @Override
    public ShopConfigDto getShopConfig(String tenantId) {
        ShopConfigDto shopConfigDto = new ShopConfigDto();
        shopConfigDto.setPayInfo(payInfo(CommonConstants.NUMBER_THREE, "", tenantId));
        MiniInfo miniInfo = miniInfoService.getByMiniTenantId(tenantId);
        shopConfigDto.setMiniInfo(miniInfo);
        MiniInfo mpInfo = miniInfoService.getByMpTenantId(tenantId);
        shopConfigDto.setMpInfo(mpInfo);
        return shopConfigDto;
    }

    @Override
    public void setShopPcNewVersion(String tenantId) {
        try {
            PlatformShopInfo shopInfo = this.getByTenantId(tenantId);
            PlatformShopTemplateDetail shopTeamplteNewDetail = platformShopTemplateDetailService.getByShopTeamplteNewDetail(shopInfo.getShopTemplateId());
            if (shopTeamplteNewDetail == null) {
                log.warn("没有最新的模板版本");
                log.warn("tenantId:".concat(tenantId));
                log.warn("shopInfo".concat(shopInfo.toString()));
                return;
            }
            //如果不是最新版本则更新为最新的
            if (!shopInfo.getShopTemplateDetailId().equals(shopTeamplteNewDetail.getId())) {
                PlatformShopInfo platformShopInfo = new PlatformShopInfo();
                platformShopInfo.setShopTemplateDetailId(shopTeamplteNewDetail.getId());
                this.getBaseMapper().update(platformShopInfo, new QueryWrapper<PlatformShopInfo>().eq("id", shopInfo.getId()));
            }
        } catch (Exception e) {
            log.error("更新最新模板版本失败!!");
            e.printStackTrace();
        }

    }

    @Override
    public List<PlatformShopInfo> getByNotAuthMiniShops(Long shopTemplateId) {
        return this.getBaseMapper().selectList(new QueryWrapper<PlatformShopInfo>().eq("shop_template_id", shopTemplateId)
                .and(obj -> obj.isNull("bind_mini_id").or().eq("bind_mini_id", 0)));
    }

    @Override
    public List<PlatformShopInfo> getByCancelAuthMiniShops(Long shopTemplateId) {
        return this.getBaseMapper().selectByCancelAuthMiniShops(shopTemplateId);
    }





    @Override
    public void defaultRepair(String tenantId) {
        PlatformShopInfo platformShopInfo = getByTenantId(tenantId);
        if (platformShopInfo == null) {
            throw new ServiceException("无效数据请求");
        }
        generateInitDefault(platformShopInfo);
    }

    @Override
    public Result<ShopInfoDto> getShopInfo(String tenantId) {
        PlatformShopInfo platformShopInfo = getByTenantId(tenantId);
        if (platformShopInfo == null) {
            return Result.failed("无效店铺数据");
        }
        ShopInfoDto infoDto = new ShopInfoDto();
        BeanUtil.copyProperties(platformShopInfo, infoDto);
        return Result.ok(infoDto);
    }


    @Override
    public List<SysShopPackageVo> getCurrentPackage() {
        PlatformShopInfo platformShopInfo = checkCurrentTenantId(TenantContextHolder.getTenantId());
        List<SysShopPackage> sysShopPackage = sysShopPackageService.getByTemplateVersionId(platformShopInfo.getShopTemplateDetailId());
        if (CollectionUtil.isEmpty(sysShopPackage)) {
            return null;
        }
        return sysShopPackage.stream().map(obj -> BeanUtil.toBean(obj, SysShopPackageVo.class)).collect(Collectors.toList());
    }

    @Override
    public void validateShopPastDue() {
        DateTime currentTime = DateUtil.date();
        DateTime startTime = DateUtil.beginOfDay(currentTime);
        DateTime endTime = DateUtil.endOfDay(currentTime);
        //搜索当日到期店铺
        List<PlatformShopInfo> platformShopInfos = this.getBaseMapper().selectList(new QueryWrapper<PlatformShopInfo>()
                .and(i -> i.isNull("due_time")
                        .or(j -> j.lt("due_time", endTime.toString()))
                ).eq("is_due", 0));

        if (CollectionUtil.isEmpty(platformShopInfos)) {
            return;
        }
        //套餐到期处理
        LocalDateTime now = LocalDateTime.now();
        for (PlatformShopInfo platformShopInfo : platformShopInfos) {
            LocalDateTime dueTime = platformShopInfo.getDueTime();
            Duration between = Duration.ofSeconds(0);
            if (dueTime != null) {
                between = LocalDateTimeUtil.between(now, dueTime);
            }
            QueueEnum platformPackageDue = QueueEnum.PLATFORM_PACKAGE_DUE;
            Duration finalBetween = between;
            rabbitTemplate.convertAndSend(platformPackageDue.getExchange(), platformPackageDue.getRouteKey(), platformShopInfo.getId(),
                    message -> {
                        message.getMessageProperties().setDelay(((Long) finalBetween.getSeconds()).intValue());
                        return message;
                    }, new CorrelationData(IdUtil.fastSimpleUUID()));
        }

        DateTime dateTime = DateUtil.offsetDay(new Date(), 7);
        List<PlatformShopInfo> shopInfos = this.getBaseMapper().selectList(new QueryWrapper<PlatformShopInfo>()
                .le("due_time", dateTime.toSqlDate())
                .eq("is_due", 0));
        if (CollectionUtil.isEmpty(shopInfos)) {
            return;
        }
        agentNotify(shopInfos);

    }

    /**
     * 店铺到期通知
     *
     * @param shopInfos com.medusa.gruul.platform.api.entity.PlatformShopInfo
     */
    private void agentNotify(List<PlatformShopInfo> shopInfos) {
        for (PlatformShopInfo shopInfo : shopInfos) {
            Date currentDate = new Date();
            Long day = DateUtil.betweenDay(currentDate, LocalDateTimeUtils.convertLDTToDate(shopInfo.getDueTime()), false);
            //计算到期通知
            if (day <= 7 && day > 0) {
                PlatformRedis platformRedis = new PlatformRedis();
                String rv = platformRedis
                        .get(RedisConstant.SHOP_DUETIME_NOTIFY
                                .concat(shopInfo.getTenantId()));
                if (StrUtil.isNotEmpty(rv)) {
                    break;
                }
                //计算即将到期通知
                AccountInfo accountInfo = accountInfoService.getById(shopInfo.getAccountId());
                if (accountInfo.getAgentId() == null || accountInfo.getAgentId() == 0) {
                    break;
                }

                LinkedList<String> titles = new LinkedList<>();
                MiniInfo miniInfo = miniInfoService.getById(shopInfo.getTenantId());
                String miniName = "-";
                if (miniInfo != null) {
                    miniName = miniInfo.getMiniName();
                }
                titles.add(miniName);
                titles.add(shopInfo.getShopName());
                titles.add(day.toString());
                PlatformShopTemplateInfo shopTemplateInfo = platformShopTemplateInfoService.getById(shopInfo.getShopTemplateId());
                SysShopPackage shopPackage = sysShopPackageService.getById(shopInfo.getPackageId());
                String key3 = "";
                if (shopPackage != null) {
                    key3 = shopPackage.getName();
                }

                //店铺套餐即将到期提醒
                Map<String, String> content = Maps.of(
                        "key1", miniName,
                        "key2", shopTemplateInfo.getName(),
                        "key3", key3,
                        "key4", LocalDateTimeUtils.formatTime(shopInfo.getDueTime(), "yyyy-MM-dd HH:mm:ss"));
                //计算距离当天结束时间,设置当天缓存
                DateTime endOfDay = DateUtil.endOfDay(currentDate);
                long betweenTime = DateUtil.between(currentDate, endOfDay, DateUnit.MS);
                platformRedis.setNxPx(rv, "1", betweenTime);
            }
        }
    }

    @Override
    public void packageDueReceive(Integer platefromInfoId) {
        PlatformShopInfo shopInfo = getById(platefromInfoId);
        if (shopInfo == null) {
            throw new ServiceException("套餐过期失败,不存在该店铺 : 店铺id {}", platefromInfoId);
        }
        if (null != shopInfo.getDueTime() && LocalDateTimeUtils.getMilliByTime(shopInfo.getDueTime()) > System.currentTimeMillis()) {
            log.warn("店铺未到期,不进行过期操作,shopId: {}", platefromInfoId);
            return;
        }

        PlatformShopInfo up = new PlatformShopInfo();
        up.setIsDue(CommonConstants.NUMBER_ONE);
        up.setId(shopInfo.getId());
        up.setStatus(CommonConstants.NUMBER_THREE);
        this.updateById(up);
        LinkedList<String> titles = new LinkedList<>();
        MiniInfo miniInfo = miniInfoService.getById(shopInfo.getTenantId());
        String miniName = "-";
        if (miniInfo != null) {
            miniName = miniInfo.getMiniName();
        }
        titles.add(miniName);
        titles.add(shopInfo.getShopName());
        PlatformShopTemplateInfo shopTemplateInfo = platformShopTemplateInfoService.getById(shopInfo.getShopTemplateId());
        SysShopPackage shopPackage = sysShopPackageService.getById(shopInfo.getPackageId());
        String key3 = "";
        if (shopPackage != null) {
            key3 = shopPackage.getName();
        }
        AccountInfo accountInfo = accountInfoService.getById(shopInfo.getAccountId());

        //套餐续费,订购,升级通知
        Map<String, String> content = Maps.of(
                "key1", miniName,
                "key2", shopTemplateInfo.getName(),
                "key3", key3,
                "key4", LocalDateTimeUtils.formatTime(shopInfo.getDueTime(), "yyyy-MM-dd HH:mm:ss"));
        SysShopPackageOrder shopPackageOrder = sysShopPackageOrderService.getById(shopInfo.getPackageOrderId());
        AgentNoticeEnum noticeEnum = null;
        AgentNoticeEnum noticeEnum2 = null;

        if (shopPackageOrder.getOrderSource().equals(CommonConstants.NUMBER_TWO)) {
            noticeEnum = AgentNoticeEnum.MSG_0011;
            noticeEnum2 = AgentNoticeEnum.MSG_0012;
        } else {
            noticeEnum = AgentNoticeEnum.MSG_005;
            noticeEnum2 = AgentNoticeEnum.MSG_0010;
        }

    }

    @Override
    public Result<ShopPackageFunctionDto> getShopFunction(String tenantId) {
        PlatformShopInfo shopInfo = getByTenantId(tenantId);
        if (shopInfo == null) {
            return Result.failed("店铺不存在");
        }
        //获取历史套餐是否有大于门店版
        Integer i = sysShopPackageOrderService.selectBoughtEnterpriseVersion(tenantId);
        ShopPackageFunctionDto dto = new ShopPackageFunctionDto();
        dto.setBoughtEnterpriseVersion(i > 0 ? Boolean.TRUE : Boolean.FALSE);
        dto.setPoint(Boolean.FALSE);
        dto.setLive(Boolean.FALSE);
        dto.setCopyright(Boolean.FALSE);
        dto.setHeadOffice(Boolean.FALSE);
        if (shopInfo.getIsDue().equals(CommonConstants.NUMBER_ONE)) {
            return Result.ok(dto);
        }
        SysShopPackageOrder sysShopPackageOrder = sysShopPackageOrderService.getById(shopInfo.getPackageOrderId());
        if (sysShopPackageOrder == null) {
            return Result.ok(dto);
        }
        SysShopPackage sysShopPackage = JSONObject.parseObject(sysShopPackageOrder.getPackageData(), SysShopPackage.class);
        dto.setCommunityPackagelevel(sysShopPackage.getLevel());
        if (sysShopPackage.getLevel() >= CommonConstants.NUMBER_TWO) {
            dto.setPoint(Boolean.TRUE);
            dto.setLive(Boolean.TRUE);
            dto.setCopyright(Boolean.TRUE);
        }
        if (sysShopPackage.getLevel() >= CommonConstants.NUMBER_THREE) {
            dto.setHeadOffice(Boolean.TRUE);
        }
        return Result.ok(dto);
    }

    @Override
    public ShopConfigDto getShopConfigAndAppId(String appId) {
        MiniInfo info = miniInfoService.getByAppId(appId);
        if (info == null) {
            log.warn("不存在该小程序信息:{}", appId);
            return null;
        }
        ShopConfigDto shopConfigDto = new ShopConfigDto();
        shopConfigDto.setPayInfo(payInfo(CommonConstants.NUMBER_THREE, "", info.getTenantId()));
        shopConfigDto.setMiniInfo(info);
        return shopConfigDto;
    }

    @Override
    public List<String> getShopsAll() {
        List<PlatformShopInfo> shopInfos = this.baseMapper.selectList(new QueryWrapper<PlatformShopInfo>().select("tenant_id"));
        return shopInfos.stream().map(PlatformShopInfo::getTenantId).collect(Collectors.toList());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void agentCreateShop(AgentCreateShopDto dto) {
        //校验商家是否存在
        AccountInfo accountInfo = accountInfoService.getById(dto.getAccountId());
        if (accountInfo == null) {
            throw new ServiceException("错误账号");
        }
        if (!accountInfo.getAgentId().equals(StpAgentUtil.getLoginIdAsLong())) {
            throw new ServiceException("非法操作");
        }
        PlatformShopTemplateInfo platformShopTemplateInfo = platformShopTemplateInfoService.getById(dto.getShopTemplateId());
        if (platformShopTemplateInfo == null) {
            throw new ServiceException("店铺模板不存在");
        }
        PlatformShopInfo info = new PlatformShopInfo();
        synchronized (PlatformShopInfoServiceImpl.class) {
            List<PlatformShopInfo> shopInfos = this.getBaseMapper()
                    .selectList(new QueryWrapper<PlatformShopInfo>().eq("shop_name", dto.getShopName()));
            if (CollectionUtil.isNotEmpty(shopInfos)) {
                throw new ServiceException("店铺名称已存在");
            }
            info.setLogoUrl(dto.getLogoUrl());
            info.setShopName(dto.getShopName());
            info.setStatus(CommonConstants.NUMBER_TWO);
            info.setShopTemplateId(dto.getShopTemplateId());
            info.setAccountId(accountInfo.getId());
            info.setAgreeOn(CommonConstants.NUMBER_ONE);
            info.setIsPrivatizationDeployment(CommonConstants.NUMBER_ZERO);
            info.setTenantId(generateTenantId());
            info.setMiniBottomLog("https://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/gruul/20200407/logo.png");
            setShopTemplateDetailId(info);
            info.setPayType(CommonConstants.NUMBER_ONE);
            this.save(info);
        }
        PlatformShopDeploy shopDeploy = new PlatformShopDeploy();
        shopDeploy.setShopId(info.getId());
        platformShopDeployService.save(shopDeploy);
        generatePackage(platformShopTemplateInfo, info);
        generateInitDefault(info);
    }

    @Override
    public Integer getByAccountIdShopCount(Long accountId) {
        return this.getBaseMapper().selectCount(new QueryWrapper<PlatformShopInfo>().eq("account_id", accountId));
    }

    @Override
    public void initShopCache() {
        List<PlatformShopInfo> shopInfos = this.getBaseMapper().selectList(new QueryWrapper<PlatformShopInfo>());
        CompletableFuture.runAsync(() -> shopInfos.forEach(shopInfo -> {
            initShopCache(shopInfo);
        })).exceptionally(e -> {
            e.printStackTrace();
            return null;
        });

    }

    /**
     * 缓存店铺基本信息
     *
     * @param shopInfo com.medusa.gruul.platform.api.entity.PlatformShopInfo
     */
    private void initShopCache(PlatformShopInfo shopInfo) {
        CurShopInfoDto curShopInfoDto = new CurShopInfoDto();
        curShopInfoDto.setPlatfromUserId(shopInfo.getAccountId());
        curShopInfoDto.setShopName(shopInfo.getShopName());
        curShopInfoDto.setBindMiniId(shopInfo.getBindMiniId());
        curShopInfoDto.setBindMpId(shopInfo.getBindMpId());
        if (shopInfo.getPackageId() != null) {
            SysShopPackageOrder sysShopPackageOrder = sysShopPackageOrderService.getById(shopInfo.getPackageOrderId());
            if (sysShopPackageOrder != null) {
                SysShopPackage sysShopPackage = JSON.parseObject(sysShopPackageOrder.getPackageData(), SysShopPackage.class);
                curShopInfoDto.setLevel(sysShopPackage.getLevel());
                curShopInfoDto.setPackageName(sysShopPackage.getName());
            }
        }
        PlatformShopTemplateInfo platformShopTemplateInfo = platformShopTemplateInfoService.getById(shopInfo.getShopTemplateId());
        try {
            TemplateCodeEnum templateCodeEnum = TemplateCodeEnum.valueOf(platformShopTemplateInfo.getCode());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        Long shopTemplateDetailId = shopInfo.getShopTemplateDetailId();
        if (shopTemplateDetailId != null && shopTemplateDetailId > 0) {
            PlatformShopTemplateDetail shopTemplateDetail = platformShopTemplateDetailService.getById(shopInfo.getShopTemplateDetailId());
            curShopInfoDto.setTemplateVersion(shopTemplateDetail.getVersion());
        }
        PlatformRedis allRedis = new PlatformRedis(CommonConstants.SHOP_INFO_REDIS_KEY);
        allRedis.set(shopInfo.getTenantId(), JSON.toJSONString(curShopInfoDto));
    }
}
