package com.medusa.gruul.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.platform.api.entity.SysShopPackage;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 店铺套餐 Mapper 接口
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
public interface SysShopPackageMapper extends BaseMapper<SysShopPackage> {

    /**
     * 获取指定模板id下最新版本的套餐
     *
     * @param templateId 模板id
     * @return com.medusa.gruul.platform.api.entity.SysShopPackage
     */
    List<SysShopPackage> selectByTemplateLastPackage(@Param("templateId") Long templateId);

}
