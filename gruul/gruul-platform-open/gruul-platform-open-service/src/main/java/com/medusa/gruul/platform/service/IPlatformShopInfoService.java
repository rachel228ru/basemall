package com.medusa.gruul.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.api.entity.PlatformShopInfo;
import com.medusa.gruul.platform.api.model.dto.ShopConfigDto;
import com.medusa.gruul.platform.api.model.dto.ShopInfoDto;
import com.medusa.gruul.platform.api.model.dto.ShopPackageFunctionDto;
import com.medusa.gruul.platform.api.model.vo.PayInfoVo;
import com.medusa.gruul.platform.model.dto.ConsoleUpdateDto;
import com.medusa.gruul.platform.model.dto.PayInfoUpdataDto;
import com.medusa.gruul.platform.model.dto.ShopInfoAdminCreateDto;
import com.medusa.gruul.platform.model.dto.ShopInfoConsoleCreateDto;
import com.medusa.gruul.platform.model.dto.agent.AgentCreateShopDto;
import com.medusa.gruul.platform.model.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 店铺信息表 服务类
 * </p>
 *
 * @author whh
 * @since 2020-03-06
 */
public interface IPlatformShopInfoService extends IService<PlatformShopInfo> {


    /**
     * 管理台创建店铺
     *
     * @param dto com.medusa.gruul.platform.model.dto.ShopInfoAdminCreateDto
     */
    void adminCreate(ShopInfoAdminCreateDto dto);

    /**
     * 商家控制台创建店铺
     *
     * @param dto com.medusa.gruul.platform.model.dto.ShopInfoConsoleCreateDto
     * @return 平台店铺id
     */
    Long consoleCreate(ShopInfoConsoleCreateDto dto);

    /**
     * 控制台查询所属用户的所有店铺
     *
     * @param page 分页对象
     * @return com.medusa.gruul.platform.model.vo.ShopViewListVo
     */
    PageUtils<List<ShopViewListVo>> consoleList(Page page);

    /**
     * 商家自己删除店铺
     *
     * @param shopId 店铺id
     */
    void consoleDel(Long shopId);

    /**
     * 店铺设置
     *
     * @param dto    com.medusa.gruul.platform.model.dto.ConsoleUpdateDto
     * @param shopId 店铺id
     */
    void consoleUpdate(ConsoleUpdateDto dto, Long shopId);

    /**
     * 校验当前店铺真实性(店铺校验,用户校验,用户对比校验)
     *
     * @param shopId 店铺id
     * @return
     */
    PlatformShopInfo check(Long shopId);

    /**
     * 打烊或营业
     *
     * @param shopId 店铺id
     */
    void closeOrOpen(Long shopId);

    /**
     * 进入指定店铺
     *
     * @param shopId 店铺id
     * @return
     */
    LoginShopInfoVo joinShop(Long shopId);

    /**
     * 获取指定店铺登录所需数据
     *
     * @param shopInfo com.medusa.gruul.platform.api.entity.PlatformShopInfo
     * @return com.medusa.gruul.platform.model.vo.LoginShopInfoVo
     */
    LoginShopInfoVo getLoginShopInfoVo(PlatformShopInfo shopInfo);

    /**
     * 根据租户id获取店铺数据
     *
     * @param tenantId 租户id
     * @return com.medusa.gruul.platform.api.entity.PlatformShopInfo
     */
    PlatformShopInfo getByTenantId(String tenantId);



    /**
     * 进入指定店铺
     *
     * @param shopId 店铺id
     * @return com.medusa.gruul.platform.model.vo.AccountInfoVo
     */
    AccountInfoVo adminJoinShop(Long shopId);

    /**
     * 校验指定租户是否存在
     *
     * @param tenantId 租户id
     * @return com.medusa.gruul.platform.api.entity.PlatformShopInfo
     */
    PlatformShopInfo checkCurrentTenantId(String tenantId);

    /**
     * 获取支付配置
     *
     * @param type     1-默认加密  2-明文数据需带上code 3-内部调用
     * @param code     扫码返回的code
     * @param tenantId
     * @return com.medusa.gruul.platform.api.model.vo.PayInfoVo
     */
    PayInfoVo payInfo(Integer type, String code, String tenantId);

    /**
     * 上传支付证书
     *
     * @param file   文件
     * @param shopId 店铺id
     * @return 上传路径
     */
    String uploadCertificate(MultipartFile file, Long shopId);


    /**
     * 更新支付配置信息
     *
     * @param payInfoUpdataDto com.medusa.gruul.platform.model.dto.PayInfoUpdataDto
     */
    void payInfoUpdata(PayInfoUpdataDto payInfoUpdataDto);

    /**
     * 获取商户的所有店铺数据
     *
     * @param accountId 商户id
     * @return com.medusa.gruul.platform.api.entity.PlatformShopInfo
     */
    List<PlatformShopInfo> getByAccountIdShops(Long accountId);

    /**
     * 获取店铺基本信息
     *
     * @return com.medusa.gruul.platform.model.vo.ShopInfoVo
     */
    ShopInfoVo shopInfo();

    /**
     * 获取使用了使用模板详情id的店铺并指定状态
     *
     * @param id     模版版本id
     * @param status 店铺状态
     * @return com.medusa.gruul.platform.api.entity.PlatformShopInfo
     */
    List<PlatformShopInfo> getByPlatformShopTemplateDetailIdAndStatus(Long id, Integer status);


    /**
     * 获取使用了使用模板详情id的店铺
     *
     * @param shopTemplateDetailId 模板详情id
     * @return com.medusa.gruul.platform.api.entity.PlatformShopInfo
     */
    List<PlatformShopInfo> getByPlatformShopTemplateDetailId(Long shopTemplateDetailId);

    /**
     * 获取店铺配置信息
     * 远程调用接口使用
     *
     * @param tenantId 租户id
     * @return com.medusa.gruul.platform.api.model.dto.ShopConfigDto
     */
    ShopConfigDto getShopConfig(String tenantId);


    /**
     * s1.0.7需求未授权和取消授权）的店铺PC端直接使用最新版；
     *
     * @param tenantId 租户id
     */
    void setShopPcNewVersion(String tenantId);

    /**
     * 查出所有未授权小程序店铺
     *
     * @param shopTemplateId
     * @return com.medusa.gruul.platform.api.entity.PlatformShopInfo
     */
    List<PlatformShopInfo> getByNotAuthMiniShops(Long shopTemplateId);

    /**
     * 查询指定模板下的所有取消授权小程序和从未上传审核过的小程序的店铺
     *
     * @param shopTemplateId 模板id
     * @return com.medusa.gruul.platform.api.entity.PlatformShopInfo
     */
    List<PlatformShopInfo> getByCancelAuthMiniShops(Long shopTemplateId);




    /**
     * 重新生产店所有默认值
     *
     * @param tenantId 租户id
     */
    void defaultRepair(String tenantId);

    /**
     * 获取指定租户店铺信息
     *
     * @param tenantId 租户id
     * @return com.medusa.gruul.platform.api.model.dto.ShopInfoDto
     */
    Result<ShopInfoDto> getShopInfo(String tenantId);

    /**
     * 获取店铺当天版本套餐
     *
     * @return com.medusa.gruul.platform.model.vo.SysShopPackageVo
     */
    List<SysShopPackageVo> getCurrentPackage();


    /**
     * 检测当日到期店铺
     */
    void validateShopPastDue();

    /**
     * 套餐过期
     *
     * @param platefromInfoId 平台id
     */
    void packageDueReceive(Integer platefromInfoId);

    /**
     * 根据租户id获取店铺当前使用的套餐功能状态
     *
     * @param tenantId 租户id
     * @return com.medusa.gruul.platform.api.model.dto.ShopPackageFunctionDto
     */
    Result<ShopPackageFunctionDto> getShopFunction(String tenantId);

    /**
     * 获取店铺配置信息
     * 远程调用接口使用
     *
     * @param appId 小程序appId
     * @return com.medusa.gruul.platform.api.model.dto.ShopConfigDto
     */
    ShopConfigDto getShopConfigAndAppId(String appId);

    /**
     * 获取所有店铺租户id
     *
     * @return 租户列表
     */
    List<String> getShopsAll();


    /**
     * 代理为商户创建店铺
     *
     * @param agentCreateShopDto com.medusa.gruul.platform.model.dto.agent.AgentCreateShopDto
     */
    void agentCreateShop(AgentCreateShopDto agentCreateShopDto);

    /**
     * 获取指定用户名下的店铺总数
     *
     * @param agentId 代理id
     * @return 店铺总数
     */
    Integer getByAccountIdShopCount(Long agentId);

    /**
     * 缓存店铺基本信息，供getTenantIdShopInfo工具类使用
     */
    void initShopCache();

}
