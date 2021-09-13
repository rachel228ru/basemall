package com.medusa.gruul.shops.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.shops.api.entity.ShopsMenuConfig;
import com.medusa.gruul.shops.mapper.ShopsMenuConfigMapper;
import com.medusa.gruul.shops.model.param.ShopsMenuConfigParam;
import com.medusa.gruul.shops.service.ShopsMenuConfigService;
import org.springframework.stereotype.Service;



/**
 * @author create by zq
 * @date created in 2020/01/14
 */
@Service(value = "shopsMenuConfigServiceImpl")
public class ShopsMenuConfigServiceImpl extends ServiceImpl<ShopsMenuConfigMapper, ShopsMenuConfig> implements ShopsMenuConfigService {


    /**
     * 合伙人菜单属性配置新增or修改
     *
     * @param param
     * @return Result
     */
    @Override
    public Result updateMenuConfig(ShopsMenuConfigParam param) {
        ShopsMenuConfig shopsMenuConfig;
        if (param.getId() == null) {
            if (null != this.getMenuConfig().getData()) {
                throw new ServiceException(String.format(SystemCode.DATA_ADD_FAILED.getMsg(), SystemCode.DATA_EXISTED.getMsg()));
            }

            shopsMenuConfig = new ShopsMenuConfig();
            BeanUtil.copyProperties(param, shopsMenuConfig);
            if (!this.save(shopsMenuConfig)) {
                throw new ServiceException(SystemCode.DATA_ADD_FAILED.getMsg());
            }

        } else {
            shopsMenuConfig = new ShopsMenuConfig();
            BeanUtil.copyProperties(param, shopsMenuConfig);
            if (!this.updateById(shopsMenuConfig)) {
                throw new ServiceException(SystemCode.DATA_UPDATE_FAILED.getMsg());
            }
        }

        return Result.ok(shopsMenuConfig);
    }


    /**
     * 获取店铺合伙人菜单属性配置
     *
     * @return Result
     */
    @Override
    public Result getMenuConfig() {
        return Result.ok(this.baseMapper.selectOne(new QueryWrapper<ShopsMenuConfig>()));
    }


}
