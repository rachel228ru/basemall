package com.medusa.gruul.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.platform.api.entity.MiniInfo;

/**
 * <p>
 * 小程序基本信息(非授权时获取的) Mapper 接口
 * </p>
 *
 * @author whh
 * @since 2019-09-07
 */
public interface MiniInfoMapper extends BaseMapper<MiniInfo> {


    /**
     * 获取id最大的小程序
     *
     * @return id最大的小程序信息
     */
    MiniInfo getLastMini();
}
