package com.medusa.gruul.goods.service.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.goods.api.entity.ShowCategory;
import com.medusa.gruul.goods.api.model.dto.manager.ShowCategoryDto;
import com.medusa.gruul.goods.api.model.dto.manager.ShowCategorySecondDto;
import com.medusa.gruul.goods.api.model.param.manager.ShowCategoryParam;
import com.medusa.gruul.goods.api.model.vo.manager.ShowCategoryVo;

import java.util.List;

/**
 * <p>
 * 产展示品分类 服务类
 * </p>
 *
 * @author alan
 * @since 2019-09-03
 */
public interface IShowCategoryService extends IService<ShowCategory> {

    /**
     * 查询列表
     *
     * @param showCategoryParam
     * @return 分页对象
     */
    IPage<ShowCategoryVo> getShowCategoryList(ShowCategoryParam showCategoryParam);

    /**
     * 查询所有展示分类
     *
     * @param showCategoryParam
     * @return 所有ShowCategoryVo对象
     */
    List<ShowCategoryVo> getAllShowCategoryList(ShowCategoryParam showCategoryParam);

    /**
     * 查询单个展示分类
     *
     * @param id
     * @return ShowCategoryVo对象
     */
    ShowCategoryVo getShowCategoryById(String id);

    /**
     * 新增一级展示分类
     *
     * @param showCategoryDto
     */
    void addShowCategory(ShowCategoryDto showCategoryDto);

    /**
     * 新增二级展示分类
     *
     * @param showCategorySecondDtos
     */
    void addSecondList(List<ShowCategorySecondDto> showCategorySecondDtos);

    /**
     * 修改一级展示分类
     *
     * @param showCategoryDto
     */
    void updateShowCategory(ShowCategoryDto showCategoryDto);

    /**
     * 删除展示分类
     *
     * @param id
     */
    void deleteShowCategoryList(Long id);

    /**
     * 修改展示分类排序
     *
     * @param showCategoryDtos
     */
    void updateShowCategorySort(List<ShowCategoryDto> showCategoryDtos);
}
