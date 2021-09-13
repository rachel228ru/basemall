package com.medusa.gruul.platform.stp;

import cn.dev33.satoken.SaTokenManager;
import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.stp.StpLogic;
import com.medusa.gruul.common.core.util.JwtUtils;
import com.medusa.gruul.platform.constant.RedisConstant;

/**
 * @author Huihuang Wu
 * @description
 * @data: 2020/10/30
 */
public class MeStpLogic extends StpLogic {

    public static JwtUtils JWT_UTILS = null;

    public MeStpLogic(String loginKey) {
        super(loginKey);
    }


    @Override
    public String randomTokenValue() {
        SaTokenConfig config = SaTokenManager.getConfig();
        if (JWT_UTILS == null) {
            JWT_UTILS = new JwtUtils("mds-platfrom-new", config.getTimeout() * 1000);
        }
        return this.loginKey.concat(":").concat(JWT_UTILS.createJwtToken("platfrom-agent"));
    }

    @Override
    public String getKeyTokenValue(String tokenValue) {
        return RedisConstant.PREFIX + loginKey + ":" + tokenValue;
    }

    @Override
    public String getKeyLoginId(Object loginId) {
        return RedisConstant.PREFIX + loginKey + ":id:" + loginId;
    }
}
