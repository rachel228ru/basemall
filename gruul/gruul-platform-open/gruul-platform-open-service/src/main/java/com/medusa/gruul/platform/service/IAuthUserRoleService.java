package com.medusa.gruul.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.platform.api.entity.AuthUserRole;

import java.util.List;

/**
 * <p>
 * 用户角色关系表 服务类
 * </p>
 *
 * @author whh
 * @since 2020-01-09
 */
public interface IAuthUserRoleService extends IService<AuthUserRole> {

//    /**
//     * 设置用户为指定角色
//     *
//     * @param accountInfoId 平台用户id
//     * @param roleCode      角色编码
//     * @param tenantId      租户id
//     * @return java.lang.Boolean
//     */
//    Boolean setUserRose(Long accountInfoId, Integer roleCode, String tenantId);
//
//
//    /**
//     * 移除用户指定角色
//     *
//     * @param accountInfoId 用户id
//     * @param roleCode      角色编码
//     * @return java.lang.Boolean
//     */
//    Boolean removeByUserIdAndRoleCode(Long accountInfoId, Integer roleCode);
//
//    /**
//     * 获取指定用户所有角色
//     *
//     * @param accountId 用户id
//     * @param tenantId  租户id
//     * @return com.medusa.gruul.platform.api.entity.AuthUserRole
//     */
//    List<AuthUserRole> getByUserIdAndTenantId(Long accountId, String tenantId);
}
