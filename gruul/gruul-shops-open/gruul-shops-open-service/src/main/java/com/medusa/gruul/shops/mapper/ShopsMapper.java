package com.medusa.gruul.shops.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.shops.api.entity.Shops;
import org.springframework.stereotype.Repository;


/**
 * @author create by zq
 * @date created in 2019/11/15
 */
@Repository
public interface ShopsMapper extends BaseMapper<Shops> {
    /**
     * 获取id最大的店铺
     *
     * @return
     */
    Shops getMaxId();

}
