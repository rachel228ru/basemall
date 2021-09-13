package com.medusa.gruul.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.platform.api.entity.AuthToken;
import com.medusa.gruul.platform.mapper.AuthTokenMapper;
import com.medusa.gruul.platform.service.IAuthTokenService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 接口调用凭据和授权信息 服务实现类
 * </p>
 *
 * @author whh
 * @since 2019-09-07
 */
@Service
public class AuthTokenServiceImpl extends ServiceImpl<AuthTokenMapper, AuthToken> implements IAuthTokenService {

    @Override
    public AuthToken getByAppid(String authorizerAppid) {
        return this.baseMapper.selectOne(new QueryWrapper<AuthToken>().eq("authorizer_appid", authorizerAppid));
    }
}
