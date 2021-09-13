package com.medusa.gruul.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.platform.api.entity.DefaultValue;
import com.medusa.gruul.platform.api.entity.SysShopPackage;
import com.medusa.gruul.platform.model.dto.SysShopPackageUpdateDto;
import com.medusa.gruul.platform.model.vo.SysShopPackageListVo;
import com.medusa.gruul.platform.model.vo.SysShopPackageVo;
import com.medusa.gruul.platform.model.vo.agent.PackageInfoListVo;

import java.util.List;

/**
 * <p>
 * 店铺套餐 服务类
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
public interface ISysShopPackageService extends IService<SysShopPackage> {

    /**
     * 获取指定模板id下最新版本的套餐
     *
     * @param templateId 模板id
     * @return com.medusa.gruul.platform.api.entity.SysShopPackage
     */
    List<SysShopPackage> getByTemplateLastPackage(Long templateId);

    /**
     * 拷贝指定套餐版本
     *
     * @param packages          上个版本套餐
     * @param templateId        模板id
     * @param templateVersionId 模板版本id
     */
    void copy(List<SysShopPackage> packages, Long templateId, Long templateVersionId);

    /**
     * 生成指定默认值套餐
     *
     * @param defaultValue      版本默认套餐
     * @param templateId        模板id
     * @param templateVersionId 模板版本id
     */
    void generatePackage(DefaultValue defaultValue, Long templateId, Long templateVersionId);

    /**
     * 查询指定模板版本套餐数据
     *
     * @param templateVersionId 模板版本id
     * @return com.medusa.gruul.platform.model.vo.SysShopPackageListVo
     */
    List<SysShopPackageListVo> templateVersionPackages(Long templateVersionId);

    /**
     * 更新指定套餐信息
     *
     * @param sysShopPackageUpdateDto com.medusa.gruul.platform.model.dto.SysShopPackageUpdateDto
     */
    void updatePackageInfo(SysShopPackageUpdateDto sysShopPackageUpdateDto);

    /**
     * 查询指定套餐数据
     *
     * @param packageId com.medusa.gruul.platform.model.vo.SysShopPackageVo
     * @return
     */
    SysShopPackageVo findByPackageId(Long packageId);

    /**
     * 获取指定模板版本套餐
     *
     * @param shopTemplateDetailId 模板版本id
     * @return com.medusa.gruul.platform.api.entity.SysShopPackage
     */
    List<SysShopPackage> getByTemplateVersionId(Long shopTemplateDetailId);

    /**
     * 获取指定模板的所有套餐
     *
     * @param templateId 模板id
     * @return com.medusa.gruul.platform.api.entity.SysShopPackage
     */
    List<SysShopPackage> getByTeamplteId(Long templateId);


    /**
     * 获取指定模板的最新的套餐
     *
     * @param templateId 模板id
     * @return com.medusa.gruul.platform.api.entity.SysShopPackage
     */
    List<SysShopPackageVo> getByTeamplteIdLastPackage(Long templateId);

    /**
     * 代理后台获取套餐相关列表
     * @return com.medusa.gruul.platform.model.vo.agent.PackageInfoListVo
     */
    List<PackageInfoListVo> packageInfo();
}
