package com.medusa.gruul.shops.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.shops.api.entity.Shops;
import com.medusa.gruul.shops.api.entity.ShopsSearchTerms;
import com.medusa.gruul.shops.model.vo.ShopsSearchTermsVo;
import org.springframework.stereotype.Repository;


/**
 * @author create by zq
 * @date created in 2019/11/15
 */
@Repository
public interface ShopsSearchTermsMapper extends BaseMapper<ShopsSearchTerms> {

    /**
     * 获取热门词汇
     *
     * @return ShopsSearchTermsVo
     */
    ShopsSearchTermsVo getTerms();

}