package com.medusa.gruul.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.account.api.entity.MiniAccountExtends;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户信息扩展表 Mapper 接口
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
public interface MiniAccountExtendsMapper extends BaseMapper<MiniAccountExtends> {

    /**
     * 查询用户当天使用的扩展数据
     *
     * @param userId        用户总id
     * @param currentStatus 当前使用  0未使用 1-当前使用
     * @return com.medusa.gruul.account.api.entity.MiniAccountExtends
     */
    MiniAccountExtends selectByCurrentStatus(@Param("userId") String userId, @Param("currentStatus") Integer currentStatus);
}
