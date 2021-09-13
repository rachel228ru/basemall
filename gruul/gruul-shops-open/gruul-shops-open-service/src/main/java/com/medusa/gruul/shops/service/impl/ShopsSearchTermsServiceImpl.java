package com.medusa.gruul.shops.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.util.CurUserUtil;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.dto.CurUserDto;
import com.medusa.gruul.shops.api.entity.ShopsSearchTerms;
import com.medusa.gruul.shops.mapper.ShopsSearchTermsMapper;
import com.medusa.gruul.shops.model.param.ShopsSearchTermsParam;
import com.medusa.gruul.shops.model.vo.ShopsSearchTermsVo;
import com.medusa.gruul.shops.properties.GlobalConstant;
import com.medusa.gruul.shops.properties.ShopsRenovationRedisTools;
import com.medusa.gruul.shops.service.ShopsSearchTermsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


/**
 * @author create by zq
 * @date created in 2020/01/14
 */
@Service(value = "shopsSearchTermsServiceImpl")
public class ShopsSearchTermsServiceImpl extends ServiceImpl<ShopsSearchTermsMapper, ShopsSearchTerms> implements ShopsSearchTermsService {


    /**
     * 获取热门词汇
     *
     * @return ShopsSearchTermsVo
     */
    @Override
    public Result getTerms() {
        return Result.ok(this.innerGetCache());
    }


    /**
     * 新增保存商铺热搜词汇信息
     *
     * @param param
     * @return Result
     */
    @Override
    public Result updateTerms(ShopsSearchTermsParam param) {
        if (null == param.getId()) {
            ShopsSearchTerms shopsSearchTerms = new ShopsSearchTerms();
            BeanUtil.copyProperties(param, shopsSearchTerms);
            if (this.baseMapper.insert(shopsSearchTerms) < 1) {
                log.error(String.format("新增 商铺热搜词汇信息失败! 参数 : %s", param));
                return Result.failed();
            }
            return Result.ok();
        }

        ShopsSearchTerms shopsSearchTerms = new ShopsSearchTerms();
        BeanUtil.copyProperties(param, shopsSearchTerms);
        if (!this.updateById(shopsSearchTerms)) {
            log.error(String.format("修改 商铺热搜词汇信息失败! 参数 : %s", param));
            return Result.failed();
        }
        ShopsRenovationRedisTools redisTools = new ShopsRenovationRedisTools();
        redisTools.innerRemoveCache(GlobalConstant.STRING_SHOP_SEARCH_TERMS);
        return Result.ok();
    }


    private ShopsSearchTermsVo innerGetCache() {
        CurUserDto userDto = CurUserUtil.getHttpCurUser();
        if (null == userDto || StringUtils.isBlank(userDto.getShopId())) {
            return null;
        }
        ShopsRenovationRedisTools redisTools = new ShopsRenovationRedisTools();
        String cache = redisTools.get(userDto.getShopId() + GlobalConstant.STRING_SHOP_SEARCH_TERMS);
        if (StringUtils.isBlank(cache)) {
            ShopsSearchTermsVo shopsSearchTermsVo = this.baseMapper.getTerms();
            redisTools.set(userDto.getShopId() + GlobalConstant.STRING_SHOP_SEARCH_TERMS, JSONObject.toJSONString(shopsSearchTermsVo));
            return shopsSearchTermsVo;
        }
        return JSONObject.parseObject(cache, ShopsSearchTermsVo.class);
    }

}
