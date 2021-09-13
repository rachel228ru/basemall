package com.medusa.gruul.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.platform.api.entity.DefaultValue;

/**
 * <p>
 * 默认数据表 Mapper 接口
 * </p>
 *
 * @author whh
 * @since 2019-09-07
 */
public interface DefaultValueMapper extends BaseMapper<DefaultValue> {

    /**
     * 获取指定标识最后一个版本
     *
     * @param uniqueIdentification 唯一标识
     * @return com.medusa.gruul.platform.api.entity.DefaultValue
     */
    DefaultValue selectByLastDefaultVersion(String uniqueIdentification);

}
