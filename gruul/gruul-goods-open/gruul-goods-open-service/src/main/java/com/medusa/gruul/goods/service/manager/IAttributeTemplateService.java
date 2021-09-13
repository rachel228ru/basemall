package com.medusa.gruul.goods.service.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.goods.api.entity.AttributeTemplate;
import com.medusa.gruul.goods.api.model.dto.manager.AttributeTemplateDto;
import com.medusa.gruul.goods.api.model.param.manager.AttributeTemplateParam;
import com.medusa.gruul.goods.api.model.vo.manager.AttributeTemplateVo;

import java.util.List;

/**
 * <p>
 * 属性模板 服务类
 * </p>
 *
 * @author kyl
 * @since 2019-10-06
 */
public interface IAttributeTemplateService extends IService<AttributeTemplate> {

    /**
     * 获取所有属性模板
     *
     * @return
     */
    List<AttributeTemplateVo> getAllAttributeTemplateList();

    /**
     * 属性模板分页列表
     *
     * @param attributeTemplateParam
     * @return
     */
    IPage<AttributeTemplateVo> getAttributeTemplateList(AttributeTemplateParam attributeTemplateParam);

    /**
     * 新增属性模板
     *
     * @param attributeTemplateDto
     */
    void addAttributeTemplate(AttributeTemplateDto attributeTemplateDto);

    /**
     * 修改属性模板
     *
     * @param attributeTemplateDto
     */
    void updateAttributeTemplate(AttributeTemplateDto attributeTemplateDto);

    /**
     * 删除属性模板
     *
     * @param ids
     */
    void deleteAttributeTemplateList(Long[] ids);

    /**
     * 删除属性模板子属性
     *
     * @param id
     */
    void deleteAttributeTemplateChild(Long id);
}
