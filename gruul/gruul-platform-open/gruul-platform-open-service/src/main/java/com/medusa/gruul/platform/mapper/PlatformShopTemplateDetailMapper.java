package com.medusa.gruul.platform.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.platform.api.entity.PlatformShopTemplateDetail;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 店铺模版详情表 Mapper 接口
 * </p>
 *
 * @author whh
 * @since 2020-03-06
 */
public interface PlatformShopTemplateDetailMapper extends BaseMapper<PlatformShopTemplateDetail> {

    /**
     * 获取指定模板下最大的模板版本
     *
     * @param shopTemplateId 模板id
     * @return com.medusa.gruul.platform.api.entity.PlatformShopTemplateDetail
     */
    PlatformShopTemplateDetail selectShopTeamplteNewDetail(@Param("shopTemplateId") Long shopTemplateId);

    /**
     * 过滤删除条件获取模板版本
     *
     * @param versionId 模板本地id
     * @return com.medusa.gruul.platform.api.entity.PlatformShopTemplateDetail
     */
    @SqlParser(filter = true)
    PlatformShopTemplateDetail selectByFilterById(Long versionId);
}
