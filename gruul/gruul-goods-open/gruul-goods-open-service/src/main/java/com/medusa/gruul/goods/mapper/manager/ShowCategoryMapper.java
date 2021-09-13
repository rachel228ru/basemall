package com.medusa.gruul.goods.mapper.manager;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.medusa.gruul.goods.api.entity.ShowCategory;
import com.medusa.gruul.goods.api.model.param.manager.ShowCategoryParam;
import com.medusa.gruul.goods.api.model.vo.manager.ShowCategoryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 产展示品分类 Mapper 接口
 * </p>
 *
 * @author alan
 * @since 2019-09-03
 */
@Repository
public interface ShowCategoryMapper extends BaseMapper<ShowCategory> {

    /**
     * 获取商品展示分类信息list
     *
     * @param page              分页对象
     * @param showCategoryParam 展示分类查询参数
     * @return 展示分类list信息
     */
    List<ShowCategoryVo> queryShowCategoryList(IPage page, @Param("showCategoryParam") ShowCategoryParam showCategoryParam);

    /**
     * 获取所有展示分类信息
     *
     * @param showCategoryParam 
     * @return 所有展示分类list信息
     */
    List<ShowCategoryVo> queryAllShowCategoryList(@Param("showCategoryParam") ShowCategoryParam showCategoryParam);

    /**
     * 获取指定商品展示分类信息
     *
     * @param id 商品展示分类id
     * @return 商品展示分类list信息
     */
    ShowCategoryVo getShowCategoryById(String id);

    /**
     * 获取默认展示分类信息
     *
     * @return ShowCategoryVo
     */
    ShowCategoryVo queryDefaultShowCategory();


}
