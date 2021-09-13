package com.medusa.gruul.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.platform.api.entity.PlatformShopTemplateInfo;
import com.medusa.gruul.platform.model.dto.ShopTemplateCraeteOrUpdateDto;
import com.medusa.gruul.platform.model.vo.ShopTemplateListVo;

import java.util.List;

/**
 * <p>
 * 店铺模版表 服务类
 * </p>
 *
 * @author whh
 * @since 2020-03-06
 */
public interface IPlatformShopTemplateInfoService extends IService<PlatformShopTemplateInfo> {

    /**
     * 新增模板
     *
     * @param sendCodeDto com.medusa.gruul.platform.model.dto.ShopTemplateCraeteOrUpdateDto
     */
    void create(ShopTemplateCraeteOrUpdateDto sendCodeDto);

    /**
     * 更新模板信息
     *
     * @param sendCodeDto com.medusa.gruul.platform.model.dto.ShopTemplateCraeteOrUpdateDto
     */
    void edit(ShopTemplateCraeteOrUpdateDto sendCodeDto);

    /**
     * 分页查询店铺模板列表
     *
     * @param page 页数
     * @param size 条数
     * @param type 分类类型
     * @return
     */
    PageUtils<List<ShopTemplateListVo>> listQuery(Integer page, Integer size, Integer type);

    /**
     * 删除店铺模板
     *
     * @param id 店铺模板id
     */
    void deleteById(Long id);

    /**
     * 获取指定模板编号模板信息
     *
     * @param code 模板编号
     * @return com.medusa.gruul.platform.api.entity.PlatformShopTemplateInfo
     */
    PlatformShopTemplateInfo getByCode(String code);
}
