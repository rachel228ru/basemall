package com.medusa.gruul.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.platform.api.entity.AuthRoleInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @author whh
 * @since 2020-01-09
 */
public interface AuthRoleInfoMapper extends BaseMapper<AuthRoleInfo> {

    /**
     * 获取指定用户角色信息
     *
     * @param accountId 用户id
     * @param tenantId  租户id
     * @return com.medusa.gruul.platform.api.entity.AuthRoleInfo
     */
    List<AuthRoleInfo> selectByUserIdAndTenantId(@Param("accountId") Long accountId, @Param("tenantId") String tenantId);
}
