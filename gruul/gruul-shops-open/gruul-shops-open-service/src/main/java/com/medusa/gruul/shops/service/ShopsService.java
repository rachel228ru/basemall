package com.medusa.gruul.shops.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.shops.api.entity.Shops;
import com.medusa.gruul.shops.model.param.ShopsSearchTermsParam;
import com.medusa.gruul.shops.model.vo.ShopsSearchTermsVo;

import java.util.List;

/**
 * @author create by zq
 * @date created in 2020/01/14
 */
public interface ShopsService extends IService<Shops> {

    /**
     * 获取商铺
     *
     * @return list
     */
    List listShops();

    /**
     * 获取店铺最大id
     *
     * @return 最大的id 不存在则返回1
     */
    Long getMaxId();


    /**
     * 获取热门词汇
     *
     * @return Result
     */
    Result getTerms();


    /**
     * 新增保存商铺热搜词汇信息
     *
     * @param param
     * @return Result
     */
    Result updateTerms(ShopsSearchTermsParam param);
}
