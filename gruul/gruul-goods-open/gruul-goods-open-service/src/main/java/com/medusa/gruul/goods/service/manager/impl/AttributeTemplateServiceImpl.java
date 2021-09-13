package com.medusa.gruul.goods.service.manager.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.goods.api.entity.AttributeTemplate;
import com.medusa.gruul.goods.api.entity.Product;
import com.medusa.gruul.goods.api.model.dto.manager.AttributeTemplateDto;
import com.medusa.gruul.goods.api.model.dto.manager.AttributeTemplateSecondDto;
import com.medusa.gruul.goods.api.model.param.manager.AttributeTemplateParam;
import com.medusa.gruul.goods.api.model.vo.manager.AttributeTemplateSecondVo;
import com.medusa.gruul.goods.api.model.vo.manager.AttributeTemplateVo;
import com.medusa.gruul.goods.mapper.manager.AttributeTemplateMapper;
import com.medusa.gruul.goods.service.api.IApiProductService;
import com.medusa.gruul.goods.service.manager.IAttributeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 属性模板 服务实现类
 * </p>
 *
 * @author kyl
 * @since 2019-10-06
 */
@Service
public class AttributeTemplateServiceImpl extends ServiceImpl<AttributeTemplateMapper, AttributeTemplate> implements IAttributeTemplateService {

    @Autowired
    private AttributeTemplateMapper attributeTemplateMapper;

    @Autowired
    private IApiProductService apiProductService;

    /**
     * 获取所有属性模板
     *
     * @return 所有属性模版对象
     */
    @Override
    public List<AttributeTemplateVo> getAllAttributeTemplateList() {
        return attributeTemplateMapper.queryAllAttributeTemplateList();
    }

    /**
     * 属性模板分页列表
     *
     * @param attributeTemplateParam
     * @return 属性模版分页对象
     */
    @Override
    public IPage<AttributeTemplateVo> getAttributeTemplateList(AttributeTemplateParam attributeTemplateParam) {
        IPage<AttributeTemplateVo> page = new Page<>(attributeTemplateParam.getCurrent(), attributeTemplateParam.getSize());
        return page.setRecords(attributeTemplateMapper.queryAttributeTemplateList(page, attributeTemplateParam));
    }

    /**
     * 新增属性模板
     *
     * @param attributeTemplateDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAttributeTemplate(AttributeTemplateDto attributeTemplateDto) {
        //判断模板名称是否重复
        AttributeTemplate sameAttributeTemplate = attributeTemplateMapper.selectOne(new QueryWrapper<AttributeTemplate>()
                .eq("name", attributeTemplateDto.getName())
                .eq("parent_id", 0L)
        );
        if (!BeanUtil.isEmpty(sameAttributeTemplate)) {
            throw new ServiceException("模板名称已存在！", SystemCode.DATA_EXISTED_CODE);
        }
        //保存属性模板
        AttributeTemplate attributeTemplate = attributeTemplateDto.coverAttributeTemplate();
        int insert = attributeTemplateMapper.insert(attributeTemplate);
        if (insert == 0) {
            throw new ServiceException("新增失败", SystemCode.DATA_ADD_FAILED_CODE);
        }
        //保存属性模板子属性
        List<AttributeTemplateSecondDto> attributeTemplateSecondDtos = attributeTemplateDto.getAttributeTemplates();
        attributeTemplateSecondDtos.stream().forEach(attributeTemplateSecondDto -> {
            attributeTemplateSecondDto.setParentId(attributeTemplate.getId());
            AttributeTemplate child = attributeTemplateSecondDto.coverAttributeTemplate();
            int insertChild = attributeTemplateMapper.insert(child);
            if (insertChild == 0) {
                throw new ServiceException("新增失败", SystemCode.DATA_ADD_FAILED_CODE);
            }
        });
    }

    /**
     * 修改属性模板
     *
     * @param attributeTemplateDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAttributeTemplate(AttributeTemplateDto attributeTemplateDto) {
        //判断原模板是否已被删除
        AttributeTemplate oldAttributeTemplate = attributeTemplateMapper.selectById(attributeTemplateDto.getId());
        if (BeanUtil.isEmpty(oldAttributeTemplate)) {
            throw new ServiceException("模板已被删除！", SystemCode.DATA_NOT_EXIST_CODE);
        }
        //判断模板名称是否重复
        AttributeTemplate sameAttributeTemplate = attributeTemplateMapper.selectOne(new QueryWrapper<AttributeTemplate>()
                .eq("name", attributeTemplateDto.getName())
                .eq("parent_id", 0L)
                .ne("id", attributeTemplateDto.getId())
        );
        if (!BeanUtil.isEmpty(sameAttributeTemplate)) {
            throw new ServiceException("模板名称已存在！", SystemCode.DATA_EXISTED_CODE);
        }
        //获取属性模板原子属性
        List<AttributeTemplateSecondVo> children = attributeTemplateMapper.queryByParentId(attributeTemplateDto.getId());
        List<Long> ids = children.stream().map(AttributeTemplateSecondVo::getId).collect(Collectors.toList());
        //删除属性模板子属性
        if (CollectionUtil.isNotEmpty(ids)) {
            attributeTemplateMapper.deleteBatchIds(ids);
        }

        //更新属性模板子属性
        List<AttributeTemplateSecondDto> newChildren = attributeTemplateDto.getAttributeTemplates();
        newChildren.stream().forEach(attributeTemplateSecondDto -> {
            attributeTemplateSecondDto.setParentId(attributeTemplateDto.getId());
            AttributeTemplate child = attributeTemplateSecondDto.coverAttributeTemplate();
            int updateChild = attributeTemplateMapper.insert(child);
            if (updateChild == 0) {
                throw new ServiceException("修改失败！", SystemCode.DATA_UPDATE_FAILED_CODE);
            }
        });
        //更新属性模板
        AttributeTemplate attributeTemplate = attributeTemplateDto.coverAttributeTemplate();
        int update = attributeTemplateMapper.updateById(attributeTemplate);
        if (update == 0) {
            throw new ServiceException("修改失败！", SystemCode.DATA_UPDATE_FAILED_CODE);
        }
    }

    /**
     * 删除属性模板
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAttributeTemplateList(Long[] ids) {
        for (int i = 0; i < ids.length; i++) {
            Integer integer = apiProductService.getBaseMapper().selectCount(new QueryWrapper<Product>().eq("attribute_id", ids[i]));
            if (integer > 0 ){
                throw new ServiceException("该属性模板下已有商品，不允许删除");
            }

        }
        //删除属性模板的子属性
        attributeTemplateMapper.delete(new QueryWrapper<AttributeTemplate>().in("parent_id", Arrays.asList(ids)));
        //删除属性模板
        attributeTemplateMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 删除属性模板子属性
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAttributeTemplateChild(Long id) {
        //获取属性模板子属性信息
        AttributeTemplate child = attributeTemplateMapper.selectById(id);
        //删除属性模板子属性
        attributeTemplateMapper.deleteById(id);
        //删除子属性后获取属性模板信息
        AttributeTemplateVo attributeTemplateVo = attributeTemplateMapper.queryByPrimaryKey(child.getParentId());
        //判断属性模板子属性是否为空
        if (CollectionUtil.isEmpty(attributeTemplateVo.getAttributeTemplates())) {
            //子属性为空时删除模板
            attributeTemplateMapper.deleteById(attributeTemplateVo.getId());
        }

    }
}
