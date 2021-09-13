package com.medusa.gruul.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.platform.api.entity.AuthRoleInfo;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author whh
 * @since 2020-01-09
 */
public interface IAuthRoleInfoService extends IService<AuthRoleInfo> {

    /**
     * 获取指定编码角色
     *
     * @param roleCode 角色编码
     * @return com.medusa.gruul.platform.api.entity.AuthRoleInfo
     */
    AuthRoleInfo getByRoleCode(Integer roleCode);

    /**
     * 获取用户拥有的角色
     *
     * @param accountId 平台用户id
     * @param tenantId  租户id
     * @return com.medusa.gruul.platform.api.entity.AuthRoleInfo
     */
    List<AuthRoleInfo> getByUserIdAndTenantId(Long accountId, String tenantId);

    /**
     * 获取指定角色
     *
     * @param roleIds 角色表id
     * @return com.medusa.gruul.platform.api.entity.AuthRoleInfo
     */
    List<AuthRoleInfo> getByRoleIds(List<Long> roleIds);

}
