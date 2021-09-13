package com.medusa.gruul.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.platform.api.entity.PlatformShopTemplateDetailMinis;
import com.medusa.gruul.platform.model.dto.MiniCodeVersionDto;

import java.util.List;

/**
 * <p>
 * 店铺模版详情小程序版本子表 服务类
 * </p>
 *
 * @author whh
 * @since 2020-10-14
 */
public interface IPlatformShopTemplateDetailMinisService extends IService<PlatformShopTemplateDetailMinis> {

    /**
     * 新增模板详细对应的多个小程序版本数据
     *
     * @param miniCodeVersions     小程序子数据
     * @param shopTemplateDetailId 店铺模版详情表id
     */
    void save(List<MiniCodeVersionDto> miniCodeVersions, Long shopTemplateDetailId);

    /**
     * 更新某个模板详细对应的多个小程序版本数据
     *
     * @param miniCodeVersions     小程序子数据
     * @param shopTemplateDetailId 店铺模版详情表id
     */
    void update(List<MiniCodeVersionDto> miniCodeVersions, Long shopTemplateDetailId);

    /**
     * 获取某个模板详情下的所有小程序版本
     *
     * @param shopTemplateDetailId 店铺模版详情表id
     * @return com.medusa.gruul.platform.api.entity.PlatformShopTemplateDetailMinis
     */
    List<PlatformShopTemplateDetailMinis> getByShopTemplateDetailId(Long shopTemplateDetailId);

    /**
     * 获取店铺详情下的某个模板库代码id对应的数据源
     *
     * @param shopTemplateDetailId 店铺模版详情表id
     * @param templateId           模板库id
     * @return com.medusa.gruul.platform.api.entity.PlatformShopTemplateDetailMinis
     */
    PlatformShopTemplateDetailMinis getByShopTemplateDetailIdAndCodeTemplateId(Long shopTemplateDetailId, Long templateId);
}
