package com.medusa.gruul.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.platform.api.entity.AuthRoleInfo;
import com.medusa.gruul.platform.mapper.AuthRoleInfoMapper;
import com.medusa.gruul.platform.service.IAuthRoleInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author whh
 * @since 2020-01-09
 */
@Service
public class AuthRoleInfoServiceImpl extends ServiceImpl<AuthRoleInfoMapper, AuthRoleInfo> implements IAuthRoleInfoService {

    @Override
    public AuthRoleInfo getByRoleCode(Integer roleCode) {
        return this.getBaseMapper().selectOne(new QueryWrapper<AuthRoleInfo>().eq("role_code", roleCode));

    }

    @Override
    public List<AuthRoleInfo> getByUserIdAndTenantId(Long accountId, String tenantId) {
        return this.getBaseMapper().selectByUserIdAndTenantId(accountId, tenantId);
    }

    @Override
    public List<AuthRoleInfo> getByRoleIds(List<Long> roleIds) {
        return this.getBaseMapper().selectList(new QueryWrapper<AuthRoleInfo>().in("id", roleIds));
    }
}
