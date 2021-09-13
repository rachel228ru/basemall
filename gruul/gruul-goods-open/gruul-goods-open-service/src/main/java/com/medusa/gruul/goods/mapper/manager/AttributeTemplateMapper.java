package com.medusa.gruul.goods.mapper.manager;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.medusa.gruul.goods.api.entity.AttributeTemplate;
import com.medusa.gruul.goods.api.model.param.manager.AttributeTemplateParam;
import com.medusa.gruul.goods.api.model.vo.manager.AttributeTemplateSecondVo;
import com.medusa.gruul.goods.api.model.vo.manager.AttributeTemplateVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 属性模板 Mapper 接口
 * </p>
 *
 * @author kyl
 * @since 2019-10-06
 */
@Repository
public interface AttributeTemplateMapper extends BaseMapper<AttributeTemplate> {

    /**
     * 获取所有属性模板
     *
     * @return 所有属性模板
     */
    List<AttributeTemplateVo> queryAllAttributeTemplateList();

    /**
     * 获取属性模板子属性
     *
     * @param parentId 模板id
     * @return 模板属性list信息
     */
    List<AttributeTemplateSecondVo> queryByParentId(Long parentId);

    /**
     * 获取属性模板详情
     *
     * @param id 模板id
     * @return 属性模板信息
     */
    AttributeTemplateVo queryByPrimaryKey(Long id);

    /**
     * 获取属性模板列表
     *
     * @param page
     * @param attributeTemplateParam
     * @return 属性模板list信息
     */
    List<AttributeTemplateVo> queryAttributeTemplateList(IPage page, @Param("attributeTemplateParam") AttributeTemplateParam attributeTemplateParam);


    /**
     * 获取默认属性模板
     *
     * @return 获取默认属性模板
     */
    AttributeTemplateVo queryDefaultAttributeTemplateList();
}
