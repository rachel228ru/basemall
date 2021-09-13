package com.medusa.gruul.shops.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.shops.api.entity.Shops;
import com.medusa.gruul.shops.mapper.ShopsMapper;
import com.medusa.gruul.shops.model.param.ShopsSearchTermsParam;
import com.medusa.gruul.shops.model.vo.ShopsSearchTermsVo;
import com.medusa.gruul.shops.service.ShopsSearchTermsService;
import com.medusa.gruul.shops.service.ShopsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author create by zq
 * @date created in 2020/01/14
 */
@Service(value = "shopsServiceImpl")
public class ShopsServiceImpl extends ServiceImpl<ShopsMapper, Shops> implements ShopsService {


    @Autowired
    private ShopsSearchTermsService shopsSearchTermsService;


    /**
     * 获取商铺 前端获取城市信息
     *
     * @return List
     */
    @Override
    public List listShops() {
        return this.baseMapper.selectList(new QueryWrapper<Shops>().eq("", ""));
    }

    @Override
    public Long getMaxId() {
        Shops shop = this.baseMapper.getMaxId();
        Long id = 1L;
        if(shop != null){
            id = shop.getId();
        }
        return id;
    }


    /**
     * 获取热门词汇
     *
     * @return ShopsSearchTermsVo
     */
    @Override
    public Result getTerms() {
        return shopsSearchTermsService.getTerms();
    }


    /**
     * 新增保存商铺热搜词汇信息
     *
     * @param param
     * @return Result
     */
    @Override
    public Result updateTerms(ShopsSearchTermsParam param) {
        return shopsSearchTermsService.updateTerms(param);
    }

}
