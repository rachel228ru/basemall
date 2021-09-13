package com.medusa.gruul.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.platform.api.entity.AuthRoleInfo;
import com.medusa.gruul.platform.api.entity.AuthUserRole;
import com.medusa.gruul.platform.mapper.AuthUserRoleMapper;
import com.medusa.gruul.platform.service.IAuthRoleInfoService;
import com.medusa.gruul.platform.service.IAuthUserRoleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 用户角色关系表 服务实现类
 * </p>
 *
 * @author whh
 * @since 2020-01-09
 */
@Service
@Log4j2
public class AuthUserRoleServiceImpl extends ServiceImpl<AuthUserRoleMapper, AuthUserRole> implements IAuthUserRoleService {

    @Autowired
    private IAuthRoleInfoService authRoleInfoService;

//    @Override
//    @Deprecated
//    public Boolean setUserRose(Long accountInfoId, Integer roleCode, String tenantId) {
//        AuthRoleInfo authRoleInfo = authRoleInfoService.getByRoleCode(roleCode);
//        if (Optional.ofNullable(authRoleInfo).isPresent()) {
//            AuthUserRole authUserRole = this.findByAccountIdAndauthRoleId(accountInfoId, authRoleInfo.getId(), tenantId);
//            if (Optional.ofNullable(authUserRole).isPresent()) {
//                log.info("该用户已添加过该角色");
//                return true;
//            }
//            authUserRole = new AuthUserRole();
//            authUserRole.setUserId(accountInfoId);
//            authUserRole.setRoleId(authRoleInfo.getId());
//            authUserRole.setTenantId(tenantId);
//            this.baseMapper.insert(authUserRole);
//            return true;
//        }
//        log.info("accountInfoId:{},roleCode:{} =====>设置用户角色失败,角色编码错误", accountInfoId, roleCode);
//        return false;
//    }
//
//    @Override
//    public Boolean removeByUserIdAndRoleCode(Long accountInfoId, Integer roleCode) {
//        AuthRoleInfo authRoleInfo = authRoleInfoService.getByRoleCode(roleCode);
//        if (authRoleInfo == null) {
//            log.info("roleCode:{}  角色编码错误", roleCode);
//            return Boolean.FALSE;
//        }
//        AuthUserRole authUserRole = this.getBaseMapper().selectOne(new QueryWrapper<AuthUserRole>().eq("user_id", accountInfoId).eq("role_id", authRoleInfo.getId()));
//        if (authUserRole == null) {
//            return Boolean.FALSE;
//        }
//        return this.removeById(authUserRole.getId());
//    }
//
//    @Override
//    public List<AuthUserRole> getByUserIdAndTenantId(Long accountId, String tenantId) {
//        return this.baseMapper.selectList(new QueryWrapper<AuthUserRole>().eq("user_id", accountId)
//                .eq("tenant_id", tenantId));
//    }
//
//
//    private AuthUserRole findByAccountIdAndauthRoleId(Long accountInfoId, Long roleId, String tenantId) {
//        return this.baseMapper.selectOne(new QueryWrapper<AuthUserRole>().eq("user_id", accountInfoId)
//                .eq("role_id", roleId)
//                .eq("tenant_id", tenantId));
//    }
}
