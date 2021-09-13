package com.medusa.gruul.goods.service.api.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.goods.api.constant.GoodsConstant;
import com.medusa.gruul.goods.api.entity.SaleMode;
import com.medusa.gruul.goods.api.entity.ShowCategory;
import com.medusa.gruul.goods.api.model.vo.api.ApiShowCategoryVo;
import com.medusa.gruul.goods.mapper.api.ApiSaleModeMapper;
import com.medusa.gruul.goods.mapper.api.ApiShowCategoryMapper;
import com.medusa.gruul.goods.service.api.IApiShowCategoryService;
import com.medusa.gruul.goods.web.enums.SaleModeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 小程序商品展示分类 服务实现类
 *
 * @author kyl
 * @since 2019-11-05
 */
@Service
public class ApiShowCategoryServiceImpl extends ServiceImpl<ApiShowCategoryMapper, ShowCategory> implements IApiShowCategoryService {

    @Autowired
    private ApiShowCategoryMapper showCategoryMapper;

    @Autowired
    private ApiSaleModeMapper apiSaleModeMapper;


    /**
     * 获取所有含有商品的展示分类
     *
     * @return 展示分类list信息
     */
    @Override
    public List<ApiShowCategoryVo> getAllApiShowCategoryList() {
        return showCategoryMapper.queryAllApiShowCategoryList();
    }

    /**
     * 获取对应分区含有商品的展示分类
     *
     * @param saleMode
     * @return 展示分类list信息
     */
    @Override
    public List<ApiShowCategoryVo> getApiShowCategoryListBySaleMode(Long saleMode) {
        SaleMode saleModeSearch = apiSaleModeMapper.selectById(saleMode);
        if(BeanUtil.isEmpty(saleModeSearch)){
            return new ArrayList<>(CommonConstants.NUMBER_ZERO);
        }

        return showCategoryMapper.queryApiSupermarketShowCategoryList(saleMode);

    }
}
