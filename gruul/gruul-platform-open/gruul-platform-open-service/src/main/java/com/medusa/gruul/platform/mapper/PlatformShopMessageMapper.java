package com.medusa.gruul.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.platform.api.entity.PlatformShopMessage;

import java.util.List;

/**
 * <p>
 * 店铺消息配置 Mapper 接口
 * </p>
 *
 * @author whh
 * @since 2020-05-22
 */
public interface PlatformShopMessageMapper extends BaseMapper<PlatformShopMessage> {

    /**
     * 获取租户最后一个版本的消息
     *
     * @return com.medusa.gruul.platform.api.entity.PlatformShopMessage
     */
    List<PlatformShopMessage> getLastMiniMsg();

}
