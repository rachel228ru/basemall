package com.medusa.gruul.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.medusa.gruul.platform.api.entity.PlatformShopInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 店铺信息表 Mapper 接口
 * </p>
 *
 * @author whh
 * @since 2020-03-06
 */
public interface PlatformShopInfoMapper extends BaseMapper<PlatformShopInfo> {

    /**
     * 获取id最大的店铺
     *
     * @return com.medusa.gruul.platform.api.entity.PlatformShopInfo
     */
    PlatformShopInfo getLastTenantId();


    /**
     * 管理台查询所有店铺
     *
     * @param page           分页对象
     * @param templateInfoId 模板id
     * @param search         搜索内容(用户手机号或者店铺昵称)
     * @param shopStatus     店铺状态
     * @param deployType     部署类型
     * @param versionId      版本id
     * @param packageId      套餐id
     * @param agentId        代理id1
     * @return com.medusa.gruul.platform.api.entity.PlatformShopInfo
     */
    IPage<PlatformShopInfo> amdinSelectPage(@Param("page") Page<PlatformShopInfo> page,
                                            @Param("templateInfoId") Integer templateInfoId,
                                            @Param("search") String search,
                                            @Param("shopStatus") Integer shopStatus,
                                            @Param("deployType") Integer deployType,
                                            @Param("versionId") Integer versionId,
                                            @Param("packageId") Long packageId, @Param("agentId") Long agentId);


    /**
     * 查询指定模板下的所有取消授权小程序和从未上传审核过的小程序的店铺
     *
     * @param shopTemplateId 店铺模板id
     * @return com.medusa.gruul.platform.api.entity.PlatformShopInfo
     */
    List<PlatformShopInfo> selectByCancelAuthMiniShops(@Param("shopTemplateId") Long shopTemplateId);

    /**
     * 查询指定代理名下的商户店铺
     *
     * @param page       分页
     * @param accountIds 用户id数组
     * @param search     店铺名称
     * @param shopStatus 店铺状态 1部署中 2正常 ，3已打烊，4禁用
     * @param packageIds 套餐id数组
     * @param startTime  创建开始时间
     * @param endTime    创建结束时间
     * @param orderBy    排序方式 1-升级 2-降序 默认降序
     * @return com.medusa.gruul.platform.api.entity.PlatformShopInfo
     */
    IPage<PlatformShopInfo> selectAccountShops(@Param("page") Page page, @Param("accountIds") List<Long> accountIds, @Param("search") String search,
                                               @Param("shopStatus") Integer shopStatus, @Param("packageIds") List<Long> packageIds, @Param("startTime") String startTime,
                                               @Param("endTime") String endTime, @Param("orderBy") Integer orderBy);
}
