package com.medusa.gruul.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.account.api.entity.MiniAccountOauths;
import com.medusa.gruul.account.api.enums.OauthTypeEnum;

/**
 * <p>
 * 用户第三方Id表 服务类
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
public interface IMiniAccountOauthsService extends IService<MiniAccountOauths> {

    /**
     * 根据openId以及指定的授权类型查询用户三方数据表
     *
     * @param openId openId
     * @param type   授权类型
     * @return com.medusa.gruul.account.api.entity.MiniAccountOauths
     */
    MiniAccountOauths getByOpenId(String openId, Integer type);

    /**
     * 获取指定用户授权列表
     *
     * @param oauthType 授权类型 1-微信小程序,2-公众号"
     * @param userId    用户id
     * @return com.medusa.gruul.account.api.entity.MiniAccountOauths
     */
    MiniAccountOauths getByUserId(Integer oauthType, String userId);

    /**
     * 获取unionId授权数据,存在小程序授权则返回小程序授权信息
     *
     * @param unionId 微信关联id
     * @return com.medusa.gruul.account.api.entity.MiniAccountOauths
     */
    @Deprecated
    MiniAccountOauths getByUnionIdAndMiniInfo(String unionId);

    /**
     * 获取指定uinoinId的授权类型
     *
     * @param unionId  微信关联id
     * @param typeEnum 授权类型
     * @return com.medusa.gruul.account.api.entity.MiniAccountOauths
     */
    MiniAccountOauths getByUnionIdAndType(String unionId, OauthTypeEnum typeEnum);
}
