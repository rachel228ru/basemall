package com.medusa.gruul.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.platform.api.entity.PlatformShopTemplateDetail;
import com.medusa.gruul.platform.model.dto.ShopTemplateVersionOptionDto;
import com.medusa.gruul.platform.model.vo.SkipUrlVo;

import java.util.List;

/**
 * <p>
 * 店铺模版详情表 服务类
 * </p>
 *
 * @author whh
 * @since 2020-03-06
 */
public interface IPlatformShopTemplateDetailService extends IService<PlatformShopTemplateDetail> {

    /**
     * 创建模板版本
     *
     * @param dto com.medusa.gruul.platform.model.dto.ShopTemplateVersionOptionDto
     */
    void create(ShopTemplateVersionOptionDto dto);

    /**
     * 更新模板版本
     *
     * @param dto com.medusa.gruul.platform.model.dto.ShopTemplateVersionOptionDto
     */
    void edit(ShopTemplateVersionOptionDto dto);

    /**
     * 删除指定模板版本id
     *
     * @param id 模板版本id
     */
    void deleteById(Long id);

    /**
     * 获取指定模板id下的所有模板版本详情
     *
     * @param infoIds ids数组
     * @return
     */
    List<PlatformShopTemplateDetail> getByShopTemplateInfoIds(List<Long> infoIds);

    /**
     * 获取指定模板的最新一条版本
     *
     * @param shopTemplateId 模板id
     * @return com.medusa.gruul.platform.api.entity.PlatformShopTemplateDetail
     */
    PlatformShopTemplateDetail getByShopTeamplteNewDetail(Long shopTemplateId);

    /**
     * 获取指定正在使用某个基础库的模板详细
     *
     * @param librariesId 基础课id
     * @return com.medusa.gruul.platform.api.entity.PlatformShopTemplateDetail
     */
    List<PlatformShopTemplateDetail> getByLibrariesId(Long librariesId);

    /**
     * 过滤删除条件获取模板版本
     *
     * @param versionId 模板本地id
     * @return com.medusa.gruul.platform.api.entity.PlatformShopTemplateDetail
     */
    PlatformShopTemplateDetail getByFilterById(Long versionId);

    /**
     * 获取当前版本配置的跳转地址
     *
     * @return com.medusa.gruul.platform.model.vo.SkipUrlVo
     */
    SkipUrlVo getSkipUrl();

}
