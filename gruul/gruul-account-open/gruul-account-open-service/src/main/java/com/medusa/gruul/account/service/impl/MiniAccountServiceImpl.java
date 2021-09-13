package com.medusa.gruul.account.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.util.crypt.WxMaCryptUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.account.api.entity.*;
import com.medusa.gruul.account.api.enums.BlacklistEnum;
import com.medusa.gruul.account.api.enums.MpLoginCodeEnum;
import com.medusa.gruul.account.api.enums.OauthTypeEnum;
import com.medusa.gruul.account.api.model.*;
import com.medusa.gruul.account.conf.AccountRedis;
import com.medusa.gruul.account.conf.SnowflakeProperty;
import com.medusa.gruul.account.constant.RedisConstant;
import com.medusa.gruul.account.mapper.MiniAccountMapper;
import com.medusa.gruul.account.model.dto.*;
import com.medusa.gruul.account.model.vo.*;
import com.medusa.gruul.account.service.*;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.constant.TimeConstants;
import com.medusa.gruul.common.core.constant.enums.LoginTerminalEnum;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.*;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.common.dto.CurMiniUserInfoDto;
import com.medusa.gruul.common.dto.CurUserDto;
import com.medusa.gruul.goods.api.feign.RemoteGoodsService;
import com.medusa.gruul.platform.api.feign.RemoteMiniInfoService;
import com.medusa.gruul.platform.api.model.dto.LoginDto;
import com.medusa.gruul.shops.api.entity.ShopsPartner;
import com.medusa.gruul.shops.api.feign.RemoteShopsService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <p>
 * 小程序用户表 服务实现类
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
@Service
@Slf4j
public class MiniAccountServiceImpl extends ServiceImpl<MiniAccountMapper, MiniAccount> implements IMiniAccountService {

    @Autowired
    private RemoteMiniInfoService remoteMiniInfoService;
    @Autowired
    private RemoteShopsService remoteShopsService;
    @Autowired
    private SnowflakeProperty snowflakeProperty;
    @Autowired
    private IMiniAccountOauthsService miniAccountOauthsService;
    @Autowired
    private IMiniAccountExtendsService miniAccountExtendsService;
    @Autowired
    private IMiniAccountTagService miniAccountTagService;
    @Autowired
    private IMiniAccountTagGroupService miniAccountTagGroupService;
    @Autowired
    private IMiniAccountAddressService iMiniAccountAddressService;
    @Autowired
    private IMiniAccountRestrictService miniAccountRestrictService;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private RemoteGoodsService remoteGoodsService;

    /**
     * 封装小程序信息
     *
     * @return
     */
    public WxMaService getWxMaService() {
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid("小程序Appid");
        config.setSecret("小程序Secret");
        config.setMsgDataFormat("JSON");
        WxMaService wxMaService = new WxMaServiceImpl();
        wxMaService.setWxMaConfig(config);
        return wxMaService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginBaseInfoVo login(String code) {
        String tenantId = TenantContextHolder.getTenantId();
        if (StrUtil.isBlank(tenantId)) {
            throw new ServiceException("登陆失败,请求错误", SystemCode.DATA_EXISTED.getCode());
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        //Todo 微信开放平台获取openid 等 调用
        LoginDto login = remoteMiniInfoService.login(tenantId, code);

        //Todo 如没有微信开放平台可使用微信小程序
//        final WxMaService wxService = getWxMaService();
//        if (StrUtil.isBlank(code)) {
//            throw new ServiceException("当前code不存在");
//        }
//        WxMaJscode2SessionResult session = null;
//        try {
//            session = wxService.getUserService().getSessionInfo(code);
//            if (ObjectUtil.isEmpty(session)) {
//                throw new ServiceException("login handler error");
//            }
//        } catch (WxErrorException e) {
//            e.printStackTrace();
//        }
//        LoginDto login = new LoginDto();
//        login.setOpenId(session.getOpenid());
//        login.setUnionId(session.getUnionid());
//        login.setSessionKey(session.getSessionKey());

        if (login == null || login.getOpenId() == null) {
            throw new ServiceException("登陆失败,调用错误", SystemCode.DATA_EXISTED.getCode());
        }
        String loginKey = RedisConstant.LOGIN_KEY.concat(tenantId).concat(":")
                .concat(OauthTypeEnum.WX_MINI.getType().toString()).concat(":").concat(login.getOpenId());
        AccountRedis accountRedis = new AccountRedis();
        //获取缓存中用户数据
        String loginBase = accountRedis.get(loginKey);
        MiniAccountOauthsDto miniAccountOauthsDto = null;
        if (StrUtil.isNotEmpty(loginBase)) {
            miniAccountOauthsDto = JSON.parseObject(loginBase, MiniAccountOauthsDto.class);
        } else {
            MiniAccountOauths miniAccountOauths = null;
            if (StrUtil.isNotBlank(login.getUnionId())) {
                miniAccountOauths = miniAccountOauthsService.getByUnionIdAndType(login.getUnionId(), OauthTypeEnum.WX_MINI);
            }
            //小程序和微信授权数据都不存在，则根据openId查询小程序用户数据
            if (miniAccountOauths == null) {
                miniAccountOauths = miniAccountOauthsService.getByOpenId(login.getOpenId(), OauthTypeEnum.WX_MINI.getType());
                //UNiconId不相同或不一致则更新
                if (StrUtil.isNotBlank(login.getUnionId()) && miniAccountOauths != null && !login.getUnionId().equals(miniAccountOauths.getUnionId())) {
                    miniAccountOauths.setUnionId(login.getUnionId());
                    miniAccountOauthsService.updateById(miniAccountOauths);

                }
            }
            MiniAccount account = null;
            //存在授权信息（可能是小程序授权信息也可能是公众号授权信息）
            if (miniAccountOauths != null) {
                account = this.getByUserId(miniAccountOauths.getUserId());
                //如果授权信息是公众号的则表示第一次进入小程序，生成小程序相关授权信息,并提供数据为小程序最新数据
                if (miniAccountOauths.getOauthType().equals(OauthTypeEnum.WX_MP.getType())) {
                    miniAccountOauths = new MiniAccountOauths();
                    miniAccountOauths.setOauthType(OauthTypeEnum.WX_MINI.getType());
                    miniAccountOauths.setUserId(account.getUserId());
                    miniAccountOauths.setOpenId(login.getOpenId());
                    miniAccountOauths.setUnionId(login.getUnionId());
                    miniAccountOauthsService.save(miniAccountOauths);

                }
                //缓存用户数据
                MiniAccountExtends accountExtends = miniAccountExtendsService.findByCurrentStatus(miniAccountOauths.getUserId());
                miniAccountOauthsDto = accuontOauthsCache(loginKey, miniAccountOauths, account, accountExtends);
            }
        }
        //新用户,创建新数据
        if (miniAccountOauthsDto == null) {
            miniAccountOauthsDto = initMiniInfo(tenantId, currentDateTime, login, loginKey);
        }
        String userId = miniAccountOauthsDto.getUserId();
        CompletableFuture.runAsync(() -> {
            //异步更新最后登陆时间
            MiniAccountExtends accountExtends = miniAccountExtendsService.findByCurrentStatus(userId);
            miniAccountExtendsService.update(new UpdateWrapper<MiniAccountExtends>()
                    .set("last_login_time", currentDateTime).eq("shop_user_id", accountExtends.getShopUserId()));
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
        //封装返回对象
        LoginBaseInfoVo vo = new LoginBaseInfoVo();
        vo.setSessionKey(login.getSessionKey());
        vo.setToken(RedisConstant.ACCOUNT_KEY.concat(miniAccountOauthsDto.getToken()));
        vo.setWhetherAuthorization(getUserWhetherAuthorization(miniAccountOauthsDto.getUserId()));
        return vo;
    }

    /**
     * 初始化新用户数据
     *
     * @param tenantId        租户id
     * @param currentDateTime 当前时间
     * @param login           com.medusa.gruul.platform.api.model.dto.LoginDto
     * @param loginKey        ket
     * @return com.medusa.gruul.account.model.dto.MiniAccountOauthsDto
     */
    private MiniAccountOauthsDto initMiniInfo(String tenantId, LocalDateTime currentDateTime, LoginDto login, String loginKey) {
        log.info("进入initMiniInfo".concat(login.toString()));
        System.out.println("进入initMiniInfo".concat(login.toString()));
        MiniAccountOauthsDto miniAccountOauthsDto;
        Snowflake snowflake = IdUtil.createSnowflake(snowflakeProperty.getWorkerId(), snowflakeProperty.getDatacenterId());
        String userId = snowflake.nextId() + "";
        MiniAccount account = new MiniAccount();
        account.setUserId(userId);
        account.setFirstLoginTime(currentDateTime);
        account.setWhetherAuthorization(false);
        this.getBaseMapper().insert(account);
        //用户第三方表
        MiniAccountOauths miniAccountOauths = new MiniAccountOauths();
        miniAccountOauths.setOauthType(OauthTypeEnum.WX_MINI.getType());
        miniAccountOauths.setUserId(account.getUserId());
        miniAccountOauths.setOpenId(login.getOpenId());
        miniAccountOauths.setUnionId(login.getUnionId());
        miniAccountOauthsService.save(miniAccountOauths);
        //用户信息扩展
        MiniAccountExtends miniAccountExtends = new MiniAccountExtends();
        miniAccountExtends.setUserId(account.getUserId());
        miniAccountExtends.setIsBlacklist(0);
        miniAccountExtends.setConsumeNum(0);
        miniAccountExtends.setCurrentStatus(CommonConstants.NUMBER_ONE);
        miniAccountExtends.setConsumeTotleMoney(BigDecimal.valueOf(0.0));
        //新用户所在店铺为默认店铺
        ShopsPartner shopsPartner = remoteShopsService.oneByTenantId(tenantId);
        miniAccountExtends.setShopId(shopsPartner.getShopId());
        miniAccountExtends.setShopUserId(snowflake.nextId() + "");
        miniAccountExtendsService.save(miniAccountExtends);
        miniAccountOauthsDto = accuontOauthsCache(loginKey, miniAccountOauths, account, miniAccountExtends);
        // Todo 通知生成店铺首次访问记录
        return miniAccountOauthsDto;
    }


    /**
     * 缓存登录授权信息
     *
     * @param loginKey       rediskey
     * @param accountOauths  认证基础数据
     * @param account        用户基础数据
     * @param accountExtends 用户扩展数据
     * @return com.medusa.gruul.account.model.dto.MiniAccountOauthsDto
     */
    private MiniAccountOauthsDto accuontOauthsCache(String loginKey, MiniAccountOauths accountOauths, MiniAccount account, MiniAccountExtends accountExtends) {
        MiniAccountOauthsDto miniAccountOauthsDto = null;
        AccountRedis accountRedis = new AccountRedis();
        String cacheUserInfo = accountRedis.get(loginKey);
        //判断用户是否token是否过期是否重新生成token信息
        if (StrUtil.isNotEmpty(cacheUserInfo)) {
            miniAccountOauthsDto = JSONObject.parseObject(cacheUserInfo, MiniAccountOauthsDto.class);
        } else {
            miniAccountOauthsDto = new MiniAccountOauthsDto();
            BeanUtils.copyProperties(accountOauths, miniAccountOauthsDto);
            String jwtToken = new JwtUtils("gruul-mini-account").createJwtToken("mini-account");
            miniAccountOauthsDto.setToken(jwtToken);
            accountRedis.setNxPx(loginKey, JSON.toJSONString(miniAccountOauthsDto), TimeConstants.ONE_HOUR * 2);
            //存入请求用户信息对象缓存
            curUserCache(miniAccountOauthsDto, account, accountExtends);
        }
        return miniAccountOauthsDto;
    }

    /**
     * 获取用户是否授权过
     *
     * @param userId 用户id
     * @return 授权过  trun  未授权 false
     */
    private Boolean getUserWhetherAuthorization(String userId) {
        MiniAccount account = this.getByUserId(userId);
        return account.getWhetherAuthorization();
    }


    /**
     * 请求用户信息对象缓存
     *
     * @param miniAccountOauthsDto com.medusa.gruul.account.model.dto.MiniAccountOauthsDto
     * @param account              用户基础数据
     * @param accountExtends       用户扩展数据
     */
    private void curUserCache(MiniAccountOauthsDto miniAccountOauthsDto, MiniAccount account, MiniAccountExtends accountExtends) {
        AccountRedis allRedis = new AccountRedis(CommonConstants.MINI_ACCOUNT_REDIS_KEY);
        CurMiniUserInfoDto miniUserInfoDto = new CurMiniUserInfoDto();
        miniUserInfoDto.setUserId(account.getUserId());
        miniUserInfoDto.setNikeName(account.getNikeName());
        miniUserInfoDto.setAvatarUrl(account.getAvatarUrl());
        miniUserInfoDto.setGender(account.getGender());
        miniUserInfoDto.setShopUserId(accountExtends.getShopUserId());
        miniUserInfoDto.setOpenId(miniAccountOauthsDto.getOpenId());

        CurUserDto curUserDto = new CurUserDto();

//        1-微信小程序,2-公众号
        if (miniAccountOauthsDto.getOauthType().equals(CommonConstants.NUMBER_ONE)) {
            curUserDto.setUserType(CommonConstants.NUMBER_ZERO);
            miniUserInfoDto.setTerminalType(LoginTerminalEnum.MINI);
        }
        if (miniAccountOauthsDto.getOauthType().equals(CommonConstants.NUMBER_TWO)) {
            curUserDto.setUserType(CommonConstants.NUMBER_TWO);
            miniUserInfoDto.setTerminalType(LoginTerminalEnum.H5);
        }
        allRedis.setNxPx(miniAccountOauthsDto.getToken(), JSON.toJSONString(miniUserInfoDto), TimeConstants.SIX_HOURS * 2);
        // curUserDto 缓存
        curUserDto.setNikeName(account.getNikeName());
        curUserDto.setAvatarUrl(account.getAvatarUrl());
        curUserDto.setGender(account.getGender());
        curUserDto.setUserId(accountExtends.getShopUserId());
        curUserDto.setShopId(accountExtends.getShopId());
        curUserDto.setShopType(0);
        curUserDto.setVersion("");
        curUserDto.setOpenId(miniAccountOauthsDto.getOpenId());
        AccountRedis accountRedis = new AccountRedis();
        accountRedis.setNxPx(miniAccountOauthsDto.getToken(), JSON.toJSONString(curUserDto), TimeConstants.SIX_HOURS * 2);
    }


    @Override
    public MiniAccount getByUserId(String userId) {
        String key = RedisConstant.ACCOUNT_DB_KEY.concat(userId);
        AccountRedis accountRedis = new AccountRedis();
        String accountJson = accountRedis.get(key);
        MiniAccount account = null;
        if (StrUtil.isNotEmpty(accountJson)) {
            account = JSONObject.parseObject(accountJson, MiniAccount.class);
        } else {
            account = this.baseMapper.selectOne(new QueryWrapper<MiniAccount>().eq("user_id", userId));
            accountCeche(account);
        }
        return account;
    }


    @Override
    public String decodePhoneInfo(DecodePhoneInfo decodePhoneInfo) {
        String json = WxMaCryptUtils.decrypt(decodePhoneInfo.getSessionKey(), decodePhoneInfo.getEncryptedData(), decodePhoneInfo.getIvStr());
        JSONObject jsonObject = JSON.parseObject(json);
        String phoneNumber = jsonObject.getString("purePhoneNumber");
        CurUserDto curUser = CurUserUtil.getHttpCurUser();
        MiniAccount miniAccount = this.getByUserId(curUser.getUserId());
        if (miniAccount == null) {
            throw new ServiceException("数据错误", SystemCode.DATA_NOT_EXIST_CODE);
        }
        miniAccount.setPhone(phoneNumber);
        this.updateById(miniAccount);
        accountCeche(miniAccount);
        return phoneNumber;
    }

    @Override
    public void updateUserBaseInfo(UpdateUserBaseInfoDto updateUserBaseInfoDto) {
        CurUserDto curUser = CurUserUtil.getHttpCurUser();
        MiniAccount miniAccount = this.getByShopUserId(curUser.getUserId());
        if (miniAccount == null) {
            throw new ServiceException("数据错误", SystemCode.DATA_NOT_EXIST_CODE);
        }
        BeanUtil.copyProperties(updateUserBaseInfoDto, miniAccount, CopyOptions.create().ignoreNullValue());
        if (StrUtil.isEmpty(updateUserBaseInfoDto.getNikeName())){
            miniAccount.setNikeName(updateUserBaseInfoDto.getNickName());
        }
        miniAccount.setWhetherAuthorization(Boolean.TRUE);
        this.updateById(miniAccount);
        //缓存用户最新的基础信息
        accountCeche(miniAccount);
    }

    /**
     * 更新缓存
     *
     * @param miniAccount com.medusa.gruul.account.api.entity.MiniAccount
     */
    private void accountCeche(MiniAccount miniAccount) {
        String key = RedisConstant.ACCOUNT_DB_KEY.concat(miniAccount.getUserId());
        AccountRedis accountRedis = new AccountRedis();
        accountRedis.set(key, JSON.toJSONString(miniAccount));
        CompletableFuture.runAsync(() -> {
            MiniAccountOauths miniAccountOauths = miniAccountOauthsService.getByUserId(OauthTypeEnum.WX_MINI.getType(), miniAccount.getUserId());
            String loginKey = RedisConstant.LOGIN_KEY.concat(miniAccount.getTenantId()).concat(":").concat(OauthTypeEnum.WX_MINI.getType().toString()).concat(":").concat(miniAccountOauths.getOpenId());
            MiniAccountExtends accountExtends = miniAccountExtendsService.findByCurrentStatus(miniAccountOauths.getUserId());
            accuontOauthsCache(loginKey, miniAccountOauths, miniAccount, accountExtends);
        });
    }


    @Override
    public UserInfoVo getUserInfo(Integer infoLevel) {
        UserInfoVo vo = new UserInfoVo();
        CurUserDto curUser = CurUserUtil.getHttpCurUser();
        if (curUser == null) {
            throw new ServiceException("token错误", SystemCode.DATA_NOT_EXIST_CODE);
        }
        MiniAccountExtends miniAccountExtends = miniAccountExtendsService.findByShopUserId(curUser.getUserId());
        if (miniAccountExtends == null) {
            throw new ServiceException("数据错误", SystemCode.DATA_NOT_EXIST_CODE);
        }
        MiniAccount miniAccount = this.getByUserId(miniAccountExtends.getUserId());
        BeanUtils.copyProperties(miniAccount, vo);
        vo.setShopUserId(curUser.getUserId());
        if (infoLevel.equals(CommonConstants.NUMBER_TWO)) {
            UserInfoExtendsVo extendsVo = new UserInfoExtendsVo();
            BeanUtils.copyProperties(miniAccountExtends, extendsVo);
            //用户扩展信息
            vo.setInfoExtends(extendsVo);
            log.error("shopId : " + extendsVo.getShopId());
            if (StrUtil.isEmpty(extendsVo.getShopId())) {
                log.error("shopId : " + extendsVo.getShopId() + "tenant_id" + TenantContextHolder.getTenantId());
                ShopsPartner shopsPartner = remoteShopsService.oneByTenantId(TenantContextHolder.getTenantId());
                log.error("shopsPartner : " + JSON.toJSONString(shopsPartner));
                if (null != shopsPartner) {
                    extendsVo.setShopId(shopsPartner.getShopId());
                }
            }
        }
        if (infoLevel.equals(CommonConstants.NUMBER_THREE)) {
            UserBlacklistAstrictVo astrictVo = new UserBlacklistAstrictVo();
            List<Integer> restrictTypes = miniAccountRestrictService.getByUserId(curUser.getUserId());
            if (CollectionUtil.isNotEmpty(restrictTypes)) {
                for (Integer restrictType : restrictTypes) {
                    if (restrictType.equals(BlacklistEnum.REJECT_ORDER.getType())) {
                        astrictVo.setRejectOrder(true);
                    }

                    if (restrictType.equals(BlacklistEnum.REJECT_COMMENT.getType())) {
                        astrictVo.setRejectOrder(true);
                    }
                }

            }
            vo.setAstrictVo(astrictVo);
        }

        return vo;
    }

    @Override
    public PageUtils<List<UserListVo>> userList(String nikeName,
                                                String becomeMemberStartTime, String becomeMemberEndTime,
                                                Long tagId,
                                                String orderSuccessStartTime, String orderSuccessEndTime,
                                                String memberNumber, Integer page, Integer size, Integer sortType) {
        String tenantId = TenantContextHolder.getTenantId();
        Map<String, Object> paramMap = new HashMap<>(8);
        if (StrUtil.isNotEmpty(becomeMemberStartTime)) {
            paramMap.put("becomeMemberStartTime", becomeMemberStartTime);
        }
        if (StrUtil.isNotEmpty(becomeMemberEndTime)) {
            paramMap.put("becomeMemberEndTime", becomeMemberEndTime);
        }
        if (tagId != null) {
            paramMap.put("tagId", tagId);
        }
        if (StrUtil.isNotEmpty(orderSuccessStartTime)) {
            paramMap.put("orderSuccessStartTime", orderSuccessStartTime);
        }
        if (StrUtil.isNotEmpty(orderSuccessEndTime)) {
            paramMap.put("orderSuccessEndTime", orderSuccessEndTime);
        }
        if (StrUtil.isNotEmpty(memberNumber)) {
            paramMap.put("memberNumber", memberNumber);
        }
        if (StrUtil.isNotBlank(nikeName)) {
            AtomicReference<String> name = new AtomicReference<>("%");
            nikeName.chars().mapToObj(obj -> (char) obj).forEach(obj -> {
                name.set(name.get().concat(obj.toString()).concat("%"));
            });
            paramMap.put("nikeName", name.toString());
        }
        if (sortType != null) {
            paramMap.put("sortType", sortType);
        }
        String shopId = ShopContextHolder.getShopId();
        if (StrUtil.isEmpty(shopId)) {
            throw new ServiceException("店铺id不存在");
        }
        paramMap.put("shopId", shopId);
        IPage<UserListDto> iPage = this.baseMapper.selectByUserList(new Page<>(page, size), paramMap);
        List<UserListDto> records = iPage.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return new PageUtils(new ArrayList(0), (int) iPage.getTotal(), (int) iPage.getSize(), (int) iPage.getCurrent());
        }
        List<UserListVo> vos = new LinkedList<>();
        List<String> userIdList = records.stream().map(UserListDto::getShopUserId).collect(Collectors.toList());
        //查询用户标签
        Map<String, List<UserTagVo>> userTag = MapUtil.newHashMap(userIdList.size());
        List<MiniAccountTagGroup> tagGroups = miniAccountTagGroupService.getByUserListId(userIdList);
        if (CollectionUtil.isNotEmpty(tagGroups)) {
            setAccountGroupTag(userTag, tagGroups);
        }

        //封装用户数据
        setPcAccountListVos(tenantId, shopId, records, vos, userTag);
        return new PageUtils(vos, (int) iPage.getTotal(), (int) iPage.getSize(), (int) iPage.getCurrent());
    }

    /**
     * @param tenantId           租户id
     * @param shopId             店铺id
     * @param records            用户数据
     * @param vos                标签数据
     * @param userTag            用户标签
     */
    private void setPcAccountListVos(String tenantId, String shopId, List<UserListDto> records, List<UserListVo> vos, Map<String, List<UserTagVo>> userTag) {
        for (UserListDto record : records) {
            UserListVo vo = new UserListVo();
            BeanUtils.copyProperties(record, vo);
            //Todo 获取返利设置里会员返利金额是否可用,可用：返利+用户余额  不可用：用户余额
            vo.setSupplyBonus(record.getSupplyBonus());
            vo.setUserId(record.getShopUserId());
            List<UserTagVo> userTagVos = userTag.get(record.getShopUserId());
            vo.setUserTagVos(userTagVos);
            vo.setState(2);
            vos.add(vo);
        }
    }

    /**
     * 设置用户标签
     *
     * @param userTag   标签
     * @param tagGroups 用户相关标签
     */
    private void setAccountGroupTag(Map<String, List<UserTagVo>> userTag, List<MiniAccountTagGroup> tagGroups) {
        List<Long> tagIdList = tagGroups.stream().map(MiniAccountTagGroup::getTagId).distinct().collect(Collectors.toList());
        List<MiniAccountTag> miniAccountTags = miniAccountTagService.getByIdList(tagIdList);
        Map<Long, MiniAccountTag> tagMap = miniAccountTags.stream().collect(Collectors.toMap(MiniAccountTag::getId, v -> v));
        tagGroups.stream().collect(Collectors.groupingBy(MiniAccountTagGroup::getUserId)).forEach((k, v) -> {
            List<UserTagVo> userTagVos = new ArrayList<>(v.size());
            for (MiniAccountTagGroup miniAccountTagGroup : v) {
                MiniAccountTag miniAccountTag = tagMap.get(miniAccountTagGroup.getTagId());
                if (miniAccountTag != null) {
                    UserTagVo userTagVo = new UserTagVo();
                    userTagVo.setTagId(miniAccountTag.getId());
                    userTagVo.setTagName(miniAccountTag.getTagName());
                    userTagVos.add(userTagVo);
                }
            }
            userTag.put(k, userTagVos);
        });
    }

    @Override
    public AccountInfoDto accountInfo(String shopUserId, List<Integer> infos) {
        AccountInfoDto accountInfoDto = new AccountInfoDto();

        for (Integer obj : infos) {
            switch (obj) {
                case 1:
                    MiniAccount miniAccount = this.getByShopUserId(shopUserId);
                    accountInfoDto.setMiniAccountunt(miniAccount);
                    break;
                case 2:
                    MiniAccountExtends miniAccountExtends = miniAccountExtendsService.findByShopUserId(shopUserId);
                    accountInfoDto.setMiniAccountExtends(miniAccountExtends);
                    List<Integer> restrictTypes = miniAccountRestrictService.getByUserId(shopUserId);
                    accountInfoDto.setRestrictTypes(restrictTypes);
                    break;
                case 3:
                    List<MiniAccountAddress> miniAccountAddresses = iMiniAccountAddressService.getUserAddress(shopUserId);
                    if (CollectionUtil.isNotEmpty(miniAccountAddresses)) {
                        //默认地址,排序在首位
                        CollectionUtil.sort(miniAccountAddresses, Comparator.comparing(MiniAccountAddress::getIsDefault).reversed());
                    }
                    accountInfoDto.setMiniAccountAddress(miniAccountAddresses);
                    break;
                case 4:
                    MiniAccountExtends ext = miniAccountExtendsService.findByShopUserId(shopUserId);
                    MiniAccountOauths miniAccountOauths = miniAccountOauthsService.getByUserId(OauthTypeEnum.WX_MINI.getType(), ext.getUserId());
                    accountInfoDto.setMiniAccountOauths(miniAccountOauths);
                    break;
                case 5:
                    MiniAccountExtends accountExtends = miniAccountExtendsService.findByShopUserId(shopUserId);
//                    AssembleActivityAssociation associationServiceAssByAssId =
//                            assActAssociationService.getAssByAssId(accountExtends.getCommunityId().toString());
//                    if (associationServiceAssByAssId != null && associationServiceAssByAssId.getId() != null) {
//                        AssembleActivityAssociationDto assembleActivityAssociationDto = BeanUtil.toBean(associationServiceAssByAssId, AssembleActivityAssociationDto.class);
//                        MiniAccount account = this.getByShopUserId(associationServiceAssByAssId.getUserId());
//                        if (ObjectUtil.isNotNull(account)) {
//                            assembleActivityAssociationDto.setAvatarUrl(account.getAvatarUrl());
//                            assembleActivityAssociationDto.setNikeName(account.getNikeName());
//                        }
//                        accountInfoDto.setAssembleActivityAssociationDto(assembleActivityAssociationDto);
//                    }
                    break;
                default:
            }
        }
        return accountInfoDto;
    }

    @Override
    public PageUtils<List<BlacklistUserVo>> blacklist(Integer page, Integer size, Integer permission, String fuzzy) {
        Map<String, Object> paramMap = new HashMap<>(8);
        paramMap.put("permission", permission);
        if (StrUtil.isNotEmpty(fuzzy)) {
            paramMap.put("fuzzy", "%" + fuzzy + "%");
        }
        String shopId = ShopContextHolder.getShopId();
        if (StrUtil.isEmpty(shopId)) {
            throw new ServiceException("店铺id不存在");
        }
        paramMap.put("shopId", shopId);
        IPage<BlacklistUserDto> userDtoIpage = this.baseMapper.selectByBlackListUser(new Page<BlacklistUserDto>(page, size), paramMap);
        List<BlacklistUserDto> records = userDtoIpage.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return new PageUtils(new ArrayList(0), (int) userDtoIpage.getTotal(), (int) userDtoIpage.getSize(), (int) userDtoIpage.getCurrent());
        }
        List<BlacklistUserVo> vos = new ArrayList<>(records.size());

        List<String> userIds = records.stream().map(BlacklistUserDto::getShopUserId).collect(Collectors.toList());
        Map<String, List<MiniAccountRestrict>> accountRestrictMap = miniAccountRestrictService.getByUserIds(userIds).stream().collect(Collectors.groupingBy(MiniAccountRestrict::getUserId));

        for (BlacklistUserDto record : records) {
            BlacklistUserVo vo = new BlacklistUserVo();
            BeanUtils.copyProperties(record, vo);
            vo.setUserId(record.getShopUserId());
            List<Integer> blacklistTypeList = accountRestrictMap.get(vo.getUserId()).stream().map(MiniAccountRestrict::getRestrictType).collect(Collectors.toList());
            vo.setBlacklistType(blacklistTypeList);
            vos.add(vo);
        }

        return new PageUtils(vos, (int) userDtoIpage.getTotal(), (int) userDtoIpage.getSize(), (int) userDtoIpage.getCurrent());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String switchShops(String shopId) {
        CurUserDto curUser = CurUserUtil.getHttpCurUser();
        if (curUser == null) {
            throw new ServiceException("token错误", SystemCode.DATA_NOT_EXIST_CODE);
        }
        MiniAccountExtends accountExtends = miniAccountExtendsService.findByShopUserId(curUser.getUserId());
        if (accountExtends == null) {
            throw new ServiceException("错误账号", SystemCode.DATA_NOT_EXIST_CODE);
        }
        //修改该用户的所有扩展店铺数据未非当前默认账号数据
        MiniAccountExtends up = new MiniAccountExtends();
        up.setCurrentStatus(CommonConstants.NUMBER_ZERO);
        miniAccountExtendsService.update(up, new QueryWrapper<MiniAccountExtends>().eq("user_id", accountExtends.getUserId()));
        //修改店铺id
        //判断是否已存在该店铺账号扩展数据
        MiniAccountExtends miniAccountExtends = miniAccountExtendsService.getOne(new QueryWrapper<MiniAccountExtends>().eq("user_id", accountExtends.getUserId()).eq("shop_id", shopId));
        if (miniAccountExtends == null) {
            miniAccountExtends = new MiniAccountExtends();
            //用户信息扩展
            miniAccountExtends.setUserId(accountExtends.getUserId());
            miniAccountExtends.setIsBlacklist(0);
            miniAccountExtends.setConsumeNum(0);
            miniAccountExtends.setCurrentStatus(CommonConstants.NUMBER_ONE);
            miniAccountExtends.setConsumeTotleMoney(BigDecimal.valueOf(0.0));
            String shopUserId = IdUtil.createSnowflake(snowflakeProperty.getWorkerId(), snowflakeProperty.getDatacenterId()) + "";
            miniAccountExtends.setShopUserId(shopUserId);
            miniAccountExtends.setShopId(shopId);
            miniAccountExtends.setTenantId(accountExtends.getTenantId());
            LocalDateTime currentDateTime = LocalDateTimeUtils.convertDateToLDT(new Date());
            miniAccountExtends.setLastLoginTime(currentDateTime);
            miniAccountExtendsService.save(miniAccountExtends);
        } else {
            miniAccountExtends.setCurrentStatus(CommonConstants.NUMBER_ONE);
            miniAccountExtends.setShopId(shopId);
        }
        miniAccountExtendsService.updateById(miniAccountExtends);
        //重新缓存店铺用户数据
        MiniAccountOauths miniAccountOauths = miniAccountOauthsService.getByUserId(OauthTypeEnum.WX_MINI.getType(), accountExtends.getUserId());
        String loginKey = RedisConstant.LOGIN_KEY.concat(miniAccountOauths.getTenantId()).concat(":").concat(OauthTypeEnum.WX_MINI.getType().toString()).concat(":").concat(miniAccountOauths.getOpenId());
        MiniAccount account = this.getByUserId(accountExtends.getUserId());
        MiniAccountOauthsDto miniAccountOauthsDto = accuontOauthsCache(loginKey, miniAccountOauths, account, miniAccountExtends);
        return RedisConstant.ACCOUNT_KEY.concat(miniAccountOauthsDto.getToken());
    }

    @Override
    public void generateAccountDefault(String jsonData) {
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        String tenantId = jsonObject.getString("tenantId");
        String shopId = jsonObject.getString("shopId");
        if (StrUtil.isEmpty(tenantId) || StrUtil.isEmpty(shopId)) {
            throw new ServiceException("jsonData:".concat(jsonData).concat("--->数据为空"));
        }
        TenantContextHolder.setTenantId(tenantId);
        ShopContextHolder.setShopId(shopId);
//        remoteShippingFeignService.createDefaultPoint(tenantId, shopId, "", "");
    }

    @Override
    public List<MiniAccountExtDto> accountsInfoList(List<String> shopUserIds) {
        return this.baseMapper.selectByShopUserIds(shopUserIds);
    }


    @Override
    public String qrCode() {
        CurUserDto httpCurUser = CurUserUtil.getHttpCurUser();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("shopUserId", httpCurUser.getUserId());
        QrConfig qrConfig = QrConfig.create();
        BufferedImage generate = QrCodeUtil.generate(jsonObject.toJSONString(), qrConfig);
        byte[] bytes = QrCodeUtil.generatePng(jsonObject.toJSONString(), qrConfig);
        return Base64.encode(bytes);
    }


    @Override
    public Result mpLogin(WxMpUserDto wxMpUserDto) {
        if (StrUtil.isEmpty(wxMpUserDto.getUnionId())) {
            return Result.failed(MpLoginCodeEnum.UNIONID_NULL.getCode(), MpLoginCodeEnum.UNIONID_NULL.getMessage());
        }
        if (StrUtil.isEmpty(wxMpUserDto.getShopId())) {
            return Result.failed(MpLoginCodeEnum.SHOPID_NULL.getCode(), MpLoginCodeEnum.SHOPID_NULL.getMessage());
        }
        TenantContextHolder.setTenantId(wxMpUserDto.getTenantId());
        String loginKey = RedisConstant.LOGIN_KEY.concat(wxMpUserDto.getTenantId()).concat(":")
                .concat(OauthTypeEnum.WX_MP.getType().toString()).concat(":").concat(wxMpUserDto.getOpenId());
        //1.获取小程序用户信息
        MiniAccountOauths miniAccountOauths = miniAccountOauthsService.getByUnionIdAndType(wxMpUserDto.getUnionId(), OauthTypeEnum.WX_MINI);
        //2.用户信息不存在直接返回
        if (miniAccountOauths == null) {
            return Result.failed(MpLoginCodeEnum.MINI_NOT_LOG.getCode(), MpLoginCodeEnum.MINI_NOT_LOG.getMessage());
        }
        //获取公众号授权信息不存在则增加
        MiniAccountOauths mpAccountOauths = miniAccountOauthsService.getByOpenId(wxMpUserDto.getOpenId(), OauthTypeEnum.WX_MP.getType());
        MiniAccount account = getByUserId(miniAccountOauths.getUserId());
        if (mpAccountOauths == null) {
            //用户第三方授权表
            mpAccountOauths = new MiniAccountOauths();
            mpAccountOauths.setOauthType(OauthTypeEnum.WX_MP.getType());
            mpAccountOauths.setUserId(account.getUserId());
            mpAccountOauths.setOpenId(wxMpUserDto.getOpenId());
            miniAccountOauthsService.save(mpAccountOauths);
        }

        MiniAccountExtends accountExtends = miniAccountExtendsService.findByShopIdAndUserId(wxMpUserDto.getShopId(), mpAccountOauths.getUserId());
        if (accountExtends == null) {
            return Result.failed(MpLoginCodeEnum.DATA_NULL.getCode(), MpLoginCodeEnum.DATA_NULL.getMessage());
        }
        //缓存用户数据
        MiniAccountOauthsDto miniAccountOauthsDto = accuontOauthsCache(loginKey, mpAccountOauths, account, accountExtends);
        miniAccountExtendsService.update(new UpdateWrapper<MiniAccountExtends>()
                .set("last_login_time", LocalDateTime.now()).eq("shop_user_id", accountExtends.getShopUserId()));
        String token = RedisConstant.ACCOUNT_KEY.concat(miniAccountOauthsDto.getToken());
        return Result.ok(token);
    }

    @Override
    public MiniAccount getByShopUserId(String shopUserId) {
        return this.getBaseMapper().selectByShopUserId(shopUserId);
    }


}



