package com.medusa.gruul.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.account.api.feign.RemoteMiniAccountService;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.constant.RegexConstants;
import com.medusa.gruul.common.core.constant.TimeConstants;
import com.medusa.gruul.common.core.constant.enums.AuthCodeEnum;
import com.medusa.gruul.common.core.constant.enums.LoginTerminalEnum;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.*;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.common.dto.CurMiniUserInfoDto;
import com.medusa.gruul.common.dto.CurPcUserInfoDto;
import com.medusa.gruul.common.dto.CurShopInfoDto;
import com.medusa.gruul.common.dto.CurUserDto;
import com.medusa.gruul.common.redis.RedisManager;
import com.medusa.gruul.platform.api.entity.*;
import com.medusa.gruul.platform.conf.MeConstant;
import com.medusa.gruul.platform.conf.PlatformRedis;
import com.medusa.gruul.platform.conf.WechatOpenProperties;
import com.medusa.gruul.platform.constant.RedisConstant;
import com.medusa.gruul.platform.constant.ScanCodeScenesEnum;
import com.medusa.gruul.platform.mapper.AccountInfoMapper;
import com.medusa.gruul.platform.model.dto.*;
import com.medusa.gruul.platform.model.dto.agent.BatchNoteDto;
import com.medusa.gruul.platform.model.vo.*;
import com.medusa.gruul.platform.model.vo.agent.AgentMerchantVo;
import com.medusa.gruul.platform.service.*;
import com.medusa.gruul.platform.stp.StpAgentUtil;
import com.medusa.gruul.shops.api.entity.ShopsPartner;
import com.medusa.gruul.shops.api.feign.RemoteShopsService;
import lombok.extern.log4j.Log4j2;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * <p>
 * 平台与租户平台用户表 服务实现类
 * </p>
 *
 * @author whh
 * @since 2019-09-07
 */
@Service
@Log4j2
public class AccountInfoServiceImpl extends ServiceImpl<AccountInfoMapper, AccountInfo> implements IAccountInfoService {

    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private IMiniInfoService miniInfoService;
    @Autowired
    private ISendCodeService sendCodeService;
    @Autowired
    private IAuthRoleInfoService authRoleInfoService;
    @Autowired
    private IBaseMenuService baseMenuService;
    @Autowired
    private WechatOpenProperties wechatOpenProperties;
    @Autowired
    private IPlatformShopInfoService platformShopInfoService;
    @Autowired
    private RemoteShopsService remoteShopsService;

    @Autowired
    private ISystemConfService systemConfService;


    /**
     * 封装用户Vo
     *
     * @param accountInfo
     * @return
     */
    @Deprecated
    private OldAccountInfoVo getAccountInfoVo(AccountInfo accountInfo) {
        OldAccountInfoVo vo = new OldAccountInfoVo();
        BeanUtils.copyProperties(accountInfo, vo);
        Long subjectId = accountInfo.getId();
        if (StrUtil.isNotEmpty(accountInfo.getBindMiniId())) {
            subjectId = accountInfo.getSubjectId();
        }
        //获取当前可登录的小程序
        MiniInfo miniInfo = miniInfoService.getByUserDefualtMini(subjectId);
        Optional.ofNullable(miniInfo).ifPresent(obj -> {
            OldAccountInfoVo.MeMini mini = new OldAccountInfoVo.MeMini();
            mini.setId(obj.getId());
            mini.setTenantId(obj.getTenantId());
            mini.setMiniName(obj.getMiniName());
            mini.setMiniHeadIcon(obj.getHeadImageUrl());
            mini.setAppId(obj.getAppId());

            //获取小程序下的角色
            List<AuthRoleInfo> authRoleInfos = authRoleInfoService.getByUserIdAndTenantId(vo.getId(), obj.getTenantId());
            Optional.ofNullable(authRoleInfos).ifPresent(roleInfos -> {
                List<OldAccountInfoVo.MiniRole> miniRoles = roleInfos.stream().map(authRoleInfo -> {
                    OldAccountInfoVo.MiniRole miniRole = new OldAccountInfoVo.MiniRole();
                    miniRole.setRoleId(authRoleInfo.getId());
                    miniRole.setRoleName(authRoleInfo.getRoleName());
                    miniRole.setRoleCode(authRoleInfo.getRoleCode());
                    return miniRole;
                }).collect(Collectors.toList());
                vo.setMiniRoles(miniRoles);
                OldAccountInfoVo.MiniRole role = new OldAccountInfoVo.MiniRole();
                role.setRoleCode(CommonConstants.NUMBER_ZERO.toString());
                //todo 未完成
                //0管理员角色
                if (vo.getMiniRoles().contains(role)) {
                    ShopsPartner shopsPartner = remoteShopsService.getByPlatformIdAndTenantId(accountInfo.getId(), mini.getTenantId());
                    if (shopsPartner != null) {
                        mini.setShopId(shopsPartner.getShopId());
                    }
                }
                //1城市合伙人角色
                role.setRoleCode(CommonConstants.NUMBER_ONE.toString());
                if (vo.getMiniRoles().contains(role)) {

                }
            });

            //获取小程序套餐对应的服务,
            List<MenuDto> menuDtos = baseMenuService.getByTenantIdMenu(obj.getTenantId());
            Optional.ofNullable(menuDtos).ifPresent(menuDto -> {
                Map<Long, List<MenuDto>> menuGroup = menuDto.stream().collect(Collectors.groupingBy(MenuDto::getPId));
                List<OldAccountInfoVo.Menu> oneMenu = menuDto.stream().filter(f -> f.getPId().intValue() == CommonConstants.NUMBER_ZERO).map(menu -> {
                    OldAccountInfoVo.Menu miniMenu = new OldAccountInfoVo.Menu();
                    miniMenu.setTitle(menu.getTitle());
                    miniMenu.setPath(menu.getPath());
                    miniMenu.setName(menu.getName());
                    miniMenu.setIcon(menu.getIcon());
                    miniMenu.setMenuId(menu.getMenuId());
                    miniMenu.setPId(0L);
                    return miniMenu;
                }).collect(Collectors.toList());
                for (OldAccountInfoVo.Menu menu : oneMenu) {
                    List<MenuDto> subDto = menuGroup.get(menu.getMenuId());
                    if (Optional.ofNullable(subDto).isPresent()) {
                        List<OldAccountInfoVo.Menu> subMenus = subDto.stream().map(subDtoMenu -> {
                            OldAccountInfoVo.Menu subMenu = new OldAccountInfoVo.Menu();
                            subMenu.setTitle(subDtoMenu.getTitle());
                            subMenu.setPath(subDtoMenu.getPath());
                            subMenu.setName(subDtoMenu.getName());
                            subMenu.setIcon(subDtoMenu.getIcon());
                            subMenu.setMenuId(subMenu.getMenuId());
                            subMenu.setPId(menu.getMenuId());
                            return subMenu;
                        }).collect(Collectors.toList());
                        menu.setSubMenu(subMenus);
                    }

                }
                vo.setMiniMenus(oneMenu);
            });
            vo.setMiniInfos(mini);
        });
        return vo;
    }


    @Override
    public void checkoutAccount(String phone, Integer type) {
        AccountInfo accountInfo = this.baseMapper.selectOne(new QueryWrapper<AccountInfo>().eq("phone", phone));
        //账号存在校验
        if (type.equals(CommonConstants.NUMBER_ONE)) {
            if (accountInfo == null) {
                throw new ServiceException("账号不存在", SystemCode.DATA_NOT_EXIST.getCode());
            }
            return;
        }
        //账号不存在校验
        if (type.equals(CommonConstants.NUMBER_TWO)) {
            if (accountInfo != null) {
                throw new ServiceException("账号已存在");
            }
            return;
        }
        throw new ServiceException("非法操作");

    }

    @Override
    public OldAccountInfoVo login(TenementLoginDto tenementLoginDto) {
        String key = SecureUtil.md5(tenementLoginDto.getUsername().concat("^2!2$3.").concat(tenementLoginDto.getPassword()));
        PlatformRedis platformRedis = new PlatformRedis();
        String loginKey = RedisConstant.LOGIN_KEY.concat(key);
        String keyData = platformRedis.get(loginKey);
        if (StrUtil.isNotEmpty(keyData)) {
            return JSON.parseObject(keyData, OldAccountInfoVo.class);
        }
        AccountInfo accountInfo = this.getByPhone(tenementLoginDto.getUsername());
        if (accountInfo == null) {
            throw new ServiceException("账号未注册", SystemCode.DATA_NOT_EXIST.getCode());
        }
        String passwd = SecureUtil.md5(tenementLoginDto.getPassword().concat(accountInfo.getSalt()));
        if (!StrUtil.equals(passwd, accountInfo.getPasswd())) {
            throw new ServiceException("密码错误", SystemCode.DATA_NOT_EXIST.getCode());
        }
        updateAccountLastLoignTime(accountInfo.getId());
        OldAccountInfoVo vo = getAccountInfoVo(accountInfo);
        //设置平台用户Token redisKey
        String userToken = cachePlatformCurUserDtoOld(accountInfo, vo);
        vo.setToken(userToken);
        long between = getTodayEndTime();
        //设置登录redisKey
        platformRedis.setNxPx(loginKey, JSON.toJSONString(vo), between);
        return vo;
    }

    /**
     * 缓存用户数据到缓存中
     * 每天12点失效
     *
     * @param info 用户基本信息
     * @param vo   前端封装用户信息
     * @return java.lang.String   redisKey
     */
    @Deprecated
    private String cachePlatformCurUserDtoOld(AccountInfo info, OldAccountInfoVo vo) {
        CurUserDto curUserDto = new CurUserDto();
        curUserDto.setUserId(info.getId().toString());
        curUserDto.setUserType(1);
        curUserDto.setAvatarUrl(info.getAvatarUrl());
        curUserDto.setGender(info.getGender());
        curUserDto.setOpenId(info.getOpenId());
        curUserDto.setNikeName(info.getNikeName());
        if (vo.getMiniInfos() != null) {
            curUserDto.setShopId(vo.getMiniInfos().getShopId());
        }
        if (CollectionUtil.isNotEmpty(vo.getMiniRoles())) {
            List<CurUserDto.MiniRole> roles = vo.getMiniRoles().stream().map(obj -> {
                CurUserDto.MiniRole miniRole = new CurUserDto.MiniRole();
                BeanUtils.copyProperties(obj, miniRole);
                return miniRole;
            }).collect(Collectors.toList());
            curUserDto.setMiniRoles(roles);
        }
        //没有手机号则是未注册绑定账号只是扫码了
        if (StrUtil.isEmpty(info.getPhone())) {
            return "no";
        }
        PlatformRedis platformRedis = new PlatformRedis();
        long between = getTodayEndTime();
        String tokenValue = SecureUtil.md5(info.getPhone()).concat(info.getSalt()).concat(info.getPasswd());
        String redisKey = RedisConstant.TOKEN_KEY.concat(tokenValue);
        platformRedis.setNxPx(redisKey, JSON.toJSONString(curUserDto), between);

        CurPcUserInfoDto curPcUserInfoDto = new CurPcUserInfoDto();
        curPcUserInfoDto.setUserId(info.getId().toString());
        curPcUserInfoDto.setTerminalType(LoginTerminalEnum.PC);
        curPcUserInfoDto.setAvatarUrl(info.getAvatarUrl());
        curPcUserInfoDto.setGender(info.getGender());
        curPcUserInfoDto.setOpenId(info.getOpenId());
        curPcUserInfoDto.setNikeName(info.getNikeName());
        curPcUserInfoDto.setIsAgent(Boolean.FALSE);
        if (info.getMeAgentId() != null && info.getMeAgentId() > 0) {
            curPcUserInfoDto.setIsAgent(Boolean.TRUE);
        }
        PlatformRedis allRedis = new PlatformRedis(CommonConstants.SHOP_INFO_REDIS_KEY);
        allRedis.setNxPx(tokenValue, JSON.toJSONString(curPcUserInfoDto), between);
        return platformRedis.getBaseKey().concat(":").concat(redisKey);
    }

    /**
     * 缓存用户数据到缓存中
     * 每天12点失效
     *
     * @param info 用户基本信息
     * @param vo   前端封装用户信息
     * @return java.lang.String   redisKey token
     */
    private String cachePlatformCurUserDto(AccountInfo info, AccountInfoVo vo) {
        CurUserDto curUserDto = new CurUserDto();
        curUserDto.setUserId(info.getId().toString());
        curUserDto.setUserType(1);
        curUserDto.setAvatarUrl(info.getAvatarUrl());
        curUserDto.setGender(info.getGender());
        curUserDto.setOpenId(info.getOpenId());
        curUserDto.setNikeName(info.getNikeName());
        LoginShopInfoVo shopInfoVo = vo.getShopInfoVo();
        if (shopInfoVo != null && StrUtil.isNotEmpty(shopInfoVo.getShopId())) {
            curUserDto.setShopId(shopInfoVo.getShopId());
        }
        PlatformRedis platformRedis = new PlatformRedis();
        long between = getTodayEndTime();
        String jwtToken = new JwtUtils(MeConstant.JWT_PRIVATE_KEY).createJwtToken(MeConstant.PLATFORM);
        String redisKey = RedisConstant.TOKEN_KEY.concat(jwtToken);
        platformRedis.setNxPx(redisKey, JSON.toJSONString(curUserDto), between);

        //新版
        CurPcUserInfoDto curPcUserInfoDto = new CurPcUserInfoDto();
        curPcUserInfoDto.setUserId(info.getId().toString());
        curPcUserInfoDto.setTerminalType(LoginTerminalEnum.PC);
        curPcUserInfoDto.setAvatarUrl(info.getAvatarUrl());
        curPcUserInfoDto.setGender(info.getGender());
        curPcUserInfoDto.setOpenId(info.getOpenId());
        curPcUserInfoDto.setNikeName(info.getNikeName());
        PlatformRedis allRedis = new PlatformRedis(CommonConstants.PC_INFO_REDIS_KEY);
        allRedis.setNxPx(jwtToken, JSON.toJSONString(curPcUserInfoDto), between);

        return platformRedis.getBaseKey().concat(":").concat(redisKey);

    }

    /**
     * 获取当前时间距离当天结束时间还有多久毫秒值
     *
     * @return 1234ms
     */
    private long getTodayEndTime() {
        Date date = new Date();
        DateTime endOfDay = DateUtil.endOfDay(date);
        return DateUtil.between(date, endOfDay, DateUnit.MS);
    }

    /**
     * 更新用户最后登录时间
     *
     * @param accountInfoId com.medusa.gruul.platform.api.entity.AccountInfo
     */
    private void updateAccountLastLoignTime(Long accountInfoId) {
        CompletableFuture.runAsync(() -> {
            AccountInfo info = new AccountInfo();
            info.setLastLoginTime(LocalDateTime.now());
            info.setId(accountInfoId);
            this.updateById(info);
        });
    }


    @Override
    public String preAccountScanCode(PreAccountVerifyDto preAccountVerifyDto) {
        if (!ScanCodeScenesEnum.findScenes(preAccountVerifyDto.getScenes())) {
            throw new ServiceException("场景类型无效");
        }
        //存在租户id并且非1则是登录之后操作,不存在则是登录前
        CurUserDto httpCurUser = CurUserUtil.getHttpCurUser();
        if (httpCurUser != null) {
            preAccountVerifyDto.setUserId(Long.valueOf(httpCurUser.getUserId()));
        }
        if (preAccountVerifyDto.getScenes().equals(ScanCodeScenesEnum.ACCOUNT_SHOP_INFO_CHECK.getScenes())) {
            Long shopInfoId = preAccountVerifyDto.getShopInfoId();
            if (shopInfoId == null) {
                throw new ServiceException("shopInfoId不能为空");
            }
            PlatformShopInfo platformShopInfo = platformShopInfoService.getById(shopInfoId);
            if (platformShopInfo == null) {
                throw new ServiceException("店铺不存在");
            }
        }

        String redirectUrl = wechatOpenProperties.getDomain().concat("/account-info/account/verify/notify");
        //网页应用目前仅填写snsapi_login即
        String scope = "snsapi_login";
        String state = SecureUtil.md5(System.currentTimeMillis() + "");
        new PlatformRedis().setNxPx(state, JSONObject.toJSONString(preAccountVerifyDto), TimeConstants.TEN_MINUTES);

        return wxMpService.switchoverTo(preAccountVerifyDto.getAppId()).buildQrConnectUrl(redirectUrl, scope, state);
    }

    @Override
    public void accountScanCodeNotify(String code, String state, HttpServletResponse response) {
        if (StrUtil.isEmpty(state)) {
            throw new ServiceException("非法请求");
        }
        PlatformRedis platformRedis = new PlatformRedis();
        String jsonData = platformRedis.get(state);
        if (StrUtil.isEmpty(jsonData)) {
            throw new ServiceException("无效数据或数据已过期");
        }
        PreAccountVerifyDto preAccountVerifyDto = JSONObject.parseObject(jsonData, PreAccountVerifyDto.class);
        Result result = Result.failed();
        //微信换绑
        if (preAccountVerifyDto.getScenes().equals(ScanCodeScenesEnum.ACCOUNT_SWITCHING.getScenes())) {
            result = this.changeTie(preAccountVerifyDto.getAppId(), code, preAccountVerifyDto.getUserId());
            //账号注册
        } else if (preAccountVerifyDto.getScenes().equals(ScanCodeScenesEnum.ACCOUNT_REGISTER.getScenes())) {
            result = this.createTempAccount(preAccountVerifyDto.getAppId(), code);
        } else if (preAccountVerifyDto.getScenes().equals(ScanCodeScenesEnum.ACCOUNT_LOGGIN.getScenes())) {
            result = this.scanCodeLogin(preAccountVerifyDto.getAppId(), code);
        } else if (preAccountVerifyDto.getScenes().equals(ScanCodeScenesEnum.ACCOUNT_SHOP_INFO_CHECK.getScenes())) {
            result = this.verifyShopAccount(preAccountVerifyDto.getAppId(), code, preAccountVerifyDto.getShopInfoId());
        } else {
            throw new ServiceException("错误数据");
        }

        //用户同意授权,跳转成功后页面
        StringBuilder redirectUrl = new StringBuilder(preAccountVerifyDto.getRedirectUrl());
        //判断是否已存在路径参数
        if (preAccountVerifyDto.getRedirectUrl().contains(MeConstant.WENHAO)) {
            redirectUrl.append("&");
        } else {
            redirectUrl.append("?");
        }
        code = SecureUtil.md5(System.currentTimeMillis() + "");
        redirectUrl.append("code=").append(code);
        //获取二维码时如存在shopInfoId则返回，供校验扫码前和扫码后是否一致
        if (preAccountVerifyDto.getShopInfoId() != null) {
            redirectUrl.append("&shopInfoId=").append(preAccountVerifyDto.getShopInfoId());
        }
        //存储查询之后不删除但只有5分钟有效的数据
        platformRedis.setNxPx(code.concat(":inside"), JSONObject.toJSONString(result), TimeConstants.TEN_MINUTES);
        //存储返回结果,提供一次查询,查询即失效
        platformRedis.setNxPx(code, JSONObject.toJSONString(result), TimeConstants.TEN_MINUTES);

        try {
            response.sendRedirect(redirectUrl.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫码校验用户是否是店铺拥有者
     *
     * @param appId      网站应用appId
     * @param code       回调code
     * @param shopInfoId 店铺id
     * @return com.medusa.gruul.common.core.util.Result
     */
    private Result verifyShopAccount(String appId, String code, Long shopInfoId) {
        try {
            wxMpService.switchoverTo(appId);
            AccountInfo accountInfo = null;
            WxMpOAuth2AccessToken wxMpOauth2AccessToken = wxMpService.oauth2getAccessToken(code);
            if (StrUtil.isNotEmpty(wxMpOauth2AccessToken.getUnionId())) {
                accountInfo = this.getByAccountUnionId(wxMpOauth2AccessToken.getUnionId());
            }
            if (accountInfo == null) {
                accountInfo = this.getByAccountOpenId(wxMpOauth2AccessToken.getOpenId());
                if (accountInfo == null) {
                    return Result.failed("请使用正确账户");
                }
            }
            PlatformShopInfo platformShopInfo = platformShopInfoService.getById(shopInfoId);
            if (platformShopInfo == null) {
                return Result.failed("店铺数据不存在,非法校验");
            }
            if (!platformShopInfo.getAccountId().equals(accountInfo.getId())) {
                return Result.failed("请使用店铺所有者的微信账号扫码");
            }
            return Result.ok(accountInfo);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return Result.failed(e.getError().getErrorMsg());
        }
    }

    /**
     * 扫码登录回调
     *
     * @param appId 网站应用appId
     * @param code  回调code
     * @return com.medusa.gruul.common.core.util.Result
     */
    private Result scanCodeLogin(String appId, String code) {

        try {
            wxMpService.switchoverTo(appId);
            AccountInfo accountInfo = null;
            WxMpOAuth2AccessToken wxMpOauth2AccessToken = wxMpService.oauth2getAccessToken(code);
            if (StrUtil.isNotEmpty(wxMpOauth2AccessToken.getUnionId())) {
                accountInfo = this.getByAccountUnionId(wxMpOauth2AccessToken.getUnionId());
                if (accountInfo != null) {
                    return Result.ok(accountInfo);
                }
            }
            accountInfo = this.getByAccountOpenId(wxMpOauth2AccessToken.getOpenId());
            //openId也查不出来则说明是未注册账号,存储临时数据,可提供跳转注册
            if (accountInfo == null) {
                accountInfo = new AccountInfo();
                getMpInfo(accountInfo, wxMpOauth2AccessToken, appId);
            }
            return Result.ok(accountInfo);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return Result.failed(e.getError().getErrorMsg());
        }
    }

    /**
     * 创建临时账号数据,不存储至数据库,待账号注册成功时正式存入
     *
     * @param appId 网站应用的appid
     * @param code  授权返回的code
     * @return com.medusa.gruul.common.core.util.Result
     */
    private Result<AccountInfo> createTempAccount(String appId, String code) {
        this.wxMpService.switchoverTo(appId);
        try {
            AccountInfo accountInfo = null;
            WxMpOAuth2AccessToken wxMpOauth2AccessToken = wxMpService.oauth2getAccessToken(code);
            if (StrUtil.isNotEmpty(wxMpOauth2AccessToken.getUnionId())) {
                accountInfo = this.getByAccountUnionId(wxMpOauth2AccessToken.getUnionId());
                if (accountInfo != null) {
                    return Result.failed("该微信账号已存在绑定账号");
                }
            }
            accountInfo = this.getByAccountOpenId(wxMpOauth2AccessToken.getOpenId());
            if (accountInfo != null) {
                return Result.failed("该微信账号已存在绑定账号");
            }
            accountInfo = new AccountInfo();
            getMpInfo(accountInfo, wxMpOauth2AccessToken, appId);
            return Result.ok(accountInfo);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return Result.failed(e.getError().getErrorMsg());
        }

    }

    /**
     * 根据openId 获取用户信息
     *
     * @param openId 微信openId
     * @return com.medusa.gruul.platform.api.entity.AccountInfo
     */
    private AccountInfo getByAccountOpenId(String openId) {
        return this.getBaseMapper().selectOne(new QueryWrapper<AccountInfo>().eq("open_id", openId));
    }

    /**
     * 根据unionId 获取用户信息
     *
     * @param unionId 平台互通unionId
     * @return com.medusa.gruul.platform.api.entity.AccountInfo
     */
    private AccountInfo getByAccountUnionId(String unionId) {
        return this.getBaseMapper().selectOne(new QueryWrapper<AccountInfo>().eq("union_id", unionId));
    }

    @Override
    public LoginAccountInfoDetailVo info() {
        CurPcUserInfoDto curUser = CurUserUtil.getPcRqeustAccountInfo();
        if (curUser == null) {
            throw new ServiceException("非法查询", SystemCode.DATA_NOT_EXIST.getCode());
        }
        AccountInfo accountInfo = this.getById(curUser.getUserId());
        AccountInfoVo loginInfoVo = getLoginInfoVo(accountInfo);
        LoginAccountInfoDetailVo vo = new LoginAccountInfoDetailVo();
        BeanUtils.copyProperties(loginInfoVo, vo);
        vo.setBalance(accountInfo.getBalance());
        vo.setAccountType(accountInfo.getAccountType());
        vo.setPhone(accountInfo.getPhone());
        return vo;
    }


    @Override
    public Result<AccountInfo> changeTie(String appId, String code, Long userId) {
        AccountInfo accountInfo = null;
        try {
            WxMpOAuth2AccessToken wxMpOauth2AccessToken = wxMpService.switchoverTo(appId).oauth2getAccessToken(code);
            //判断是否已存在绑定账号
            AccountInfo old = null;
            if (StrUtil.isNotEmpty(wxMpOauth2AccessToken.getUnionId())) {
                old = this.baseMapper.selectOne(new QueryWrapper<AccountInfo>().eq("union_id", wxMpOauth2AccessToken.getUnionId()).notIn("id", userId));
            }
            if (old == null) {
                old = this.baseMapper.selectOne(new QueryWrapper<AccountInfo>().eq("open_id", wxMpOauth2AccessToken.getUnionId()).notIn("id", userId));
            }
            if (old != null) {
                throw new ServiceException("该微信号已绑定账号");
            }
            accountInfo = this.baseMapper.selectById(userId);
            AccountInfo info = getMpInfo(accountInfo, wxMpOauth2AccessToken, appId);
            cachePlatformCurUserDtoOld(info, getAccountInfoVo(info));
            this.updateById(info);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return Result.failed(e.getMessage());
        } catch (ServiceException e) {
            return Result.failed(e.getMessage());
        }
        return Result.ok(accountInfo);
    }


    @Override
    public void phoneChangeTie(PhoneChangeTieDto phoneChangeTieDto) {
        sendCodeService.certificateCheck(phoneChangeTieDto.getOneCertificate(), phoneChangeTieDto.getOldPhone(), AuthCodeEnum.ACCOUNT_PHONE_IN_TIE.getType());
        AccountInfo phoneAccount = this.getByPhone(phoneChangeTieDto.getNewPhone());
        if (phoneAccount != null) {
            throw new ServiceException("换绑手机账号已被使用");
        }
        sendCodeService.certificateCheck(phoneChangeTieDto.getTwoCertificate(), phoneChangeTieDto.getNewPhone(), AuthCodeEnum.ACCOUNT_PHONE_IN_TIE.getType());
        AccountInfo accountInfo = this.getById(CurUserUtil.getPcRqeustAccountInfo().getUserId());
        if (accountInfo == null) {
            throw new ServiceException("无效token");
        }
        accountInfo.setId(accountInfo.getId());
        accountInfo.setPhone(phoneChangeTieDto.getNewPhone());
        this.baseMapper.updateById(accountInfo);
        removeAccountLogin(accountInfo);

    }

    @Override
    public void passChangeTie(PassChangeTieDto passChangeTieDto) {
        AccountInfo accountInfo = this.getById(CurUserUtil.getPcRqeustAccountInfo().getUserId());
        if (accountInfo == null) {
            throw new ServiceException("无效账号");
        }
        if (!accountInfo.getPhone().equals(passChangeTieDto.getPhone())) {
            throw new ServiceException("手机号不正确");
        }
        sendCodeService.certificateCheck(passChangeTieDto.getCertificate(), accountInfo.getPhone(), AuthCodeEnum.ACCOUNT_PASSWORD_IN_TIE.getType());
        removeAccountLogin(accountInfo);
        accountInfo.setPassword(passChangeTieDto.getPasswd());
        String salt = RandomUtil.randomString(6);
        accountInfo.setSalt(salt);
        accountInfo.setPasswd(SecureUtil.md5(accountInfo.getPassword().concat(salt)));
        this.baseMapper.updateById(accountInfo);


    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public AccountInfoVo accountRegister(AccountRegisterDto accountRegisterDto) {

        //校验state数据是否存在
        String jsonData = new PlatformRedis().get(accountRegisterDto.getCode().concat(":inside"));
        if (StrUtil.isEmpty(jsonData)) {
            throw new ServiceException("操作超时请重新注册");
        }
        Result result = JSONObject.parseObject(jsonData, Result.class);
        if (result.getCode() != CommonConstants.SUCCESS) {
            throw new ServiceException("授权异常:" + result.getMsg());
        }

        if (this.getByPhone(accountRegisterDto.getPhone()) != null) {
            throw new ServiceException("手机号已存在绑定账号");
        }
        //校验验证与手机号是否正确
        sendCodeService.certificateCheck(accountRegisterDto.getCertificate(), accountRegisterDto.getPhone(), AuthCodeEnum.CREATE_MINI_REGISTER.getType());
        AccountInfo accountInfo = ((JSONObject) result.getData()).toJavaObject(AccountInfo.class);
        accountInfo.setPhone(accountRegisterDto.getPhone());
        accountInfo.setPassword(accountRegisterDto.getPassword());
        accountInfo.setForbidStatus(CommonConstants.NUMBER_ZERO);
        String salt = RandomUtil.randomString(6);
        accountInfo.setSalt(salt);
        accountInfo.setPasswd(SecureUtil.md5(accountRegisterDto.getPassword().concat(salt)));
        accountInfo.setRegion(accountRegisterDto.getRegion());
        accountInfo.setAddress(accountRegisterDto.getAddress());
        accountInfo.setAccountType(CommonConstants.NUMBER_ZERO);
        //Todo 邀请码 删除
        this.save(accountInfo);

        CompletableFuture.runAsync(() -> {
            //通知客服注册成功
            JSONObject json = new JSONObject();
            json.put("first", "有一位新客户注册成功");
            List<String> keywords = CollectionUtil.newLinkedList(LocalDateTimeUtil.format(accountInfo.getCreateTime(), "yyyy-MM-dd HH:mm:ss"),
                    accountInfo.getNikeName(), accountInfo.getPhone(), accountInfo.getAddress());
            json.put("keyword", keywords);
            json.put("remark", "请及时安排人员跟进");
            systemConfService.sendKfmsg(CommonConstants.NUMBER_ONE, json);
        });

        return getLoginInfoVo(accountInfo);
    }

    @Override
    public AccountInfoVo login(LoginDto loginDto) {
        AccountInfoVo vo = null;
        switch (loginDto.getLoginType()) {
            case 1:
                vo = passwdLogin(loginDto.getPhone(), loginDto.getPassword());
                break;
            case 2:
                vo = phoneCodeLogin(loginDto.getPhone(), loginDto.getCertificate());
                break;
            case 3:
                vo = wxScanCodeLogin(loginDto.getCode());
                break;
            default:
                throw new ServiceException("非法登录请求");
        }
        updateAccountLastLoignTime(vo.getId());
        return vo;
    }

    /**
     * @param code code
     * @return
     */
    @Override
    public Result verifyStateResult(String code) {
        PlatformRedis platformRedis = new PlatformRedis();
        String jsonData = platformRedis.get(code);
        if (StrUtil.isEmpty(jsonData)) {
            return Result.failed("code已失效");
        }
        platformRedis.del(code);
        return JSONObject.parseObject(jsonData, Result.class);
    }

    @Override
    public void passwordRetrieve(PasswordRetrieveDto passwordRetrieveDto) {
        AccountInfo accountInfo = getByPhone(passwordRetrieveDto.getPhone());
        if (accountInfo == null) {
            throw new ServiceException("不存在该账号");
        }
        removeAccountLogin(accountInfo);
        //校验验证与手机号是否正确
        sendCodeService.certificateCheck(passwordRetrieveDto.getCertificate(), passwordRetrieveDto.getPhone(), AuthCodeEnum.ACCOUNT_FORGET_PASSWD.getType());

        accountInfo.setPassword(passwordRetrieveDto.getPasswd());
        String salt = RandomUtil.randomString(6);
        accountInfo.setSalt(salt);
        accountInfo.setPasswd(SecureUtil.md5(accountInfo.getPassword().concat(salt)));
        this.baseMapper.updateById(accountInfo);


    }

    /**
     * 清除指定用户缓存token
     *
     * @param accountInfo 用户数据
     */
    private void removeAccountLogin(AccountInfo accountInfo) {
        PlatformRedis platformRedis = new PlatformRedis();
        String key = SecureUtil.md5(accountInfo.getPhone()).concat(accountInfo.getSalt()).concat(accountInfo.getPasswd());
        String redisKey = RedisConstant.TOKEN_KEY.concat(key);
        platformRedis.del(redisKey);
    }

    /**
     * @param state
     * @return
     */
    private AccountInfoVo wxScanCodeLogin(String state) {
        String jsonData = new PlatformRedis().get(state);
        if (StrUtil.isEmpty(jsonData)) {
            throw new ServiceException("不存在该账号");
        }
        Result result = JSONObject.parseObject(jsonData, Result.class);
        if (result.getCode() != CommonConstants.SUCCESS) {
            throw new ServiceException(result.getMsg());
        }
        AccountInfo accountInfo = ((JSONObject) result.getData()).toJavaObject(AccountInfo.class);
        if (accountInfo.getId() == null) {
            throw new ServiceException("该微信账号未注册");
        }
        return getLoginInfoVo(accountInfo);
    }

    private AccountInfoVo phoneCodeLogin(String phone, String certificate) {
        AccountInfo accountInfo = this.getByPhone(phone);
        if (accountInfo == null) {
            throw new ServiceException("账号不存在");
        }
        sendCodeService.certificateCheck(certificate, phone, AuthCodeEnum.MINI_LOGIN.getType());
        return getLoginInfoVo(accountInfo);
    }

    /**
     * 手机号登录
     *
     * @param phone    手机号
     * @param password 密码
     * @return
     */
    private AccountInfoVo passwdLogin(String phone, String password) {
        AccountInfo accountInfo = this.getByPhone(phone);
        if (accountInfo == null) {
            throw new ServiceException("账号或密码错误");
        }
        String md5Pw = SecureUtil.md5(password.concat(accountInfo.getSalt()));
        if (!md5Pw.equals(accountInfo.getPasswd())) {
            throw new ServiceException("账号或密码错误");
        }
        return getLoginInfoVo(accountInfo);
    }

    /**
     * 封装返回用户登录信息
     *
     * @param accountInfo 账号信息
     * @return com.medusa.gruul.platform.model.vo.AccountInfoVo
     */
    @Override
    public AccountInfoVo getLoginInfoVo(AccountInfo accountInfo) {
        if (accountInfo.getForbidStatus().equals(CommonConstants.NUMBER_ONE)) {
            throw new ServiceException("账号当前无法登陆，请联系客服");
        }
        AccountInfoVo vo = new AccountInfoVo();
        BeanUtils.copyProperties(accountInfo, vo);
        //获取最后一次进入的店铺
        PlatformShopInfo shopInfo = platformShopInfoService.getById(accountInfo.getLastLoginShopId());
        if (shopInfo != null) {
            LoginShopInfoVo infoVo = platformShopInfoService.getLoginShopInfoVo(shopInfo);
            this.getShopAccountRoleInfo(accountInfo.getId(), infoVo);

            vo.setShopInfoVo(infoVo);
        }
        vo.setIsAgent(Boolean.FALSE);
        if (accountInfo.getMeAgentId() != null && accountInfo.getMeAgentId() > 0) {
            vo.setIsAgent(Boolean.TRUE);
        }
        //设置平台用户Token redisKey
        String userToken = cachePlatformCurUserDto(accountInfo, vo);
        vo.setToken(userToken);
        return vo;
    }

    @Override
    public void getShopAccountRoleInfo(Long accountId, LoginShopInfoVo infoVo) {
//        List<AuthUserRole> userRoles = authUserRoleService.getByUserIdAndTenantId(accountId, infoVo.getTenantId());
//        if (CollectionUtil.isNotEmpty(userRoles)) {
//            List<Long> roleIds = userRoles.stream().map(AuthUserRole::getRoleId).collect(Collectors.toList());
//            List<AuthRoleInfo> roleInfos = authRoleInfoService.getByRoleIds(roleIds);
//            List<RoleInfoVo> roleInfo = roleInfos.stream().map(obj -> BeanUtil.toBean(obj, RoleInfoVo.class)).collect(Collectors.toList());
//            infoVo.setRoleInfoVo(roleInfo);
//        }
    }


    @Override
    public Boolean affirmLessee(String token) {
        String tenantId = TenantContextHolder.getTenantId();
        if (StrUtil.isEmpty(tenantId)) {
            return Boolean.FALSE;
        }
        RedisManager redisManager = RedisManager.getInstance();
        String user = redisManager.get(token);
        if (StrUtil.isEmpty(user)) {
            return Boolean.FALSE;
        }
        CurUserDto curUserDto = JSON.parseObject(user, CurUserDto.class);
        PlatformShopInfo platformShopInfo = platformShopInfoService.getByTenantId(tenantId);
        if (!curUserDto.getUserId().equals(platformShopInfo.getAccountId().toString())) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public void emailChange(EmailChangeDto emailChangeDto) {
        AccountInfo accountInfo = this.getById(CurUserUtil.getPcRqeustAccountInfo().getUserId());
        if (ObjectUtil.isNull(accountInfo)) {
            throw new ServiceException("非法数据");
        }
        AccountInfo up = new AccountInfo();
        up.setId(accountInfo.getId());
        up.setEmail(emailChangeDto.getEmail());
        this.updateById(up);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchNote(BatchNoteDto noteDto) {
        List<AccountInfo> accountInfos = this.listByIds(noteDto.getAccountIds());
        if (CollectionUtil.isEmpty(accountInfos) || accountInfos.size() != noteDto.getAccountIds().size()) {
            throw new ServiceException("用户数据不一致");
        }
        for (AccountInfo accountInfo : accountInfos) {
            AccountInfo up = null;
            if (StrUtil.isEmpty(accountInfo.getCommentText())) {
                up = new AccountInfo();
                up.setCommentText(noteDto.getCommentText());
                up.setId(accountInfo.getId());

            } else if (noteDto.getIsCoverage().equals(CommonConstants.NUMBER_ONE)) {
                up = new AccountInfo();
                up.setCommentText(noteDto.getCommentText());
                up.setId(accountInfo.getId());
            }
            if (up != null) {
                this.updateById(up);
            }
        }
    }



    @Override
    public List<AccountInfo> getByAgentId(long agentId) {
        return this.list(new QueryWrapper<AccountInfo>().eq("agent_id", agentId));
    }



    @Override
    public Boolean verifyData(VerifyDataDto verifyDataDto) {
        CurUserDto curUser = CurUserUtil.getHttpCurUser();
        if (curUser == null) {
            throw new ServiceException("非法查询", SystemCode.DATA_NOT_EXIST.getCode());
        }
        AccountInfo accountInfo = this.getById(curUser.getUserId());
        Boolean flag = Boolean.FALSE;
        if (verifyDataDto.getOption().equals(CommonConstants.NUMBER_ONE)) {
            flag = accountInfo.getPhone().equals(verifyDataDto.getPhone());
        }
        return flag;
    }

    private AccountInfo getMpInfo(AccountInfo accountInfo, WxMpOAuth2AccessToken wxMpOauth2AccessToken, String appId) throws WxErrorException {
        WxMpUser wxMpUser = wxMpService.switchoverTo(appId).oauth2getUserInfo(wxMpOauth2AccessToken, "zh_CN");
        accountInfo.setRefreshToken(wxMpOauth2AccessToken.getRefreshToken());
        accountInfo.setAccessToken(wxMpOauth2AccessToken.getAccessToken());
        accountInfo.setAccessExpiresTime(DateUtils.timestampCoverLocalDateTime(wxMpOauth2AccessToken.getExpiresIn()));
        accountInfo.setOpenId(wxMpOauth2AccessToken.getOpenId());
        accountInfo.setCity(wxMpUser.getCity());
        accountInfo.setLanguage(wxMpUser.getLanguage());
        accountInfo.setNikeName(wxMpUser.getNickname());
        accountInfo.setAvatarUrl(wxMpUser.getHeadImgUrl());
        accountInfo.setGender(wxMpUser.getSex());
        accountInfo.setUnionId(StrUtil.isNotEmpty(wxMpUser.getUnionId()) ? wxMpUser.getUnionId() : null);
        accountInfo.setProvince(wxMpUser.getProvince());
        accountInfo.setCountry(wxMpUser.getCountry());
        accountInfo.setPrivilege(JSON.toJSONString(wxMpUser.getPrivileges()));
        return accountInfo;
    }

    private AccountInfo getByPhone(String username) {
        if (!ReUtil.isMatch(RegexConstants.REGEX_MOBILE_EXACT, username)) {
            throw new ServiceException("手机号错误", SystemCode.DATA_NOT_EXIST.getCode());
        }
        return this.baseMapper.selectOne(new QueryWrapper<AccountInfo>().eq("phone", username));
    }
}
