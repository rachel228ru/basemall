package com.medusa.gruul.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.platform.api.entity.AuthToken;

/**
 * <p>
 * 接口调用凭据和授权信息 服务类
 * </p>
 *
 * @author whh
 * @since 2019-09-07
 */
public interface IAuthTokenService extends IService<AuthToken> {

    /**
     * 根据appid获取小程序token等信息
     *
     * @param authorizerAppid appid
     * @return info
     */
    AuthToken getByAppid(String authorizerAppid);
}
