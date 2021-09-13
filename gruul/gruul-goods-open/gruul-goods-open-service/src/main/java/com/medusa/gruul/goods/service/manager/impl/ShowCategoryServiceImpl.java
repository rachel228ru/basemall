package com.medusa.gruul.goods.service.manager.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.goods.api.entity.ProductShowCategory;
import com.medusa.gruul.goods.api.entity.ShowCategory;
import com.medusa.gruul.goods.api.model.dto.manager.ShowCategoryDto;
import com.medusa.gruul.goods.api.model.dto.manager.ShowCategorySecondDto;
import com.medusa.gruul.goods.api.model.param.manager.ShowCategoryParam;
import com.medusa.gruul.goods.api.model.vo.manager.ShowCategoryVo;
import com.medusa.gruul.goods.mapper.manager.ProductShowCategoryMapper;
import com.medusa.gruul.goods.mapper.manager.ShowCategoryMapper;
import com.medusa.gruul.goods.service.manager.IShowCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 产展示品分类 服务实现类
 * </p>
 *
 * @author alan
 * @since 2019-09-03
 */
@Service
public class ShowCategoryServiceImpl extends ServiceImpl<ShowCategoryMapper, ShowCategory> implements IShowCategoryService {

    @Autowired
    private ShowCategoryMapper showCategoryMapper;

    @Autowired
    private ProductShowCategoryMapper productShowCategoryMapper;

    /**
     * 获取展示分类分页信息
     *
     * @param showCategoryParam
     * @return 展示分类分页对象
     */
    @Override
    public IPage<ShowCategoryVo> getShowCategoryList(ShowCategoryParam showCategoryParam) {
        IPage<ShowCategoryVo> page = new Page<>(showCategoryParam.getCurrent(), showCategoryParam.getSize());
        return page.setRecords(this.baseMapper.queryShowCategoryList(page, showCategoryParam));
    }

    /**
     * 获取所有展示分类信息
     *
     * @param showCategoryParam
     * @return 所有展示分类list对象
     */
    @Override
    public List<ShowCategoryVo> getAllShowCategoryList(ShowCategoryParam showCategoryParam) {
        return this.baseMapper.queryAllShowCategoryList(showCategoryParam);
    }

    /**
     * 根据id获取展示分类信息
     *
     * @param id
     * @return 展示分类对象
     */
    @Override
    public ShowCategoryVo getShowCategoryById(String id) {
        return showCategoryMapper.getShowCategoryById(id);
    }

    /**
     * 一级展示分类新增，先判断大分类名称是否已存在
     *
     * @param showCategoryDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addShowCategory(ShowCategoryDto showCategoryDto) {
        if (showCategoryDto.getSort() == null) {
            showCategoryDto.setSort(0);
        }
        //保存大分类
        ShowCategory showCategory = ifExistShowCategoryName(showCategoryDto);
        int insert = showCategoryMapper.insert(showCategory);
        if (insert == 0) {
            throw new ServiceException("新增失败！", SystemCode.DATA_ADD_FAILED.getCode());
        }
    }

    /**
     * 二级展示分类新增，先判断大分类下面名称是否已存在
     *
     * @param showCategorySecondDtos
     */
    @Override
    public void addSecondList(List<ShowCategorySecondDto> showCategorySecondDtos) {
        showCategorySecondDtos.stream().forEach(bean -> {
            if (bean.getSort() == null) {
                bean.setSort(0);
            }
            ShowCategory showCategorySearch = showCategoryMapper.selectOne(new QueryWrapper<ShowCategory>()
                    .eq("name", bean.getName())
                    .eq("parent_id", bean.getParentId()));
            if (!BeanUtil.isEmpty(showCategorySearch)) {
                throw new ServiceException(showCategorySearch.getName() + "分类名称已存在！", SystemCode.DATA_EXISTED.getCode());
            }
            ShowCategory showCategorySecond = bean.coverShowCategory();
            int secondInsert = showCategoryMapper.insert(showCategorySecond);
            if (secondInsert == 0) {
                throw new ServiceException("新增失败！", SystemCode.DATA_UPDATE_FAILED.getCode());
            }
        });
    }

    /**
     * 一级展示分类修改，先判断修改后的大分类名称是否已存在
     *
     * @param showCategoryDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShowCategory(ShowCategoryDto showCategoryDto) {
        //判断原分类是否已被删除
        ShowCategory oldShowCategory = showCategoryMapper.selectById(showCategoryDto.getId());
        if (BeanUtil.isEmpty(oldShowCategory)) {
            throw new ServiceException("该分类不存在！", SystemCode.DATA_NOT_EXIST_CODE);
        }
        ShowCategory showCategorySearch = showCategoryMapper.selectOne(new QueryWrapper<ShowCategory>()
                .eq("name", showCategoryDto.getName())
                .eq("parent_id", showCategoryDto.getParentId())
                .eq("sale_mode", showCategoryDto.getSaleMode())
                .ne("id", showCategoryDto.getId()));
        if (!BeanUtil.isEmpty(showCategorySearch)) {
            throw new ServiceException("分类名称已存在！", SystemCode.DATA_EXISTED.getCode());
        }
        //大类保存
        ShowCategory showCategory = showCategoryDto.coverShowCategory();
        int update = showCategoryMapper.updateById(showCategory);
        if (update == 0) {
            throw new ServiceException("修改失败！", SystemCode.DATA_UPDATE_FAILED.getCode());
        }
    }

    /**
     * 展示分类删除 删除分类信息同时删除关联商品表分类信息
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteShowCategoryList(Long id) {
        ShowCategory showCategory = showCategoryMapper.selectById(id);
        if (BeanUtil.isEmpty(showCategory)) {
            throw new ServiceException("该分类不存在！", SystemCode.DATA_EXISTED.getCode());
        }
        List<Long> ids = new ArrayList<>();
        //判断分类是否为大分类
        Integer num = productShowCategoryMapper.selectCount(new QueryWrapper<ProductShowCategory>().eq("parent_id", id)
                .or()
                .eq("show_category_id", id));
        if (num > 0){
            throw new ServiceException("该分类下有商品，暂时无法删除");
        }
        if (showCategory.getParentId() == 0) {
            List<ShowCategory> showCategories =
                    showCategoryMapper.selectList(new QueryWrapper<ShowCategory>().eq("parent_id", id));
            ids = showCategories.stream().map(ShowCategory::getId).collect(Collectors.toList());
        }
        //有子类删除子类
        if (!CollectionUtil.isEmpty(ids)) {
            showCategoryMapper.delete(new QueryWrapper<ShowCategory>().eq("parent_id", id));
        }
        //删除自身分类
        showCategoryMapper.deleteById(id);
        //清空商品展示分类表该分类的信息
        ids.add(id);
        productShowCategoryMapper.delete(new QueryWrapper<ProductShowCategory>().in("show_category_id", ids));

    }

    /**
     * 修改展示分类排序
     *
     * @param showCategoryDtos
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShowCategorySort(List<ShowCategoryDto> showCategoryDtos) {
        showCategoryDtos.stream().forEach(showCategoryDto -> {
            boolean update = new LambdaUpdateChainWrapper<>(showCategoryMapper)
                    .eq(ShowCategory::getId, showCategoryDto.getId())
                    .set(ShowCategory::getSort, showCategoryDto.getSort())
                    .update();
            if (!update) {
                throw new ServiceException("修改失败！", SystemCode.DATA_UPDATE_FAILED.getCode());
            }
        });
    }

    /**
     * 判断查询分类名称是否已存在
     *
     * @param showCategoryDto
     * @return sortingCategory
     */
    private ShowCategory ifExistShowCategoryName(ShowCategoryDto showCategoryDto) {
        ShowCategory showCategory = this.baseMapper.selectOne(new QueryWrapper<ShowCategory>().eq("name", showCategoryDto.getName()).eq("sale_mode", showCategoryDto.getSaleMode()));
        if (!BeanUtil.isEmpty(showCategory)) {
            throw new ServiceException("分类名称已存在！", SystemCode.DATA_EXISTED.getCode());
        }
        showCategory = showCategoryDto.coverShowCategory();
        return showCategory;
    }

}
